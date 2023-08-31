package com.kingwarluo.match.service;

import com.kingwarluo.match.util.MatchPool;
import com.kingwarluo.match.util.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author kingwarluo
 * @{description}
 * @date 2023/8/31 16:02
 */
@Service
public class MatchService {

    @Autowired
    private MatchPool matchPool;

    public boolean put(User user) {
        matchPool.put(user);
        return true;
    }

    public List<User> result(Long id) {
        return matchPool.get(id);
    }

    public boolean cancel(Long id) {
        return matchPool.clear(id);
    }
}
