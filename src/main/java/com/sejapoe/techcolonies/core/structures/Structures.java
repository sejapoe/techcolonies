package com.sejapoe.techcolonies.core.structures;

import com.sejapoe.techcolonies.block.AbstractInterfaceBlock;
import com.sejapoe.techcolonies.core.ModBlockTags;
import com.sejapoe.techcolonies.core.ModBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockMaterialPredicate;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.material.Material;

public class Structures {
  public static final PlatedBlockPattern SMELTERY_PATTERN = new PlatedBlockPattern(BlockPatternBuilder.start()
          .aisle("AWA", "WBW", "BIB", "BBB")
          .aisle( "WWW", "BBB", "IAI", "BLB")
          .aisle("AWA",  "WBW", "BBB", "BSB")
          .where('B', blockInWorld -> blockInWorld.getState().is(ModBlockTags.PLATED_BRICKS))
          .where('I', blockInWorld -> blockInWorld.getState().is(ModBlockTags.PLATED_BRICKS) || blockInWorld.getState().getBlock() instanceof AbstractInterfaceBlock)
          .where('L', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.LAVA)))
          .where('S', BlockInWorld.hasState(BlockStatePredicate.forBlock(ModBlocks.SMELTERY_BLOCK.get())))
          .where('A', BlockInWorld.hasState(BlockMaterialPredicate.forMaterial(Material.AIR)))
          .where('W',blockInWorld -> blockInWorld.getState().is(ModBlockTags.PLATED_BRICK_WALLS))
          .build());
}
