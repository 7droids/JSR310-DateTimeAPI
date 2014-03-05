package org.sevendroids.java.datatimeapi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

/**
 * This class provides some useful utility methods for date and time operations.
 * The implementations is done using the new date and time api in Java8.
 * 
 * @see add further links
 *
 * @author created: 7droids.org on 27.02.2014 20:45:58
 * @author last change: $Author: $ on $Date: $
 * @version $Revision: $
 */
public class DateUtils {

    /**
     * Constructor.
     */
    private DateUtils() {
	super();
    }

    /**
     * This method checks whether the given date object is representing a date
     * at the weekend (Saturday or Sunday)
     * 
     * @param date
     *            Date to check, cannot be null
     * @return TRUE is Saturday or Sunday
     */
    public static boolean isWeekend(LocalDate date) {
	DayOfWeek dayOfWeek = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
	switch (dayOfWeek) {
	case SATURDAY:
	case SUNDAY:
	    return true;
	default:
	    return false;
	}
    }

    /**
     * The methods calculates the previous working day. It only recognize
     * Saturday and Sunday as non -working days.
     * 
     * @param date
     *            Date as starting point for the calculation, cannot be null
     * @return The previous working day
     */
    public static LocalDate getPreviousWorkingDay(LocalDate date) {
	DayOfWeek dayOfWeek = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
	switch (dayOfWeek) {
	case MONDAY:
	    return date.minus(3, ChronoUnit.DAYS);
	case SUNDAY:
	    return date.minus(2, ChronoUnit.DAYS);
	default:
	    return date.minus(1, ChronoUnit.DAYS);

	}
    }

    public static enum DayType {
	STANDARD_TIME, DAYLIGHT_SAVING_TIME, TO_DAYLIGHT_SAVING_TIME, TO_STANDARD_TIME
    };

    /**
     * Checks if the type of the given date. Possible return values are standard
     * time, the date when to switch to daylight saving time (in Europe the last
     * Sunday in March), daylight saving time or the date when to switch back to
     * standard time (in Europe the last Sunday in October).
     * 
     * @return DayType
     * @param cal
     *            Date to check, cannot be null
     */
    public static DayType getDSTType(LocalDate cal) {
	DayType status = DayType.DAYLIGHT_SAVING_TIME;
	LocalDateTime testDate = cal.atStartOfDay();
	ZonedDateTime zdt = ZonedDateTime.of(testDate, ZoneId.systemDefault());
	// Find type of day
	if (zdt.getZone().getRules()
		.isDaylightSavings(testDate.toInstant(zdt.getOffset())))
	    status = DayType.DAYLIGHT_SAVING_TIME;
	else
	    status = DayType.STANDARD_TIME;
	// Check the day after
	testDate = testDate.plusDays(1);
	zdt = ZonedDateTime.of(testDate, ZoneId.systemDefault());
	// Find type of day after
	if (zdt.getZone().getRules()
		.isDaylightSavings(testDate.toInstant(zdt.getOffset()))) {
	    if (status != DayType.DAYLIGHT_SAVING_TIME)
		status = DayType.TO_DAYLIGHT_SAVING_TIME;
	} else {
	    if (status == DayType.DAYLIGHT_SAVING_TIME)
		status = DayType.TO_STANDARD_TIME;
	}
	return status;
    }

    /**
     * Helper method to redirect LocalDateTime objects to the method with
     * LocalDate.
     * 
     * @param cal
     * @return
     */
    public static DayType getDSTType(LocalDateTime cal) {
	return getDSTType(cal.toLocalDate());
    }

    /**
     * Returns the current quarter of the given date
     * 
     * @return int (0 .. 3)
     * @param cal
     *            Given date, cannot be null
     */
    public static int getQuarter(LocalDate cal) {
	int month = cal.get(ChronoField.MONTH_OF_YEAR);
	switch (Month.of(month)) {
	case JANUARY:
	case FEBRUARY:
	case MARCH:
	default:
	    return 0;
	case APRIL:
	case MAY:
	case JUNE:
	    return 1;
	case JULY:
	case AUGUST:
	case SEPTEMBER:
	    return 2;
	case OCTOBER:
	case NOVEMBER:
	case DECEMBER:
	    return 3;
	}
    }

    /**
     * Calculates the number of quarters between two given dates
     * 
     * @return Number of quarters
     * @param date1
     *            First given date cannot be null
     * @param date2
     *            Second given date cannot be null
     */
    public static int getQuartersBetweenDates(LocalDate date1, LocalDate date2) {
	LocalDate beginn = null;
	LocalDate end = null;
	if (date1.isBefore(date2)) {
	    beginn = date1;
	    end = date2;
	} else {
	    beginn = date2;
	    end = date1;
	}
	int quarters = getQuarter(end) - getQuarter(beginn);
	int years = end.get(ChronoField.YEAR) - beginn.get(ChronoField.YEAR);
	quarters += years * 4;
	return Math.abs(quarters);
    }

    /**
     * Creates a new date object with the last day of the same month as the
     * given date.
     */
    public static LocalDate endOfMonth(LocalDate date) {
	return date.withDayOfMonth(date.lengthOfMonth());
    }

    /**
     * Creates a new date object with the first date in the same season as the
     * given date. A season is defined as a period from April to September and
     * from October to March.
     */
    public static LocalDate beginOfSeason(LocalDate date) {
	int nMonth = date.get(ChronoField.MONTH_OF_YEAR);
	switch (Month.of(nMonth)) {
	case JANUARY:
	case FEBRUARY:
	case MARCH:
	    // Jan-Mar --> move to the previous year 1. October
	    return date.minusMonths(
		    nMonth + Month.DECEMBER.getValue()
			    - Month.OCTOBER.getValue()).withDayOfMonth(1);
	case APRIL:
	case MAY:
	case JUNE:
	case JULY:
	case AUGUST:
	case SEPTEMBER:
	    // Apr-Sep --> move to 1. April
	    return date.minusMonths(nMonth - Month.APRIL.getValue())
		    .withDayOfMonth(1);
	default:
	    // Oct-Dec --> move to 1. October
	    return date.minusMonths(nMonth - Month.OCTOBER.getValue())
		    .withDayOfMonth(1);
	}
    }

    /**
     * Creates a new date object with the last day of the season of the given
     * date. The time is set to 0 o'clock.
     */
    public static LocalDate endOfSeason(LocalDate date) {
	LocalDate result = beginOfSeason(date);
	return endOfMonth(result.plusMonths(5));
    }
}
