package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MonitoringRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private int intervalSeconds;
    private int expectedStatusCode;
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "channel_id")
    private Channel channel;
    public MonitoringRule(String url, Integer intervalSeconds, Integer expectedStatusCode, Boolean isActive){
        this.url = url;
        this.expectedStatusCode = expectedStatusCode;
        this.intervalSeconds = intervalSeconds;
        this.isActive = isActive;
    }
}
