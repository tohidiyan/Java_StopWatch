package com.maryam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class StopWatchWithKeyboard extends KeyAdapter implements StopWatchUserInterface {

    StopWatch stopWatch = new StopWatch();

    ScheduledFuture timeUpdater;
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    DefaultListModel<String> listModelForLapsInKeyBoard = new DefaultListModel<>();

    public int calculatePeriod() {
        int refreshRate = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDisplayMode()
                .getRefreshRate();
        if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) {
            refreshRate = 1000;
        }
        return 1000 / refreshRate;
    }


    @Override
    public void startUserInterface() {
        JFrame frame = new JFrame();
        frame.setSize(300, 300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addKeyListener(this);
    }

    private void start() {
        stopWatch.start();
        if (timeUpdater != null && !timeUpdater.isDone()) {
            timeUpdater.cancel(false);
        }
        timeUpdater = scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.print("\r"+StopWatch.format(stopWatch.calculateDuration()));
        }, 0, calculatePeriod(), TimeUnit.MILLISECONDS);
    }

    private void stop() {
        stopWatch.stop();
        timeUpdater.cancel(false);
    }

    private void reset() {
        System.out.println("");
        stopWatch.reset();
        timeUpdater.cancel(false);
        System.out.println("start again");
    }
    int counter = 0;
    private void lap (){
        System.out.println("");
        counter++;
        stopWatch.newLap();
        listModelForLapsInKeyBoard.addAll(stopWatch.lapList);
        System.out.println("Lap" + counter);
        System.out.print(listModelForLapsInKeyBoard.lastElement());
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getExtendedKeyCode()) {
            case KeyEvent.VK_RIGHT:
                start();
                break;
            case KeyEvent.VK_LEFT:
                try{
                    stop();
                }catch(IndexOutOfBoundsException i){
                    System.out.println("start the stopWatch");
                }
                break;
            case KeyEvent.VK_DOWN:
                try{
                    if (stopWatch.isRunning){
                        lap ();
                    }
                }catch(IndexOutOfBoundsException i){
                    System.out.println("start the stopWatch");
                }
                break;
            case KeyEvent.VK_UP:
                try{
                    reset();
                }catch(IndexOutOfBoundsException i){
                    System.out.println("start the stopWatch");
                }
                break;

        }
    }
}
