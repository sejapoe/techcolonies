package com.sejapoe.techcolonies.datagen.client;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.FluidDeferredRegister;
import com.sejapoe.techcolonies.registry.ModBlocks;
import com.sejapoe.techcolonies.registry.ModFluids;
import com.sejapoe.techcolonies.registry.ModItems;
import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.loaders.DynamicBucketModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.Objects;

public class ModItemModelProvider extends ItemModelProvider {
  public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
    super(generator, TechColonies.MOD_ID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    // Block Group Items
    platedSimpleBlockItems(ModBlocks.PLATED_BRICKS_BLOCKS);
    platedWallBlockItems(ModBlocks.PLATED_BRICK_WALL_BLOCKS);

    // Block Items
    simpleBlockItem(ModBlocks.SMELTERY_BLOCK.get().asItem(), modLoc("block/none_smeltery"));
    simpleBlockItem(ModBlocks.ITEM_INTERFACE_BLOCK.get().asItem(), modLoc("block/none_item_interface_input"));
    simpleBlockItem(ModBlocks.FLUID_INTERFACE_BLOCK.get().asItem(), modLoc("block/none_fluid_interface_input"));
    simpleBlockItem(ModBlocks.PORTAL_CONTROLLER_BLOCK.get().asItem(), modLoc("block/none_portal_controller"));

    // Buckets
    bucketItem(ModFluids.MOLTEN_COPPER);
    bucketItem(ModFluids.MOLTEN_IRON);

    // Just Items
    oneLayerItem(ModItems.STRANGE_WAND.get());
    oneLayerItem(ModItems.TOOL_BELT.get());
  }

  protected void platedSimpleBlockItems(Map<PlatingMaterial, RegistryObject<Block>> blocks) {
    for (RegistryObject<Block> registryObject : blocks.values()) {
      simpleBlockItem(registryObject.get().asItem());
    }
  }
  protected void simpleBlockItem(Item item) {
    getBuilder(Objects.requireNonNull(item.getRegistryName()).toString()).parent(getExistingFile(modLoc("block/" + item.getRegistryName().getPath())));
  }

  protected void simpleBlockItem(Item item, ResourceLocation model) {
    getBuilder(Objects.requireNonNull(item.getRegistryName()).toString()).parent(getExistingFile(model));
  }

  protected void platedWallBlockItems(Map<PlatingMaterial, RegistryObject<Block>> blocks) {
    for (Map.Entry<PlatingMaterial, RegistryObject<Block>> entry : blocks.entrySet()) {
      wallBlockItem(entry.getValue().get().asItem(),
              modLoc("block/" + Objects.requireNonNull(ModBlocks.PLATED_BRICKS_BLOCKS.get(entry.getKey()).get().getRegistryName()).getPath()));
    }
  }
  protected void wallBlockItem(Item item ,ResourceLocation texture) {
    getBuilder(Objects.requireNonNull(item.getRegistryName()).toString()).parent(getExistingFile(mcLoc("block/wall_inventory"))).texture("wall", texture);
  }

  protected void oneLayerItem(Item item, ResourceLocation texture) {
    ResourceLocation itemTexture = new ResourceLocation(texture.getNamespace(), "item/" + texture.getPath());
    if (existingFileHelper.exists(itemTexture, PackType.CLIENT_RESOURCES, ".png", "textures")) {
      getBuilder(Objects.requireNonNull(item.getRegistryName()).getPath()).parent(getExistingFile(mcLoc("item/generated"))).texture("layer0", itemTexture);
    } else {
      TechColonies.LOGGER.error("Texture for " + Objects.requireNonNull(item.getRegistryName()) + " not present at " + itemTexture);
    }
  }

  protected void oneLayerItem(Item item) {
    oneLayerItem(item, Objects.requireNonNull(item.getRegistryName()));
  }

  protected void bucketItem(FluidDeferredRegister.FluidRegistryObject fluidRegistryObject) {
    withExistingParent(Objects.requireNonNull(fluidRegistryObject.getBucket().getRegistryName()).getPath(), new ResourceLocation("forge", "item/bucket_drip"))
            .customLoader(DynamicBucketModelBuilder::begin)
            .fluid(fluidRegistryObject.getStillFluid());
  }
}
