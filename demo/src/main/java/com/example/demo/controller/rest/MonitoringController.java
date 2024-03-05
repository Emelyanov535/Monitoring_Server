package com.example.demo.controller.rest;

import com.example.demo.model.MonitoringRule;
import com.example.demo.service.MonitoringRuleService;
import com.example.demo.service.MonitoringTaskService;
import com.example.demo.controller.dto.MonitoringRuleDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rules")
@AllArgsConstructor
public class MonitoringController {
    private MonitoringRuleService monitoringService;

    @PostMapping("/addRule")
    public MonitoringRuleDto addRule(@RequestBody MonitoringRuleDto monitoringRuleDto){
        return new MonitoringRuleDto(monitoringService.addRule(monitoringRuleDto.getUrl(), monitoringRuleDto.getIntervalSeconds(), monitoringRuleDto.getExpectedStatusCode()));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRule(@PathVariable("id") Long id){
        monitoringService.deleteRule(id);
    }

    @PutMapping("/switch/{id}")
    public void switchRule(@PathVariable("id")Long ruleId){
        monitoringService.switchRule(ruleId);
    }

    @GetMapping("/getList")
    public List<MonitoringRuleDto> getList(){
        return monitoringService.getList().stream()
                .map(MonitoringRuleDto::new)
                .toList();
    }
}
