package logic;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.event.InputEvent;


import net.sourceforge.tess4j.*;
import net.sourceforge.lept4j.*;
import net.sourceforge.tess4j.TesseractException;


public class Logic {
    static String channel = "blumcrypto";
    static String bot = "BlumCryptoBot";
    static String dataPath = "C:\\Program Files\\Tesseract-OCR\\tessdata";

    public static void main(String[] args) throws IOException,
            AWTException, InterruptedException {
        String command = "notepad.exe";
        String runTelegram = "C:\\Users\\sanya\\AppData\\Roaming\\Telegram Desktop\\Telegram.exe";
        Runtime run = Runtime.getRuntime();
        // run.exec(command);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Robot robot = new Robot();

//        robot.keyPress(KeyEvent.VK_B);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_R);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_Y);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_N);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_D);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_I);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_O);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_BACK_QUOTE);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_S);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_SPACE);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_C);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_L);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_I);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_C);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_K);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_E);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_R);
//        Thread.sleep(500);
//        robot.keyPress(KeyEvent.VK_CONTROL);
//        robot.keyPress(KeyEvent.VK_W);
//        robot.keyRelease(KeyEvent.VK_W);
//        robot.keyRelease(KeyEvent.VK_CONTROL);
//        Thread.sleep(200);
//        robot.keyPress(KeyEvent.VK_RIGHT);
//        robot.keyRelease(KeyEvent.VK_RIGHT);
//        Thread.sleep(200);
//
//        robot.keyPress(KeyEvent.VK_ENTER);
//        robot.keyRelease(KeyEvent.VK_ENTER);
//
//        Thread.sleep(1000);


        // check if telegram is running now

        try {
            Process process = Runtime.getRuntime().exec("tasklist");

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean telegramRunning = false;

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
            }
            run.exec(runTelegram);
            Thread.sleep(1000);
            String runChannel = "cmd /c start tg://resolve?domain=" + channel;
            String runBot = "cmd /c start tg://resolve?domain=" + bot + "&startapp";
            run.exec(runChannel);
            Thread.sleep(2000);
            run.exec(runBot);
            Thread.sleep(10000);

            BufferedImage screenshot = new Robot().createScreenCapture(
                    new java.awt.Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize())
            );
            Thread.sleep(2000);
            ImageIO.write(screenshot, "png", new File("findLaunch.png"));
            Thread.sleep(1000);
           // run.exec("cmd /c start \"\" \"C:\\Test Clicker\\AutoClicker\\blumkiss\\findLaunch.png\"");
            Thread.sleep(1000);
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath(dataPath);
            tesseract.setLanguage("eng");
            File imageFile = new File("findLaunch.png");
            try {
                String resultText = tesseract.doOCR(imageFile);
                System.out.println(resultText);
            } catch (TesseractException e) {
                e.printStackTrace();
            }
                List<Word> words = tesseract.getWords(screenshot, ITessAPI.TessPageIteratorLevel.RIL_WORD);

                for (Word word : words) {
                    if (word.getText().equalsIgnoreCase("Launch")) {
                        java.awt.Rectangle boundingBox = word.getBoundingBox();
                        int x = boundingBox.x;
                        int y = boundingBox.y;
                        int width = boundingBox.width;
                        int height = boundingBox.height;

                        System.out.println("Found 'Launch bot' at: " + x + ", " + y);
                        System.out.println("Bounding box size: " + width + "x" + height);
                        robot.mouseMove(x+5, y+8);
                        Thread.sleep(2000);
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        break;
                    }
                    else{
                        System.out.println("Cant find needed phrase. Found: " + word.getText());
                    }
                }


                //second screenshot
                Thread.sleep(10000);
            BufferedImage screenshotBot = new Robot().createScreenCapture(
                    new java.awt.Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize())
            );
            Thread.sleep(2000);
            ImageIO.write(screenshotBot, "png", new File("findUsername.png"));
            Thread.sleep(1000);
            // run.exec("cmd /c start \"\" \"C:\\Test Clicker\\AutoClicker\\blumkiss\\findLaunch.png\"");
            Thread.sleep(1000);
            File imageFileBot = new File("findUsername.png");
            try {
                String resultTextBot = tesseract.doOCR(imageFileBot);
                System.out.println(resultTextBot);
            } catch (TesseractException e) {
                e.printStackTrace();
            }
            List<Word> wordsBot = tesseract.getWords(screenshotBot, ITessAPI.TessPageIteratorLevel.RIL_WORD);

            for (Word worda : wordsBot) {
                if (worda.getText().equalsIgnoreCase("userbryndio")) {
                    java.awt.Rectangle boundingBoxBot = worda.getBoundingBox();
                    int x = boundingBoxBot.x;
                    int y = boundingBoxBot.y;
                    int width = boundingBoxBot.width;
                    int height = boundingBoxBot.height;

                    System.out.println("Found 'Launch bot' at: " + x + ", " + y);
                    System.out.println("Bounding box size: " + width + "x" + height);
                    robot.mouseMove(x+5, y+8);
                    Thread.sleep(2000);
                    robot.mouseWheel(50);
                    break;
                }
                else{
                    System.out.println("Cant find user phrase. Found: " + worda.getText());
                }
            }


            //third screenshot
            Thread.sleep(10000);
            BufferedImage screenshotPlay = new Robot().createScreenCapture(
                    new java.awt.Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize())
            );
            Thread.sleep(2000);
            ImageIO.write(screenshotPlay, "png", new File("findPlay.png"));
            Thread.sleep(1000);
            // run.exec("cmd /c start \"\" \"C:\\Test Clicker\\AutoClicker\\blumkiss\\findLaunch.png\"");
            Thread.sleep(1000);
            File imageFilePlay = new File("findPlay.png");
            try {
                String resultTextPlay = tesseract.doOCR(imageFilePlay);
                System.out.println(resultTextPlay);
            } catch (TesseractException e) {
                e.printStackTrace();
            }
            List<Word> wordsPlay = tesseract.getWords(screenshotPlay, ITessAPI.TessPageIteratorLevel.RIL_WORD);

            for (Word wordw : wordsPlay) {
                if (wordw.getText().equalsIgnoreCase("game")) {
                    java.awt.Rectangle boundingBoxPlay = wordw.getBoundingBox();
                    int x = boundingBoxPlay.x;
                    int y = boundingBoxPlay.y;
                    int width = boundingBoxPlay.width;
                    int height = boundingBoxPlay.height;

                    System.out.println("Found 'Launch bot' at: " + x + ", " + y);
                    System.out.println("Bounding box size: " + width + "x" + height);
                    robot.mouseMove(x+236, y+10);
                    Thread.sleep(2000);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    break;
                }
                else{
                    System.out.println("Cant find play phrase. Found: " + wordw.getText());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}