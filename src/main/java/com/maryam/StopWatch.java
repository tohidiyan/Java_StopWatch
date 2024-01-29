package com.maryam;

import javax.swing.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class StopWatch {

    public boolean isRunning;
    public boolean finished = false;
    ArrayList<TimesRecord> timeList = new ArrayList<>();
    DefaultListModel<String> listModelLapItems = new DefaultListModel<>();
    enum Status{
        START,
        STOP,
        LAP,
        FINISH;
    }
    public void startFunction() {
        isRunning = true;
        TimesRecord timeRecord = new TimesRecord(Instant.now() , Status.START);
        addToTimeList(timeList, timeRecord);
    }
    public void stopFunction() {
        isRunning = false;
        TimesRecord timeRecord = new TimesRecord(Instant.now() , Status.STOP);
        addToTimeList(timeList, timeRecord);
    }

    public void lapFunction() {
        TimesRecord timeRecord = new TimesRecord(Instant.now() , Status.LAP);
        listModelLapItems.addElement(changeFormatFromMillisToTime(calculateDuration(timeList)));
    }
    public void resetFunction() {
        isRunning = false;
        finished= true;
        listModelLapItems.removeAllElements();
        timeList.clear();
    }
    public void addToTimeList(ArrayList<TimesRecord> timeList, TimesRecord time) {
        timeList.add(time);
    }

    public Duration calculateGaps(ArrayList<TimesRecord> timeList) {
        Duration gap = Duration.between(Instant.now(), Instant.now());
        for (int i = 1; i < timeList.size(); i++) {
            if (timeList.get(i).status() == Status.START  && timeList.get(i - 1).status() == Status.STOP) {
                gap = gap.plus(Duration.between(timeList.get(i - 1).time() , timeList.get(i).time()));
            }
        }
        return gap;
    }

    public Duration calculateDuration(ArrayList<TimesRecord> timeList) {
        Duration gap = calculateGaps(timeList);
        return Duration.between(timeList.get(0).time() ,Instant.now()).minus(gap);
    };

    public String changeFormatFromMillisToTime(Duration timeDuration) {
        long Millis = timeDuration.toMillis();
        long minutes = (Millis / 1000) / 60;
        long seconds = Millis / 1000 % 60;
        long millis = Millis % 1000;
        return minutes + " : " + seconds + " : " + millis;
    }
}