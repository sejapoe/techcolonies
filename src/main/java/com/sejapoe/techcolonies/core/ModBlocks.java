package com.sejapoe.techcolonies.core;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.blocks.CopperPlatedBricksBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
  public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, TechColonies.MOD_ID);

  public static final RegistryObject<Block>
        COPPER_PLATED_BRICKS_BLOCK = BLOCK_REGISTER.register("copper_plated_bricks", () -> new CopperPlatedBricksBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS))),
        COPPER_PLATED_BRICK_WALL_BLOCK = BLOCK_REGISTER.register("copper_plated_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(COPPER_PLATED_BRICKS_BLOCK.get())));

  public static void register(IEventBus bus) {
    BLOCK_REGISTER.register(bus);
  }
}
