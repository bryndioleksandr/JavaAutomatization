package logic;

import net.sourceforge.tess4j.*;

import java.io.File;

public class Logic {
    public static void main(String[] args) {
        File imageFile = new File("C:\\Users\\sanya\\tess.png");
        Tesseract tesseract = new Tesseract();

        try {
            tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
            String result = tesseract.doOCR(imageFile);
            System.out.println("Результат OCR: " + result);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }
}
