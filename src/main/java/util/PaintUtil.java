package util;

public class PaintUtil {

    public static String formatTime(long ms) {
        long s = ms / 1000, m = s / 60, h = m / 60;
        h %= 24;
        m %= 60;
        s %= 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
