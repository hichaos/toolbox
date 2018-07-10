package com.chaos;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Created by chaos on 2018/6/27.
 */
public interface BigDecimalKit {

    static BigDecimal wrap(Double d) {
        return Optional.ofNullable(d).map(String::valueOf).map(BigDecimal::new).orElse(null);
    }

    static BigDecimal wrap(Integer i) {
        return Optional.ofNullable(i).map(String::valueOf).map(BigDecimal::new).orElse(null);
    }

    static BigDecimal multiply(Double d1, Double d2) {
        if (null == d1 || null == d2) {
            return null;
        }
        return wrap(d1).multiply(wrap(d2));
    }

    static BigDecimal multiply(Double d, Integer i) {
        if (null == d || null == i) {
            return null;
        }
        return wrap(d).multiply(wrap(i));
    }

    static BigDecimal add(Double d1, Double d2) {
        if (null == d1 || null == d2) {
            return null;
        }
        return wrap(d1).add(wrap(d2));
    }

    static BigDecimal subtract(Double d1, Double d2) {
        if (null == d1 || null == d2) {
            return null;
        }
        return wrap(d1).subtract(wrap(d2));
    }
}
