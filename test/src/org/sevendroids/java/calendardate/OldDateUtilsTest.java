package org.sevendroids.java.calendardate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.util.Calendar;

import org.junit.Test;
import org.sevendroids.java.calendardateapi.OldDateUtils;
import org.sevendroids.java.calendardateapi.OldDateUtils.DayType;

/**
 * @author 7droids.org
 *
 */
public class OldDateUtilsTest {
    private static final Calendar friday;
    private static final Calendar saturday;
    private static final Calendar sunday;
    private static final Calendar monday;
    private static final Calendar tuesday;

    static {
	friday = Calendar.getInstance();
	friday.set(2014, Calendar.FEBRUARY, 28, 0, 0, 0);
	friday.set(Calendar.MILLISECOND, 0);
	saturday = Calendar.getInstance();
	saturday.set(2014, Calendar.MARCH, 1, 0, 0, 0);
	saturday.set(Calendar.MILLISECOND, 0);
	sunday = Calendar.getInstance();
	sunday.set(2014, Calendar.MARCH, 2, 0, 0, 0);
	sunday.set(Calendar.MILLISECOND, 0);
	monday = Calendar.getInstance();
	monday.set(2014, Calendar.MARCH, 3, 0, 0, 0);
	monday.set(Calendar.MILLISECOND, 0);
	tuesday = Calendar.getInstance();
	tuesday.set(2014, Calendar.MARCH, 4, 0, 0, 0);
	tuesday.set(Calendar.MILLISECOND, 0);
    }

    private DateFormat dateFormat = DateFormat.getDateTimeInstance();

    @Test
    public final void testFridayIsNoWeekend() {
	assertFalse(OldDateUtils.isWeekend(friday));
    }

    @Test
    public final void testSatusdayIsWeekend() {
	assertTrue(OldDateUtils.isWeekend(saturday));
    }

    @Test
    public final void testSundayIsWeekend() {
	assertTrue(OldDateUtils.isWeekend(sunday));
    }

    @Test
    public final void testMondayIsNoWeekend() {
	assertFalse(OldDateUtils.isWeekend(monday));
    }

    @Test
    public final void testPreviousWorkingDayForTuesdayIsMonday() {
	assertEquals(
		dateFormat.format(monday.getTime()) + " <-- "
			+ dateFormat.format(tuesday.getTime()), monday,
		OldDateUtils.getPreviousWorkingDay(tuesday));
    }

    @Test
    public final void testPreviousWorkingDayForMondayIsFriday() {
	assertEquals(
		dateFormat.format(friday.getTime()) + " <-- "
			+ dateFormat.format(monday.getTime()), friday,
		OldDateUtils.getPreviousWorkingDay(monday));
    }

    @Test
    public final void testPreviousWorkingDayForSundayIsFriday() {
	assertEquals(
		dateFormat.format(friday.getTime()) + " <-- "
			+ dateFormat.format(sunday.getTime()), friday,
		OldDateUtils.getPreviousWorkingDay(sunday));
    }

    @Test
    public final void testPreviousWorkingDayForSaturdayIsFriday() {
	assertEquals(
		dateFormat.format(friday.getTime()) + " <-- "
			+ dateFormat.format(saturday.getTime()), friday,
		OldDateUtils.getPreviousWorkingDay(saturday));
    }

    @Test
    public final void test30_03_2014IsChangeDayToDST() {
	Calendar date30032014 = Calendar.getInstance();
	date30032014.set(2014, Calendar.MARCH, 30);
	assertEquals(DayType.TO_DAYLIGHT_SAVING_TIME,
		OldDateUtils.getDSTType(date30032014));
    }

    @Test
    public final void test29_03_2014IsStandardTime() {
	Calendar date29032014 = Calendar.getInstance();
	date29032014.set(2014, Calendar.MARCH, 29);
	assertEquals(DayType.STANDARD_TIME,
		OldDateUtils.getDSTType(date29032014));
    }

    @Test
    public final void test31_03_2014IsDST() {
	Calendar date31032014 = Calendar.getInstance();
	date31032014.set(2014, Calendar.MARCH, 31);
	assertEquals(DayType.DAYLIGHT_SAVING_TIME,
		OldDateUtils.getDSTType(date31032014));
    }

    @Test
    public final void test25_10_2014IsDST() {
	Calendar date25102014 = Calendar.getInstance();
	date25102014.set(2014, Calendar.OCTOBER, 25);
	assertEquals(DayType.DAYLIGHT_SAVING_TIME,
		OldDateUtils.getDSTType(date25102014));
    }

    @Test
    public final void test26_10_2014IsChangeDayToStandardTime() {
	Calendar date26102014 = Calendar.getInstance();
	date26102014.set(2014, Calendar.OCTOBER, 26);
	assertEquals(DayType.TO_STANDARD_TIME,
		OldDateUtils.getDSTType(date26102014));
    }

    @Test
    public final void test27_10_2014IsStandardTime() {
	Calendar date27102014 = Calendar.getInstance();
	date27102014.set(2014, Calendar.OCTOBER, 27);
	assertEquals(DayType.STANDARD_TIME,
		OldDateUtils.getDSTType(date27102014));
    }

    @Test
    public final void test2DatesInMarchReturn0Quarter() {
	Calendar date01032014 = Calendar.getInstance();
	date01032014.set(2014, Calendar.MARCH, 1);
	Calendar date02032014 = Calendar.getInstance();
	date02032014.set(2014, Calendar.MARCH, 2);
	assertEquals(0, OldDateUtils.getQuartersBetweenDates(date01032014,
		date02032014));
    }

