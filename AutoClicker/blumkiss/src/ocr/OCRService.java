package ocr;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.*;
import config.Config;

public class OCRService {
    private final Tesseract tesseract;

    public OCRService() {
        tesseract = new Tesseract();
        tesseract.setDatapath(Config.dataPath);
        tesseract.setLanguage("eng");
    }

    public String doOCR(String fileName) throws TesseractException {
        File imageFile = new File(fileName);
        return tesseract.doOCR(imageFile);
    }

    public void findAndClickText(Robot robot, BufferedImage screenshot, String searchText, String foundText) throws TesseractException, AWTException, InterruptedException {
        List<Word> words = tesseract.getWords(screenshot, ITessAPI.TessPageIteratorLevel.RIL_WORD);
        for (Word word : words) {
            if (word.getText().equalsIgnoreCase(searchText)) {
                Rectangle boundingBox = word.getBoundingBox();
                int x = boundingBox.x + 5;
                int y = boundingBox.y + 8;
                robot.mouseMove(x, y);
                Thread.sleep(2000);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                break;
            } else {
                System.out.println("Cant find needed phrase. Found: " + word.getText());
            }
        }
    }
}
