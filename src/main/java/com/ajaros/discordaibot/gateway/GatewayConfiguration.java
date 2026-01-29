package com.ajaros.discordaibot.gateway;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class GatewayConfiguration {
  @Bean
  public GatewayDiscordClient gatewayDiscordClient(@Value("${discord.token}") String token) {
    log.info("Initializing Discord client");
    return DiscordClient.create(token).login().block();
  }
}
