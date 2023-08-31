package com.kingwarluo.match.util;

import lombok.Data;

/**
 * @author kingwarluo
 * @{description}
 * @date 2023/8/30 17:29
 */
@Data
public class User {

    private Long id;
    private Integer special; // 1-是 2-否
    private Integer sex; // 1-男 2-女

    private Integer callForSex;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.id.equals(user.id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", sex=" + sex +
                ", callForSex=" + callForSex +
                '}';
    }
}
