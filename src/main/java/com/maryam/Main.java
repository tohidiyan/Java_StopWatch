package com.maryam;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        StopWatchUserInterface stopWatchInterface = getStopWatchUserInterfaceImpl(args.length == 1? args[0]: null);
        stopWatchInterface.startUserInterface();
    }

    private static StopWatchUserInterface getStopWatchUserInterfaceImpl(String type) {
        if (type == null) {
            return new StopWatchNullUserInterface();
        }
        return new StopWatchGuiFrame();

    }
}