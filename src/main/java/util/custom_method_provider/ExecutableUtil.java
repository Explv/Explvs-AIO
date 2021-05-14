package util.custom_method_provider;

import util.executable.Executable;

public class ExecutableUtil extends CustomMethodProvider {

    public void execute(final Executable executable) throws InterruptedException {
        if (!executable.hasContext()) {
            executable.exchangeContext(getBot(), this);
        }
        executable.run();
    }
}
