package org.example.test_server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private HttpStatus currentStatus = HttpStatus.OK;
    private HttpStatus Status = HttpStatus.ACCEPTED;

    @GetMapping("/get")
    public ResponseEntity<String> get() {
        return ResponseEntity.status(currentStatus).body("Hello World!!!");
    }

    @Scheduled(fixedDelay = 10000)
    public void changeStatus() {
        if (currentStatus == HttpStatus.OK) {
            currentStatus = HttpStatus.ACCEPTED;
        } else {
            currentStatus = HttpStatus.OK;
        }
    }

    @Scheduled(fixedDelay = 25000)
    public void changeStatusTwo() {
        if (Status == HttpStatus.ACCEPTED) {
            Status = HttpStatus.OK;
        } else {
            Status = HttpStatus.ACCEPTED;
        }
    }

    @GetMapping("/getHello")
    public ResponseEntity<String> getHello() {
        return ResponseEntity.status(Status).body("Hello World!!!");
    }
}

