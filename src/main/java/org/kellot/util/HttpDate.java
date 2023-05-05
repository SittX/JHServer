package org.kellot.util;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HttpDate {
    /**
     * Get the current date and time in the UTC timezone and format it in the HTTP "Date" header format.
     * Format -> Fri, 31 Dec 1999 23:59:59 GMT
     *
     * @return String of "Fri, 31 Dec 1999 23:59:59 GMT"
     */
    public static String setCurrentDateTime() {
        DateTimeFormatter httpDateFormat = DateTimeFormatter.RFC_1123_DATE_TIME;
        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        return httpDateFormat.format(currentDateTime);
    }

    /**
     * Same as getHttpDate() method but it adds some additional hours for expiry date.
     *
     * @param durationHour
     * @return String of "Fri, 31 Dec 1999 23:59:59 GMT"
     */
    public static String setExpiryDateTime(long durationHour) {
        DateTimeFormatter httpDateFormat = DateTimeFormatter.RFC_1123_DATE_TIME;
        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneOffset.UTC).plusHours(durationHour);
        return httpDateFormat.format(currentDateTime);
    }
}
