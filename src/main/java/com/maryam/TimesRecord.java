package com.maryam;


import java.time.Instant;

public record TimesRecord(Instant time, StopWatch.Status status) {}