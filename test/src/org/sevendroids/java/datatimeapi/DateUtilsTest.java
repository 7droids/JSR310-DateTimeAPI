package org.sevendroids.java.datatimeapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Test;
import org.sevendroids.java.datatimeapi.DateUtils.DayType;

/**
 * @author 7droids.org
 *
 */
public class DateUtilsTest {
    private LocalDate friday = LocalDate.of(2014, Month.FEBRUARY, 28);
    private LocalDate saturday = LocalDate.of(2014, Month.MARCH, 1);
    private LocalDate sunday = LocalDate.of(2014, Month.MARCH, 2);
    private LocalDate monday = LocalDate.of(2014, Month.MARCH, 3);
    private LocalDate tuesday = LocalDate.of(2014, Month.MARCH, 4);

    @Test
    public final void testFridayIsNotWeekend() {
	assertFalse(DateUtils.isWeekend(friday));
    }

    @Test
    public final void testSaturdayIsNotWeekend() {
	assertTrue(DateUtils.isWeekend(saturday));
    }

    @Test
    public final void testSundayIsWeekend() {
	assertTrue(DateUtils.isWeekend(sunday));
    }

    @Test
    public final void testMondayIsNotWeekend() {
	assertFalse(DateUtils.isWeekend(monday));
    }

    @Test
    public final void testPreviousWorkingDayForTuesdayIsMonday() {
	assertEquals(monday, DateUtils.getPreviousWorkingDay(tuesday));
    }

    @Test
    public final void testPreviousWorkingDayForMondayIsFriday() {
	assertEquals(friday, DateUtils.getPreviousWorkingDay(monday));
    }

    @Test
    public final void testPreviousWorkingDayForSundayIsFriday() {
	assertEquals(friday, DateUtils.getPreviousWorkingDay(sunday));
    }

    @Test
    public final void testPreviousWorkingDayForSaturdayIsFriday() {
	assertEquals(friday, DateUtils.getPreviousWorkingDay(saturday));
    }

    @Test
    public final void test30_03_2014IsChangeDayToDST() {
	LocalDate date30032014 = LocalDate.of(2014, Month.MARCH, 30);
	assertEquals(DayType.TO_DAYLIGHT_SAVING_TIME,
		DateUtils.getDSTType(date30032014));
    }

    @Test
    public final void test30_03_2014IsChangeDayToDSTIndependedOfTime() {
	LocalDateTime date30032014 = LocalDateTime.of(2014, Month.MARCH, 30, 3,
		16);
	assertEquals(DayType.TO_DAYLIGHT_SAVING_TIME,
		DateUtils.getDSTType(date30032014));
    }

    @Test
    public final void test29_03_2014IsStandardTime() {
	LocalDate date29032014 = LocalDate.of(2014, Month.MARCH, 29);
	assertEquals(DayType.STANDARD_TIME, DateUtils.getDSTType(date29032014));
    }

    @Test
    public final void test31_03_2014IsDST() {
	LocalDate date31032014 = LocalDate.of(2014, Month.MARCH, 31);
	assertEquals(DayType.DAYLIGHT_SAVING_TIME,
		DateUtils.getDSTType(date31032014));
    }

    @Test
    public final void test25_10_2014IsDST() {
	LocalDate date25102014 = LocalDate.of(2014, Month.OCTOBER, 25);
	assertEquals(DayType.DAYLIGHT_SAVING_TIME,
		DateUtils.getDSTType(date25102014));
    }

    @Test
    public final void test26_10_2014IsChangeDayToStandardTime() {
	LocalDate date26102014 = LocalDate.of(2014, Month.OCTOBER, 26);
	assertEquals(DayType.TO_STANDARD_TIME,
		DateUtils.getDSTType(date26102014));
    }

    @Test
    public final void test27_10_2014IsStandardTime() {
	LocalDate date27102014 = LocalDate.of(2014, Month.OCTOBER, 27);
	assertEquals(DayType.STANDARD_TIME, DateUtils.getDSTType(date27102014));
    }

    @Test
    public final void test2DatesInMarchReturn0Quarter() {
	LocalDate date01032014 = LocalDate.of(2014, Month.MARCH, 1);
	LocalDate date02032014 = LocalDate.of(2014, Month.MARCH, 2);
	assertEquals(0,
		DateUtils.getQuartersBetweenDates(date01032014, date02032014));
    }

