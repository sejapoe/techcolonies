package com.sejapoe.techcolonies.datagen.client;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.ModBlocks;
import com.sejapoe.techcolonies.core.properties.InterfaceDirection;
import com.sejapoe.techcolonies.core.properties.ModProperties;
import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
  public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
    super(gen, TechColonies.MOD_ID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    platedSimpleBlocks(ModBlocks.PLATED_BRICKS_BLOCKS);
    platedWallBlocks(ModBlocks.PLATED_BRICK_WALL_BLOCKS);
    horizontalMechanismPlatedBlock(ModBlocks.SMELTERY_BLOCK.get(),
            modLoc("block/" + ModBlocks.SMELTERY_BLOCK.get().getRegistryName().getPath() + "_front"),
            modLoc("block/" + ModBlocks.SMELTERY_BLOCK.get().getRegistryName().getPath() + "_front_on"));
    cubeAllInterfacePlatedBlock(ModBlocks.ITEM_INTERFACE_BLOCK.get(),
            modLoc("block/interface"),
            modLoc("block/" + ModBlocks.ITEM_INTERFACE_BLOCK.get().getRegistryName().getPath() + "_input"),
            modLoc("block/" + ModBlocks.ITEM_INTERFACE_BLOCK.get().getRegistryName().getPath() + "_output"));
    cubeAllInterfacePlatedBlock(ModBlocks.FLUID_INTERFACE_BLOCK.get(),
            modLoc("block/interface"),
            modLoc("block/" + ModBlocks.FLUID_INTERFACE_BLOCK.get().getRegistryName().getPath() + "_input"),
            modLoc("block/" + ModBlocks.FLUID_INTERFACE_BLOCK.get().getRegistryName().getPath() + "_output"));
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

  protected void cubeAllInterfacePlatedBlock(Block block, ResourceLocation baseOverlay, ResourceLocation inputOverlay, ResourceLocation outputOverlay) {
    Map<PlatingMaterial, Function<BlockState, ModelBuilder>> map = new HashMap<>();
    for (PlatingMaterial material : PlatingMaterial.values()) {
      Function<BlockState, ModelBuilder> modelFunc = state -> {
        ResourceLocation side = modLoc("block/" + ModBlocks.PLATED_BRICKS_BLOCKS.get(material).getId().getPath());
        ModelBuilder model = models().getBuilder(material.getSerializedName()+ "_" + block.getRegistryName().getPath() + "_" + state.getValue(ModProperties.INTERFACE_DIRECTION).getSerializedName())
                .parent(models().getExistingFile(mcLoc("block/block")))
                .texture("side", side)
                .texture("baseOverlay", baseOverlay)
                .texture("overlay", state.getValue(ModProperties.INTERFACE_DIRECTION) == InterfaceDirection.INPUT ? inputOverlay : outputOverlay)
                .texture("particle", "#side");
        model.element().textureAll("#side");
        model.element().textureAll("#baseOverlay");
        model.element().textureAll("#overlay");
        return model;
      };
      map.put(material, modelFunc);
    }
    getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(map.get(state.getValue(ModProperties.PLATING_MATERIAL)).apply(state))
            .build());
  }

  protected void horizontalMechanismPlatedBlock(Block block, ResourceLocation front, ResourceLocation frontOn) {
    Map<PlatingMaterial, Pair<ModelBuilder, ModelBuilder>> map = new HashMap<>();
    for (PlatingMaterial material : PlatingMaterial.values()) {
      ResourceLocation side = modLoc("block/" + ModBlocks.PLATED_BRICKS_BLOCKS.get(material).getId().getPath());
      ModelBuilder model = models().getBuilder(material.getSerializedName()+ "_" + block.getRegistryName().getPath())
              .parent(models().getExistingFile(mcLoc("block/block")))
              .texture("side", side)
              .texture("front", front)
              .texture("particle", "#side");
      model.element().textureAll("#side");
      model.element().face(Direction.NORTH).texture("#front").tintindex(0);

      ModelBuilder modelOn = models().getBuilder(material.getSerializedName()+ "_" + block.getRegistryName().getPath() + "_lit")
              .parent(models().getExistingFile(mcLoc("block/block")))
              .texture("side", side)
              .texture("front", frontOn)
              .texture("particle", "#side");
      modelOn.element().textureAll("#side");
      modelOn.element().face(Direction.NORTH).texture("#front").tintindex(0);

      map.put(material, Pair.of(model, modelOn));
    }
    horizontalBlock(block, state -> {
      PlatingMaterial material = state.getValue(ModProperties.PLATING_MATERIAL);
      Pair<ModelBuilder, ModelBuilder> models = map.get(material);
      return state.getValue(BlockStateProperties.LIT) ? models.getRight() : models.getLeft();
    });
  }
}
