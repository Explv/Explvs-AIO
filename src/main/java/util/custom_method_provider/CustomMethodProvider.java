package util.custom_method_provider;

import org.osbot.rs07.Bot;
import org.osbot.rs07.script.MethodProvider;
import util.executable.Executable;

/**
 * CustomMethodProvider extends OSBot's MethodProvider
 * to provide additional easily accessible utilities for
 * this script
 */
public class CustomMethodProvider extends MethodProvider {

    private ExtendedGroundItems extendedGroundItems;
    private InteractionHelper interactionHelper;
    private ExtendedCamera extendedCamera;
    private ExtendedNPCS extendedNPCS;
    private Graphics graphics;
    private SkillTracker skillTracker;
    private ExecutableUtil executableUtil;
    private ExtendedInventory extendedInventory;
    private MakeAllInterface makeAllInterface;
    private boolean hasContext;

    public void init(final Bot bot) {
        super.exchangeContext(bot);
        this.extendedGroundItems = new ExtendedGroundItems();
        this.extendedCamera = new ExtendedCamera();
        this.interactionHelper = new InteractionHelper();
        this.extendedNPCS = new ExtendedNPCS();
        this.graphics = new Graphics();
        this.skillTracker = new SkillTracker();
        this.executableUtil = new ExecutableUtil();
        this.extendedInventory = new ExtendedInventory();
        this.makeAllInterface = new MakeAllInterface();
        extendedGroundItems.exchangeContext(bot);
        extendedCamera.exchangeContext(bot);
        interactionHelper.exchangeContext(bot);
        extendedNPCS.exchangeContext(bot);
        graphics.exchangeContext(bot);
        skillTracker.exchangeContext(bot);
        executableUtil.exchangeContext(bot, this);
        extendedInventory.exchangeContext(bot);
        makeAllInterface.exchangeContext(bot, this);
        hasContext = true;
    }

    public boolean hasContext() {
        return hasContext;
    }

    // Deprecated as exchangeContext(Bot bot, CustomMethodProvider methodProvider) should be used instead.
    @Deprecated
    public MethodProvider exchangeContext(final Bot bot) { return super.exchangeContext(bot); }

    public CustomMethodProvider exchangeContext(final Bot bot, final CustomMethodProvider methodProvider) {
        this.extendedGroundItems = methodProvider.extendedGroundItems;
        this.extendedCamera = methodProvider.extendedCamera;
        this.interactionHelper = methodProvider.interactionHelper;
        this.extendedNPCS = methodProvider.extendedNPCS;
        this.graphics = methodProvider.graphics;
        this.skillTracker = methodProvider.skillTracker;
        this.executableUtil = methodProvider.executableUtil;
        this.extendedInventory = methodProvider.extendedInventory;
        this.makeAllInterface = methodProvider.makeAllInterface;
        super.exchangeContext(bot);
        hasContext = true;
        return this;
    }

    @Override
    public ExtendedGroundItems getGroundItems() {
        return extendedGroundItems;
    }

    @Override
    public ExtendedCamera getCamera() {
        return extendedCamera;
    }

    public InteractionHelper getInteractionHelper() {
        return interactionHelper;
    }

    @Override
    public ExtendedNPCS getNpcs() {
        return extendedNPCS;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public SkillTracker getSkillTracker() { return skillTracker; }

    @Override
    public ExtendedInventory getInventory() {
        return extendedInventory;
    }

    public MakeAllInterface getMakeAllInterface() { return makeAllInterface; }

    public ExecutableUtil getExecutableUtil() { return executableUtil; }

    public void execute(Executable executable) throws InterruptedException {
        getExecutableUtil().execute(executable);
    }
}

