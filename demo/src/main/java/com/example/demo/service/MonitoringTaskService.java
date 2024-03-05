package com.example.demo.service;

import com.example.demo.bot.TelegramBot;
import com.example.demo.model.MonitoringRule;
import com.example.demo.model.User;
import com.example.demo.repository.MonitoringRuleRepository;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class MonitoringTaskService {

    private final TelegramBot telegramBot;
    private final Map<Long, Integer> ruleStatusMap = new HashMap<>();

    public void checkRule(MonitoringRule rule) {
        String url = rule.getUrl();
        int expectedStatusCode = rule.getExpectedStatusCode();

        int actualStatusCode = executeGetRequest(url);

        Integer previousStatus = ruleStatusMap.getOrDefault(rule.getId(), expectedStatusCode);

        if (previousStatus == null || previousStatus != actualStatusCode) {
            String message = "Статус код для " + url + " изменился с " + previousStatus + " на " + actualStatusCode;
            telegramBot.sendMessage(rule.getChannel().getChatId(), message);
            ruleStatusMap.put(rule.getId(), actualStatusCode);
        }
    }

    private int executeGetRequest(String url) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        return response.getStatusCodeValue();
    }
}
