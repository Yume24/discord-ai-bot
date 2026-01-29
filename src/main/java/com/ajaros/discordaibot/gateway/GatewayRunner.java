package com.ajaros.discordaibot.gateway;

import com.ajaros.discordaibot.events.EventHandler;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
@Slf4j
public class GatewayRunner implements ApplicationRunner {
  private final Mono<GatewayDiscordClient> client;
  private final Set<EventHandler<?>> eventHandlers;

  @Override
  public void run(@NonNull ApplicationArguments args) {
    client
        .flatMap(
            c -> {
              eventHandlers.forEach(event -> register(event, c));
              log.info("Listening for events...");
              return c.onDisconnect();
            })
        .block();
  }

  private <T extends Event> void register(
      EventHandler<T> eventHandler, GatewayDiscordClient client) {
    client
        .on(eventHandler.getEventType())
        .flatMap(eventHandler::handleEvent)
        .onErrorContinue(eventHandler::handleException)
        .subscribe();
    log.info("Registered event handler for {}", eventHandler.getEventType());
  }
}
