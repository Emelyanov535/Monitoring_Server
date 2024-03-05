package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "channel")
    private List<MonitoringRule> monitoringRules;
    public Channel(Long chatId){
        this.chatId = chatId;
    }
}
