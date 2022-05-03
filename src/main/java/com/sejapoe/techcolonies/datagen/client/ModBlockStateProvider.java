package com.sejapoe.techcolonies.datagen.client;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
  public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
    super(gen, TechColonies.MOD_ID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    simpleBlock(ModBlocks.COPPER_PLATED_BRICKS_BLOCK.get());
    wallBlock((WallBlock) ModBlocks.COPPER_PLATED_BRICK_WALL_BLOCK.get(),
            new ResourceLocation(TechColonies.MOD_ID, "block/" + ModBlocks.COPPER_PLATED_BRICKS_BLOCK.get().getRegistryName().getPath()));
    horizontalFurnaceBlock(ModBlocks.SMELTERY_BLOCK.get(),
            new ResourceLocation(TechColonies.MOD_ID, "block/" + ModBlocks.SMELTERY_BLOCK.get().getRegistryName().getPath() + "_side"),
            new ResourceLocation(TechColonies.MOD_ID, "block/" + ModBlocks.SMELTERY_BLOCK.get().getRegistryName().getPath() + "_front"),
            new ResourceLocation(TechColonies.MOD_ID, "block/" + ModBlocks.SMELTERY_BLOCK.get().getRegistryName().getPath() + "_side"),
            new ResourceLocation(TechColonies.MOD_ID, "block/" + ModBlocks.SMELTERY_BLOCK.get().getRegistryName().getPath() + "_front_on"));
  }

  protected void horizontalFurnaceBlock(Block block, ResourceLocation side, ResourceLocation front, ResourceLocation top, ResourceLocation frontOn) {
    ModelFile model = models().orientable(block.getRegistryName().getPath(), side, front, top);
    ModelFile modelOn = models().orientable(block.getRegistryName().getPath() + "_lit", side, frontOn, top);
    horizontalBlock(block, state -> state.getValue(BlockStateProperties.LIT) ? modelOn : model);
  }
}
