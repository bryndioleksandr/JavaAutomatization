package main;

import java.awt.*;
import java.io.IOException;
import java.util.Scanner;

import telegram.TelegramAutomation;
import config.Config;

public class Main {
    public static void main(String[] args) throws IOException, AWTException, InterruptedException {
        TelegramAutomation telegramAutomation = new TelegramAutomation();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Your OS name -> " + System.getProperty("os.name"));
        System.out.println("Your OS version -> " + System.getProperty("os.version"));
        System.out.println("Resolution -> " + java.awt.Toolkit.getDefaultToolkit().getScreenResolution());
        System.out.println("Size -> " + java.awt.Toolkit.getDefaultToolkit().getScreenSize());

        System.out.print("Tg username: ");
        Config.username = scanner.nextLine();

        telegramAutomation.runTelegram();
    }
}