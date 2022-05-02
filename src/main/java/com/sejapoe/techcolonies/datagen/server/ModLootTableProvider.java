package com.sejapoe.techcolonies.datagen.server;

import com.sejapoe.techcolonies.core.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;

public class ModLootTableProvider extends BaseLootTableProvider{
  public ModLootTableProvider(DataGenerator dataGeneratorIn) {
    super(dataGeneratorIn);
  }

  @Override
  protected void addTables() {
    dropSelf(ModBlocks.COPPER_PLATED_BRICKS_BLOCK.get());
    dropSelf(ModBlocks.COPPER_PLATED_BRICK_WALL_BLOCK.get());
  }

  protected void dropSelf(Block block) {
    add(block, createSimpleTable(block.getRegistryName().getPath(), block));
  }
}
