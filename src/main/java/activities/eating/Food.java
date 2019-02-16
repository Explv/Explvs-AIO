package activities.eating;

public enum Food {
    NONE("None", 0),
    SHRIMP("Shrimp", 3),
    CHICKEN("Cooked chicken", 3),
    SARDINE("Sardine", 3),
    MEAT("Cooked meat", 3),
    BREAD("Bread", 5),
    HERRING("Herring", 5),
    MACKEREL("Mackerel", 5),
    TROUT("Trout", 7),
    COD("Cod", 7),
    PIKE("Pike", 8),
    ROAST_BEAST("Roast beast meat", 8),
    PINEAPPLE_PUNCH("Pineapple punch", 9),
    SALMON("Salmon", 9),
    TUNA("Tuna", 10),
    JUG_OF_WINE("Jug of wine", 11),
    RAINBOW_FISH("Rainbow fish", 11),
    STEW("Stew", 11),
    CAKE("Cake", 4),
    MEAT_PIE("Meat pie", 6),
    LOBSTER("Lobster", 12),
    BASS("Bass", 13),
    PLAIN_PIZZA("Plain pizza", 7),
    SWORDFISH("Swordfish", 14),
    POTATO_WITH_BUTTER("Potato with butter", 14),
    CHOCOLATE_CAKE("Chocolate cake", 5),
    TANGLED_TOADS_LEGS("Tangled toad's legs", 15),
    POTATO_WITH_CHEESE("Potato with cheese", 16),
    MEAT_PIZZA("Meat pizza", 8),
    MONKFISH("Monkfish", 16),
    ANCHOVY_PIZZA("Anchovy pizza", 9),
    COOKED_KARAMBWAN("Cooked karambwan", 18),
    CURRY("Curry", 19),
    UGTHANKI_KEBAB("Ugthanki kebab", 19),
    MUSHROOM_POTATO("Mushroom potato", 20),
    SHARK("Shark", 20),
    SEA_TURTLE("Sea turtle", 21),
    PINEAPPLE_PIZZA("Pineapple pizza", 11),
    MANTA_RAY("Manta ray", 22),
    TUNA_POTATO("Tuna potato", 22),
    DARK_CRAB("Dark crab", 22),
    BASKET_OF_STRAWBERRIES("Basket of strawberries", -1),
    SARADOMIN_BREW("Saradomin brew", -1);

    String name;
    int healthGain;

    Food(final String name, final int healthGain) {
        this.name = name;
        this.healthGain = healthGain;
    }

    @Override
    public String toString() {
        return name;
    }
}
