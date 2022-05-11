package com.sejapoe.techcolonies.datagen.server;

import com.sejapoe.techcolonies.registry.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class ModLootTableProvider extends BaseLootTableProvider{
  public ModLootTableProvider(DataGenerator dataGeneratorIn) {
    super(dataGeneratorIn);
  }

  @Override
  protected void addTables() {
    groupDropSelf(ModBlocks.PLATED_BRICKS_BLOCKS);
    groupDropSelf(ModBlocks.PLATED_BRICK_WALL_BLOCKS);
    dropSelf(ModBlocks.SMELTERY_BLOCK.get());
  }

  protected void groupDropSelf(Map<?, RegistryObject<Block>>... maps) {
    for (Map<?, RegistryObject<Block>> map : maps) {
      for (RegistryObject<Block> registryObject : map.values()) {
        dropSelf(registryObject.get());
      }
    }
  }

  protected void dropSelf(Block block) {
    add(block, createSimpleTable(block.getRegistryName().getPath(), block));
  }
}
