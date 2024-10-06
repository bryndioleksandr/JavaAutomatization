package imageProcessing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.*;
import net.sourceforge.lept4j.*;
import config.Config;
import java.util.List;
import ocr.*;

public class ImageProcessor {

    private final OCRService ocrService;

    public ImageProcessor() {
        ocrService = new OCRService();
    }

    public void captureAndSearchText(Robot robot, String fileName, String searchText, String foundText) throws AWTException, IOException, InterruptedException, TesseractException {
        BufferedImage screenshot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIO.write(screenshot, "png", new File(fileName));
        Thread.sleep(1000);

        String resultText = ocrService.doOCR(fileName);
        System.out.println(resultText);
        ocrService.findAndClickText(robot, screenshot, searchText, foundText);
    }

    public void captureAndSearchTextScroll(Robot robot, String fileName, String searchText, String foundText) throws AWTException, IOException, InterruptedException, TesseractException {
        BufferedImage screenshot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIO.write(screenshot, "png", new File(fileName));
        Thread.sleep(1000);

        String resultText = ocrService.doOCR(fileName);
        System.out.println(resultText);
        ocrService.findAndClickText(robot, screenshot, searchText, foundText);
        scrollToPlay(robot);
    }

    private void scrollToPlay(Robot robot) {
        robot.mouseWheel(200);
    }
}
