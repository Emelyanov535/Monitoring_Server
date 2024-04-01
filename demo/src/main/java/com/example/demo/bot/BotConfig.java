package com.example.demo.bot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "bot")
@Data
@Component
public class BotConfig {
    String botName;
    String token;
}
