package com.chaos;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.*;

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

    static <ITEM> List<List<ITEM>> dichotomizeList(List<ITEM> itemList) {
        return splitList(itemList, itemList.size()/2+1);
    }

    static <ITEM> List<List<ITEM>> splitList(List<ITEM> itemList, int splitSize) {
        Preconditions.checkArgument(splitSize > 0);
        Preconditions.checkNotNull(itemList);
        if (splitSize >= itemList.size()) {
            return Collections.singletonList(itemList);
        }
        List<List<ITEM>> resultList = new ArrayList<>();
        int index = 0;
        for (int i = 0; i <= itemList.size()-splitSize; i = index) {
            index += splitSize;
            resultList.add(itemList.subList(i, index));
        }
        if (index < itemList.size()) {
            resultList.add(itemList.subList(index, itemList.size()));
        }
        return resultList;
    }

    static <KEY, VAL> Map<KEY, List<VAL>> mapping(List<KEY> keyList, List<VAL> valList) {
        Preconditions.checkNotNull(keyList);
        Preconditions.checkNotNull(valList);
        List<KEY> keys = new ArrayList<>(new HashSet<>(keyList));
        List<List<VAL>> splittedValList = splitList(valList, keys.size());
        Map<KEY, List<VAL>> resultMap = new HashMap<>();
        splittedValList.forEach(splittedVals -> {
            CollectionOptional.ofEmpty(splittedVals).each((index, splittedVal) ->
                resultMap.merge(keyList.get(index), Lists.newArrayList(splittedVal), (oldVal, newVal) -> {
                    oldVal.addAll(newVal);
                    return oldVal;
                }));
        });
        return resultMap;
    }
}
