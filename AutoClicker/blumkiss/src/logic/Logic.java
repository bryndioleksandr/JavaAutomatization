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


import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class Logic {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    static String channel = "blumcrypto";
    static String bot = "BlumCryptoBot";
    static String dataPath = "C:\\Program Files\\Tesseract-OCR\\tessdata";
    static boolean findPlay = false;


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
            run.exec(runBot);
            Thread.sleep(5000);

            BufferedImage screenshot = new Robot().createScreenCapture(
                    new java.awt.Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize())
            );
            ImageIO.write(screenshot, "png", new File("findLaunch.png"));
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
                    robot.mouseMove(x + 5, y + 8);
                    Thread.sleep(2000);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    break;
                } else {
                    System.out.println("Cant find needed phrase. Found: " + word.getText());
                }
            }


            //second screenshot
            Thread.sleep(5000);
            BufferedImage screenshotBot = new Robot().createScreenCapture(
                    new java.awt.Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize())
            );
            ImageIO.write(screenshotBot, "png", new File("findUsername.png"));
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
                    robot.mouseMove(x + 5, y + 8);
                    Thread.sleep(500);
                    robot.mouseWheel(50);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    break;
                } else {
                    System.out.println("Cant find user phrase. Found: " + worda.getText());
                }
            }


            //third screenshot
            Thread.sleep(10000);
            int xPlay = 717; // Верхня ліва межа по X
            int yPlay = 82;  // Верхня ліва межа по Y
            int widthPlay = 488; // Ширина вікна
            int heightPlay = 872; // Висота вікна
            BufferedImage screenshotPlay = new Robot().createScreenCapture(
                    new java.awt.Rectangle(xPlay, yPlay, widthPlay, heightPlay)
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
                if (wordw.getText().equalsIgnoreCase("@BlumCryptoBot")) {
                    java.awt.Rectangle boundingBoxPlay = wordw.getBoundingBox();
                    int x = boundingBoxPlay.x;
                    int y = boundingBoxPlay.y;
                    int width = boundingBoxPlay.width;
                    int height = boundingBoxPlay.height;
                    findPlay = true;
                    System.out.println("Found 'Wallet' at: " + x + ", " + y);
                    System.out.println("Bounding box size: " + width + "x" + height);
                    robot.mouseMove(xPlay+x + 170, yPlay+y - 230);
                    Thread.sleep(2000);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    break;
                } else {
                    System.out.println("Cant find play phrase. Found: " + wordw.getText());
                    findPlay = false;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (findPlay) {
            //now making logic to detect needed items on the game-screen

            int x = 717; // Верхня ліва межа по X
            int y = 82;  // Верхня ліва межа по Y
            int width = 488; // Ширина вікна
            int height = 872; // Висота вікна
            int gameplayIndex = 0;
            long gameDuration = 30 * 1000;
            long startTime = System.currentTimeMillis();

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(2000);
            while (System.currentTimeMillis() - startTime < gameDuration) {
                BufferedImage screenshotGameplay = new Robot().createScreenCapture(
                        new java.awt.Rectangle((int) Math.round(x * 0.8), (int) Math.round(y * 0.8), (int) Math.round(width * 0.8), (int) Math.round(height * 0.8))
                );
                gameplayIndex++;
                //File outputfile = new File("gameplay.png");
                //ImageIO.write(screenshotGameplay, "png", outputfile);
                String screenshotFilename = "gameplay\\gameplay_" + gameplayIndex + ".png";
                File outputfile = new File(screenshotFilename);
                ImageIO.write(screenshotGameplay, "png", outputfile);

                Mat image = Imgcodecs.imread(screenshotFilename);

                Mat hsvImage = new Mat();
                Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);
                Scalar lowerGreen = new Scalar(35, 150, 160, 0); // Adjust to your green
                Scalar upperGreen = new Scalar(70, 255, 255, 0);

                // Detect green objects (snowflakes)
                Mat mask = new Mat();
                Core.inRange(hsvImage, lowerGreen, upperGreen, mask);


                // Find contours of the detected green objects
                List<MatOfPoint> contours = new java.util.ArrayList<>();
                Imgproc.findContours(mask, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

                int starIndex = 0;
                for (MatOfPoint contour : contours) {
                    Rect boundingRect = Imgproc.boundingRect(contour);

                    int centerX = boundingRect.x + boundingRect.width / 2;
                    int centerY = boundingRect.y + boundingRect.height / 2;

                    System.out.println("Snowflake found at x, y: " + centerX + ", " + centerY);

//                    robot.mouseMove(x + centerX, y + centerY);
                    robot.mouseMove(centerX + (int) Math.round(x * 0.8), centerY + (int) Math.round(y * 0.8));
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    Mat singleStarMask = new Mat(mask.size(), CvType.CV_8UC1, new Scalar(0));
                    Imgproc.drawContours(singleStarMask, contours, starIndex, new Scalar(255), -1);

                    // Збереження маски для кожної сніжинки
                    String maskFilename = "masks\\star_mask_" + gameplayIndex + "_" + starIndex + ".png";
                    Imgcodecs.imwrite(maskFilename, singleStarMask);

                    System.out.println("Маска сніжинки збережена як: " + maskFilename);

                    starIndex++;
                }

                Thread.sleep(1500);
            }

            System.out.println("Game over: 30 seconds have passed.");
        } else {
            System.out.print("Couldnt find play btn. Fix it");
        }

    }
}


//need to calculate stars` speed