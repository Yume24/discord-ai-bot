package com.ajaros.discordaibot.events;

import discord4j.core.event.domain.message.MessageCreateEvent;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class MessageEventHandler implements EventHandler<MessageCreateEvent> {

  @Override
  public @Nonnull Mono<Void> handleEvent(MessageCreateEvent event) {
    var message = event.getMessage();
    if (message.getContent().equals("!ping")) {
      return message.getChannel().flatMap(channel -> channel.createMessage("pong!")).then();
    }
    return Mono.empty();
  }

  @Override
  public void handleException(Throwable throwable, Object o) {
    log.error("Error handling event: {}", throwable.getMessage());
  }

  @Override
  public @Nonnull Class<MessageCreateEvent> getEventType() {
    return MessageCreateEvent.class;
  }
}
