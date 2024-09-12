package com.dremoline.portablemobs;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.item.BaseItem;
import com.supermartijn642.core.item.ItemProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Consumer;

public class PortableMobItem extends BaseItem {

    public static final TagKey<EntityType<?>> BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("portablemobs", "capture_blacklist"));

    public static final DataComponentType<CompoundTag> CAPTURED_ENTITY = DataComponentType.<CompoundTag>builder().persistent(CompoundTag.CODEC).networkSynchronized(ByteBufCodecs.COMPOUND_TAG).build();

    public final PortableMobTypes type;

    public PortableMobItem(PortableMobTypes type) {
        super(ItemProperties.create().maxStackSize(1).group(PortableMobs.GROUP));
        this.type = type;
    }

    @Override
    public InteractionFeedback interactWithBlock(ItemStack stack, Player player, InteractionHand hand, Level level, BlockPos hitPos, Direction hitSide, Vec3 hitLocation) {
        CompoundTag compound = stack.get(CAPTURED_ENTITY);
        if (compound != null && compound.getBoolean("has_entity")) {
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
    public InteractionFeedback interactWithEntity(ItemStack stack, LivingEntity target, Player player, InteractionHand hand) {
        CompoundTag compound = stack.get(CAPTURED_ENTITY);
        if (compound == null || !compound.getBoolean("has_entity")) {
            if (target instanceof Player) {
                if (player.level().isClientSide)
                    player.sendSystemMessage(TextComponents.translation("portablemobs.capture_failed_player").color(ChatFormatting.RED).get());
                else
                    PortableMobs.playerCaptureTrigger.trigger((ServerPlayer) player);
            } else if (target.getType().is(BLACKLIST)) {
                if (player.level().isClientSide)
                    player.sendSystemMessage(TextComponents.translation("portablemobs.capture_failed").color(ChatFormatting.RED).get());
            } else {
                if (target.isPassenger())
                    target.stopRiding();
                target.ejectPassengers();

                compound = new CompoundTag();
                compound.putString("entity_type", BuiltInRegistries.ENTITY_TYPE.getKey(target.getType()).toString());
                compound.put("entity_data", target.saveWithoutId(new CompoundTag()));
                compound.putString("entity_name", Component.Serializer.toJson(TextComponents.entity(target).get(), target.level().registryAccess()));
                compound.putBoolean("has_entity", true);
                stack.set(CAPTURED_ENTITY, compound);
                target.remove(Entity.RemovalReason.UNLOADED_TO_CHUNK);

                player.setItemInHand(hand, stack);
                if (player.level().isClientSide) {
                    player.sendSystemMessage(TextComponents.translation("portablemobs.capture_success").color(ChatFormatting.GREEN).get());
                } else
                    PortableMobs.captureTrigger.trigger((ServerPlayer) player);
                return InteractionFeedback.SUCCESS;
            }
        }
        return super.interactWithEntity(stack, target, player, hand);
    }

    @Override
    protected void appendItemInformation(ItemStack stack, Consumer<Component> info, boolean advanced) {
        CompoundTag compound = stack.get(CAPTURED_ENTITY);
        if (compound != null && compound.getBoolean("has_entity")) {
            Component entityName = TextComponents.fromTextComponent(Component.Serializer.fromJson(compound.getString("entity_name"), CommonUtils.getRegistryAccess())).color(ChatFormatting.YELLOW).get();
            info.accept(TextComponents.translation("portablemobs.tooltip_name", entityName).color(ChatFormatting.WHITE).get());
        }
    }
}
