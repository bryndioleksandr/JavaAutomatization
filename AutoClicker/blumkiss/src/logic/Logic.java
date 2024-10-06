package logic;

import java.awt.*;
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

import config.Config;
import java.util.Scanner;

//
//  TO DOWNLOAD ON THE NEW DEVICE: TESSERACT AND OPENCV. SET SYSTEM VARIABLE FOR OPENCV ROOT. AND GET ROOT OF TESSERACT
//


public class Logic {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws IOException, AWTException, InterruptedException {
        Logic logic = new Logic();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Your OS name -> " + System.getProperty("os.name"));
        
        System.out.println("Resolution -> " + java.awt.Toolkit.getDefaultToolkit().getScreenResolution());

        System.out.println("Size -> " + java.awt.Toolkit.getDefaultToolkit().getScreenSize());

        System.out.print("Tg username: ");
        Config.username = scanner.nextLine();

        logic.runTelegram();
    }

    public void runTelegram() throws IOException, AWTException, InterruptedException {
        Robot robot = new Robot();
        Runtime run = Runtime.getRuntime();

        if(Config.osName.contains("Windows")) {
            if (isTelegramRunning()) {
                killProcess("Telegram.exe");
                Thread.sleep(1000);
            }

            run.exec("Telegram.exe");
            Thread.sleep(1000);
        }
        else{
            if (isTelegramRunning()) {
                killProcess("Telegram");
                Thread.sleep(1000);
            }

            run.exec("open -a Telegram");
            Thread.sleep(1000);
        }

        runTelegramBot(run, robot);

        if (Config.findPlay) {
            playGame(robot);
        } else {
            System.out.print("Couldn`t find play btn. Fix it");
        }
    }

