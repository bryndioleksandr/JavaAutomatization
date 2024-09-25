package logic;

import java.awt.*;
import java.awt.event.KeyEvent;
import net.sourceforge.tess4j.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Logic {
    public static void main(String[] args) throws IOException, AWTException, InterruptedException {
        String runTelegram = "C:\\Users\\sanya\\AppData\\Roaming\\Telegram Desktop\\Telegram.exe";
        Runtime run = Runtime.getRuntime();

        // Check if Telegram is running and close it
        checkAndCloseTelegram(run);

        // Open Telegram
        run.exec(runTelegram);
        Thread.sleep(2000);

        // Open Telegram channel and bot
        openTelegramChannel(run, "blumcrypto");
        openTelegramBot(run, "BlumCryptoBot&startapp=ref_QMJLhDf43z");

        // Take a screenshot
        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenShot = robot.createScreenCapture(screenRect);
        String screenshotPath = "findLaunch.png";
        ImageIO.write(screenShot, "png", new File(screenshotPath));

        // Find the text "Launch Bot" in the screenshot
        findTextCoordinates(screenshotPath, "Launch Bot");
    }

    private static void checkAndCloseTelegram(Runtime run) throws IOException, InterruptedException {
        Process process = run.exec("tasklist");
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
            System.out.println("Telegram is running. Closing it...");
            String killProc = "taskkill /IM Telegram.exe /F";
            run.exec(killProc);
            Thread.sleep(1000);
        }
    }

    private static void openTelegramChannel(Runtime run, String channel) throws IOException, InterruptedException {
        String openChannel = "cmd /c start tg://resolve?domain=" + channel;
        run.exec(openChannel);
        System.out.println("Telegram channel opened.");
        Thread.sleep(2000);
    }

    private static void openTelegramBot(Runtime run, String bot) throws IOException, InterruptedException {
        String openBot = "cmd /c start tg://resolve?domain=" + bot;
        run.exec(openBot);
        Thread.sleep(2000);
    }

    private static void findTextCoordinates(String imagePath, String searchText) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("tessdata"); // Specify the path to tessdata folder
        tesseract.setLanguage("eng"); // Set the language if needed

        try {
            // Recognize text in the image
            File imageFile = new File(imagePath);
            String result = tesseract.doOCR(imageFile);
            System.out.println("OCR Result:\n" + result);

            // Find the text and get its coordinates
            ITesseract instance = new Tesseract();
            Rectangle[] boundingBoxes = instance.getBoundingBoxes(imageFile);

            for (Rectangle box : boundingBoxes) {
                String recognizedText = result.substring(box.x, box.y);
                if (recognizedText.contains(searchText)) {
                    System.out.println("Found text: " + searchText);
                    System.out.println("Coordinates: x=" + box.x + ", y=" + box.y);
                }
            }
        } catch (TesseractException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//        try {
//            Thread.sleep(2000);
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
//
//        Robot robot = new Robot();
//
//        // Дії з Notepad
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
