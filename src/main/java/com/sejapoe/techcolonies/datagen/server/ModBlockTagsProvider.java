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
import org.jetbrains.annotations.Nullable;

public class ModBlockTagsProvider extends BlockTagsProvider {
  public ModBlockTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
    super(generator, TechColonies.MOD_ID, existingFileHelper);
  }

  @Override
  protected void addTags() {
    tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(ModBlocks.COPPER_PLATED_BRICKS_BLOCK.get())
            .add(ModBlocks.COPPER_PLATED_BRICK_WALL_BLOCK.get());
    tag(BlockTags.WALLS).add(ModBlocks.COPPER_PLATED_BRICK_WALL_BLOCK.get());
  }
}
