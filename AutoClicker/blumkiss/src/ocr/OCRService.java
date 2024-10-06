package ocr;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.*;
import config.Config;

public class OCRService {
    private static final Tesseract tesseract = new Tesseract();

    public OCRService() {
        tesseract.setDatapath(Config.dataPath);
        tesseract.setLanguage("eng");
    }

    public String doOCR(String fileName) throws TesseractException {
        File imageFile = new File(fileName);
        return tesseract.doOCR(imageFile);
    }

    public static void findAndClickText(Robot robot, BufferedImage screenshot, String searchText, String foundText) throws TesseractException, AWTException, InterruptedException {
        List<Word> words = tesseract.getWords(screenshot, ITessAPI.TessPageIteratorLevel.RIL_WORD);
        for (Word word : words) {
            if (word.getText().equalsIgnoreCase(searchText)) {
                Rectangle boundingBox = word.getBoundingBox();
                int xPlay = (717 * Config.width) / 1920;
                int yPlay = (82 * Config.height) / 1080;

                int x = boundingBox.x;
                int y = boundingBox.y;
                int widthBox = boundingBox.width;
                int heightBox = boundingBox.height;
                if (searchText.matches("Wallet")) {
                    robot.mouseMove(xPlay + x, yPlay + y - ((225 * Config.width) / 1920));
                    Thread.sleep(2000);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    Config.findPlay = true;
                } else if (searchText.matches("Rewards")) {
                    robot.mouseMove(xPlay + x, yPlay + y + ((300 * Config.width) / 1920));
                    Thread.sleep(2000);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    Config.findPlay = true;
                } else {
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

    public static boolean findAndClickPlayButton(Robot robot) throws IOException, InterruptedException, AWTException {
        int xPlay = (717*Config.width)/1920;
        int yPlay = (82*Config.height)/1080;
        int widthPlay = (488*Config.width)/1920;
        int heightPlay = (872*Config.height)/1080;
        BufferedImage screenshot = new Robot().createScreenCapture(
                new Rectangle(xPlay, yPlay , widthPlay, heightPlay)
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

                findAndClickText(robot, screenshot, "Rewards", "Play");
                return true;
            } else {
                System.out.println("Play button not found.");
            }
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return false;
    }
}