    @Test
    public final void test1DateInMarchAnd1DateInAprilReturn1Quarter() {
	Calendar date01032014 = Calendar.getInstance();
	date01032014.set(2014, Calendar.MARCH, 1);
	Calendar date02042014 = Calendar.getInstance();
	date02042014.set(2014, Calendar.APRIL, 2);
	assertEquals(1, OldDateUtils.getQuartersBetweenDates(date01032014,
		date02042014));
    }

    @Test
    public final void test1DateInAprilAnd1DateInMarchReturn1Quarter() {
	Calendar date01032014 = Calendar.getInstance();
	date01032014.set(2014, Calendar.MARCH, 1);
	Calendar date02042014 = Calendar.getInstance();
	date02042014.set(2014, Calendar.APRIL, 2);
	assertEquals(1, OldDateUtils.getQuartersBetweenDates(date02042014,
		date01032014));
    }

    @Test
    public final void test1DateInMarchAnd1DateInOctoberPlusOneYearReturn7Quarter() {
	Calendar date01032014 = Calendar.getInstance();
	date01032014.set(2014, Calendar.MARCH, 1);
	Calendar date02102015 = Calendar.getInstance();
	date02102015.set(2015, Calendar.OCTOBER, 2);
	assertEquals(7, OldDateUtils.getQuartersBetweenDates(date01032014,
		date02102015));
    }

    @Test
    public final void test1DateInMarchAnd1DateInOctoberMinusOneYearReturn2Quarter() {
	Calendar date01032014 = Calendar.getInstance();
	date01032014.set(2014, Calendar.MARCH, 1);
	Calendar date02102013 = Calendar.getInstance();
	date02102013.set(2013, Calendar.OCTOBER, 2);
	assertEquals(1, OldDateUtils.getQuartersBetweenDates(date02102013,
		date01032014));
    }

    @Test
    public final void testEndOfMonthForFeb2014Gives28Feb2014() {
	Calendar in = Calendar.getInstance();
	in.set(2014, Calendar.FEBRUARY, 3, 12, 13, 14);
	Calendar expected = Calendar.getInstance();
	expected.set(2014, Calendar.FEBRUARY, 28, 0, 0, 0);
	expected.set(Calendar.MILLISECOND, 0);
	assertEquals(expected, OldDateUtils.endOfMonth(in));
    }

    @Test
    public final void testEndOfMonthForFeb2012Gives29Feb2012() {
	Calendar in = Calendar.getInstance();
	in.set(2012, Calendar.FEBRUARY, 3, 12, 13, 14);
	Calendar expected = Calendar.getInstance();
	expected.set(2012, Calendar.FEBRUARY, 29, 0, 0, 0);
	expected.set(Calendar.MILLISECOND, 0);
	assertEquals(expected, OldDateUtils.endOfMonth(in));
    }

    @Test
    public final void testBeginOfSeasonFor28022014Is01102013() {
	Calendar in = Calendar.getInstance();
	in.set(2014, Calendar.FEBRUARY, 28, 12, 13, 14);
	Calendar expected = Calendar.getInstance();
	expected.set(2013, Calendar.OCTOBER, 1, 0, 0, 0);
	expected.set(Calendar.MILLISECOND, 0);
	assertEquals(expected, OldDateUtils.beginOfSeason(in));
    }

    @Test
    public final void testBeginOfSeasonFor13042014Is01042013() {
	Calendar in = Calendar.getInstance();
	in.set(2014, Calendar.APRIL, 13, 12, 13, 14);
	Calendar expected = Calendar.getInstance();
	expected.set(2014, Calendar.APRIL, 1, 0, 0, 0);
	expected.set(Calendar.MILLISECOND, 0);
	assertEquals(expected, OldDateUtils.beginOfSeason(in));
    }

    @Test
    public final void testBeginOfSeasonFor13122014Is01102013() {
	Calendar in = Calendar.getInstance();
	in.set(2014, Calendar.DECEMBER, 13, 12, 13, 14);
	Calendar expected = Calendar.getInstance();
	expected.set(2014, Calendar.OCTOBER, 1, 0, 0, 0);
	expected.set(Calendar.MILLISECOND, 0);
	assertEquals(expected, OldDateUtils.beginOfSeason(in));
    }

    @Test
    public final void testEndOfSeasonFor28022014Is31032014() {
	Calendar in = Calendar.getInstance();
	in.set(2014, Calendar.FEBRUARY, 28, 12, 13, 14);
	Calendar expected = Calendar.getInstance();
	expected.set(2014, Calendar.MARCH, 31, 0, 0, 0);
	expected.set(Calendar.MILLISECOND, 0);
	assertEquals(expected, OldDateUtils.endOfSeason(in));
    }

    @Test
    public final void testEndOfSeasonFor13042014Is30092014() {
	Calendar in = Calendar.getInstance();
	in.set(2014, Calendar.APRIL, 13, 12, 13, 14);
	Calendar expected = Calendar.getInstance();
	expected.set(2014, Calendar.SEPTEMBER, 30, 0, 0, 0);
	expected.set(Calendar.MILLISECOND, 0);
	assertEquals(expected, OldDateUtils.endOfSeason(in));
    }

    @Test
    public final void testEndOfSeasonFor13122014Is31032015() {
	Calendar in = Calendar.getInstance();
	in.set(2014, Calendar.DECEMBER, 13, 12, 13, 14);
	Calendar expected = Calendar.getInstance();
	expected.set(2015, Calendar.MARCH, 31, 0, 0, 0);
	expected.set(Calendar.MILLISECOND, 0);
	assertEquals(expected, OldDateUtils.endOfSeason(in));
    }
}
