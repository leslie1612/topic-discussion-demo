package org.chou.demo.topic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
public class ShortPolling {

    private int randomNumber = 0;

    public ShortPolling() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//        executorService.scheduleAtFixedRate(this::generateRandomNumber, 0, 5, TimeUnit.SECONDS);
        // TODO 每 10 秒會呼叫一次 generateRandomNumber() 讓數字改變
        executorService.scheduleAtFixedRate(() -> generateRandomNumber(), 0, 10, TimeUnit.SECONDS);
    }


    private void generateRandomNumber() {
//        randomNumber = (int) Math.floor(100 * Math.random());
        randomNumber++;
    }

    @ResponseBody
    @GetMapping("/shortPolling")
    public ResponseEntity<?> getData() {

        SimpleDateFormat sdFormat = new SimpleDateFormat("HH:mm:ss");
        Date current = new Date();

        Map<String, String> response = new HashMap<>();
        response.put("data", sdFormat.format(current) + " Server : " + randomNumber);

        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/changeData")
    public void change() {
        generateRandomNumber();
    }
}
