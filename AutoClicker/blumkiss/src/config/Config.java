package config;

import java.awt.*;

public class Config {
    public static String channel = "blumcrypto";
    public static String bot = "BlumCryptoBot";
    public static String dataPath = "C:\\Program Files\\Tesseract-OCR\\tessdata";
    public static boolean findPlay = false;
    public static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static int resolutionScale = Toolkit.getDefaultToolkit().getScreenResolution() / 96;
    public static String username;
    public static String launchPng = "findLaunch.png";
    public static String searchLaunch = "Launch";
    public static String launchBot = "Launch bot";
    public static String usernamePng = "findUsername.png";
    public static String playPng = "findPlay.png";
    public static String searchWallet = "Wallet";
    public static String osName = System.getProperty("os.name");
}
