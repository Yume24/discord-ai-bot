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
              var registeredHandlers =
                  eventHandlers.stream().map(event -> register(event, c)).toList();
              log.info("Listening for events...");
              return Mono.when(registeredHandlers).and(c.onDisconnect());
            })
        .block();
  }

  private <T extends Event> Mono<Void> register(
      EventHandler<T> eventHandler, GatewayDiscordClient client) {

    log.info("Registered event handler for {}", eventHandler.getEventType().getSimpleName());
    return client
        .on(eventHandler.getEventType())
        .flatMap(eventHandler::handleEvent)
        .onErrorContinue(eventHandler::handleException)
        .then();
  }
}
