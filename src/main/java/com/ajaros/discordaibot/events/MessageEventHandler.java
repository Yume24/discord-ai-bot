package com.ajaros.discordaibot.events;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class MessageEventHandler implements EventHandler<MessageCreateEvent> {

  @Override
  public @NonNull Mono<Void> handleEvent(MessageCreateEvent event) {
    var message = event.getMessage();
    if (isBotMentioned(message) && !isOwnMessage(message)) return handleMention(message);
    return Mono.empty();
  }

  @Override
  public void handleException(Throwable throwable, Object o) {
    log.error("Error handling event: {}", throwable.getMessage());
  }

  @Override
  public @NonNull Class<MessageCreateEvent> getEventType() {
    return MessageCreateEvent.class;
  }

  private Mono<Void> handleMention(Message message) {

    return message
        .getChannel()
        .flatMap(c -> c.createMessage("I was mentioned! You said: " + message.getContent()))
        .then();
  }

  private boolean isOwnMessage(Message message) {
    return message
        .getAuthor()
        .map(user -> user.getId().equals(message.getClient().getSelfId()))
        .orElse(false);
  }

  private boolean isBotMentioned(Message message) {
    var id = message.getClient().getSelfId();
    return message.getUserMentionIds().contains(id);
  }
}
