package com.chaos;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by chaos on 2018/6/1.
 */
public class CollectionOptional<C extends Collection<ITEM>, ITEM> {

    private final C collection;
    private final boolean filterNulls;

    private static final CollectionOptional<?, ?> EMPTY = new CollectionOptional<>();

    private CollectionOptional() {
        this(null, false);
    }

    private CollectionOptional(C collection, boolean filterNulls) {
        this.collection = collection;
        this.filterNulls = filterNulls;
    }

    public static <C extends Collection<ITEM>, ITEM> CollectionOptional<C, ITEM> ofEmpty(C c)  {
        return ofEmpty(c, false);
    }

    public static <C extends Collection<ITEM>, ITEM> CollectionOptional<C, ITEM> ofEmpty(C c, boolean filterNulls)  {
        return (c == null || c.isEmpty()) ? empty() : of(c, filterNulls);
    }

    @SuppressWarnings("unchecked")
    private static <C extends Collection<ITEM>, ITEM> CollectionOptional<C, ITEM> empty() {
        return (CollectionOptional<C, ITEM>) EMPTY;
    }

    private static <C extends Collection<ITEM>, ITEM> CollectionOptional<C, ITEM> of(C collection, boolean filterNulls) {
        return new CollectionOptional<>(collection, filterNulls);
    }

    public void each(Consumer<? super ITEM> action) {
        Objects.requireNonNull(action);
        if (null != this.collection) {
            for (ITEM t : this.collection) {
                if (this.filterNulls && null == t) {
                    continue;
                }
                action.accept(t);
            }
        }
    }

    public void each(BiConsumer<Integer, ? super ITEM> action) {
        Objects.requireNonNull(action);
        if (null != this.collection) {
            int index = 0;
            for (ITEM t : this.collection) {
                if (this.filterNulls && null == t) {
                    continue;
                }
                action.accept(index++, t);
            }
        }
    }

    public <K> void flatEach(Function<ITEM, Collection<K>> function, BiConsumer<ITEM, ? super K> action) {
        Objects.requireNonNull(function);
        Objects.requireNonNull(action);
        if (null != this.collection) {
            for (ITEM t : this.collection) {
                if (this.filterNulls && null == t) {
                    continue;
                }
                CollectionOptional.ofEmpty(function.apply(t)).each(k -> action.accept(t, k));
            }
        }
    }

    public ITEM firstOrDefault(ITEM item) {
        return getOrDefault(0, item);
    }

    public Optional<ITEM> first() {
        return Optional.ofNullable(getOrDefault(0, null));
    }

    @SuppressWarnings("unchecked")
    public ITEM getOrDefault(int index, ITEM item) {
        if (null == collection) {
            return item;
        }
        if (collection instanceof List) {
            return (ITEM) ((List) collection).get(index);
        }
        return (ITEM)(collection.toArray()[index]);
    }

    public Optional<ITEM> get(int index) {
        return Optional.ofNullable(getOrDefault(index, null));
    }

    public C orElse(C c) {
        return CollectionKit.isEmpty(collection) ? c : collection;
    }

    /**
     * If values which every item applied @param function is same, return the value or null
     * @param function
     * @param <R>
     * @return
     */
    public <R> Optional<R> everySame(Function<? super ITEM, R> function) {
        if (null == collection) {
            return Optional.ofNullable(null);
        }
        R result = null;
        int index = 0;
        for (ITEM t : this.collection) {
            if (this.filterNulls && null == t) {
                continue;
            }
            if (index > 0 && !result.equals(function.apply(t))) {
                return Optional.ofNullable(null);
            }
            result = function.apply(t);
            index ++;
        }
        return Optional.ofNullable(result);
    }

    public void ifPresent(Consumer<? super Collection<ITEM>> consumer) {
        if (collection != null) {
            consumer.accept(collection);
        }
    }

    public void ifSingleton(Consumer<ITEM> consumer) {
        ITEM item;
        if (collection != null && !collection.isEmpty() && (item = collection.iterator().next()) != null ) {
            consumer.accept(item);
        }
    }
}
