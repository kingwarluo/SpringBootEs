package com.kingwarluo.match.util;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author kingwarluo
 * @{description}
 * @date 2023/8/30 17:30
 */
@Component
public class MatchPool {

    // 待匹配用户
    private List<User> userList = new MatchList();

    private Map<Long, MatchThread> matchThreadMap = new ConcurrentHashMap<>();

    private Map<Long, List<User>> resultMap = new ConcurrentHashMap<>();

    public void put(User user) {
        boolean add = userList.add(user);
        if(add) {
            System.out.println("人员入场，当前池：" + userList.toString());
            // 匹配
            MatchThread matchThread = new MatchThread(user, this);
            matchThread.setName("amatchThread-" + user.getId());
            matchThread.start();
            matchThreadMap.put(user.getId(), matchThread);
        }
    }

    public List<User> getUserList() {
        return userList;
    }

    // 匹配中 -- 返回null
    // 匹配成功 -- 返回list
    // 匹配失败 -- 返回空list
    public List<User> get(Long id) {
        List<User> users = resultMap.remove(id);
        if (!CollectionUtils.isEmpty(users)) {
            return users;
        }
        // 从线程中拿
        MatchThread matchThread = matchThreadMap.get(id);
        if (matchThread == null) {
            return new ArrayList<>();
        }
        if (matchThread.matching) {
            return null;
        }
        // 匹配结束
        return matchThread.getResultList();
    }

    public Boolean clear(Long id) {
        User user = new User();
        user.setId(id);
        MatchThread matchThread = matchThreadMap.remove(id);
        matchThread.matching = false;
        boolean remove = userList.remove(user);
        if (remove) {
            System.out.println(id + "退出匹配，剩余池:" + userList.toString());
        }
        return remove;
    }

    public void stopMatchAndSetResult(List<User> userList) {
        for (User user : userList) {
            MatchThread matchThread = matchThreadMap.get(user.getId());
            if (matchThread != null) {
                if (matchThread.matching) {
                    matchThread.matching = false;
                    matchThread.setResultList(userList);
                    resultMap.putIfAbsent(user.getId(), userList);
                }
            }
        }
    }
}
