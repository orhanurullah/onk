package com.onk.core.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeUtil {

    public static final String PATTERN_DATE_TIME = "yyyy-MM-dd hh:mm:ss";

    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneOffset.of("+03:00"));
    }
}
