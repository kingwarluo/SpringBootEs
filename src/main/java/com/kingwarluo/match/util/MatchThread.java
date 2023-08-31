package com.kingwarluo.match.util;


import java.util.ArrayList;
import java.util.List;

/**
 * @author kingwarluo
 * @{description}
 * @date 2023/8/30 17:53
 */
public class MatchThread extends Thread {

    private User user;

    private MatchPool matchPool;

    public volatile boolean matching = true;

    private List<User> resultList = new ArrayList<>();

    private Long endTimeMills = System.currentTimeMillis() + 50 * 1000;

    public MatchThread(User user, MatchPool matchPool) {
        this.user = user;
        this.matchPool = matchPool;
    }

    public List<User> getResultList() {
        return resultList;
    }

    public void setResultList(List<User> userList) {
        this.matching = false;
        this.resultList.addAll(userList);
    }

    @Override
    public void run() {
        while(matching) {
            if (System.currentTimeMillis() > endTimeMills) {
                matchPool.clear(user.getId());
                return;
            }
            List<User> userList = matchPool.getUserList();
            for (User u : userList) {
                if (u.equals(user)) {
                    continue;
                }
                boolean userCommand = user.getCallForSex().equals(u.getSex());
                boolean uCommand = u.getCallForSex().equals(user.getSex());
                if (userCommand && uCommand) {
                    synchronized (userList) {
                        if (userList.indexOf(user) != -1 && userList.indexOf(u) != -1) {
                            userList.remove(user);
                            userList.remove(u);
                            resultList.add(user);
                            resultList.add(u);
                            System.out.println("匹配成功" + resultList.toString());
                            System.out.println("匹配后，剩余池：" + userList.toString());
                            matching = false;
                            matchPool.stopMatchAndSetResult(resultList);
                        }
                    }
                    return;
                }
            }
        }
    }

}
