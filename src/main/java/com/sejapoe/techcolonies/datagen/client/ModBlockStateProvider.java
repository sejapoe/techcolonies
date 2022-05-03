package com.sejapoe.techcolonies.datagen.client;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.ModBlocks;
import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class ModBlockStateProvider extends BlockStateProvider {
  public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
    super(gen, TechColonies.MOD_ID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    platedSimpleBlocks(ModBlocks.PLATED_BRICKS_BLOCKS);
    platedWallBlocks(ModBlocks.PLATED_BRICK_WALL_BLOCKS);
    horizontalFurnaceBlock(ModBlocks.SMELTERY_BLOCK.get(),
            modLoc("block/" + ModBlocks.SMELTERY_BLOCK.get().getRegistryName().getPath() + "_side"),
            modLoc("block/" + ModBlocks.SMELTERY_BLOCK.get().getRegistryName().getPath() + "_front"),
            modLoc("block/" + ModBlocks.SMELTERY_BLOCK.get().getRegistryName().getPath() + "_side"),
            modLoc("block/" + ModBlocks.SMELTERY_BLOCK.get().getRegistryName().getPath() + "_front_on"));
  }

  protected void platedSimpleBlocks(Map<PlatingMaterial, RegistryObject<Block>> blocks) {
    for (RegistryObject<Block> registryObject : blocks.values()) {
      simpleBlock(registryObject.get());
    }
  }

  protected void platedWallBlocks(Map<PlatingMaterial, RegistryObject<Block>> blocks) {
    for (Map.Entry<PlatingMaterial, RegistryObject<Block>> entry : blocks.entrySet()) {
      wallBlock((WallBlock) entry.getValue().get(),
              modLoc("block/" + ModBlocks.PLATED_BRICKS_BLOCKS.get(entry.getKey()).get().getRegistryName().getPath()));
    }
  }

  protected void horizontalFurnaceBlock(Block block, ResourceLocation side, ResourceLocation front, ResourceLocation top, ResourceLocation frontOn) {
    ModelFile model = models().orientable(block.getRegistryName().getPath(), side, front, top);
    ModelFile modelOn = models().orientable(block.getRegistryName().getPath() + "_lit", side, frontOn, top);
    horizontalBlock(block, state -> state.getValue(BlockStateProperties.LIT) ? modelOn : model);
  }
}
