package com.spellbladenext.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "server")
public class ServerConfig  implements ConfigData {
    public ServerConfig(){}
    @Comment("Upper bound of Ashes of the Stars (Default: 50)")
    public  int ashes_upper_bound = 50;
    @Comment("Lower bound of Ashes of the Stars (Default: 10)")
    public  int ashes_lower_bound = 10;
    @Comment("Upper bound of Crystallised Omniscience (Default: 30)")
    public  int omni_upper = 30;
    @Comment("Lower bound of Crystallised Omniscience (Default: 5)")
    public  int omni_lower = 5;
    @Comment("Coefficient for Voidforge gained damage (Default: 1x, results in 100% gained as)")
    public  int voidforge = 1;

    @Comment("Distance from 0,0 below which Hexblades will not invade in the Glass Ocean (Default: 0)")
    public  int hexblade_grace = 0;
    @Comment("Damage multiplier for spin attacks (eg: Whirlwind) (Default: 1.0)")
    public  float spin_attack_coeff = 1.0F;
    @Comment("Hexblade module (Default: TRUE)")
    public  boolean hexblade_on = true;
    @Comment("Magus changes the weather on phase end (Default: TRUE)")
    public  boolean magusWeather = true;
    @Comment("Reactive ward regen rate coefficient (Default: 1.0)")
    public  float wardrate = 1.0F;
}
