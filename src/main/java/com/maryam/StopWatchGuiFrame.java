package com.maryam;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class StopWatchGuiFrame {

    final StopWatch stopWatch;
    final JLabel timeLabel;

    final ScheduledExecutorService timeLabelUpdater;
    private final int refreshPeriod;
    ScheduledFuture timeLabelUpdate;


    StopWatchGuiFrame() {
        int refreshRate = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDisplayMode()
                .getRefreshRate();
        if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) {
            refreshRate = 1000;
        }
        refreshPeriod = 1000/refreshRate;
        timeLabel = new JLabel("00 : 00 : 00");
        stopWatch = new StopWatch();
        timeLabelUpdater=Executors.newScheduledThreadPool(1);
        createMainWindow();
    }

    private void createMainWindow() {
        JFrame myFrame = new JFrame();
        myFrame.setTitle("stop watch");
        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        myFrame.setSize(300, 300);
        addMainPanelToWindow(myFrame);
        myFrame.setVisible(true);
    }


    private void addMainPanelToWindow(JFrame myFrame) {
        JPanel myPanel = new JPanel();
        JPanel labelPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel lapPanel = new JPanel();
        myPanel.add(labelPanel, BorderLayout.NORTH);
        myPanel.add(buttonPanel, BorderLayout.CENTER);
        myPanel.add(lapPanel, BorderLayout.SOUTH);

        labelPanel.add(timeLabel);
        JList<String> jList = new JList<>(stopWatch.listModelLapItems);
        JScrollPane jScrollPane = new JScrollPane(jList);

        lapPanel.add(jScrollPane, BorderLayout.CENTER);
        lapPanel.setSize(200, 150);
        JButton lapButton = createLapButton();
        JButton stopButton = createStopButton(lapButton);
        JButton startButton = createStartButton(stopButton, lapButton);
        JButton resetButton = createResetButton(lapButton, stopButton);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(lapButton);
        buttonPanel.add(resetButton);
        stopButton.setEnabled(false);
        lapButton.setEnabled(false);
        myFrame.add(myPanel);
    }

    private JButton createStartButton(JButton stopButton, JButton lapButton) {
        JButton startButton = new JButton("start");
        startButton.addActionListener((e) -> {
            stopWatch.start();
            stopButton.setEnabled(true);
            lapButton.setEnabled(true);

            timeLabelUpdate = timeLabelUpdater.scheduleAtFixedRate(() -> {
                timeLabel.setText(StopWatch.format(stopWatch.calculateDuration()));
            }, 0, refreshPeriod, TimeUnit.MILLISECONDS);
        });
        return startButton;
    }

    private JButton createStopButton(JButton lapButton) {
        JButton stopButton = new JButton("stop");
        stopButton.addActionListener((e) -> {
            stopWatch.stop();
            timeLabelUpdate.cancel(false);
            timeLabel.setText(StopWatch.format(stopWatch.calculateDuration()));
            lapButton.setEnabled(false);
        });
        return stopButton;
    }

    private JButton createLapButton() {
        JButton lapButton = new JButton("lap");
        lapButton.addActionListener((e) -> stopWatch.newLap());
        return lapButton;
    }

    private JButton createResetButton(JButton lapButton, JButton stopButton) {
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener((e) -> {
            stopWatch.reset();
            timeLabelUpdate.cancel(false);
            timeLabel.setText("00 : 00 : 00");
            lapButton.setEnabled(false);
            stopButton.setEnabled(false);
        });
        return resetButton;
    }

    void close(){
        timeLabelUpdater.shutdownNow();
    }
}
