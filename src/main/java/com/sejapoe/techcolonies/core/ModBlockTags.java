package com.sejapoe.techcolonies.core;

import com.sejapoe.techcolonies.TechColonies;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
  public static TagKey<Block> PLATED_BRICKS = BlockTags.create(new ResourceLocation(TechColonies.MOD_ID, "plated_bricks"));
  public static TagKey<Block> PLATED_BRICK_WALLS = BlockTags.create(new ResourceLocation(TechColonies.MOD_ID, "plated_brick_walls"));

}
