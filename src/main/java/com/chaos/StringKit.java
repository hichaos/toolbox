package com.chaos;

import java.util.UUID;

/**
 * Created by chaos on 2018/6/1.
 */
public interface StringKit {

    static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
