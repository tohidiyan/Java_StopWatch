package com.maryam;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class StopWatchTest {

    @Test
    public void checkFormatForGivenDuration(){
        assertEquals("00 : 01 : 00",StopWatch.format(Duration.between(Instant.now() ,Instant.now().plusMillis(1000))));
    }

}