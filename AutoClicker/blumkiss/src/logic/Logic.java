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

    // Основна логіка запуску
    public void runTelegram() throws IOException, AWTException, InterruptedException {
        Robot robot = new Robot();
        Runtime run = Runtime.getRuntime();

        // Перевірка, чи запущений Telegram
        if (isTelegramRunning()) {
            killProcess("Telegram.exe");
            Thread.sleep(1000);
        }

        // Запуск Telegram
        run.exec("C:\\Users\\sanya\\AppData\\Roaming\\Telegram Desktop\\Telegram.exe");
        Thread.sleep(1000);

        // Запуск бота
        runTelegramBot(run, robot);

        // Перевірка, чи знайдено кнопку "Play"
        if (findPlay) {
            // Запуск гри
            playGame(robot);
        } else {
            System.out.print("Couldnt find play btn. Fix it");
        }
    }

    // Функція для перевірки чи запущений процес Telegram
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

    // Запуск бота Telegram
    public void runTelegramBot(Runtime run, Robot robot) throws IOException, InterruptedException, AWTException {
        String runChannel = "cmd /c start tg://resolve?domain=" + channel;
        String runBot = "cmd /c start tg://resolve?domain=" + bot + "&startapp";
        run.exec(runBot);
        Thread.sleep(5000);

        // Скріншот екрана і OCR для пошуку кнопки "Launch"
        captureAndSearchText(robot, "findLaunch.png", "Launch", "Launch bot");

        Thread.sleep(5000);

        // Скріншот і OCR для пошуку юзернейму
        captureAndSearchText(robot, "findUsername.png", "userbryndio", "Launch bot");
        Thread.sleep(5000);


        // Скріншот і OCR для пошуку бота
        captureAndSearchText(robot, "findPlay.png", "@BlumCryptoBot", "Wallet");
    }

    // Функція для створення скріншота і пошуку тексту на зображенні за допомогою Tesseract
    public void captureAndSearchText(Robot robot, String fileName, String searchText, String foundText) throws AWTException, IOException, InterruptedException {
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
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }

    public void findAndClickText(Robot robot, Tesseract tesseract, BufferedImage screenshot, String searchText, String foundText) throws AWTException, InterruptedException {
        List<Word> words = tesseract.getWords(screenshot, ITessAPI.TessPageIteratorLevel.RIL_WORD);
        for (Word word : words) {
            if (word.getText().equalsIgnoreCase(searchText)) {
                java.awt.Rectangle boundingBox = word.getBoundingBox();
                int x = boundingBox.x;
                int y = boundingBox.y;
                int width = boundingBox.width;
                int height = boundingBox.height;

                System.out.println("Found '" + foundText + "' at: " + x + ", " + y);
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
    }

    // Логіка гри
    public void playGame(Robot robot) throws IOException, InterruptedException, AWTException {
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
            String screenshotFilename = "gameplay\\gameplay_" + gameplayIndex + ".png";
            File outputfile = new File(screenshotFilename);
            ImageIO.write(screenshotGameplay, "png", outputfile);

            detectObjects(robot, screenshotFilename, x, y);
        }

        System.out.println("Game over: 30 seconds have passed.");
    }

    // Виявлення об'єктів (наприклад, сніжинок) на екрані
    public void detectObjects(Robot robot, String screenshotFilename, int x, int y) throws IOException, InterruptedException {
        Mat image = Imgcodecs.imread(screenshotFilename);

        Mat hsvImage = new Mat();
        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);
        Scalar lowerGreen = new Scalar(35, 150, 160, 0); // Adjust to your green
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
