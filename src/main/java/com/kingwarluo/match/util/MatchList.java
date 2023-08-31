package com.kingwarluo.match.util;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author kingwarluo
 * @{description}
 * @date 2023/8/30 17:53
 */
public class MatchList extends CopyOnWriteArrayList<User> {

    int normalPos = 0;

    @Override
    public boolean add(User user) {
        if (user.getSpecial() == 1) {
            return super.add(user);
        } else {
            super.add(normalPos + 1, user);
            normalPos++;
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        return super.remove(o);
    }
}
