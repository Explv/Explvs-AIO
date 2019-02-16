package activities.skills.firemaking;

public enum Log {

    NORMAL("Logs", 1),
    ACHEY_TREE("Achey tree logs", 1),
    OAK("Oak logs", 15),
    WILLOW("Willow logs", 30),
    TEAK("Teak logs", 35),
    ARCTIC_PINE("Arctic pine logs", 42),
    MAPLE("Maple logs", 45),
    MAHOGANY("Mahogany logs", 50),
    YEW("Yew logs", 60),
    MAGIC("Magic logs", 75);

    String NAME;
    int LEVEL_REQUIRED;

    Log(final String NAME, final int LEVEL_REQUIRED) {
        this.NAME = NAME;
        this.LEVEL_REQUIRED = LEVEL_REQUIRED;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
