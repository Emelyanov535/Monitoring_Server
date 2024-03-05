package com.example.demo.repository;

import com.example.demo.model.MonitoringRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringRuleRepository extends JpaRepository<MonitoringRule, Long> {
}
