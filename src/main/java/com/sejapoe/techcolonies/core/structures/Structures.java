package com.sejapoe.techcolonies.core.structures;

import com.sejapoe.techcolonies.block.AbstractInterfaceBlock;
import com.sejapoe.techcolonies.block.PortalBlock;
import com.sejapoe.techcolonies.registry.ModBlockTags;
import com.sejapoe.techcolonies.registry.ModBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockMaterialPredicate;
import net.minecraft.world.level.block.state.predicate.BlockPredicate;
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

  public static final PlatedBlockPattern PORTAL_PATTERN = new PlatedBlockPattern(BlockPatternBuilder.start()
          .aisle("AABAA", "AAWAA", "AAWAA", "AAWAA", "**B**")
          .aisle("AAWAA", "AAPAA", "AAPAA", "AAPAA", "**B**")
          .aisle("BWWWB", "WPPPW", "WPPPW", "WPPPW", "BBCBB")
          .aisle("AAWAA", "AAPAA", "AAPAA", "AAPAA", "**B**")
          .aisle("AABAA", "AAWAA", "AAWAA", "AAWAA", "**B**")
          .where('B', blockInWorld -> blockInWorld.getState().is(ModBlockTags.PLATED_BRICKS))
          .where('W', blockInWorld -> blockInWorld.getState().is(ModBlockTags.PLATED_BRICK_WALLS))
          .where('*', blockInWorld -> true)
          .where('P', BlockInWorld.hasState(BlockMaterialPredicate.forMaterial(Material.AIR)).or(BlockInWorld.hasState(BlockStatePredicate.forBlock(ModBlocks.PORTAL_BLOCK.get()))))
          .where('A', BlockInWorld.hasState(BlockMaterialPredicate.forMaterial(Material.AIR)))
          .where('C', BlockInWorld.hasState(BlockStatePredicate.forBlock(ModBlocks.PORTAL_CONTROLLER_BLOCK.get())))
          .build());
}
