package com.dremoline.portablemobs;

import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.TextComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class PortableMobItem extends Item {

    public static final TagKey<EntityType<?>> BLACKLIST = TagKey.create(ForgeRegistries.ENTITIES.getRegistryKey(), new ResourceLocation("portablemobs", "capture_blacklist"));

    public final PortableMobTypes type;

    public PortableMobItem(PortableMobTypes type){
        super(new Item.Properties().stacksTo(1).tab(PortableMobs.GROUP));
        this.type = type;
        this.setRegistryName(type.toSuffix() + "_capture_cell");
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer){
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer(){
                return new PortableMobItemStackRenderer(ClientUtils.getMinecraft().getBlockEntityRenderDispatcher());
            }
        });
    }

    @Override
    public InteractionResult useOn(UseOnContext context){
        CompoundTag compound = context.getItemInHand().getOrCreateTag();
        if(compound.getBoolean("has_entity")){
            Optional<EntityType<?>> optional = EntityType.byString(compound.getString("entity_type"));
            if(optional.isPresent()){
                Entity living = optional.get().create(context.getLevel());
                if(living != null){
                    living.load(compound.getCompound("entity_data"));
                    BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
                    living.setPos(pos.getX() + 0.5, pos.getY() + 0.01, pos.getZ() + 0.5);
                    context.getLevel().addFreshEntity(living);
                    if(this.type.reusable){
                        compound.putBoolean("has_entity", false);
                    }else{
                        context.getPlayer().setItemInHand(context.getHand(), ItemStack.EMPTY);
                    }
                }
            }
        }
        return super.useOn(context);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity living, InteractionHand hand){
        CompoundTag compound = stack.getOrCreateTag();
        if(!compound.getBoolean("has_entity")){
            if(living.getType().is(BLACKLIST)){
                if(player.level.isClientSide)
                    player.sendMessage(TextComponents.translation("portablemobs.capture_failed").color(ChatFormatting.RED).get(), player.getUUID());
            }else{
                if(living.isPassenger())
                    living.stopRiding();
                living.ejectPassengers();

                compound.putString("entity_type", living.getType().getRegistryName().toString());
                compound.put("entity_data", living.saveWithoutId(new CompoundTag()));
                compound.putString("entity_name", Component.Serializer.toJson(TextComponents.entity(living).get()));
                compound.putBoolean("has_entity", true);
                living.remove(Entity.RemovalReason.UNLOADED_TO_CHUNK);

                player.setItemInHand(hand, stack);
                if(player.level.isClientSide){
                    player.sendMessage(TextComponents.translation("portablemobs.capture_success").color(ChatFormatting.GREEN).get(), player.getUUID());
                }
                return InteractionResult.sidedSuccess(player.level.isClientSide);
            }
        }
        return super.interactLivingEntity(stack, player, living, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> components, TooltipFlag flag){
        CompoundTag compound = stack.getOrCreateTag();
        if(compound.getBoolean("has_entity")){
            Component entityName = TextComponents.fromTextComponent(Component.Serializer.fromJson(compound.getString("entity_name"))).color(ChatFormatting.YELLOW).get();
            components.add(TextComponents.translation("portablemobs.tooltip_name", entityName).color(ChatFormatting.WHITE).get());
        }
    }
}
