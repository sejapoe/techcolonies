package com.sejapoe.techcolonies.item;

import com.sejapoe.techcolonies.entity.DwarfEntity;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ToolBeltItem extends Item {
  public ToolBeltItem(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack pStack, @NotNull Player pPlayer, @NotNull LivingEntity pInteractionTarget, @NotNull InteractionHand pUsedHand) {
    if (pInteractionTarget instanceof DwarfEntity) {
      if (((DwarfEntity) pInteractionTarget).getControllerPos() != null) {
        pPlayer.sendMessage(new TranslatableComponent("dwarf.job.need_reset_controller"), Util.NIL_UUID);
        return InteractionResult.PASS;
      }
      if (((DwarfEntity) pInteractionTarget).hasToolBelt()) {
        pPlayer.sendMessage(new TranslatableComponent("dwarf.job.already_has_belt"), Util.NIL_UUID);
        return InteractionResult.PASS;
      }
      if (!pPlayer.level.isClientSide()) {
        ((DwarfEntity) pInteractionTarget).setHasToolBelt(true);
        pPlayer.setItemInHand(pUsedHand, ItemStack.EMPTY);
      }
      return InteractionResult.CONSUME;
    }
    return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
  }
}
