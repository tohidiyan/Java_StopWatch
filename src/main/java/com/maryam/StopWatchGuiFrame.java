package com.maryam;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StopWatchGuiFrame {

    StopWatch stopWatch = new StopWatch();
    JLabel timeLabel = new JLabel();


    public StopWatchGuiFrame() {
        frameCreator();
    }

    private void frameCreator() {
        JFrame myFrame = new JFrame();
        myFrame.setTitle("stop watch");
        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        myFrame.setSize(300, 300);
        panelCreator(myFrame);
        myFrame.setVisible(true);
    }


    public void panelCreator(JFrame myFrame) {
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
        JButton lapButton = setLapButton();
        JButton stopButton = setStopButton(lapButton);
        JButton startButton = setStartButton(stopButton, lapButton);
        JButton resetButton = setResetButton(lapButton, stopButton);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(lapButton);
        buttonPanel.add(resetButton);
        stopButton.setEnabled(false);
        lapButton.setEnabled(false);
        myFrame.add(myPanel);
    }

    public JButton setStartButton(JButton stopButton, JButton lapButton) {
        JButton startButton = new JButton("start");
        startButton.addActionListener((e) -> {
            stopWatch.startFunction();
            stopButton.setEnabled(true);
            lapButton.setEnabled(true);

            ScheduledExecutorService myScheduler = Executors.newScheduledThreadPool(1);
            myScheduler.scheduleAtFixedRate(() -> {
                if (stopWatch.isRunning) {
                    timeLabel.setText(stopWatch.changeFormatFromMillisToTime(stopWatch.calculateDuration(stopWatch.timeList)));
                }
            }, 0, 1, TimeUnit.MILLISECONDS);
            if (stopWatch.finished) {
                myScheduler.shutdown();
            }
        });
        return startButton;
    }

    public JButton setStopButton(JButton lapButton) {
        JButton stopButton = new JButton("stop");
        stopButton.addActionListener((e) -> {
            stopWatch.stopFunction();
            timeLabel.setText(stopWatch.changeFormatFromMillisToTime(stopWatch.calculateDuration(stopWatch.timeList)));
            lapButton.setEnabled(false);
        });
        return stopButton;
    }

    public JButton setLapButton() {
        JButton lapButton = new JButton("lap");
        lapButton.addActionListener((e) -> stopWatch.lapFunction());
        return lapButton;
    }

    public JButton setResetButton(JButton lapButton, JButton stopButton) {
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener((e) -> {
            stopWatch.resetFunction();
            timeLabel.setText("00 : 00 : 00");
            lapButton.setEnabled(false);
            stopButton.setEnabled(false);
        });
        return resetButton;
    }
}
