package util;

import org.osbot.rs07.script.MethodProvider;

/**
 * This class is used throughout the script, it is designed as a replacement for the
 * OSBot Event class. Unlike the OSBot Event class, this will not block the Script thread.
 * <p>
 * This means that unlike the OSBot Event class, you are able to have nested Executables
 */
public abstract class Executable extends MethodProvider {

    private boolean failed;

    public void onStart() throws InterruptedException {
    }

    public abstract void run() throws InterruptedException;

    public void onEnd() throws InterruptedException {
    }

    public boolean canExit() {
        return true;
    }

    protected void setFailed() {
        failed = true;
    }

    public boolean hasFailed() {
        return failed;
    }
}

