package org.aio.activities.banking;

import org.aio.util.Location;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;

import java.util.Arrays;

// TODO: Check these areas
public enum Bank {

    LUMBRIDGE_CASTLE(new Location("Lumbridge Castle", new Area(3207, 3220, 3209, 3216).setPlane(2))),
    DRAYNOR(new Location("Draynor", new Area(3087, 3248, 3098, 3239))),
    DUEL_ARENA(new Location("Duel Arena", new Area(
            new int[][]{
                    { 3380, 3267 },
                    { 3380, 3272 },
                    { 3382, 3274 },
                    { 3384, 3274 },
                    { 3385, 3273 },
                    { 3385, 3267 }
            }
    ))),
    AL_KHARID(new Location("Al-Kharid", new Area(3265, 3161, 3272, 3173))),
    SHANTAY_PASS(new Location("Shantay Pass", new Area(3303, 3128, 3308, 3119))),
    CLAN_WARS(new Location("Clan Wars", new Area(3362, 3173, 3372, 3168))),
    CANIFIS(new Location("Canifis", new Area(3509, 3484, 3513, 3474))),
    GRAND_EXCHANGE(new Location("Grand Exchange", new Area(3159, 3495, 3170, 3485))),
    VARROCK_WEST(new Location("Varrock West", new Area(3180, 3433, 3185, 3447))),
    VARROCK_EAST(new Location("Varrock East", new Area(3250, 3419, 3257, 3423))),
    EDGEVILLE(new Location("Edgeville", new Area(3092, 3499, 3098, 3489))),
    FALADOR_WEST(new Location("Falador West", new Area(
            new int[][]{
                    { 2945, 3366 },
                    { 2945, 3368 },
                    { 2943, 3368 },
                    { 2943, 3374 },
                    { 2948, 3374 },
                    { 2948, 3369 },
                    { 2950, 3369 },
                    { 2950, 3366 }
            }
    ))),
    FALADOR_EAST(new Location("Falador East", new Area(3009, 3359, 3018, 3355))),
    PORT_KHAZARD(new Location("Port Khazard", new Area(2660, 3163, 2664, 3160))),
    YANILLE(new Location("Yanille", new Area(2609, 3088, 2615, 3098))),
    CASTLE_WARS(new Location("Castle Wars", new Area(2441, 3088, 2445, 3082))),
    LLETYA(new Location("Lletya", new Area(2350, 3166, 2355, 3161))),
    ARDOUGNE_NORTH(new Location("Ardougne North", new Area(2611, 3336, 2623, 3329))),
    ARDOUGNE_SOUTH(new Location("Ardougne South", new Area(2649, 3287, 2655, 3280))),
    FISHING_GUILD(new Location("Fishing Guild", new Area(2586, 3422, 2590, 3418))),
    CAMELOT(new Location("Camelot", new Area(
            new int[][]{
                    {2721, 3490},
                    {2721, 3494},
                    {2731, 3494},
                    {2731, 3490},
                    {2728, 3490},
                    {2728, 3487},
                    {2724, 3487},
                    {2724, 3490}
            }
    ))),
    CATHERBY(new Location("Catherby", new Area(2806, 3442, 2812, 3438))),
    BURGH_DE_ROTT(new Location("Burgh de Rott", new Area(3495, 3214, 3501, 3210))),
    PORT_PHASMATYS(new Location("Port Phasmatys", new Area(3686, 3471, 3692, 3461))),
    WARRIORS_GUILD(new Location("Warriors' Guild", new Area(2848, 3537, 2843, 3544))),
    JATIZSO(new Location("Jatizso", new Area(2413, 3804, 2421, 3797))),
    NEITIZNOT(new Location("Neitiznot", new Area(2333, 3807, 2340, 3804))),
    ETCETERIA(new Location("Etceteria", new Area(2617, 3897, 2622, 3893))),
    LUNAR_ISLES(new Location("Lunar Isles", new Area(2097, 3920, 2105, 3917))),
    ZEAH_BLAST_MINE(new Location("Zeah Blast Mine", new Area(1500, 3855, 1506, 3862))),
    PISCATORIS_FISHING_COLONY(new Location("Piscatoris Fishing Colony", new Area(2327, 3686, 2332, 3693))),
    PESCARILIUS(new Location("Pescarilius", new Area(1794, 3791, 1811, 3784))),
    ZEAH(new Location("Zeah", new Area(1519, 3742, 1534, 3735))),
    ZEAH_LOVAKITE_MINE(new Location("Zeah Lovakite Mine", new Area(1433, 3836, 1441, 3821)));

    public Location location;

    Bank(final Location location) {
        this.location = location;
    }

    public static boolean inAnyBank(final Position position) {
        for (Bank bank : Bank.values()) {
            if (bank.location.getArea().contains(position)) return true;
        }
        return false;
    }

    public static Area[] getAreas(){
        return Arrays.stream(Bank.values()).map(bank -> bank.location.getArea()).toArray(Area[]::new);
    }

    @Override
    public String toString(){
        return location.toString();
    }
}
