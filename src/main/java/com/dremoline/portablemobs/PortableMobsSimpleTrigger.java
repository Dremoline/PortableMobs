package com.dremoline.portablemobs;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class PortableMobsSimpleTrigger extends SimpleCriterionTrigger<PortableMobsSimpleTrigger.TriggerInstance> {
    private final ResourceLocation identifier;

    public PortableMobsSimpleTrigger(ResourceLocation identifier) {
        this.identifier = identifier;
    }

    @Override
    protected TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext deserializationContext) {
        return new TriggerInstance(this.identifier, player);
    }

    @Override
    public ResourceLocation getId() {
        return this.identifier;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, instance -> true);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        public TriggerInstance(ResourceLocation criterionIdentifier, EntityPredicate.Composite player) {
            super(criterionIdentifier, player);
        }
    }
}
