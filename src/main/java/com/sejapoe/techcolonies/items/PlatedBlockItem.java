package com.sejapoe.techcolonies.items;

import com.sejapoe.techcolonies.core.properties.ModProperties;
import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PlatedBlockItem extends BlockItem {

  private PlatingMaterial material = PlatingMaterial.NONE;
  public PlatedBlockItem(Block block, PlatingMaterial material, Properties properties) {
    super(block, properties);
    material = this.material;
  }



  @Override
  public Component getName(ItemStack stack) {
    return new TranslatableComponent(super.getRegistryName().getPath());
  }

  //  @Nullable
//  @Override
//  protected BlockState getPlacementState(BlockPlaceContext context) {
//    BlockState state = super.getPlacementState(context);
//    if (state == null) {
//      state =  this.getBlock().getStateDefinition().any();
//    }
//    return state.setValue(ModProperties.PLATING_MATERIAL, material);
//  }
}
