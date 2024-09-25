package logic;

import net.sourceforge.tess4j.*;

import java.io.File;

public class Logic {
    public static void main(String[] args) {
        File imageFile = new File("C:\\Users\\sanya\\tess.png"); // Шлях до твого зображення
        Tesseract tesseract = new Tesseract();

        try {
            tesseract.setDatapath("C:\\Users\\sanya\\Downloads\\tess4j-5.13.0.jar\\tessdata"); // Онови до свого шляху tessdata
            String result = tesseract.doOCR(imageFile);
            System.out.println("Результат OCR: " + result);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }
}
