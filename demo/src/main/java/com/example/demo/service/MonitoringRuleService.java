package com.example.demo.service;

import com.example.demo.model.MonitoringRule;
import com.example.demo.model.User;
import com.example.demo.repository.MonitoringRuleRepository;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class MonitoringRuleService {

    private UserRepository userRepository;
    private MonitoringRuleRepository monitoringRuleRepository;
    private final MonitoringTaskService monitoringTaskService;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new HashMap<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
    @PostConstruct
    public void init() {
        updateRules();
    }

    @Transactional
    public List<MonitoringRule> getList(){
        User user = getUserFromPrincipal();
        return monitoringRuleRepository.findAll().stream()
                .filter(rule -> Objects.equals(rule.getChannel().getId(), user.getChannel().getId()))
                .toList();
    }

    @Transactional
    public MonitoringRule addRule(String url, Integer intervalSeconds, Integer expectedStatusCode) {
        User user = getUserFromPrincipal();
        MonitoringRule monitoringRule = new MonitoringRule(url, intervalSeconds, expectedStatusCode, true);
        monitoringRuleRepository.save(monitoringRule);
        monitoringRule.setChannel(user.getChannel());
        updateRules();
        return monitoringRule;
    }

    @Transactional
    public void deleteRule(Long ruleId) {
        User user = getUserFromPrincipal();
        MonitoringRule rule = monitoringRuleRepository.getReferenceById(ruleId);
        if(Objects.equals(user.getChannel().getId(), rule.getChannel().getId())){
            monitoringRuleRepository.deleteById(ruleId);
            scheduledTasks.remove(ruleId).cancel(true);
            updateRules();
        }
    }

    @Transactional
    public void switchRule(Long ruleId) {
        User user = getUserFromPrincipal();
        MonitoringRule rule = monitoringRuleRepository.getReferenceById(ruleId);
        if(Objects.equals(user.getChannel().getId(), rule.getChannel().getId())){
            MonitoringRule monitoringRule = monitoringRuleRepository.getReferenceById(ruleId);
            if (!monitoringRule.getIsActive()) {
                monitoringRule.setIsActive(true);
            } else {
                monitoringRule.setIsActive(false);
                scheduledTasks.remove(ruleId).cancel(true);
            }
            updateRules();
        }
    }

    private void updateRules() {
        for (ScheduledFuture<?> task : scheduledTasks.values()) {
            task.cancel(true);
        }
        scheduledTasks.clear();

        List<MonitoringRule> rules = monitoringRuleRepository.findAll().stream()
                .filter(MonitoringRule::getIsActive)
                .toList();
        for (MonitoringRule rule : rules) {
            int intervalMillis = rule.getIntervalSeconds() * 1000;
            ScheduledFuture<?> task = executorService.scheduleAtFixedRate(() -> monitoringTaskService.checkRule(rule), 0, intervalMillis, TimeUnit.MILLISECONDS);
            scheduledTasks.put(rule.getId(), task);
        }
    }

    @Transactional
    protected User getUserFromPrincipal(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.getReferenceById(user.getId());
    }
}
