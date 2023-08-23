package com.dremoline.portablemobs;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.item.BaseItem;
import com.supermartijn642.core.item.ItemProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public class PortableMobItem extends BaseItem {

    public static final Tags.IOptionalNamedTag<EntityType<?>> BLACKLIST = EntityTypeTags.createOptional(new ResourceLocation("portablemobs", "capture_blacklist"));

    public final PortableMobTypes type;

    public PortableMobItem(PortableMobTypes type) {
        super(ItemProperties.create().maxStackSize(1).group(PortableMobs.GROUP));
        this.type = type;
    }

    @Override
    public InteractionFeedback interactWithBlock(ItemStack stack, PlayerEntity player, Hand hand, World level, BlockPos hitPos, Direction hitSide, Vector3d hitLocation) {
        CompoundNBT compound = stack.getOrCreateTag();
        if (compound.getBoolean("has_entity")) {
            Optional<EntityType<?>> optional = EntityType.byString(compound.getString("entity_type"));
            if (optional.isPresent()) {
                Entity living = optional.get().create(level);
                if (living != null) {
                    living.load(compound.getCompound("entity_data"));
                    BlockPos pos = hitPos.relative(hitSide);
                    living.setPos(pos.getX() + 0.5, pos.getY() + 0.01, pos.getZ() + 0.5);
                    level.addFreshEntity(living);
                    if (this.type.reusable) {
                        compound.putBoolean("has_entity", false);
                    } else {
                        player.setItemInHand(hand, ItemStack.EMPTY);
                    }
                }
            }
        }
        return super.interactWithBlock(stack, player, hand, level, hitPos, hitSide, hitLocation);
    }

    @Override
    public InteractionFeedback interactWithEntity(ItemStack stack, LivingEntity target, PlayerEntity player, Hand hand) {
        CompoundNBT compound = stack.getOrCreateTag();
        if (!compound.getBoolean("has_entity")) {
            if (target instanceof PlayerEntity) {
                if (player.level.isClientSide)
                    player.displayClientMessage(TextComponents.translation("portablemobs.capture_failed_player").color(TextFormatting.RED).get(), true);
                else
                    PortableMobs.playerCaptureTrigger.trigger((ServerPlayerEntity) player);
            } else if (target.getType().is(BLACKLIST)) {
                if (player.level.isClientSide)
                    player.displayClientMessage(TextComponents.translation("portablemobs.capture_failed").color(TextFormatting.RED).get(), true);
            } else {
                if (target.isPassenger())
                    target.stopRiding();
                target.ejectPassengers();

                compound.putString("entity_type", ForgeRegistries.ENTITIES.getKey(target.getType()).toString());
                compound.put("entity_data", target.saveWithoutId(new CompoundNBT()));
                compound.putString("entity_name", ITextComponent.Serializer.toJson(TextComponents.entity(target).get()));
                compound.putBoolean("has_entity", true);
                target.remove();

                player.setItemInHand(hand, stack);
                if (player.level.isClientSide) {
                    player.displayClientMessage(TextComponents.translation("portablemobs.capture_success").color(TextFormatting.GREEN).get(), true);
                } else
                    PortableMobs.captureTrigger.trigger((ServerPlayerEntity) player);
                return InteractionFeedback.SUCCESS;
            }
        }
        return super.interactWithEntity(stack, target, player, hand);
    }

    @Override
    protected void appendItemInformation(ItemStack stack, @Nullable IBlockReader level, Consumer<ITextComponent> info, boolean advanced) {
        CompoundNBT compound = stack.getOrCreateTag();
        if (compound.getBoolean("has_entity")) {
            ITextComponent entityName = TextComponents.fromTextComponent(ITextComponent.Serializer.fromJson(compound.getString("entity_name"))).color(TextFormatting.YELLOW).get();
            info.accept(TextComponents.translation("portablemobs.tooltip_name", entityName).color(TextFormatting.WHITE).get());
        }
    }
}
