package com.chaos;

import org.joda.time.DateTime;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Created by chaos on 2018/8/17.
 */
public interface DateTimeKit {

    static ZonedDateTime translateFrom(DateTime dateTime) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(dateTime.getMillis()), ZoneId.of(dateTime.getZone().getID()));
    }
}
