package com.chaos;

import java.util.Collection;

/**
 * Created by chaos on 2018/6/4.
 */
public interface CollectionKit {

    static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }

    static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);
    }
}
