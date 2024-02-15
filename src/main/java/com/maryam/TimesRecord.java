package com.maryam;


import java.io.Serializable;
import java.time.Instant;

record TimesRecord(Instant time, StopWatch.Status status) implements Serializable {}