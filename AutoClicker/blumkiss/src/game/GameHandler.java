package game;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import config.Config;
import ocr.OCRService;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.util.List;

public class GameHandler {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    private final Robot robot;

    public GameHandler(Robot robot) {
        this.robot = robot;
    }

    public void playGame() throws IOException, InterruptedException, AWTException {
        int xPlay = (717 * Config.width) / 1920;
        int yPlay = (82 * Config.height) / 1080;
        int widthPlay = (488 * Config.width) / 1920;
        int heightPlay = (872 * Config.height) / 1080;
        int gameplayIndex = 0;
        long gameDuration = 32 * 1000;
        long startTime = System.currentTimeMillis();

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(2000);

        while (System.currentTimeMillis() - startTime < gameDuration) {
            while (System.currentTimeMillis() - startTime < gameDuration / 2) {
                BufferedImage screenshotGameplay = new Robot().createScreenCapture(
                        new java.awt.Rectangle(xPlay, yPlay, widthPlay, heightPlay)
                );
                gameplayIndex++;
                String screenshotFilename = "gameplay\\gameplay_" + gameplayIndex + ".png";
                File outputfile = new File(screenshotFilename);
                ImageIO.write(screenshotGameplay, "png", outputfile);

                detectObjects(screenshotFilename, xPlay, yPlay);
                Thread.sleep(200);
            }
            while (System.currentTimeMillis() - startTime < gameDuration) {
                BufferedImage screenshotGameplay = new Robot().createScreenCapture(
                        new java.awt.Rectangle(xPlay, yPlay, widthPlay, heightPlay)
                );
                gameplayIndex++;
                String screenshotFilename = "gameplay\\gameplay_" + gameplayIndex + ".png";
                File outputfile = new File(screenshotFilename);
                ImageIO.write(screenshotGameplay, "png", outputfile);

                detectObjectsSecondHalf(screenshotFilename, xPlay, yPlay);
                Thread.sleep(200);
            }
        }


        while (true) {
            System.out.println("Checking for Play button...");
            if (OCRService.findAndClickPlayButton(robot)) {
                System.out.println("Found Play button! Starting a new game...");
                playGame();
                break;
            } else {
                System.out.println("Play button not found. Retrying in 3 seconds...");
                Thread.sleep(3000);
            }
        }
    }

    public void detectObjects(String screenshotFilename, int x, int y) throws IOException, InterruptedException {
        Mat image = Imgcodecs.imread(screenshotFilename);

        Mat hsvImage = new Mat();
        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);
        Scalar lowerGreen = new Scalar(35, 150, 160);
        Scalar upperGreen = new Scalar(70, 255, 255);

        Mat mask = new Mat();
        Core.inRange(hsvImage, lowerGreen, upperGreen, mask);

        List<MatOfPoint> contours = new java.util.ArrayList<>();
        Imgproc.findContours(mask, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        for (MatOfPoint contour : contours) {
            Rect boundingRect = Imgproc.boundingRect(contour);
            int centerX = boundingRect.x + boundingRect.width / 2;
            int centerY = boundingRect.y + boundingRect.height / 2;

            if (boundingRect.width > 20 && boundingRect.height > 20) {
                robot.mouseMove(centerX + x, centerY + y);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(4);
            }
        }

        mask.release();
        hsvImage.release();
        image.release();
    }


    public void detectObjectsSecondHalf(String screenshotFilename, int x, int y) throws IOException, InterruptedException {
        Mat image = Imgcodecs.imread(screenshotFilename);

        Mat hsvImage = new Mat();
        Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);
        Scalar lowerGreen = new Scalar(35, 150, 160);
        Scalar upperGreen = new Scalar(70, 255, 255);

        Mat mask = new Mat();
        Core.inRange(hsvImage, lowerGreen, upperGreen, mask);

        List<MatOfPoint> contours = new java.util.ArrayList<>();
        Imgproc.findContours(mask, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        for (MatOfPoint contour : contours) {
            Rect boundingRect = Imgproc.boundingRect(contour);
            int centerX = boundingRect.x + boundingRect.width / 2;
            int centerY = boundingRect.y + boundingRect.height / 2;

            if (boundingRect.width < 22 && boundingRect.height < 22 && boundingRect.width > 15 && boundingRect.height > 15) {
                robot.mouseMove(centerX + x, centerY + y);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(6);
            }
        }

        mask.release();
        hsvImage.release();
        image.release();
    }
}


