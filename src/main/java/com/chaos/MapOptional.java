package com.chaos;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Created by chaos on 2018/7/24.
 */
public class MapOptional<M extends Map<K, V>, K, V> {

    private final M map;
    private static final MapOptional<?, ?, ?> EMPTY = new MapOptional();

    private MapOptional() {
        this.map = null;
    }

    private MapOptional(M map) {
        this.map = map;
    }

    public static <M extends Map<K, V>, K, V> MapOptional<M, K, V> ofEmpty(M m)  {
        return (m == null || m.isEmpty()) ? empty() : of(m);
    }

    @SuppressWarnings("unchecked")
    private static <M extends Map<K, V>, K, V> MapOptional<M, K, V> empty() {
        return (MapOptional<M, K, V>) EMPTY;
    }

    @SuppressWarnings("unchecked")
    private static <M extends Map<K, V>, K, V> MapOptional<M, K, V> of(M m) {
        return new MapOptional(m);
    }

    public void each(BiConsumer<K, V> action) {
        Objects.requireNonNull(action);
        if (null != this.map) {
            map.forEach(action);
        }
    }
}
