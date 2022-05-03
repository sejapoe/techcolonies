package com.sejapoe.techcolonies.datagen.server;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ModBlockTagsProvider extends BlockTagsProvider {
  public ModBlockTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
    super(generator, TechColonies.MOD_ID, existingFileHelper);
  }

  @Override
  protected void addTags() {
    addGroupToTag(BlockTags.MINEABLE_WITH_PICKAXE,
            ModBlocks.PLATED_BRICKS_BLOCKS,
            ModBlocks.PLATED_BRICK_WALL_BLOCKS);
    addGroupToTag(BlockTags.WALLS,
            ModBlocks.PLATED_BRICK_WALL_BLOCKS);
  }

  protected void addGroupToTag(TagKey<Block> key, Map<?, RegistryObject<Block>>... maps) {
    for (Map<?, RegistryObject<Block>> map : maps) {
      for (RegistryObject<Block> v : map.values()) {
        tag(key).add(v.get());
      }
    }
  }
}
