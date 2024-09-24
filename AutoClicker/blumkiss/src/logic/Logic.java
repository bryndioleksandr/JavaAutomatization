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

        // Create an instance of Robot class
        Robot robot = new Robot();

        // Press keys using robot. A gap of
        // of 500 milli seconds is added after
        // every key press
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

    }
    }
