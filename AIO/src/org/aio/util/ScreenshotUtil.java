package org.aio.util;

import org.osbot.rs07.Bot;
import org.osbot.rs07.canvas.paint.Painter;
import org.osbot.rs07.script.Script;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd_HH.mm.ss");

    public static void takeScreenshot(final Bot bot) throws IOException {
        takeScreenshot(bot, dateFormat.format(LocalDateTime.now()) + ".png");
    }

    public static void takeScreenshot(final Bot bot, final String imageName) throws IOException {
        Script currentScript = bot.getScriptExecutor().getCurrent();
        String scriptName = currentScript.getName();

        File screenshotDir = Paths.get(currentScript.getDirectoryData(), scriptName, "Screenshots", bot.getMethods().myPlayer().getName()).toFile();

        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }

        BufferedImage gameBuffer = bot.getCanvas().getGameBuffer();

        if (gameBuffer == null) {
            return;
        }

        BufferedImage outImage = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                                    .getDefaultScreenDevice()
                                                    .getDefaultConfiguration()
                                                    .createCompatibleImage(gameBuffer.getWidth(), gameBuffer.getHeight(), 1);

        Graphics2D graphics = outImage.createGraphics();

        graphics.drawImage(gameBuffer, 0, 0, null);

        for (final Painter painter : bot.getPainters()) {
            painter.onPaint(graphics);
        }

        graphics.dispose();

        File outputFile = Paths.get(screenshotDir.getAbsolutePath(), imageName).toFile();
        ImageIO.write(outImage, "PNG", outputFile);
    }
}
