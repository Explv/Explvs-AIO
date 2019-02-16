package activities.skills.agility;

public enum Obstacle {

    LOG_BALANCE("Log balance", "Walk-across"),
    TREE_BRANCH_UP("Tree branch", "Climb"),
    TREE_BRANCH_DOWN("Tree branch", "Climb-down"),
    OBSTACLE_NET("Obstacle net", "Climb-over"),
    OBSTACLE_PIPE("Obstacle pipe", "Squeeze-through"),
    BALANCING_ROPE("Balancing rope", "Walk-on"),
    ROUGH_WALL("Rough wall", "Climb"),
    TIGHTROPE("Tightrope", "Cross"),
    NARROW_WALL("Narrow wall", "Balance"),
    WALL("Wall", "Jump-up"),
    GAP("Gap", "Jump"),
    CRATE("Crate", "Climb-down"),
    CABLE("Cable", "Swing-across"),
    ZIP_LINE("Zip line", "Teeth-grip"),
    TROPICAL_TREE("Tropical tree", "Swing-across"),
    ROOF_TOP_BEAMS("Roof top beams", "Climb"),
    CLOTHES_LINE("Clothes line", "Cross"),
    LEAP_GAP("Gap", "Leap"),
    BALANCE_WALL("Wall", "Balance"),
    LEDGE("Ledge", "Hurdle"),
    EDGE("Edge", "Jump-off"),
    ROPE_SWING("Ropeswing", "Swing-on"),
    BALANCING_LEDGE("Balancing ledge", "Walk-across"),
    LADDER_DOWN("Ladder", "Climb-down"),
    CRUMBLING_WALL("Crumbling wall", "Climb-over"),
    TALL_TREE("Tall tree", "Climb"),
    POLE_VAULT("Pole-vault", "Vault"),
    HAND_HOLDS("Hand holds", "Cross"),
    JUMP_LEDGE("Ledge", "Jump"),
    JUMP_EDGE("Edge", "Jump"),
    CLIMB_WALL("Wall", "Climb-up"),
    BASKET("Basket", "Climb-on"),
    MARKET_STALL("Market stall", "Jump-on"),
    BANNER("Banner", "Grab"),
    TREE("Tree", "Jump-to"),
    MONKEY_BARS("Monkeybars", "Cross"),
    DRYING_LINE("Drying line", "Jump-to"),
    JUMP_ON_TREE("Tree", "Jump-on"),
    HURDLE_GAP("Gap", "Hurdle"),
    PILE_OF_FISH("Pile of fish", "Jump-in");

    String NAME;
    String ACTION;

    Obstacle(final String NAME, final String ACTION) {
        this.NAME = NAME;
        this.ACTION = ACTION;
    }
}
