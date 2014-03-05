package org.sevendroids.java.calendardateapi;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * This class represents the original implementation using the old Date and
 * Calendar available before Java8.
 * 
 * @see add further links
 *
 * @author created: 7droids.org on 27.02.2014 20:52:50
 * @author last change: $Author: $ on $Date: $
 * @version $Revision: $
 */
public class OldDateUtils {

    /**
     * Private constructor.
     */
    private OldDateUtils() {
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
    public static boolean isWeekend(Calendar cal) {
	int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
	switch (dayOfWeek) {
	case Calendar.SATURDAY:
	case Calendar.SUNDAY:
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
    public static Calendar getPreviousWorkingDay(Calendar cal) {
	Calendar result = Calendar.getInstance();
	result.setTimeInMillis(cal.getTimeInMillis());
	int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
	switch (dayOfWeek) {
	case Calendar.MONDAY:
	    result.set(Calendar.DAY_OF_MONTH,
		    cal.get(Calendar.DAY_OF_MONTH) - 3);
	    break;
	case Calendar.SUNDAY:
	    result.set(Calendar.DAY_OF_MONTH,
		    cal.get(Calendar.DAY_OF_MONTH) - 2);
	    break;
	default:
	    result.set(Calendar.DAY_OF_MONTH,
		    cal.get(Calendar.DAY_OF_MONTH) - 1);
	}
	return result;
    }

    public static enum DayType {
	STANDARD_TIME, DAYLIGHT_SAVING_TIME, TO_DAYLIGHT_SAVING_TIME, TO_STANDARD_TIME
    };

    /**
     * Checks if the type of the given date. The given date will first set to 0
     * o'clock and afterwards the type will be calculated. Possible return
     * values are standard time, the date when to switch to daylight saving time
     * (in Europe the last Sunday in March), daylight saving time or the date
     * when to switch back to standard time (in Europe the last Sunday in
     * October).
     * 
     * @return DayType
     * @param cal
     *            Date to check, cannot be null
     */
    public static DayType getDSTType(Calendar cal) {
	DayType status = DayType.DAYLIGHT_SAVING_TIME;
	Calendar result = Calendar.getInstance();
	result.setTimeInMillis(cal.getTimeInMillis());
	setTimeToZero(result);
	TimeZone zone = result.getTimeZone();
	// Find type of day
	if (zone.inDaylightTime(result.getTime()) == true)
	    status = DayType.DAYLIGHT_SAVING_TIME;
	else
	    status = DayType.STANDARD_TIME;
	// Check the day after
	result.add(Calendar.DAY_OF_MONTH, 1);
	// Find type of day after
	if (zone.inDaylightTime(result.getTime()) == true) {
	    if (status != DayType.DAYLIGHT_SAVING_TIME)
		status = DayType.TO_DAYLIGHT_SAVING_TIME;
	} else {
	    if (status == DayType.DAYLIGHT_SAVING_TIME)
		status = DayType.TO_STANDARD_TIME;
	}
	return status;
    }

    private static void setTimeToZero(Calendar result) {
	result.set(Calendar.HOUR_OF_DAY, 0);
	result.set(Calendar.MINUTE, 0);
	result.set(Calendar.SECOND, 0);
	result.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Returns the current quarter of the given date
     * 
     * @return int (0 .. 3)
     * @param cal
     *            Given date, cannot be null
     */
    public static int getQuarter(Calendar cal) {
	int month = cal.get(Calendar.MONTH);
	switch (month) {
	case Calendar.JANUARY:
	case Calendar.FEBRUARY:
	case Calendar.MARCH:
	default:
	    return 0;
	case Calendar.APRIL:
	case Calendar.MAY:
	case Calendar.JUNE:
	    return 1;
	case Calendar.JULY:
	case Calendar.AUGUST:
	case Calendar.SEPTEMBER:
	    return 2;
	case Calendar.OCTOBER:
	case Calendar.NOVEMBER:
	case Calendar.DECEMBER:
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
    public static int getQuartersBetweenDates(Calendar date1, Calendar date2) {
	Calendar beginn = Calendar.getInstance();
	Calendar end = Calendar.getInstance();
	if (date1.before(date2)) {
	    beginn.setTimeInMillis(date1.getTimeInMillis());
	    end.setTimeInMillis(date2.getTimeInMillis());
	} else {
	    beginn.setTimeInMillis(date2.getTimeInMillis());
	    end.setTimeInMillis(date1.getTimeInMillis());
	}
	int quarters = getQuarter(end) - getQuarter(beginn);
	int years = end.get(Calendar.YEAR) - beginn.get(Calendar.YEAR);
	quarters += years * 4;
	return Math.abs(quarters);
    }

    /**
     * Creates a new date object with the last day of the same month as the
     * given date.
     */
    public static Calendar endOfMonth(Calendar cal) {
	Calendar result = Calendar.getInstance();
	result.setTimeInMillis(cal.getTimeInMillis());
	result.add(Calendar.MONTH, 1);
	result.set(Calendar.DAY_OF_MONTH, 1);
	setTimeToZero(result);
	result.add(Calendar.DAY_OF_MONTH, -1);
	return result;
    }

    /**
     * Creates a new date object with the first date in the same season as the
     * given date. A season is defined as a period from April to September and
     * from October to March. The time is set to 0 o'clock
     */
    public static Calendar beginOfSeason(Calendar cal) {
	Calendar result = Calendar.getInstance();
	result.setTimeInMillis(cal.getTimeInMillis());
	int nMonth = cal.get(Calendar.MONTH);
	if (nMonth < Calendar.APRIL) { // Jan-Mar --> move to previous year
	    result.add(Calendar.MONTH, -(nMonth + 3));
	} else if (nMonth < Calendar.OCTOBER) { // Apr-Sep
	    result.add(Calendar.MONTH, (3 - nMonth));
	} else { // Okt-Dec
	    result.add(Calendar.MONTH, (9 - nMonth));
	}
	result.set(Calendar.DAY_OF_MONTH, 1);
	setTimeToZero(result);
	return result;
    }

    /**
     * Creates a new date object with the last day of the season of the given
     * date. The time is set to 0 o'clock.
     */
    public static Calendar endOfSeason(Calendar cal) {
	Calendar result = beginOfSeason(cal);
	result.add(Calendar.MONTH, 5);
	return endOfMonth(result);
    }
}
