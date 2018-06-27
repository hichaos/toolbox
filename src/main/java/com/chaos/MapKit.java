package com.chaos;

import java.util.Map;

/**
 * Created by chaos on 2018/6/27.
 */
public interface MapKit {

    static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
}
