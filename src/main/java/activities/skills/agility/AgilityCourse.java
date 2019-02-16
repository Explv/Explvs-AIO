package activities.skills.agility;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;

public enum AgilityCourse {

    GNOME_STRONGHOLD("Gnome Stronghold Agility Course",
            new CoursePart(Obstacle.LOG_BALANCE, new Area(2471, 3435, 2477, 3438)),
            new CoursePart(Obstacle.OBSTACLE_NET, new Area(2469, 3425, 2479, 3429)),
            new CoursePart(Obstacle.TREE_BRANCH_UP, new Area(new Position(2476, 3424, 1), new Position(2471, 3422, 1))),
            new CoursePart(Obstacle.BALANCING_ROPE, new Area(new Position(2477, 3421, 2), new Position(2472, 3418, 2))),
            new CoursePart(Obstacle.TREE_BRANCH_DOWN, new Area(new Position(2488, 3421, 2), new Position(2483, 3418, 2))),
            new CoursePart(Obstacle.OBSTACLE_NET, new Area(2481, 3417, 2490, 3426)),
            new CoursePart(Obstacle.OBSTACLE_PIPE, new Area(2490, 3427, 2481, 3431))
    ),
    DRAYNOR_VILLAGE_ROOFTOP("Draynor Village Rooftop Course",
            new CoursePart(
                    Obstacle.ROUGH_WALL,
                    new Area(3105, 3277, 3103, 3281)
            ),
            new CoursePart(
                    Obstacle.TIGHTROPE,
                    new Area(3097, 3277, 3102, 3281).setPlane(3)
            ),
            new CoursePart(
                    Obstacle.TIGHTROPE,
                    new Area(
                            new int[][]{
                                    {3090, 3272},
                                    {3087, 3275},
                                    {3091, 3279},
                                    {3094, 3276}
                            }
                    ).setPlane(3)
            ),
            new CoursePart(
                    Obstacle.NARROW_WALL,
                    new Area(3089, 3265, 3095, 3268).setPlane(3)
            ),
            new CoursePart(
                    Obstacle.WALL,
                    new Area(3083, 3256, 3088, 3261).setPlane(3)
            ),
            new CoursePart(
                    Obstacle.GAP,
                    new Area(3087, 3251, 3094, 3255).setPlane(3),
                    new Position[]{
                            new Position(3093, 3255, 3)
                    }
            ),
            new CoursePart(
                    Obstacle.CRATE,
                    new Area(3096, 3256, 3101, 3261).setPlane(3)
            )
    ),
    AL_KHARID_ROOFTOP("Al Kharid Rooftop Course",
            new CoursePart(
                    Obstacle.ROUGH_WALL,
                    new Area(3276, 3198, 3270, 3195)
            ),
            new CoursePart(
                    Obstacle.TIGHTROPE,
                    new Area(
                            new int[][]{
                                    {3278, 3195},
                                    {3270, 3195},
                                    {3270, 3189},
                                    {3272, 3189},
                                    {3272, 3184},
                                    {3271, 3184},
                                    {3271, 3179},
                                    {3276, 3179},
                                    {3276, 3184},
                                    {3280, 3184},
                                    {3280, 3189},
                                    {3278, 3189}
                            }
                    ).setPlane(3),
                    new Position[]{
                            new Position(3273, 3188, 3),
                            new Position(3273, 3183, 3)
                    }
            ),
            new CoursePart(
                    Obstacle.CABLE,
                    new Area(3265, 3161, 3272, 3173).setPlane(3),
                    new Position[]{
                            new Position(3269, 3168, 3)
                    }
            ),
            new CoursePart(
                    Obstacle.ZIP_LINE,
                    new Area(3282, 3159, 3303, 3177).setPlane(3),
                    new Position[]{
                            new Position(3289, 3166, 3),
                            new Position(3295, 3166, 3),
                            new Position(3300, 3163, 3)
                    }
            ),
            new CoursePart(
                    Obstacle.TROPICAL_TREE,
                    new Area(3318, 3165, 3313, 3160).setPlane(1),
                    new Position[]{
                            new Position(3318, 3165, 1)
                    }
            ),
            new CoursePart(
                    Obstacle.ROOF_TOP_BEAMS,
                    new Area(3318, 3179, 3312, 3173).setPlane(2),
                    new Position[]{
                            new Position(3317, 3178, 2)
                    }
            ),
            new CoursePart(
                    Obstacle.TIGHTROPE,
                    new Area(3318, 3180, 3312, 3186).setPlane(3),
                    new Position[]{
                            new Position(3313, 3184, 3)
                    }
            ),
            new CoursePart(
                    Obstacle.GAP,
                    new Area(
                            new int[][]{
                                    {3303, 3185},
                                    {3297, 3191},
                                    {3301, 3195},
                                    {3307, 3189}
                            }
                    ).setPlane(3),
                    new Position[]{
                            new Position(3301, 3191, 3)
                    }
            )
    ),
    VARROCK_ROOFTOP("Varrock Rooftop Course",
            new CoursePart(
                    Obstacle.ROUGH_WALL,
                    new Area(3222, 3412, 3221, 3416)
            ),
            new CoursePart(
                    Obstacle.CLOTHES_LINE,
                    new Area(3212, 3409, 3221, 3421).setPlane(3),
                    new Position[]{
                            new Position(3214, 3414, 3)
                    }
            ),
            new CoursePart(
                    Obstacle.LEAP_GAP,
                    new Area(
                            new int[][]{
                                    {3207, 3411},
                                    {3206, 3412},
                                    {3206, 3413},
                                    {3205, 3413},
                                    {3201, 3417},
                                    {3204, 3420},
                                    {3208, 3420},
                                    {3210, 3418},
                                    {3209, 3417},
                                    {3209, 3412},
                                    {3208, 3411}
                            }
                    ).setPlane(3),
                    new Position[]{
                            new Position(3202, 3416, 3)
                    }
            ),
            new CoursePart(
                    Obstacle.BALANCE_WALL,
                    new Area(3198, 3417, 3193, 3415).setPlane(1),
                    new Position[]{
                            new Position(3193, 3416, 1)
                    }
            ),
            new CoursePart(
                    Obstacle.LEAP_GAP,
                    new Area(3192, 3402, 3198, 3406).setPlane(3),
                    new Position[]{
                            new Position(3194, 3403, 3)
                    }
            ),
            new CoursePart(
                    Obstacle.LEAP_GAP,
                    new Area(
                            new int[][]{
                                    {3182, 3382},
                                    {3182, 3398},
                                    {3183, 3399},
                                    {3202, 3399},
                                    {3202, 3404},
                                    {3209, 3404},
                                    {3209, 3395},
                                    {3201, 3395},
                                    {3196, 3390},
                                    {3196, 3388},
                                    {3190, 3388},
                                    {3190, 3382}
                            }
                    ).setPlane(3),
                    new Position[]{
                            new Position(3196, 3398, 3),
                            new Position(3201, 3398, 3),
                            new Position(3205, 3398, 3),
                            new Position(3207, 3398, 3)
                    },
                    new Position(3209, 3397, 3)
            ),
            new CoursePart(
                    Obstacle.LEAP_GAP,
                    new Area(
                            new int[][]{
                                    {3218, 3393},
                                    {3218, 3403},
                                    {3220, 3405},
                                    {3221, 3405},
                                    {3223, 3403},
                                    {3233, 3403},
                                    {3233, 3393}
                            }
                    ).setPlane(3),
                    new Position[]{
                            new Position(3218, 3394, 3),
                            new Position(3222, 3394, 3),
                            new Position(3226, 3393, 3),
                            new Position(3230, 3393, 3),
                            new Position(3232, 3396, 3),
                            new Position(3232, 3400, 3)
                    },
                    new Position(3233, 3402, 3)
            ),
            new CoursePart(
                    Obstacle.LEDGE,
                    new Area(3236, 3409, 3240, 3403).setPlane(3),
                    new Position[]{
                            new Position(3236, 3408, 3)
                    }
            ),
            new CoursePart(
                    Obstacle.EDGE,
                    new Area(3236, 3410, 3240, 3416).setPlane(3)
            )
    ),
    /*BARBARIAN_COURSE("Barbarian Course",
            new CoursePart(Obstacle.ROPE_SWING, new Area(2550, 3553, 2553, 3559)),
            new CoursePart(Obstacle.LOG_BALANCE, new Area(2553, 3549, 2549, 3542)),
            new CoursePart(Obstacle.OBSTACLE_NET, new Area(2543, 3542, 2539, 3551)),
            new CoursePart(Obstacle.BALANCING_LEDGE, new Area(new Position(2538, 3545, 1), new Position(2536, 3547, 1))),
            new CoursePart(Obstacle.LADDER_DOWN, new Area(new Position(2532, 3547, 1), new Position(2532, 3546, 1))),
            new CoursePart(Obstacle.CRUMBLING_WALL, new Area(new int[][]{
                    {2532, 3546},
                    {2532, 3556},
                    {2537, 3556},
                    {2537, 3548},
                    {2533, 3548},
                    {2533, 3546}
            }), new Position[]{new Position(2532, 3549, 0), new Position(2535, 3553, 0)}),
            new CoursePart(Obstacle.CRUMBLING_WALL, new Area(2537, 3552, 2539, 3554), new Position(2539, 3553, 0)),
            new CoursePart(Obstacle.CRUMBLING_WALL, new Area(2540, 3552, 2542, 3554), new Position(2542, 3553, 0))
    ),*/

