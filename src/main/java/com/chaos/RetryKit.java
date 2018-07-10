package com.chaos;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Created by chaos on 2018/6/28.
 */
public interface RetryKit {

    static <T> T retry(Supplier<T> supplier, int retryTimes) throws Throwable {
        return retry(supplier, retryTimes, 0L, null);
    }

    static <T> T retry(Supplier<T> supplier, int retryTimes, T expected) throws Throwable {
        return retry(supplier, retryTimes, 0L, expected);
    }

    static <T> T retry(Supplier<T> supplier, int retryTimes, long retryInterval) throws Throwable {
        return retry(supplier, retryTimes, retryInterval, null);
    }

    static <T> T retry(Supplier<T> supplier, int retryTimes, long retryInterval, T expected) throws Throwable {
        if (retryTimes < 0) {
            throw new IllegalArgumentException("retryTimes < 0");
        } else {
            try {
                T returnObject = supplier.get();
                if (expected != null && !returnObject.equals(expected) && retryTimes != 0) {
                    if (retryInterval > 0) {
                        TimeUnit.SECONDS.sleep(retryInterval);
                    }
                    return retry(supplier, retryTimes-1, retryInterval, expected);
                }
                return returnObject;
            } catch (Throwable th) {
                if (retryTimes != 0) {
                    if (retryInterval > 0) {
                        TimeUnit.SECONDS.sleep(retryInterval);
                    }
                    return retry(supplier, retryTimes-1, retryInterval, expected);
                } else {
                    throw th;
                }
            }
        }
    }
}
