package util.executable;

import org.osbot.rs07.event.Event;

public abstract class BlockingExecutable extends Executable {

    private boolean finished;
    private ExecutionFailedException executionFailedException;

    @Override
    public final void run() throws InterruptedException {
        finished = false;
        executionFailedException = null;
        onStart();
        execute(new Event() {
            @Override
            public int execute() throws InterruptedException {
                if (finished) {
                    setFinished();
                } else {
                    try {
                        blockingRun();
                    } catch (ExecutionFailedException executionFailedException) {
                        BlockingExecutable.this.executionFailedException = executionFailedException;
                        setFailed();
                    }
                }
                return 0;
            }
        });
        onEnd();
        if (executionFailedException != null) {
            throw executionFailedException;
        }
    }

    protected abstract void blockingRun() throws InterruptedException;

    protected void setFinished() {
        finished = true;
    }
}
