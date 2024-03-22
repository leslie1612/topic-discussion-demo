package org.chou.demo.topic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
@Controller
public class LongPolling2 {
    // 儲存訂單
    private Map<String, DeferredResult<String>> orders = new HashMap<>();
    private Set<String> timeoutOrders = new HashSet<>();


    @GetMapping("/longPolling2.html")
    public String longPollingPage() {
        return "longPolling2";
    }

    @ResponseBody
    @GetMapping("/order/{something}")
    public DeferredResult<String> order(@PathVariable("something") String something) {


        DeferredResult<String> result = new DeferredResult<>(10 * 1000L);

        orders.put(something, result);
        log.info("get request");

        result.onCompletion(() -> {
//            if (!timeoutOrders.contains(something)) { // 只有當訂單沒有超時時才移除
                orders.remove(something);
                log.info(something + "做好了！");
//            }
        });

        result.onTimeout(() -> {
            orders.remove(something);
            timeoutOrders.add(something);
            log.info("做得太慢了，我不要了");

        });

        result.onError((e) -> {
            orders.remove(something);
            log.error("出錯");

        });


//            SimpleDateFormat sdFormat = new SimpleDateFormat("HH:mm:ss");
//            Date current = new Date();
//
//            Map<String, String> response = new HashMap<>();
//            response.put("data", sdFormat.format(current) + " Server : " + responseMessage);

        return result;

    }

    @ResponseBody
    @GetMapping("/finish/{order}")
    public String finish(@PathVariable("order") String order) {
        if (orders.containsKey(order)) {
//            SimpleDateFormat sdFormat = new SimpleDateFormat("HH:mm:ss");
//            Date current = new Date();
//

            DeferredResult<String> result = orders.get(order);
            result.setResult("完成了 " + order);
        }
        return "success";
    }
}


