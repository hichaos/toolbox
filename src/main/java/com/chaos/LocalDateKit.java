package com.chaos;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by chaos on 2018/6/25.
 */
public interface LocalDateKit {

    static List<LocalDate> flatLocalDate(String start, String end) {
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return flatLocalDate(startDate, endDate);
    }

    static List<LocalDate> flatLocalDate(LocalDate startDate, LocalDate endDate) {
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);
        return IntStream.rangeClosed(0, Days.daysBetween(startDate, endDate).getDays())
                        .mapToObj(startDate::plusDays)
                        .collect(Collectors.toList());
    }

    static List<String> format(List<LocalDate> localDateList) {
        Objects.requireNonNull(localDateList);
        return localDateList.stream().map(String::valueOf).collect(Collectors.toList());
    }

    static List<String> format(List<LocalDate> localDateList, String pattern) {
        Objects.requireNonNull(localDateList);
        Objects.requireNonNull(pattern);
        return localDateList.stream().map(localDate -> localDate.toString(pattern)).collect(Collectors.toList());
    }

    static LocalDate maxDate(List<LocalDate> localDateList) {
        Objects.requireNonNull(localDateList);
        return LocalDate.parse(max(format(localDateList)));
    }

    static LocalDate minDate(List<LocalDate> localDateList) {
        Objects.requireNonNull(localDateList);
        return LocalDate.parse(min(format(localDateList)));
    }

    static String max(List<String> localDateList) {
        Objects.requireNonNull(localDateList);
        return localDateList.stream().max(Comparator.naturalOrder()).orElseThrow(IllegalStateException::new);
    }

    static String min(List<String> localDateList) {
        Objects.requireNonNull(localDateList);
        return localDateList.stream().min(Comparator.naturalOrder()).orElseThrow(IllegalStateException::new);
    }

    static void each(String start, String end, BiConsumer<Integer, String> biConsumer) {
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
        Objects.requireNonNull(biConsumer);
        CollectionOptional.ofEmpty(format(flatLocalDate(start, end))).each(biConsumer);
    }

    static void each(LocalDate start, LocalDate end, BiConsumer<Integer, LocalDate> biConsumer) {
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
        Objects.requireNonNull(biConsumer);
        CollectionOptional.ofEmpty(flatLocalDate(start, end)).each(biConsumer);
    }

    static List<Range<LocalDate>> range(List<String> localDateList) {
        return range(localDateList, 1);
    }

    static List<Range<LocalDate>> range(List<String> localDateList, int allowableInterval) {
        Objects.requireNonNull(localDateList);
        Preconditions.checkArgument(allowableInterval > 0);
        return localDateList.stream()
                            .sorted()
                            .map(LocalDate::parse)
                            .reduce(new ArrayList<Range<LocalDate>>(), (dateRanges, date) -> {
                                Range<LocalDate> dateRange;
                                if (CollectionKit.isEmpty(dateRanges)) {
                                    dateRanges.add(Range.singleton(date));
                                } else {
                                    dateRange = dateRanges.get(dateRanges.size() - 1);
                                    int days = Days.daysBetween(dateRange.upperEndpoint(), date).getDays();
                                    if (days > allowableInterval) {
                                        dateRanges.add(Range.singleton(date));
                                    } else {
                                        dateRanges.set(dateRanges.size() - 1, Range.closed(dateRange.lowerEndpoint(), date));
                                    }
                                }
                                return dateRanges;
                            }, (dateRanges, dateRanges2) -> {
                                dateRanges.addAll(dateRanges2);
                                return dateRanges;
                            });
    }
}