    @Test
    public final void test1DateInMarchAnd1DateInAprilReturn1Quarter() {
	LocalDate date01032014 = LocalDate.of(2014, Month.MARCH, 1);
	LocalDate date02042014 = LocalDate.of(2014, Month.APRIL, 2);
	assertEquals(1,
		DateUtils.getQuartersBetweenDates(date01032014, date02042014));
    }

    @Test
    public final void test1DateInAprilAnd1DateInMarchReturn1Quarter() {
	LocalDate date01032014 = LocalDate.of(2014, Month.MARCH, 1);
	LocalDate date02042014 = LocalDate.of(2014, Month.APRIL, 2);
	assertEquals(1,
		DateUtils.getQuartersBetweenDates(date02042014, date01032014));
    }

    @Test
    public final void test1DateInMarchAnd1DateInOctoberPlusOneYearReturn7Quarter() {
	LocalDate date01032014 = LocalDate.of(2014, Month.MARCH, 1);
	LocalDate date02102015 = LocalDate.of(2015, Month.OCTOBER, 2);
	assertEquals(7,
		DateUtils.getQuartersBetweenDates(date01032014, date02102015));
    }

    @Test
    public final void test1DateInMarchAnd1DateInOctoberMinusOneYearReturn2Quarter() {
	LocalDate date01032014 = LocalDate.of(2014, Month.MARCH, 1);
	LocalDate date02102015 = LocalDate.of(2013, Month.OCTOBER, 2);
	assertEquals(1,
		DateUtils.getQuartersBetweenDates(date02102015, date01032014));
    }

    @Test
    public final void testEndOfMonthForFeb2014Gives28Feb2014() {
	assertEquals(LocalDate.of(2014, Month.FEBRUARY, 28),
		DateUtils.endOfMonth(LocalDate.of(2014, Month.FEBRUARY, 3)));
    }

    @Test
    public final void testEndOfMonthForFeb2012Gives29Feb2012() {
	assertEquals(LocalDate.of(2012, Month.FEBRUARY, 29),
		DateUtils.endOfMonth(LocalDate.of(2012, Month.FEBRUARY, 3)));
    }

    @Test
    public final void testBeginOfSeasonFor28022014Is01102013() {
	LocalDate in = LocalDate.of(2014, Month.FEBRUARY, 28);
	LocalDate expected = LocalDate.of(2013, Month.OCTOBER, 1);
	assertEquals(expected, DateUtils.beginOfSeason(in));
    }

    @Test
    public final void testBeginOfSeasonFor13042014Is01042013() {
	LocalDate in = LocalDate.of(2014, Month.APRIL, 13);
	LocalDate expected = LocalDate.of(2014, Month.APRIL, 1);
	assertEquals(expected, DateUtils.beginOfSeason(in));
    }

    @Test
    public final void testBeginOfSeasonFor13122014Is01102013() {
	LocalDate in = LocalDate.of(2014, Month.DECEMBER, 13);
	LocalDate expected = LocalDate.of(2014, Month.OCTOBER, 1);
	assertEquals(expected, DateUtils.beginOfSeason(in));
    }

    @Test
    public final void testEndOfSeasonFor28022014Is31032014() {
	LocalDate in = LocalDate.of(2014, Month.FEBRUARY, 28);
	LocalDate expected = LocalDate.of(2014, Month.MARCH, 31);
	assertEquals(expected, DateUtils.endOfSeason(in));
    }

    @Test
    public final void testEndOfSeasonFor13042014Is20092014() {
	LocalDate in = LocalDate.of(2014, Month.APRIL, 13);
	LocalDate expected = LocalDate.of(2014, Month.SEPTEMBER, 30);
	assertEquals(expected, DateUtils.endOfSeason(in));
    }

    @Test
    public final void testEndOfSeasonFor13122014Is31032015() {
	LocalDate in = LocalDate.of(2014, Month.DECEMBER, 13);
	LocalDate expected = LocalDate.of(2015, Month.MARCH, 31);
	assertEquals(expected, DateUtils.endOfSeason(in));
    }
}
