package com.sejapoe.techcolonies.datagen.client;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
  public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
    super(gen, TechColonies.MOD_ID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    simpleBlock(ModBlocks.COPPER_PLATED_BRICKS_BLOCK.get());
  }
}
