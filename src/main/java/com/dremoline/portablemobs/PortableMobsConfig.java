package com.dremoline.portablemobs;

import com.supermartijn642.configlib.ModConfigBuilder;

import java.util.function.Supplier;

/**
 * Created 3/25/2021 by SuperMartijn642
 */
public class PortableMobsConfig {

    static {
        ModConfigBuilder builder = new ModConfigBuilder("portablemobs");
        builder.build();
    }

    public static void init() {
        // just to cause this class to load
    }

}
