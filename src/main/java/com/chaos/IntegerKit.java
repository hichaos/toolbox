package com.chaos;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by chaos on 2018/9/28.
 */
public interface IntegerKit {

    static int[] flatInteger(int[] arr) {
        if (arr.length == 0) {
            return arr;
        }
        return IntStream.rangeClosed(Arrays.stream(arr).min().getAsInt(), Arrays.stream(arr).max().getAsInt()).toArray();
    }

    static int[] flatInteger(int first, int last) {
        return IntStream.rangeClosed(first, last).toArray();
    }
}
