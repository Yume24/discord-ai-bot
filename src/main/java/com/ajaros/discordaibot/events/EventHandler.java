package com.ajaros.discordaibot.events;

import discord4j.core.event.domain.Event;
import org.jspecify.annotations.NonNull;
import reactor.core.publisher.Mono;

public interface EventHandler<T extends Event> {
  @NonNull Mono<Void> handleEvent(T event);

  void handleException(Throwable throwable, Object o);

  @NonNull Class<T> getEventType();
}
