package com.dremoline.portablemobs;

import com.supermartijn642.configlib.ModConfigBuilder;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Created 3/25/2021 by SuperMartijn642
 */
public class PortableMobsConfig {

    public static final Supplier<Boolean> captureBosses;

    static {
        ModConfigBuilder builder = new ModConfigBuilder("portablemobs");
        builder.push("General");
        captureBosses = builder.comment("Is the player able to capture bosses?").define("captureBosses",false);
        builder.build();
    }

    public static void init() {
        // just to cause this class to load
    }

}
