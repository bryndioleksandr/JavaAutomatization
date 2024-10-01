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

    public static void main(String[] args) throws IOException, AWTException, InterruptedException {
        Logic logic = new Logic();
        logic.runTelegram();
    }

    public void runTelegram() throws IOException, AWTException, InterruptedException {
        Robot robot = new Robot();
        Runtime run = Runtime.getRuntime();

        if (isTelegramRunning()) {
            killProcess("Telegram.exe");
            Thread.sleep(1000);
        }

        // Запуск Telegram
        run.exec("C:\\Users\\sanya\\AppData\\Roaming\\Telegram Desktop\\Telegram.exe");
        Thread.sleep(1000);

        // Запуск бота
        runTelegramBot(run, robot);

        if (findPlay) {
            playGame(robot);
        } else {
            System.out.print("Couldn`t find play btn. Fix it");
        }
    }

    public boolean isTelegramRunning() throws IOException {
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

    // Завершення процесу
    public void killProcess(String processName) throws IOException {
        Runtime.getRuntime().exec("taskkill /IM " + processName + " /F");
    }

    public void runTelegramBot(Runtime run, Robot robot) throws IOException, InterruptedException, AWTException {
        String runChannel = "cmd /c start tg://resolve?domain=" + channel;
        String runBot = "cmd /c start tg://resolve?domain=" + bot + "&startapp";
        run.exec(runBot);
        Thread.sleep(10000);

        captureAndSearchText(robot, "findLaunch.png", "Launch", "Launch bot");

        Thread.sleep(10000);

        captureAndSearchTextScroll(robot, "findUsername.png", "userbryndio", "Launch bot");
        Thread.sleep(10000);

        captureAndSearchText(robot, "findPlay.png", "Wallet", "Wallet");
    }

    public void scrollToPlay(Robot robot) {
            robot.mouseWheel(200);
    }

    public void captureAndSearchTextScroll(Robot robot, String fileName, String searchText, String foundText) throws AWTException, IOException, InterruptedException {
        BufferedImage screenshot = robot.createScreenCapture(new java.awt.Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIO.write(screenshot, "png", new File(fileName));
        Thread.sleep(1000);

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(dataPath);
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
            int xPlay = 717; // Верхня ліва межа по X
            int yPlay = 82;  // Верхня ліва межа по Y
            int widthPlay = 488; // Ширина вікна
            int heightPlay = 872; // Висота вікна
            screenshot = new Robot().createScreenCapture(
                    new java.awt.Rectangle((int) Math.round(xPlay * 0.8), (int) Math.round(yPlay * 0.8), (int) Math.round(widthPlay * 0.8), (int) Math.round(heightPlay * 0.8))
            );
        }
        else{
            screenshot = robot.createScreenCapture(new java.awt.Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize()));
        }

        ImageIO.write(screenshot, "png", new File(fileName));
        Thread.sleep(1000);

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(dataPath);
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
                int xPlay = 717; // Верхня ліва межа по X
                int yPlay = 82;  // Верхня ліва межа по Y
                int widthPlay = 488; // Ширина вікна
                int heightPlay = 872; // Висота вікна

                int x = boundingBox.x;
                int y = boundingBox.y;
                int width = boundingBox.width;
                int height = boundingBox.height;
                if(searchText.matches("Wallet")){
                    robot.mouseMove(xPlay+x - 155, yPlay+y - 179);
                    Thread.sleep(2000);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    findPlay = true;
                }

                else {
                    System.out.println("Found '" + foundText + "' at: " + x + ", " + y);
                    System.out.println("Bounding box size: " + width + "x" + height);
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
        int x = 717; // Верхня ліва межа по X
        int y = 82;  // Верхня ліва межа по Y
        int width = 488; // Ширина вікна
        int height = 872; // Висота вікна
        int gameplayIndex = 0;
        long gameDuration = 30 * 1000;  // Час гри
        long startTime = System.currentTimeMillis();

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(2000);

        while (System.currentTimeMillis() - startTime < gameDuration) {
            BufferedImage screenshotGameplay = new Robot().createScreenCapture(
                    new java.awt.Rectangle((int) Math.round(x * 0.8), (int) Math.round(y * 0.8), (int) Math.round(width * 0.8), (int) Math.round(height * 0.8))
            );
            gameplayIndex++;
            String screenshotFilename = "gameplay\\gameplay_" + gameplayIndex + ".png";
            File outputfile = new File(screenshotFilename);
            ImageIO.write(screenshotGameplay, "png", outputfile);

            detectObjects(robot, screenshotFilename, x, y);
        }

        System.out.println("Game over: 30 seconds have passed.");

        // Після завершення гри шукаємо кнопку "Play" і натискаємо на неї
        while (true) {
            System.out.println("Checking for Play button...");
            if (findAndClickPlayButton(robot)) {
                System.out.println("Found Play button! Starting a new game...");
                playGame(robot);  // Розпочати гру знову
                break;
            } else {
                System.out.println("Play button not found. Retrying in 3 seconds...");
                Thread.sleep(3000);  // Пауза перед повторною спробою
            }
        }
    }

    public boolean findAndClickPlayButton(Robot robot) throws IOException, InterruptedException, AWTException {
        // Робимо скріншот екрана
        BufferedImage screenshot = robot.createScreenCapture(new java.awt.Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIO.write(screenshot, "png", new File("findPlayAgain.png"));
        Thread.sleep(1000);

        // Використовуємо Tesseract для розпізнавання тексту на екрані
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(dataPath);
        tesseract.setLanguage("eng");

        File imageFile = new File("findPlayAgain.png");
        try {
            String resultText = tesseract.doOCR(imageFile);
            System.out.println("Recognized text: " + resultText);

            // Шукаємо текст "Play" на скріншоті
            if (resultText.contains("Play")) {
                System.out.println("Found 'Play' button! Clicking...");

                // Знайти координати та натиснути на кнопку Play
                findAndClickText(robot, tesseract, screenshot, "Play", "Play");
                return true;  // Play знайдено і натиснуто
            } else {
                System.out.println("Play button not found.");
            }
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return false;  // Play не знайдено
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

            System.out.println("Snowflake found at x, y: " + centerX + ", " + centerY);
            robot.mouseMove(centerX + (int) Math.round(x * 0.8), centerY + (int) Math.round(y * 0.8));
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            Mat singleStarMask = new Mat(mask.size(), CvType.CV_8UC1, new Scalar(0));
            Imgproc.drawContours(singleStarMask, contours, starIndex, new Scalar(255), -1);
            String maskFilename = "masks\\star_mask_" + starIndex + ".png";
            Imgcodecs.imwrite(maskFilename, singleStarMask);

            starIndex++;
        }

        mask.release();
        hsvImage.release();
        image.release();
    }
}
