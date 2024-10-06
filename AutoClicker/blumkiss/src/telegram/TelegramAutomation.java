package telegram;

import java.awt.*;
import java.io.*;
import config.Config;
import imageprocessing.ImageProcessor;
import ocr.OCRService;
import game.GameHandler;

public class TelegramAutomation {

    private final OCRService ocrService;
    private final ImageProcessor imageProcessor;

    public TelegramAutomation() {
        ocrService = new OCRService();
        imageProcessor = new ImageProcessor();
    }

    public void runTelegram() throws IOException, AWTException, InterruptedException {
        Robot robot = new Robot();
        Runtime runtime = Runtime.getRuntime();

        if (Config.osName.contains("Windows")) {
            if (isTelegramRunning()) {
                killProcess("Telegram.exe");
                Thread.sleep(1000);
            }
            runtime.exec("Telegram.exe");
        } else {
            if (isTelegramRunning()) {
                killProcess("Telegram");
                Thread.sleep(1000);
            }
            runtime.exec("open -a Telegram");
        }

        Thread.sleep(1000);
        runTelegramBot(runtime, robot);

        if (Config.findPlay) {
            new GameHandler(robot).playGame();
        } else {
            System.out.println("Couldn`t find play btn. Fix it");
        }
    }

    private boolean isTelegramRunning() throws IOException {
        Process process = Runtime.getRuntime().exec(Config.osName.contains("Windows") ? "tasklist" : "ps -A");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("Telegram")) {
                return true;
            }
        }
        return false;
    }

    private void killProcess(String processName) throws IOException {
        String command = Config.osName.contains("Windows") ? "taskkill /IM " + processName + " /F" : "pkill -f " + processName;
        Runtime.getRuntime().exec(command);
    }

    private void runTelegramBot(Runtime runtime, Robot robot) throws IOException, InterruptedException, AWTException {
        String runBotCommand = Config.osName.contains("Windows") ?
                "cmd /c start tg://resolve?domain=" + Config.bot + "&startapp" :
                "open tg://resolve?domain=" + Config.bot + "&startapp";
        runtime.exec(runBotCommand);
        Thread.sleep(10000);

        imageProcessor.captureAndSearchText(robot, Config.launchPng, Config.searchLaunch, Config.launchBot);
        Thread.sleep(10000);
        imageProcessor.captureAndSearchTextScroll(robot, Config.usernamePng, Config.username, Config.username);
        Thread.sleep(2500);
        imageProcessor.captureAndSearchText(robot, Config.playPng, Config.searchWallet, Config.searchWallet);
    }
}
