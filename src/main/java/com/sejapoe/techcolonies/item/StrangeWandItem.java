package com.sejapoe.techcolonies.item;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.block.entity.AbstractInterfaceBlockEntity;
import com.sejapoe.techcolonies.block.entity.AbstractStructureControllerBlockEntity;
import com.sejapoe.techcolonies.block.entity.FluidInterfaceBlockEntity;
import com.sejapoe.techcolonies.block.entity.ItemInterfaceBlockEntity;
import com.sejapoe.techcolonies.core.properties.ModProperties;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
import org.jetbrains.annotations.NotNull;

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
          useOnContext.getPlayer().sendMessage(new TranslatableComponent("dwarf.job.set_controller"), Util.NIL_UUID);
          return InteractionResult.SUCCESS;
        }
      }
      if (blockEntity instanceof AbstractInterfaceBlockEntity) {
        if (useOnContext.getPlayer().isCrouching()) {
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
        useOnContext.getPlayer().sendMessage(new TranslatableComponent("dwarf.job.set_input"), Util.NIL_UUID);
        return InteractionResult.SUCCESS;
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
