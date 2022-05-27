package com.sejapoe.techcolonies.item;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.block.entity.AbstractInterfaceBlockEntity;
import com.sejapoe.techcolonies.block.entity.AbstractStructureControllerBlockEntity;
import com.sejapoe.techcolonies.block.entity.FluidInterfaceBlockEntity;
import com.sejapoe.techcolonies.block.entity.ItemInterfaceBlockEntity;
import com.sejapoe.techcolonies.core.properties.ModProperties;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import com.sejapoe.techcolonies.entity.ai.job.miner.VirtualMine;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class StrangeWandItem extends Item {
  private DwarfEntity configurableDwarf;
  public StrangeWandItem(Properties properties) {
    super(properties);
  }

  @Override
  public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
    Level level = useOnContext.getLevel();
    BlockPos pos = useOnContext.getClickedPos();
    BlockState state = level.getBlockState(pos);
    BlockEntity blockEntity = level.getBlockEntity(pos);
    if (blockEntity != null) {
      if (level.isClientSide()) return InteractionResult.SUCCESS;
      if (blockEntity instanceof AbstractStructureControllerBlockEntity) {
        if (this.configurableDwarf != null) {
          this.configurableDwarf.setControllerPos(pos);
          Objects.requireNonNull(useOnContext.getPlayer()).sendMessage(new TranslatableComponent("dwarf.job.set_controller"), Util.NIL_UUID);
          return InteractionResult.SUCCESS;
        }
      }
      if (blockEntity instanceof AbstractInterfaceBlockEntity) {
        if (Objects.requireNonNull(useOnContext.getPlayer()).isCrouching()) {
          if (blockEntity instanceof ItemInterfaceBlockEntity) {
            TechColonies.LOGGER.debug(((ItemInterfaceBlockEntity) blockEntity).getItems().toString());
          }
          if (blockEntity instanceof FluidInterfaceBlockEntity) {
            TechColonies.LOGGER.debug(blockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null).getFluidInTank(0).getAmount() + "");
          }
          return InteractionResult.SUCCESS;
        }
        state = state.setValue(ModProperties.INTERFACE_DIRECTION, state.getValue(ModProperties.INTERFACE_DIRECTION).getOpposite());
        level.setBlockAndUpdate(pos, state);
        ((AbstractInterfaceBlockEntity) blockEntity).setInterfaceDirection(state.getValue(ModProperties.INTERFACE_DIRECTION));
        blockEntity.setChanged();
        return InteractionResult.SUCCESS;
      }
      if (blockEntity instanceof Container && this.configurableDwarf != null) {
        this.configurableDwarf.setInputContainerPos(pos);
        Objects.requireNonNull(useOnContext.getPlayer()).sendMessage(new TranslatableComponent("dwarf.job.set_input"), Util.NIL_UUID);
        return InteractionResult.SUCCESS;
      }
    } else {
      if (!level.isClientSide) {
        VirtualMine virtualMine = new VirtualMine((ServerLevel) level, 1, 30, 50);
        virtualMine.addListener(virtualMine1 -> {
          ItemStack tool = Objects.requireNonNull(useOnContext.getPlayer()).getItemInHand(InteractionHand.OFF_HAND);
          long ticks = virtualMine1.getMiningTime(tool);
          TechColonies.LOGGER.debug("Need " + ticks + " ticks to break this all");
          List<ItemStack> itemStackList = virtualMine1.calculateResult((ServerLevel) level, tool);
          IItemHandler capability = Objects.requireNonNull(useOnContext.getPlayer()).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(null);
          for (ItemStack itemStack : itemStackList) {
            ItemHandlerHelper.insertItemStacked(capability, itemStack, false);
          }
        });
      }
    }
    return InteractionResult.PASS;
  }

  @Override
  public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player player, @NotNull LivingEntity livingEntity, @NotNull InteractionHand interactionHand) {
    if (!player.level.isClientSide() && livingEntity instanceof DwarfEntity) {
      if (player.isCrouching()) {
        ((DwarfEntity) livingEntity).setControllerPos(null);
        ((DwarfEntity) livingEntity).setInputContainerPos(null);
        ((DwarfEntity) livingEntity).setHasToolBelt(false);
        player.sendMessage(new TranslatableComponent("dwarf.job.reset"), Util.NIL_UUID);
      } else {
        setConfigurableDwarf((DwarfEntity) livingEntity);
        player.sendMessage(new TextComponent("dwarf.job.select"), Util.NIL_UUID);
      }
      return InteractionResult.SUCCESS;
    }
    return super.interactLivingEntity(stack, player, livingEntity, interactionHand);
  }


  private void setConfigurableDwarf(DwarfEntity configurableDwarf) {
    this.configurableDwarf = configurableDwarf;
  }
}
