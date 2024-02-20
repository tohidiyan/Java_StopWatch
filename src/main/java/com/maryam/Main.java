package com.maryam;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the preferred mode (Keyboard or Gui) :");
        String type = scanner.next();

        StopWatchUserInterface stopWatchInterface = getStopWatchUserInterfaceImpl(type);
        stopWatchInterface.startUserInterface();
    }

    private static StopWatchUserInterface getStopWatchUserInterfaceImpl(String type) {
        if (type.equalsIgnoreCase("Gui")) {
            return new StopWatchGuiFrame();
        }
        return new StopWatchWithKeyboard();

    }
}