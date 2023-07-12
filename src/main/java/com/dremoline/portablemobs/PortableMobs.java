package com.dremoline.portablemobs;

import com.dremoline.portablemobs.generators.*;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("portablemobs")
public class PortableMobs {

    public static final CreativeItemGroup GROUP = CreativeItemGroup.create("portablemobs", PortableMobTypes.BASIC::getItem);

    public static PlayerTrigger playerCaptureTrigger, captureTrigger;

    public PortableMobs() {
        RegistrationHandler handler = RegistrationHandler.get("portablemobs");
        for (PortableMobTypes type : PortableMobTypes.values()) {
            handler.registerItemCallback(type::registerItem);
        }
        handler.registerRecipeSerializer("upgrade_capture_cell", PortableMobUpgradeRecipe.SERIALIZER);

        GeneratorRegistrationHandler generatorHandler = GeneratorRegistrationHandler.get("portablemobs");
        generatorHandler.addGenerator(PortableMobsLanguageGenerator::new);
        generatorHandler.addGenerator(PortableMobsTagGenerator::new);
        generatorHandler.addGenerator(PortableMobsRecipeGenerator::new);
        generatorHandler.addGenerator(PortableMobsModelGenerator::new);
        generatorHandler.addGenerator(PortableMobsAdvancementGenerator::new);

        playerCaptureTrigger = CriteriaTriggers.register(new PlayerTrigger(new ResourceLocation("portablemobs", "playercapture")));
        captureTrigger = CriteriaTriggers.register(new PlayerTrigger(new ResourceLocation("portablemobs", "capture")));
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Events {
        @SubscribeEvent
        public static void onEntityInteract(PlayerInteractEvent.EntityInteract playerInteractEvent) {
            ItemStack stack = playerInteractEvent.getEntity().getItemInHand(playerInteractEvent.getHand());
            if (playerInteractEvent.getEntity().isShiftKeyDown() && stack.getItem() instanceof PortableMobItem && playerInteractEvent.getTarget() instanceof LivingEntity) {
                playerInteractEvent.setCancellationResult(stack.getItem().interactLivingEntity(playerInteractEvent.getItemStack(), playerInteractEvent.getEntity(), (LivingEntity) playerInteractEvent.getTarget(), playerInteractEvent.getHand()));
            }
        }
    }
}
