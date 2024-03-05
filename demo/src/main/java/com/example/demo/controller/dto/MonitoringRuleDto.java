package com.example.demo.controller.dto;


import com.example.demo.model.MonitoringRule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MonitoringRuleDto {
    private Long id;
    private String url;
    private int intervalSeconds;
    private int expectedStatusCode;
    private Boolean isActive;

    public MonitoringRuleDto(MonitoringRule monitoringRule){
        this.id = monitoringRule.getId();
        this.url = monitoringRule.getUrl();
        this.intervalSeconds = monitoringRule.getIntervalSeconds();
        this.expectedStatusCode = monitoringRule.getExpectedStatusCode();
    }
}
