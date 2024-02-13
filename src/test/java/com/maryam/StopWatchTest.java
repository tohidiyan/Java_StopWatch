package com.maryam;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class StopWatchTest {

    private StopWatch sw;
    @BeforeEach
    void createObjectOfStopWatch(){
        sw = new StopWatch();
    }

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

    @Test
    void testStart() {
        sw.start();
        assertAll("start method test" ,
                ()->{assertTrue(sw.isRunning());},
                ()->{assertFalse(sw.isFinished());}
                );
    }
    @Test
    void testStop() {
        sw.stop();
        assertFalse(sw.isRunning());
    }
    @Test
    void testReset(){
        sw.timeList.add(new TimesRecord(Instant.now(), StopWatch.Status.START));
        sw.reset();
        assertTrue(sw.isFinished());
        assertFalse(sw.isRunning());
        assertEquals(0,sw.timeList.size());
        assertEquals(0,sw.listModelLapItems.size());
    }
    @Test
    void testNewLap(){
        sw.start();
        int firstSizeCheck = sw.listModelLapItems.size();
        sw.newLap();
        int secondSizeCheck = sw.listModelLapItems.size();

        assertEquals(1,secondSizeCheck-firstSizeCheck);
    }
 /*   @TestFactory
    Collection<DynamicTest> testAggregateGapsMethodWithGivenTimes(){
        Instant now = Instant.now();
        sw.timeList.add(new TimesRecord(now, StopWatch.Status.START));
        Instant stop = now.plusSeconds(1);
        Instant secondStart = now.plusSeconds(2);
        Instant secondStop = now.plusSeconds(3);
        Instant thirdStart = now.plusSeconds(5);
        return Arrays.asList(
                DynamicTest.dynamicTest("one second stop" , ()->{
                    sw.timeList.add(new TimesRecord(stop , StopWatch.Status.STOP));
                    sw.timeList.add(new TimesRecord(secondStart , StopWatch.Status.START));
                    assertEquals(1000 , sw.aggregateGaps().toMillis());
                }),
                DynamicTest.dynamicTest("two second stop" , ()->{
                    sw.timeList.add(new TimesRecord(secondStop , StopWatch.Status.STOP));
                    sw.timeList.add(new TimesRecord(thirdStart , StopWatch.Status.START));
                    assertEquals(3000 , sw.aggregateGaps().toMillis());
                })
        );
    }*/

    @ParameterizedTest
    @ValueSource(ints = {2 , 3 , 4})
    void testAggregateGapsMethodWithGivenTimes(int number ){
        Instant now = Instant.now();
        sw.timeList.add(new TimesRecord(now, StopWatch.Status.START));
        Instant stop = now.plusSeconds(number);
        sw.timeList.add(new TimesRecord(stop, StopWatch.Status.STOP));
        Instant secondStart = stop.plusSeconds(number);
        sw.timeList.add(new TimesRecord(secondStart, StopWatch.Status.START));
        Instant secondStop = secondStart.plusSeconds(number);
        sw.timeList.add(new TimesRecord(secondStop, StopWatch.Status.STOP));
        Instant thirdStart = secondStop.plusSeconds(number);
        sw.timeList.add(new TimesRecord(thirdStart, StopWatch.Status.START));

        assertEquals(2L *number*1000 , sw.aggregateGaps().toMillis());

    }

    @RepeatedTest(10)
    void testCalculateDuration() {
        sw.start();
        Duration firstDurationCalculation= sw.calculateDuration();
        Duration secondDurationCalculation = sw.calculateDuration();

        assertNotEquals(firstDurationCalculation , secondDurationCalculation);
    }
}