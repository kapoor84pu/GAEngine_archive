package uk.co.cloud.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;

/**
 * <p>Utility to provide the following to applications:<br>
 * <ul>
 * <li>Access to various thread safe date and time processing utilities</li>
 * </ul>
 * </p>
 */
public class DateUtils {

  private static final int LEN_ISO_DATETIME_NO_TZ = 19;

  private static final DateTimeFormatter localISO8601DateTimeFormatter = DateTimeFormat.forPattern(
    "yyyy-MM-dd'T'HH:mm:ss");

  private static final DateTimeFormatter utcISO8601DateTimeFormatter =
    ISODateTimeFormat.dateTimeNoMillis().withZone(DateTimeZone.UTC);

  private static final DateTimeFormatter utcMillisISO8601DateTimeFormatter =
    ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC);

  private static final DateTimeFormatter utcISO8601DateFormatter =
    ISODateTimeFormat.yearMonthDay().withZone(DateTimeZone.UTC);

  private static final DateTimeFormatter utcForcedDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(DateTimeZone.UTC);
  private static final DateTimeFormatter utcMillisForcedDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").withZone(DateTimeZone.UTC);

  /**
   * 1 minute in seconds
   */
  public static final int ONE_MINUTE = 60;

  /**
   * 5 minutes in seconds
   */
  public static final int FIVE_MINUTES = 300;

  /**
   * 10 minutes in seconds
   */
  public static final int TEN_MINUTES = 600;

  /**
   * 1 hour in seconds
   */
  public static final int ONE_HOUR = 3600;

  /**
   * 1 day in seconds
   */
  public static final int ONE_DAY = 86400;

  /**
   * Parse the given string into a date and time in UTC<br/>
   * <p>
   * This parser checks for timezone information being present, and if so then the formats supported are:
   * <ul>
   * <li>"yyyy-MM-dd'T'HH:mm:ss+tt00" e.g. "2000-01-01T12:13:14+0100"</li>
   * <li>"yyyy-MM-dd'T'HH:mm:ss'Z'" e.g. "2000-01-01T12:13:14Z"</li>
   * <li>"yyyy-MM-dd'T'HH:mm:ss.ttt'Z'" e.g. "2000-01-01T12:13:14.567Z"</li>
   * </ul>
   * </p>
   * <p>
   * In accordance with the ISO8601 specification, if no timezone information is provided then the string
   * represents an instant <strong>in local time</strong>. This means that the fields present in the string
   * will be interpreted using the local timezone and daylight savings rules that would have been in force
   * at that moment. The final local time is then shifted into UTC before being returned.
   * <ul>
   * <li>"yyyy-MM-dd'T'HH:mm:ss" e.g. "2000-08-01T12:13:14" -> "2000-08-01T<strong>11</strong>:13:14Z" (local UK time with DST in August)</li>
   * </ul>
   * </p>
   * <br/>
   * <strong>Thread-safe</strong>
   *
   * @param iso8601DateTime The ISO8601 date time input format
   * @return A corresponding date and time
   */
  public static DateTime parseISO8601DateTime(String iso8601DateTime) {
    if (iso8601DateTime == null) {
      return null;
    }
    if (iso8601DateTime.length() <= LEN_ISO_DATETIME_NO_TZ) {
      // No timezone provided so assume date is in local time, and then move it to UTC
      return localISO8601DateTimeFormatter.parseDateTime(iso8601DateTime).withZone(DateTimeZone.UTC);
    }
    try {
      // Try first with no millis
      return utcISO8601DateTimeFormatter.parseDateTime(iso8601DateTime);
    } catch (IllegalArgumentException e) {
      // Fall back to include milliseconds
      return utcMillisISO8601DateTimeFormatter.parseDateTime(iso8601DateTime);
    }
  }

  /**
   * Parse the given string into a date in UTC
   * <br/><strong>Thread-safe</strong>
   *
   * @param iso8601DateTime The ISO8601 date input format ("yyyy-MM-dd")
   * @return A corresponding date and time
   */
  public static DateTime parseISO8601Date(String iso8601DateTime) {
    if (iso8601DateTime == null) {
      return null;
    }
    return utcISO8601DateFormatter.parseDateTime(iso8601DateTime);
  }

  /**
   * Parse the given <strong>local</strong> string into a date and time in UTC<br/>
   * <p>
   * This parser assumes no timezone is supplied, and the formats supported are:
   * <ul>
   * <li>"yyyy-MM-dd'T'HH:mm:ss" e.g. "2000-01-01T12:13:14"</li>
   * <li>"yyyy-MM-dd'T'HH:mm:ss" e.g. "2000-01-01T12:13:14.000"</li>
   * </ul>
   * </p>
   * <p>
   * This breaks the ISO8601 specification in that if no timezone information is provided then the string
   * represents an instant <strong>in UTC</strong>.
   * <ul>
   * <li>"yyyy-MM-dd'T'HH:mm:ss" e.g. "2000-08-01T12:13:14" -> "2000-08-01T<strong>11</strong>:13:14Z" (local UK time with DST in August)</li>
   * </ul>
   * </p>
   * <br/>
   * <strong>Threadsafe</strong>
   *
   * @param iso8601DateTime The ISO8601 date time input format
   * @return A corresponding date and time
   */
  public static DateTime parseISO8601DateTimeAsUtc(String iso8601DateTime) {

    if (iso8601DateTime == null) {
      return null;
    }
    // Attempt to guess the format
    if (iso8601DateTime.endsWith("Z")) {
      // Assume a timezone is present
      if (iso8601DateTime.contains(".")) {
        // Assume milliseconds are present
        return utcMillisISO8601DateTimeFormatter.parseDateTime(iso8601DateTime);
      } else {
        // Timezone, no millis
        return utcISO8601DateTimeFormatter.parseDateTime(iso8601DateTime);
      }
    } else {
      // Assume local time so force UTC
      if (iso8601DateTime.contains(".")) {
        // Assume milliseconds are present
        return utcMillisForcedDateTimeFormatter.parseDateTime(iso8601DateTime);
      } else {
        // No timezone, no millis
        return utcForcedDateTimeFormatter.parseDateTime(iso8601DateTime);
      }
    }
  }

  /**
   * Format the given DateTime into the ISO8601 date and time format in UTC "yyyy-MM-dd'T'HH:mm:ss'Z'" format
   * <br/><strong>Thread-safe</strong>
   *
   * @param dateTime The date and time
   * @return The formatted string
   */
  public static String format(ReadableInstant dateTime) {
    return utcISO8601DateTimeFormatter.print(dateTime);
  }

  /**
   * Format the given DateTime into the ISO8601 date and time format in UTC "yyyy-MM-dd'T'HH:mm:ss.ttt'Z'" format
   * <br/><strong>Thread-safe</strong>
   *
   * @param dateTime The date and time
   * @return The formatted string
   */
  public static String formatWithMillis(ReadableInstant dateTime) {
    return utcMillisISO8601DateTimeFormatter.print(dateTime);
  }

  /**
   * Creates a SQL Timestamp from a DateTime (with zeroed nanoseconds)
   * <br/><strong>Thread-safe</strong>
   *
   * @param dateTime The date and time
   * @return A SQL Timestamp based on the date and time  (or null)
   */
  public static Timestamp toTimestamp(ReadableInstant dateTime) {
    return dateTime == null ? null : new Timestamp(dateTime.getMillis());
  }

  /**
   * Creates a DateTime from a SQL Timestamp (dropping the nanoseconds component)
   * <br/><strong>Thread-safe</strong>
   *
   * @param timestamp The SQL timestamp
   * @return A date and time based on the timestamp (or null)
   */
  public static DateTime fromTimestamp(Timestamp timestamp) {
    return timestamp == null ? null : new DateTime(timestamp.getTime());
  }

  /**
   * Creates a DateTimeFormatter from a pattern string to allow for arbitrary formats to be supported
   * in a thread-safe and reusable manner.
   * <br/><strong>Thread-safe</strong>
   * Provides a quick initialisation process for a UTC date time formatter
   *
   * @param pattern A date format pattern
   * @return A thread safe and immutable instance of a DateTimeFormatter for the given pattern
   */
  public static DateTimeFormatter createDateTimeFormatter(String pattern) {
    return DateTimeFormat.forPattern(pattern).withZone(DateTimeZone.UTC);
  }

  /**
   * @param calendar required instance of XMLGregorianCalendar
   * @return DateTime instance representing the same instance as the calendar
   */
  public static DateTime fromXMLGregorianCalendar(XMLGregorianCalendar calendar) {
    if (calendar == null) {
      throw new IllegalArgumentException("'calendar' is required");
    }
    return new DateTime(calendar.toGregorianCalendar());
  }


  /**
   * Returns the current time with UTC as the TimeZone.
   * <p>shorthand for <code>new DateTime(DateTimeZone.UTC)</code></p>
   *
   * @return current time with the UTC zone set
   */
  public static DateTime nowUtc() {
    return new DateTime(DateTimeZone.UTC);
  }

  /**
   * Creates a DateTime instance with a time zone of UTC.
   *
   * @param year           the year
   * @param monthOfYear    the month of the year
   * @param dayOfMonth     the day of the month
   * @param hourOfDay      the hour of the day
   * @param minuteOfHour   the minute of the hour
   * @param secondOfMinute the second of the minute
   * @return new DateTime instance with the given fields, zero milliseconds and UTC zone.
   */
  public static DateTime utc(
    int year,
    int monthOfYear,
    int dayOfMonth,
    int hourOfDay,
    int minuteOfHour,
    int secondOfMinute) {
    return new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, 0,
      DateTimeZone.UTC);
  }
}
