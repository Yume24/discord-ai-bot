package com.ajaros.discordaibot.events;

import discord4j.core.event.domain.Event;
import jakarta.annotation.Nonnull;
import reactor.core.publisher.Mono;

public interface EventHandler<T extends Event> {
  @Nonnull
  Mono<Void> handleEvent(T event);

  void handleException(Throwable throwable, Object o);

  @Nonnull
  Class<T> getEventType();
}
