package logic;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.*;

public class Logic {
    public static void main(String[] args) throws IOException,
            AWTException, InterruptedException
    {
        String command = "notepad.exe";
        String runTelegram = "C:\\Users\\sanya\\AppData\\Roaming\\Telegram Desktop\\Telegram.exe";
        Runtime run = Runtime.getRuntime();
        run.exec(command);
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Robot robot = new Robot();

        robot.keyPress(KeyEvent.VK_B);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_R);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_Y);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_N);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_D);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_I);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_O);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_BACK_QUOTE);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_S);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_SPACE);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_C);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_L);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_I);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_C);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_K);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_E);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_R);
        Thread.sleep(500);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_W);
        robot.keyRelease(KeyEvent.VK_W);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        Thread.sleep(200);
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);
        Thread.sleep(200);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        Thread.sleep(1000);



        // check if telegram is running now

        try {
            // Виконання команди tasklist для отримання списку запущених процесів
            Process process = Runtime.getRuntime().exec("tasklist");

            // Читання виведення команди
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean telegramRunning = false;

            // Перевірка на наявність процесу Telegram.exe
            while ((line = reader.readLine()) != null) {
                if (line.contains("Telegram.exe") || line.contains("telegram.exe")) {
                    telegramRunning = true;
                    break;
                }
            }

            if (telegramRunning) {
                System.out.println("Telegram is running.");
                String killProc = "taskkill /IM Telegram.exe /F";
                run.exec(killProc);
                Thread.sleep(1000);
                run.exec(runTelegram);
            } else {
                System.out.println("Telegram is not running.");
                run.exec(runTelegram);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }
