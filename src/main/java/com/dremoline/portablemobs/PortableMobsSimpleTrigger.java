package com.dremoline.portablemobs;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

public class PortableMobsSimpleTrigger extends AbstractCriterionTrigger<PortableMobsSimpleTrigger.TriggerInstance> {
    private final ResourceLocation identifier;

    public PortableMobsSimpleTrigger(ResourceLocation identifier) {
        this.identifier = identifier;
    }

    @Override
    protected TriggerInstance createInstance(JsonObject json, EntityPredicate.AndPredicate player, ConditionArrayParser deserializationContext) {
        return new TriggerInstance(this.identifier, player);
    }

    @Override
    public ResourceLocation getId() {
        return this.identifier;
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, instance -> true);
    }

    public static class TriggerInstance extends CriterionInstance {

        public TriggerInstance(ResourceLocation criterionIdentifier, EntityPredicate.AndPredicate player) {
            super(criterionIdentifier, player);
        }
    }
}
