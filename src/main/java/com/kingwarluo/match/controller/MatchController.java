package com.kingwarluo.match.controller;

import com.kingwarluo.es.domain.ShopSku;
import com.kingwarluo.match.service.MatchService;
import com.kingwarluo.match.util.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author kingwarluo
 * @{description}
 * @date 2022/5/23 10:03
 */
@RestController
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping("/put")
    public boolean put(@RequestParam("id") Long id, @RequestParam("sex") Integer sex, @RequestParam("callForSex") Integer callForSex) {
        User user = new User();
        user.setId(id);
        user.setSex(sex);
        user.setCallForSex(callForSex);
        return matchService.put(user);
    }

    @GetMapping("/result/{id}")
    public List<User> result(@PathVariable("id") Long id) {
        CompletableFuture<List<User>> future = CompletableFuture.supplyAsync(() -> matchService.result(id));
        try {
            return future.get(1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @GetMapping("/cancel/{id}")
    public boolean cancel(@PathVariable("id") Long id) {
        return matchService.cancel(id);
    }


    @GetMapping("/async")
    public void async(HttpServletRequest req) {
        System.out.println(new Date());
        AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(0L);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setName("com.alibaba.nacos.LongPolling");
                return t;
            }
        });
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            Date now = new Date();
            System.out.println(now);
            HttpServletResponse response = (HttpServletResponse)asyncContext.getResponse();
            // 禁用缓存
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-cache,no-store");
            response.setStatus(HttpServletResponse.SC_OK);
            try {
                response.getWriter().println(now);
            } catch (IOException e) {
                e.printStackTrace();
            }
            asyncContext.complete();
        }, 20, TimeUnit.SECONDS);
    }

}
