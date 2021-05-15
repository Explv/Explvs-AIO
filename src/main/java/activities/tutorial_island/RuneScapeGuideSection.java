package activities.tutorial_island;

import org.osbot.rs07.api.Configs;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.script.MethodProvider;
import util.Sleep;
import util.event.ConfigureClientEvent;
import util.event.DisableAudioEvent;
import util.widget.CachedWidget;
import util.widget.filters.WidgetActionFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class RuneScapeGuideSection extends TutorialSection {
    private enum UsernameCheckStatus {
        NOT_AVAILABLE(0),
        CHECKING(1),
        AVAILABLE(2);

        int bitNum;

        UsernameCheckStatus(int bitNum) {
            this.bitNum = bitNum;
        }

        static UsernameCheckStatus getUsernameCheckStatus(Configs configs) {
            int configVal = configs.get(1042);

            for (UsernameCheckStatus checkStatus : values()) {
                if ((configVal & (1 << checkStatus.bitNum)) != 0) {
                    return checkStatus;
                }
            }

            return null;
        }
    }

    private final CachedWidget nameAcceptedWidget = new CachedWidget(w -> w.getMessage().contains("Great!"));
    private final CachedWidget lookupNameWidget = new CachedWidget(w -> w.getMessage().contains("Look up name"));
    private final CachedWidget inputNameWidget = new CachedWidget(w -> w.getMessage().contains("Please pick a unique display name"));
    private final CachedWidget setNameWidget = new CachedWidget("Set name");
    private final CachedWidget chooseDisplayNameWidget = new CachedWidget("Choose display name");
    private final CachedWidget suggestionsWidget = new CachedWidget("one of our suggestions");
    private final CachedWidget creationScreenWidget = new CachedWidget("Head");
    private final CachedWidget experienceWidget = new CachedWidget("What's your experience with Old School Runescape?");

    private boolean isConfigured;

    public RuneScapeGuideSection() {
        super("Gielinor Guide");
    }

    @Override
    public final void run() throws InterruptedException {
        if (pendingContinue()) {
            selectContinue();
            return;
        }

        switch (getProgress()) {
            case 0:
            case 1:
            case 2:
                if (chooseDisplayNameWidget.isVisible(getWidgets())) {
                    setDisplayName();
                } else if (creationScreenWidget.isVisible(getWidgets())) {
                    createRandomCharacter();
                } else if (experienceWidget.isVisible(getWidgets())) {
                    if (getDialogues().selectOption(random(1, 3))) {
                        Sleep.sleepUntil(() -> !experienceWidget.isVisible(getWidgets()), 2000, 600);
                    }
                } else {
                    talkToInstructor();
                }
                break;
            case 3:
                getTabs().open(Tab.SETTINGS);
                break;
            case 10:
                if (!isConfigured) {
                   execute(new ConfigureClientEvent());
                   isConfigured = true;
                } else if (getObjects().closest("Door").interact("Open")) {
                    Sleep.sleepUntil(() -> getProgress() != 10, 5000, 600);
                }
                break;
            default:
                talkToInstructor();
                break;
        }
    }

    private void setDisplayName() throws InterruptedException {
        UsernameCheckStatus checkStatus = UsernameCheckStatus.getUsernameCheckStatus(getConfigs());

        if (checkStatus == null) {
            log("Couldn't determine username check status");
            getBot().getScriptExecutor().stop();
            return;
        }

        switch (checkStatus) {
            case NOT_AVAILABLE:
                if (suggestionsWidget.isVisible(getWidgets())) {
                    Optional<RS2Widget> suggestionWidget = suggestionsWidget.getRelative(getWidgets(), 0, random(2, 5), 0);
                    if (suggestionWidget.isPresent() && suggestionWidget.get().interact("Set name")) {
                        Sleep.sleepUntil(() -> nameAcceptedWidget.get(getWidgets()).isPresent(), 5000);
                    }
                } else if (inputNameWidget.isVisible(getWidgets()) && getKeyboard().typeString(generateRandomString(5), true)) {
                    Sleep.sleepUntil(() -> UsernameCheckStatus.getUsernameCheckStatus(getConfigs()) == UsernameCheckStatus.CHECKING, 2000, 100);
                } else if (lookupNameWidget.interact(getWidgets())) {
                    Sleep.sleepUntil(() -> !inputNameWidget.isVisible(getWidgets()), 8000, 600);
                }
                break;
            case CHECKING:
                Sleep.sleepUntil(() -> UsernameCheckStatus.getUsernameCheckStatus(getConfigs()) != UsernameCheckStatus.CHECKING, 2000, 100);
                break;
            case AVAILABLE:
                if (setNameWidget.interact(getWidgets())) {
                    Sleep.sleepUntil(
                            () -> !chooseDisplayNameWidget.isVisible(getWidgets()),
                            8000,
                            600
                    );
                }
                break;
        }
    }

    private String generateRandomString(int maxLength) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz"
                + "0123456789";
        return new Random().ints(new Random().nextInt(maxLength) + 1, 0, chars.length())
                .mapToObj(i -> "" + chars.charAt(i))
                .collect(Collectors.joining());
    }

    private void createRandomCharacter() throws InterruptedException {
        if (new Random().nextInt(2) == 1) {
            getWidgets().getWidgetContainingText("Female").interact();
        }

        final RS2Widget[] childWidgets = getWidgets().getWidgets(creationScreenWidget.get(getWidgets()).get().getRootId());

        final WidgetActionFilter selectableWidgetActionFilter = new WidgetActionFilter("Select");

        final RS2Widget[] selectableWidgets = Stream.of(childWidgets).filter(selectableWidgetActionFilter::match).toArray(RS2Widget[]::new);

        Collections.shuffle(Arrays.asList(selectableWidgets));

        int maxSelection = new Random().nextInt(Math.min(selectableWidgets.length, 15));

        for (int i = 0; i < maxSelection; i ++) {
            clickRandomTimes(selectableWidgets[i]);
        }

        if (getWidgets().getWidgetContainingText("Confirm").interact()) {
            Sleep.sleepUntil(() -> !creationScreenWidget.isVisible(getWidgets()), 3000, 600);
        }
    }

    private void clickRandomTimes(final RS2Widget widget) throws InterruptedException {
        int clickCount = new Random().nextInt(4);

        for (int i = 0; i < clickCount; i++) {
            if (widget.interact()) {
                MethodProvider.sleep(150);
            }
        }
    }
}