package com.maryam;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class StopWatchTest {

    @Test
    public void testFormatForGivenDuration() {
        Instant now = Instant.now();
        assertEquals("00 : 01 : 00", StopWatch.
                format(Duration.between(now, now.plusMillis(1000))));
        assertEquals("01 : 00 : 00", StopWatch.
                format(Duration.between(now, now.plus(1, TimeUnit.MINUTES.toChronoUnit()))));
        assertEquals("60 : 00 : 00", StopWatch.
                format(Duration.between(now, now.plus(1, TimeUnit.HOURS.toChronoUnit()))));
    }
   /* @Test
    void testStart() {
        StopWatch sw = new StopWatch();
        sw.start();
        Duration first = sw.calculateDuration();
        Duration second = sw.calculateDuration();
        assertNotEquals(first, second);
    }*/

    @Test
    void testStart() {
        StopWatch sw = new StopWatch();
        sw.start();
        assertTrue(sw.isRunning());
        assertFalse(sw.isFinished());
    }
    @Test
    void testStop() {
        StopWatch sw = new StopWatch();
        sw.stop();
        assertFalse(sw.isRunning());
    }
    @Test
    void testReset(){
        var sw = new StopWatch();
        sw.timeList.add(new TimesRecord(Instant.now(), StopWatch.Status.START));
        sw.reset();
        assertTrue(sw.isFinished());
        assertFalse(sw.isRunning());
        assertEquals(0,sw.timeList.size());
        assertEquals(0,sw.listModelLapItems.size());
    }
    @Test
    void testNewLap(){
        var sw = new StopWatch();
        sw.start();
        int firstSizeCheck = sw.listModelLapItems.size();
        sw.newLap();
        int secondSizeCheck = sw.listModelLapItems.size();

        assertEquals(1,secondSizeCheck-firstSizeCheck);
    }
    @Test
    void testAggregateGapsMethodWithGivenTimes(){
        var sw= new StopWatch();
        Instant now = Instant.now();
        Instant stop = now.plusSeconds(1);
        Instant secondStart = now.plusSeconds(2);
        sw.timeList.add(new TimesRecord(now, StopWatch.Status.START));
        sw.timeList.add(new TimesRecord(stop , StopWatch.Status.STOP));
        sw.timeList.add(new TimesRecord(secondStart , StopWatch.Status.START));

        assertEquals(1000 , sw.aggregateGaps().toMillis());
    }
    @Test
    void testCalculateDuration(){
        var sw = new StopWatch();
        sw.start();
        Duration firstDurationCalculation= sw.calculateDuration();
        Duration secondDurationCalculation = sw.calculateDuration();

        assertNotEquals(firstDurationCalculation , secondDurationCalculation);
    }
}