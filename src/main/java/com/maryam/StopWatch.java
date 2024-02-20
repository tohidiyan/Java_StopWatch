package com.maryam;

import javax.swing.*;
import java.io.*;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class StopWatch implements Serializable {

    @Serial
    private static final long serialVersionUID = 40L;
    private static final DecimalFormat TIME_FORMAT = new DecimalFormat("00");
    public boolean isRunning;
    ArrayList<TimesRecord> timeList= new ArrayList<>();
    ArrayList<String> lapList = new ArrayList<>();

    enum Status {
        START,
        STOP,
        LAP,
        SAVED
    }
 /*   public boolean isRunning(){
        return isRunning();
    };*/

    public void start() {
        isRunning = true;
        TimesRecord timeRecord = new TimesRecord(Instant.now(), Status.START);
        if (timeList.isEmpty()) {
            addToTimeList(timeList, timeRecord);
        } else if (timeList.get(timeList.size() - 1).status() != Status.START) {
            addToTimeList(timeList, timeRecord);
        }
    }
    public void stop() {
        isRunning = false;
        TimesRecord timeRecord = new TimesRecord(Instant.now(), Status.STOP);
        if (timeList.get(timeList.size() - 1).status() != Status.STOP) {
            addToTimeList(timeList, timeRecord);
        }
    }

    public void newLap() {
        //TimesRecord timeRecord = new TimesRecord(Instant.now() , Status.LAP);
        lapList.add(format(calculateDuration()));
    }

    public void reset() {
        isRunning = false;
        lapList.clear();
        timeList.clear();
    }

    private void addToTimeList(ArrayList<TimesRecord> timeList, TimesRecord time) {
        timeList.add(time);
    }

    public Duration aggregateGaps() {
        Duration gap = Duration.ofNanos(0);
        for (int i = 1; i < timeList.size(); i++) {
            if (timeList.get(i).status() == Status.START && timeList.get(i - 1).status() == Status.STOP) {
                gap = gap.plus(Duration.between(timeList.get(i - 1).time(), timeList.get(i).time()));
            }
        }
        return gap;
    }

    public Duration calculateDuration() {
        Duration gap = aggregateGaps();
        return Duration.between(timeList.get(0).time(), Instant.now()).minus(gap);
    }

    public static String format(Duration timeDuration) {
        long durationMillis = timeDuration.toMillis();
        long minutes = (durationMillis / 1000) / 60;
        long seconds = durationMillis / 1000 % 60;
        long millis = durationMillis % 1000;
        return TIME_FORMAT.format(minutes) + " : " + TIME_FORMAT.format(seconds) + " : " + TIME_FORMAT.format(millis / 10);
    }
}