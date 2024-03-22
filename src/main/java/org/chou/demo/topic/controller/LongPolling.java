package org.chou.demo.topic.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
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
public class LongPolling {
    private int randomNumber = 0;
    private final Object lock = new Object();
    // 每10

    public LongPolling() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//        executorService.scheduleAtFixedRate(this::generateRandomNumber, 0, 5, TimeUnit.SECONDS);
        // TODO 每 10 秒會呼叫一次 generateNumber() 讓數字改變
        executorService.scheduleAtFixedRate(() -> generateRandomNumber(), 0, 10, TimeUnit.SECONDS);
    }

    private void generateRandomNumber() {
        synchronized (lock) {
//            randomNumber = (int) Math.floor(10 * Math.random());
            randomNumber++;
            lock.notifyAll();
        }
    }

    @ResponseBody
    @GetMapping("/longPolling")
    public ResponseEntity<?> getData() {
        synchronized (lock) {
            try {
                // TODO 設定server要等15秒
                lock.wait(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            SimpleDateFormat sdFormat = new SimpleDateFormat("HH:mm:ss");
            Date current = new Date();

            Map<String, String> response = new HashMap<>();
            response.put("data", sdFormat.format(current) + " Server : " + randomNumber);

            return ResponseEntity.ok().body(response);
        }
    }


    @GetMapping("/longPolling.html")
    public String longPollingPage() {
        return "longPolling";
    }
}
