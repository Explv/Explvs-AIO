package org.aio.util;

import org.osbot.rs07.script.MethodProvider;

public abstract class Executable extends MethodProvider {

    private boolean failed;

    public void onStart() throws InterruptedException {}

    public abstract void run() throws InterruptedException;

    public void onEnd() throws InterruptedException {}

    public boolean canExit() { return true; }

    protected void setFailed() {
        failed = true;
    }

    public boolean hasFailed() {
        return failed;
    }
}