    public boolean isTelegramRunning() throws IOException {
        if(Config.osName.contains("Windows")) {
            Process process = Runtime.getRuntime().exec("tasklist");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Telegram.exe") || line.contains("telegram.exe")) {
                    return true;
                }
            }
            return false;
        }
        else{
            Process process = Runtime.getRuntime().exec("ps -A");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Telegram") || line.contains("telegram")) {
                    return true;
                }
            }
            return false;
        }
    }

    public void killProcess(String processName) throws IOException {
        if(Config.osName.contains("Windows"))
        Runtime.getRuntime().exec("taskkill /IM " + processName + " /F");
        else
            Runtime.getRuntime().exec("pkill -f " + processName);
    }

    public void runTelegramBot(Runtime run, Robot robot) throws IOException, InterruptedException, AWTException {
        String runBot;
        if(Config.osName.contains("Windows")) runBot = "cmd /c start tg://resolve?domain=" + Config.bot + "&startapp";
        else runBot = "open tg://resolve?domain=" + Config.bot + "&startapp";
        run.exec(runBot);
        Thread.sleep(10000);

        captureAndSearchText(robot, Config.launchPng, Config.searchLaunch, Config.launchBot);

        Thread.sleep(10000);

        captureAndSearchTextScroll(robot, Config.usernamePng, Config.username, Config.username);
        Thread.sleep(2500);

        captureAndSearchText(robot, Config.playPng, Config.searchWallet, Config.searchWallet);

    }

    public void scrollToPlay(Robot robot) {
            robot.mouseWheel(200);
    }

    public void captureAndSearchTextScroll(Robot robot, String fileName, String searchText, String foundText) throws AWTException, IOException, InterruptedException {
        BufferedImage screenshot = robot.createScreenCapture(new java.awt.Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIO.write(screenshot, "png", new File(fileName));
        Thread.sleep(1000);

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(Config.dataPath);
        tesseract.setLanguage("eng");

        File imageFile = new File(fileName);
        try {
            String resultText = tesseract.doOCR(imageFile);
            System.out.println(resultText);
            findAndClickText(robot, tesseract, screenshot, searchText, foundText);
            scrollToPlay(robot);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }


    public void captureAndSearchText(Robot robot, String fileName, String searchText, String foundText) throws AWTException, IOException, InterruptedException {
        BufferedImage screenshot;
        if(searchText.equals("Wallet")){

            int xPlay = (717*Config.width)/1920;
            int yPlay = (82*Config.height)/1080;
            int widthPlay = (488*Config.width)/1920;
            int heightPlay = (872*Config.height)/1080;
            screenshot = new Robot().createScreenCapture(
                    new java.awt.Rectangle(xPlay, yPlay , widthPlay, heightPlay)
            );
        }
        else{
            screenshot = robot.createScreenCapture(new java.awt.Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize()));
        }

        ImageIO.write(screenshot, "png", new File(fileName));
        Thread.sleep(1000);

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(Config.dataPath);
        tesseract.setLanguage("eng");

        File imageFile = new File(fileName);
        try {
            String resultText = tesseract.doOCR(imageFile);
            System.out.println(resultText);
            findAndClickText(robot, tesseract, screenshot, searchText, foundText);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }


    public void findAndClickText(Robot robot, Tesseract tesseract, BufferedImage screenshot, String searchText, String foundText) throws AWTException, InterruptedException {
        List<Word> words = tesseract.getWords(screenshot, ITessAPI.TessPageIteratorLevel.RIL_WORD);
        for (Word word : words) {
            if (word.getText().equalsIgnoreCase(searchText)) {
                java.awt.Rectangle boundingBox = word.getBoundingBox();
                int xPlay = (717*Config.width)/1920;
                int yPlay = (82*Config.height)/1080;

                int x = boundingBox.x;
                int y = boundingBox.y;
                int widthBox = boundingBox.width;
                int heightBox = boundingBox.height;
                if(searchText.matches("Wallet")){
                    robot.mouseMove(xPlay+x, yPlay+y - ((225*Config.width)/1920));
                    Thread.sleep(2000);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    Config.findPlay = true;
                }
                else if(searchText.matches("Rewards")){
                    robot.mouseMove(xPlay+x, yPlay+y + ((300*Config.width)/1920));
                    Thread.sleep(2000);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    Config.findPlay = true;
                }

                else {
                    System.out.println("Found '" + foundText + "' at: " + x + ", " + y);
                    System.out.println("Bounding box size: " + widthBox + "x" + heightBox);
                    robot.mouseMove(x + 5, y + 8);
                    Thread.sleep(2000);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }
                break;
            } else {
                System.out.println("Cant find needed phrase. Found: " + word.getText());
            }
        }
    }

    public void playGame(Robot robot) throws IOException, InterruptedException, AWTException {
        int xPlay = (717*Config.width)/1920;
        int yPlay = (82*Config.height)/1080;
        int widthPlay = (488*Config.width)/1920;
        int heightPlay = (872*Config.height)/1080;
        int gameplayIndex = 0;
        long gameDuration = 32 * 1000;
        long startTime = System.currentTimeMillis();

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(2000);

        while (System.currentTimeMillis() - startTime < gameDuration) {
            while (System.currentTimeMillis() - startTime < gameDuration/2) {
                BufferedImage screenshotGameplay = new Robot().createScreenCapture(
                        new java.awt.Rectangle(xPlay, yPlay , widthPlay, heightPlay)
                );
                gameplayIndex++;
                String screenshotFilename = "gameplay\\gameplay_" + gameplayIndex + ".png";
                File outputfile = new File(screenshotFilename);
                ImageIO.write(screenshotGameplay, "png", outputfile);

                detectObjects(robot, screenshotFilename, xPlay, yPlay);
                Thread.sleep(200);
            }
            while(System.currentTimeMillis() - startTime < gameDuration) {
                BufferedImage screenshotGameplay = new Robot().createScreenCapture(
                        new java.awt.Rectangle(xPlay, yPlay , widthPlay, heightPlay)
                );
                gameplayIndex++;
                String screenshotFilename = "gameplay\\gameplay_" + gameplayIndex + ".png";
                File outputfile = new File(screenshotFilename);
                ImageIO.write(screenshotGameplay, "png", outputfile);

                detectObjectsSecondHalf(robot, screenshotFilename, xPlay, yPlay);
                Thread.sleep(200);
            }
        }

        System.out.println("Game over: 30 seconds have passed.");

        while (true) {
            System.out.println("Checking for Play button...");
            if (findAndClickPlayButton(robot)) {
                System.out.println("Found Play button! Starting a new game...");
                playGame(robot);
                break;
            } else {
                System.out.println("Play button not found. Retrying in 3 seconds...");
                Thread.sleep(3000);
            }
        }
    }

    public boolean findAndClickPlayButton(Robot robot) throws IOException, InterruptedException, AWTException {
        int xPlay = (717*Config.width)/1920;
        int yPlay = (82*Config.height)/1080;
        int widthPlay = (488*Config.width)/1920;
        int heightPlay = (872*Config.height)/1080;
        BufferedImage screenshot = new Robot().createScreenCapture(
                new java.awt.Rectangle(xPlay, yPlay , widthPlay, heightPlay)
        );
        ImageIO.write(screenshot, "png", new File("findPlayAgain.png"));
        Thread.sleep(1000);

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(Config.dataPath);
        tesseract.setLanguage("eng");

        File imageFile = new File("findPlayAgain.png");
        try {
            String resultText = tesseract.doOCR(imageFile);
            System.out.println("Recognized text: " + resultText);

            if (resultText.contains("Rewards")) {
                System.out.println("Found 'Play' button! Clicking...");

                findAndClickText(robot, tesseract, screenshot, "Rewards", "Play");
                return true;
            } else {
                System.out.println("Play button not found.");
            }
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public void detectObjects(Robot robot, String screenshotFilename, int x, int y) throws IOException, InterruptedException {
        Mat image = Imgcodecs.imread(screenshotFilename);

        Mat hsvImage = new Mat();
        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);
        Scalar lowerGreen = new Scalar(35, 150, 160, 0);
        Scalar upperGreen = new Scalar(70, 255, 255, 0);

        Mat mask = new Mat();
        Core.inRange(hsvImage, lowerGreen, upperGreen, mask);

        List<MatOfPoint> contours = new java.util.ArrayList<>();
        Imgproc.findContours(mask, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        int starIndex = 0;
        for (MatOfPoint contour : contours) {
            Rect boundingRect = Imgproc.boundingRect(contour);
            int centerX = boundingRect.x + boundingRect.width / 2;
            int centerY = boundingRect.y + boundingRect.height / 2;

            if(boundingRect.width > 20 && boundingRect.height > 20) {
                System.out.println("Snowflake found at x, y: " + centerX + ", " + centerY);
                robot.mouseMove(centerX + x, centerY + y);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                Mat singleStarMask = new Mat(mask.size(), CvType.CV_8UC1, new Scalar(0));
                Imgproc.drawContours(singleStarMask, contours, starIndex, new Scalar(255), -1);
                String maskFilename = "masks\\star_mask_" + starIndex + ".png";
                Imgcodecs.imwrite(maskFilename, singleStarMask);

                starIndex++;
                Thread.sleep(6);
            }
            else{
                System.out.println("Object is too small, skipping it" + boundingRect.width + " - width; " + boundingRect.height + " - height.");
            }
        }

        mask.release();
        hsvImage.release();
        image.release();
    }

    public void detectObjectsSecondHalf(Robot robot, String screenshotFilename, int x, int y) throws IOException, InterruptedException {
        Mat image = Imgcodecs.imread(screenshotFilename);

        Mat hsvImage = new Mat();
        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);
        Scalar lowerGreen = new Scalar(35, 150, 160, 0);
        Scalar upperGreen = new Scalar(70, 255, 255, 0);

        Mat mask = new Mat();
        Core.inRange(hsvImage, lowerGreen, upperGreen, mask);

        List<MatOfPoint> contours = new java.util.ArrayList<>();
        Imgproc.findContours(mask, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        int starIndex = 0;
        for (MatOfPoint contour : contours) {
            Rect boundingRect = Imgproc.boundingRect(contour);
            int centerX = boundingRect.x + boundingRect.width / 2;
            int centerY = boundingRect.y + boundingRect.height / 2;

            if(boundingRect.width < 22 && boundingRect.height < 22 && boundingRect.width > 15 && boundingRect.height > 15) {
                System.out.println("Snowflake found at x, y: " + centerX + ", " + centerY);
                robot.mouseMove(centerX + x, centerY + y);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                Mat singleStarMask = new Mat(mask.size(), CvType.CV_8UC1, new Scalar(0));
                Imgproc.drawContours(singleStarMask, contours, starIndex, new Scalar(255), -1);
                String maskFilename = "masks\\star_mask_" + starIndex + ".png";
                Imgcodecs.imwrite(maskFilename, singleStarMask);

                starIndex++;
                Thread.sleep(8);
            }
            else{
                System.out.println("Object is too small, skipping it" + boundingRect.width + " - width; " + boundingRect.height + " - height.");
            }
        }

        mask.release();
        hsvImage.release();
        image.release();
    }
}
