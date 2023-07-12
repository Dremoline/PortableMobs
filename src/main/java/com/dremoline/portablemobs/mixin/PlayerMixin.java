package com.dremoline.portablemobs.mixin;

import com.dremoline.portablemobs.PortableMobItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(
            method = "interactOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"
            ),
            cancellable = true
    )
    private void interactOn(Entity entity, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> ci) {
        //noinspection DataFlowIssue
        Player player = (Player) (Object) this;
        ItemStack stack = player.getItemInHand(interactionHand);
        if (player.isShiftKeyDown() && stack.getItem() instanceof PortableMobItem && entity instanceof LivingEntity) {
            ci.setReturnValue(stack.getItem().interactLivingEntity(stack, player, (LivingEntity) entity, interactionHand));
        }
    }
}
