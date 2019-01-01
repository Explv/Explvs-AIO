package org.aio.activities.skills.smithing.item_making;

public enum SmithItem {

    DAGGER("Dagger", 1),
    SWORD("Sword", 1),
    SCIMITAR("Scimitar", 2),
    LONG_SWORD("Long sword", 2),
    TWO_HANDED_SWORD("2-hand sword", 3),
    AXE("Axe", 1),
    MACE("Mace", 1),
    WARHAMMER("Warhammer", 3),
    BATTLE_AXE("Battle axe", 3),
    CLAWS ("Claws", 2),
    CHAIN_BODY("Chain body", 3),
    PLATE_LEGS("Plate legs", 3),
    PLATE_SKIRT("Plate skirt", 3),
    PLATE_BODY("Plate body", 5),
    NAILS ("Nails", 1),
    MED_HELM("Medium helm", 1),
    FULL_HELM("Full helm", 2),
    SQUARE_SHIELD("Square shield", 2),
    KITE_SHIELD("Kite shield", 3),
    BULLSEYE_LANTERN("Bullseye lantern", 1),
    DART_TIPS("Dart tips", 1),
    ARROW_TIPS("Arrowtips", 1),
    THROWING_KNIVES("Throwing knives", 1),
    STUDS ("Studs", 1),
    WIRE("Wire", 1),
    BOLTS("Bolts", 1),
    Limbs("Limbs", 1);

    String name;
    int barsRequired;

    SmithItem(final String name, final int barsRequired){
        this.name = name;
        this.barsRequired = barsRequired;
    }

    @Override
    public String toString(){
        return name;
    }
}
