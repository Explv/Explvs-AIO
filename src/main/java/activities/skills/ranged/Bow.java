package activities.skills.ranged;

public enum Bow {

    TRAINING_BOW("Training bow", 1),
    SHORTBOW("Shortbow", 1),
    LONGBOW("Longbow", 1),
    OAK_SHORTBOW("Oak shortbow", 5),
    OAK_LONGBOW("Oak longbow", 5),
    WILLOW_SHORTBOW("Willow shortbow", 20),
    WILLOW_COMP_BOW("Willow comp bow", 20),
    WILLOW_LONGBOW("Willow longbow", 20),
    MAPLE_SHORTBOW("Maple shortbow", 30),
    MAPLE_LONGBOW("Maple longbow", 30),
    OGRE_BOW("Ogre bow", 30),
    COMP_OGRE_BOW("Comp ogre bow", 30),
    YEW_SHORTBOW("Yew shortbow", 40),
    YEW_LONGBOW("Yew longbow", 40),
    YEW_COMP_BOW("Yew comp bow", 40),
    MAGIC_SHORTBOW("Magic shortbow", 50),
    MAGIC_LONGBOW("Magic longbow", 50),
    MAGIC_COMP_BOW("Magic comp bow", 50),
    SEERCULL("Seercull", 50),
    DARK_BOW("Dark bow", 60),
    THIRD_AGE_BOW("3rd age bow", 65),
    CRYSTAL_BOW("Crystal bow", 70);

    String name;
    int rangedLvl;

    Bow(final String name, final int rangedLvl) {
        this.name = name;
        this.rangedLvl = rangedLvl;
    }

    @Override
    public String toString() {
        return name;
    }
}