    CANAFIS_ROOFTOP("Canafis Rooftop",
            new CoursePart(
                    Obstacle.TALL_TREE,
                    new Area(3504, 3484, 3511, 3490)
            ),
            new CoursePart(
                    Obstacle.GAP,
                    new Area(new Position(3504, 3488, 2), new Position(3511, 3498, 2)),
                    new Position[]{
                            new Position(3506, 3493, 2),
                            new Position(3506, 3496, 2)
                    }
            ),
            new CoursePart(
                    Obstacle.GAP,
                    new Area(new Position(3504, 3503, 2), new Position(3496, 3508, 2)),
                    new Position[]{
                            new Position(3498, 3504, 2)
                    },
                    new Position(3496, 3504, 2)
            ),
            new CoursePart(
                    Obstacle.GAP,
                    new Area(new Position(3493, 3498, 2), new Position(3485, 3505, 2)),
                    new Position[]{
                            new Position(3489, 3500, 2)
                    },
                    new Position(3485, 3499, 2)
            ),
            new CoursePart(
                    Obstacle.GAP,
                    new Area(new Position(3480, 3500, 3), new Position(3474, 3491, 3)),
                    new Position[]{
                            new Position(3478, 3495, 3)
                    },
                    new Position(3478, 3491, 3)
            ),
            new CoursePart(
                    Obstacle.POLE_VAULT,
                    new Area(new Position(3477, 3487, 2), new Position(3484, 3481, 2))
            ),
            new CoursePart(
                    Obstacle.GAP,
                    new Area(new Position(3488, 3479, 3), new Position(3504, 3468, 3)),
                    new Position[]{
                            new Position(3494, 3476, 3), new Position(3499, 3476, 3)
                    },
                    new Position(3503, 3476, 3)
            ),
            new CoursePart(
                    Obstacle.GAP,
                    new Area(new Position(3508, 3474, 2), new Position(3517, 3483, 2)),
                    new Position[]{
                            new Position(3510, 3481, 2)
                    },
                    new Position(3510, 3483, 2)
            )
    ),
    FALADOR_ROOFTOP("Falador Rooftop",
            new CoursePart(
                    Obstacle.ROUGH_WALL,
                    new Area(3034, 3339, 3038, 3341)
            ),
            new CoursePart(
                    Obstacle.TIGHTROPE,
                    new Area(new Position(3036, 3342, 3), new Position(3040, 3343, 3))
            ),
            new CoursePart(
                    Obstacle.HAND_HOLDS,
                    new Area(new Position(3044, 3341, 3), new Position(3051, 3349, 3)),
                    new Position[]{
                            new Position(3051, 3349, 3)
                    }
            ),
            new CoursePart(
                    Obstacle.GAP,
                    new Area(new Position(3051, 3356, 3), new Position(3047, 3358, 3))
            ),
            new CoursePart(
                    Obstacle.GAP,
                    new Area(new Position(3048, 3361, 3), new Position(3045, 3367, 3)),
                    new Position(3044, 3361, 3)
            ),
            new CoursePart(
                    Obstacle.TIGHTROPE,
                    new Area(new Position(3041, 3361, 3), new Position(3034, 3365, 3)),
                    new Position[]{
                            new Position(3036, 3361, 3)
                    }
            ),
            new CoursePart(
                    Obstacle.TIGHTROPE,
                    new Area(new Position(3029, 3352, 3), new Position(3026, 3356, 3))
            ),
            new CoursePart(
                    Obstacle.GAP,
                    new Area(new Position(3021, 3353, 3), new Position(3009, 3358, 3))
            ),
            new CoursePart(
                    Obstacle.JUMP_LEDGE,
                    new Area(new Position(3022, 3343, 3), new Position(3016, 3349, 3))
            ),
            new CoursePart(
                    Obstacle.JUMP_LEDGE,
                    new Area(new Position(3014, 3346, 3), new Position(3011, 3343, 3)),
                    new Position(3011, 3343, 3)
            ),
            new CoursePart(
                    Obstacle.JUMP_LEDGE,
                    new Area(new Position(3013, 3342, 3), new Position(3009, 3335, 3)),
                    new Position(3012, 3334, 3)
            ),
            new CoursePart(
                    Obstacle.JUMP_LEDGE,
                    new Area(new Position(3012, 3334, 3), new Position(3018, 3331, 3)),
                    new Position(3018, 3332, 3)
            ),
            new CoursePart(
                    Obstacle.JUMP_EDGE,
                    new Area(new Position(3019, 3335, 3), new Position(3024, 3332, 3))
            )
    ),
    SEERS_VILLAGE_ROOFTOP("Seers Village Rooftop",
            new CoursePart(Obstacle.CLIMB_WALL, new Area(2728, 3487, 2730, 3489)),
            new CoursePart(Obstacle.GAP, new Area(new Position(2730, 3490, 3), new Position(2721, 3497, 3)), new Position[]{new Position(2724, 3492, 3)}),
            new CoursePart(Obstacle.TIGHTROPE, new Area(new Position(2714, 3498, 2), new Position(2704, 3487, 2))),
            new CoursePart(Obstacle.GAP, new Area(new Position(2716, 3482, 2), new Position(2709, 3476, 2))),
            new CoursePart(Obstacle.GAP, new Area(new Position(2716, 3475, 3), new Position(2699, 3470, 3)), new Position[]{new Position(2705, 3472, 3)}),
            new CoursePart(Obstacle.JUMP_EDGE, new Area(new Position(2697, 3466, 2), new Position(2703, 3459, 2)))
    ),
    POLLNIVNEACH_ROOFTOP("Pollnivneach Rooftop",
            new CoursePart(Obstacle.BASKET, new Area(3350, 2961, 3352, 2962)),
            new CoursePart(Obstacle.MARKET_STALL, new Area(new Position(3351, 2968, 1), new Position(3346, 2963, 1)), new Position[]{new Position(3351, 2968, 1)}),
            new CoursePart(Obstacle.BANNER, new Area(new Position(3352, 2976, 1), new Position(3355, 2973, 1)), new Position[]{new Position(3355, 2976, 1)}),
            new CoursePart(Obstacle.LEAP_GAP, new Area(new Position(3360, 2979, 1), new Position(3362, 2977, 1))),
            new CoursePart(Obstacle.TREE, new Area(new Position(3366, 2976, 1), new Position(3370, 2974, 1))),
            new CoursePart(Obstacle.ROUGH_WALL, new Area(new Position(3365, 2982, 1), new Position(3369, 2986, 1))),
            new CoursePart(Obstacle.MONKEY_BARS, new Area(new Position(3355, 2980, 2), new Position(3365, 2985, 2)), new Position[]{new Position(3361, 2984, 2)}),
            new CoursePart(Obstacle.JUMP_ON_TREE, new Area(new Position(3357, 2991, 2), new Position(3370, 2995, 2)), new Position[]{new Position(3360, 2995, 2)}),
            new CoursePart(Obstacle.DRYING_LINE, new Area(new Position(3356, 3004, 2), new Position(3362, 3000, 2)))
    ),
    RELLEKA_ROOFTOP("Relleka Rooftop",
            new CoursePart(Obstacle.ROUGH_WALL, new Area(2623, 3677, 2627, 3679)),
            new CoursePart(Obstacle.LEAP_GAP, new Area(2620, 3669, 2628, 3677).setPlane(3)),
            new CoursePart(Obstacle.TIGHTROPE, new Area(2623, 3656, 2615, 3668).setPlane(3)),
            new CoursePart(Obstacle.LEAP_GAP, new Area(2632, 3657, 2625, 3651).setPlane(3)),
            new CoursePart(Obstacle.HURDLE_GAP, new Area(2646, 3655, 2638, 3649).setPlane(3)),
            new CoursePart(Obstacle.TIGHTROPE, new Area(2652, 3665, 2642, 3656).setPlane(3)),
            new CoursePart(Obstacle.PILE_OF_FISH, new Area(2669, 3687, 2652, 3665).setPlane(3))
    );

    public CoursePart[] COURSE_PARTS;
    String NAME;

    AgilityCourse(final String NAME, final CoursePart... COURSE_PARTS) {
        this.NAME = NAME;
        this.COURSE_PARTS = COURSE_PARTS;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
