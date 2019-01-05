package org.aio.gui.utils;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class EventDispatchThreadRunner {

    public static void runOnDispatchThread(final Runnable runnable, final boolean wait) throws InvocationTargetException, InterruptedException {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else if (wait) {
            SwingUtilities.invokeAndWait(runnable);
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }
}
