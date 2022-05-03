package com.sejapoe.techcolonies.datagen.client;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.ModBlocks;
import com.sejapoe.techcolonies.core.ModItems;
import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

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
    simpleBlockItem(ModBlocks.SMELTERY_BLOCK.get().asItem());

    // Just Items
    oneLayerItem(ModItems.STRANGE_WAND.get());
  }

  protected void platedSimpleBlockItems(Map<PlatingMaterial, RegistryObject<Block>> blocks) {
    for (RegistryObject<Block> registryObject : blocks.values()) {
      simpleBlockItem(registryObject.get().asItem());
    }
  }
  protected void simpleBlockItem(Item item) {
    getBuilder(item.getRegistryName().toString()).parent(getExistingFile(modLoc("block/" + item.getRegistryName().getPath())));
  }

  protected void platedWallBlockItems(Map<PlatingMaterial, RegistryObject<Block>> blocks) {
    for (Map.Entry<PlatingMaterial, RegistryObject<Block>> entry : blocks.entrySet()) {
      wallBlockItem(entry.getValue().get().asItem(),
              new ResourceLocation(TechColonies.MOD_ID, "block/" + ModBlocks.PLATED_BRICKS_BLOCKS.get(entry.getKey()).get().getRegistryName().getPath()));
    }
  }
  protected void wallBlockItem(Item item ,ResourceLocation texture) {
    getBuilder(item.getRegistryName().toString()).parent(getExistingFile(mcLoc("block/wall_inventory"))).texture("wall", texture);
  }

  protected void oneLayerItem(Item item, ResourceLocation texture) {
    ResourceLocation itemTexture = new ResourceLocation(texture.getNamespace(), "item/" + texture.getPath());
    if (existingFileHelper.exists(itemTexture, PackType.CLIENT_RESOURCES, ".png", "textures")) {
      getBuilder(item.getRegistryName().getPath()).parent(getExistingFile(mcLoc("item/generated"))).texture("layer0", itemTexture);
    } else {
      TechColonies.LOGGER.error("Texture for " + item.getRegistryName().toString() + " not present at " + itemTexture.toString());
    }
  }

  protected void oneLayerItem(Item item) {
    oneLayerItem(item, item.getRegistryName());
  }

}
