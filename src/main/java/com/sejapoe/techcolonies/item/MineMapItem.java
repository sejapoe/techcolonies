package com.sejapoe.techcolonies.item;

import com.sejapoe.techcolonies.entity.ai.job.miner.Mine;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MineMapItem extends Item {
  public MineMapItem(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public @NotNull InteractionResult useOn(@NotNull UseOnContext pContext) {
    ItemStack itemInHand = pContext.getItemInHand();
    Player player = Objects.requireNonNull(pContext.getPlayer());
    Mine mine = Mine.loadFromNBT(itemInHand.getOrCreateTag());
    if (mine != null) {
      if (!pContext.getLevel().isClientSide) {
        player.sendMessage(new TranslatableComponent("map.techcolonies.need_reset"), Util.NIL_UUID);
      }
      return InteractionResult.PASS;
    } else {
      if (!pContext.getLevel().isClientSide) {
        BlockPos pos = pContext.getClickedPos();
        Direction facing = player.getDirection();
        itemInHand.getOrCreateTag().put("Mine", Mine.saveToNBT(new Mine(pos, facing)));
        player.sendMessage(new TranslatableComponent("map.techcolonies.created"), Util.NIL_UUID);
      }
      return InteractionResult.SUCCESS;
    }
  }

  @Override
  public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
    Mine mine = Mine.loadFromNBT(pPlayer.getItemInHand(pUsedHand).getOrCreateTag());
    if (mine == null) {
      if (!pLevel.isClientSide) {
        pPlayer.sendMessage(new TranslatableComponent("map.techcolonies.not_found"), Util.NIL_UUID);
      }
      return super.use(pLevel, pPlayer, pUsedHand);
    }
    // Open GUI;
    return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
  }
}
