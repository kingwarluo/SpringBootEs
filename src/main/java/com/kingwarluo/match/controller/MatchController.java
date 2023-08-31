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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

    @RequestMapping("/cancel/{id}")
    public boolean cancel(@PathVariable("id") Long id) {
        return matchService.cancel(id);
    }
}
