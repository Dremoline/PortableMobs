package com.dremoline.portablemobs.generators;

import com.supermartijn642.core.generator.ModelGenerator;
import com.supermartijn642.core.generator.ResourceCache;

public class PortableMobsModelGenerator extends ModelGenerator {
    public PortableMobsModelGenerator (ResourceCache cache) {
        super("portablemobs", cache);
    }

    @Override
    public void generate() {
        this.model("item/basic_capture_cell")
                .parent("item/capture_cell_template")
                .texture("sides","item/basic_capture_cell")
                .texture("top","minecraft", "block/dark_oak_log_top");

        this.model("item/master_capture_cell")
                .parent("item/capture_cell_template")
                .texture("sides","item/master_capture_cell")
                .texture("top","minecraft", "block/obsidian");
    }
}
