package com.maryam;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.Instant;
import java.util.concurrent.*;

class StopWatchGuiFrame implements StopWatchUserInterface {
    StopWatch stopWatch = new StopWatch();
    final JLabel timeLabel;
    ScheduledExecutorService timeLabelUpdater ;
    final private int refreshPeriod;
    ScheduledFuture timeLabelUpdate;
    DefaultListModel<String> listModelForLapsInGui = new DefaultListModel<>();

    StopWatchGuiFrame() {
        int refreshRate = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDisplayMode()
                .getRefreshRate();
        if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN) {
            refreshRate = 1000;
        }
        refreshPeriod = 1000 / refreshRate;
        timeLabelUpdater = Executors.newScheduledThreadPool(1);
        timeLabel = new JLabel("00 : 00 : 00");
    }
    @Override
    public void startUserInterface() {
        JFrame myFrame = new JFrame();
        myFrame.setTitle("stop watch");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        JList<String> jList = new JList<>(listModelForLapsInGui);
        JScrollPane jScrollPane = new JScrollPane(jList);
        lapPanel.add(jScrollPane, BorderLayout.CENTER);
        labelPanel.add(timeLabel);
        lapPanel.setSize(200, 150);
        labelPanel.add(createUploadButton());
        labelPanel.add(createSaveButton());
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
            if (timeLabelUpdate != null && !timeLabelUpdate.isDone()) {
                timeLabelUpdate.cancel(false);
            }
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
            stopButton.setEnabled(false);
        });
        return stopButton;
    }
    private JButton createResetButton(JButton lapButton, JButton stopButton) {
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener((e) -> {
            stopWatch.reset();
            listModelForLapsInGui.removeAllElements();
            timeLabelUpdate.cancel(false);
            timeLabel.setText("00 : 00 : 00");
            lapButton.setEnabled(false);
            stopButton.setEnabled(false);
        });
        return resetButton;
    }
    private JButton createLapButton() {
        JButton lapButton = new JButton("lap");
        lapButton.addActionListener((e) -> {
            stopWatch.newLap();
            listModelForLapsInGui.addElement(stopWatch.lapList.getLast());
                }
        );
        return lapButton;
    }
    private void saveStopWatchInFile(StopWatch stopWatch , int counter) {

        String fileName = "timeRecords.ser" + counter;
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            if(stopWatch.timeList.getLast().status() == StopWatch.Status.START){
                stopWatch.timeList.add(new TimesRecord(Instant.now() , StopWatch.Status.STOP));
                objectOut.writeObject(stopWatch);
            }else{
                objectOut.writeObject(stopWatch);
            }
            stopWatch.timeList.removeLast();
            System.out.println("saved: " + fileName);
        } catch (IOException i) {
            System.out.println("not saved: " + i.getMessage());
        }
    }
    private JButton createSaveButton() {
        JButton saveButton = new JButton("Save");
        final int[] counter = {0};
        saveButton.addActionListener((e) -> {
            counter[0]++;
            saveStopWatchInFile(stopWatch , counter[0]);
        });
        return saveButton;
    }
    private StopWatch readStopWatchFromFile(String fileName) {
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn); ){
            return (StopWatch) objectIn.readObject();
        } catch (IOException | ClassNotFoundException i) {
            System.out.println("not found" +  i.getMessage());
            return null;
        }
    }
    private JButton createUploadButton() {
        JButton uploadButton = new JButton("Upload");
        uploadButton.addActionListener((e) -> {
            StopWatch stopWatchFromFile = readStopWatchFromFile(openFile());
            if (stopWatchFromFile != null) {
                stopWatch.reset();
                timeLabelUpdate.cancel(false);
                stopWatch = stopWatchFromFile;
                timeLabel.setText("click start");
                listModelForLapsInGui.removeAllElements();
                listModelForLapsInGui.addAll(stopWatchFromFile.lapList);

                System.out.println(StopWatch.format(stopWatch.calculateDuration()));
            }
        });
        return uploadButton;
    }
    private String openFile() {
        String fileName = "";
        JFileChooser fileChooser = new JFileChooser();
        int response = fileChooser.showOpenDialog(null);
        try {
            File appFolder = new File("/home/maryam/Dokumente/StopWatch");
            fileChooser.setCurrentDirectory(appFolder);
        } catch (NullPointerException | SecurityException | IndexOutOfBoundsException e) {
            System.err.println("Error setting current directory: " + e.getMessage());
        }
        if (response == JFileChooser.APPROVE_OPTION) {
            fileName = fileChooser.getSelectedFile().getName();
        }
        return fileName;
    }
}
