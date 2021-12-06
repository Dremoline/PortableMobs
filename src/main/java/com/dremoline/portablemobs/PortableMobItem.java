package com.dremoline.portablemobs;

import com.supermartijn642.core.TextComponents;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class PortableMobItem extends Item {
    public final PortableMobTypes type;

    public PortableMobItem(PortableMobTypes type) {
        super(new Item.Properties().stacksTo(1).tab(PortableMobs.GROUP).setISTER(() -> PortableMobItemStackRenderer::getInstance));
        this.type = type;
        this.setRegistryName(type.toSuffix() + "_capture_cell");
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        CompoundNBT compound = context.getItemInHand().getOrCreateTag();
        if (compound.getBoolean("has_entity")) {
            Optional<EntityType<?>> optional = EntityType.byString(compound.getString("entity_type"));
            if (optional.isPresent()) {
                Entity living = optional.get().create(context.getLevel());
                if (living != null) {
                    living.load(compound.getCompound("entity_data"));
                    BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
                    living.setPos(pos.getX() + 0.5, pos.getY() + 0.01, pos.getZ() + 0.5);
                    context.getLevel().addFreshEntity(living);
                    if (this.type.reusable) {
                        compound.putBoolean("has_entity", false);
                    } else {
                        context.getPlayer().setItemInHand(context.getHand(), ItemStack.EMPTY);
                    }
                }
            }
        }
        return super.useOn(context);
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity living, Hand hand) {
        CompoundNBT compound = stack.getOrCreateTag();
        if (!compound.getBoolean("has_entity")) {
            if (living instanceof WitherEntity || living instanceof EnderDragonEntity) {
                if (player.level.isClientSide)
                    player.sendMessage(TextComponents.translation("portablemobs.capture_failed").color(TextFormatting.RED).get(), player.getUUID());
            } else {
                if (living.isPassenger())
                    living.stopRiding();
                living.ejectPassengers();

                compound.putString("entity_type", living.getType().getRegistryName().toString());
                compound.put("entity_data", living.saveWithoutId(new CompoundNBT()));
                compound.putString("entity_name", ITextComponent.Serializer.toJson(TextComponents.entity(living).get()));
                compound.putBoolean("has_entity", true);
                living.remove();

                player.setItemInHand(hand, stack);
                if (player.level.isClientSide) {
                    player.sendMessage(TextComponents.translation("portablemobs.capture_success").color(TextFormatting.GREEN).get(), player.getUUID());
                }
                return ActionResultType.sidedSuccess(player.level.isClientSide);
            }
        }
        return super.interactLivingEntity(stack, player, living, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> components, ITooltipFlag flag) {
        CompoundNBT compound = stack.getOrCreateTag();
        if (compound.getBoolean("has_entity")) {
            ITextComponent entityName = TextComponents.fromTextComponent(ITextComponent.Serializer.fromJson(compound.getString("entity_name"))).color(TextFormatting.YELLOW).get();
            components.add(TextComponents.translation("portablemobs.tooltip_name", entityName).color(TextFormatting.WHITE).get());
        }
    }
}
