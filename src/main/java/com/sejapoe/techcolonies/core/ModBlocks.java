package com.sejapoe.techcolonies.core;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.blocks.CooperPlatedBrickWallBlock;
import com.sejapoe.techcolonies.blocks.CooperPlatedBricksBlock;
import net.minecraft.world.level.block.Block;
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
        COOPER_PLATED_BRICKS_BLOCK = BLOCK_REGISTER.register("cooper_plated_bricks", () -> new CooperPlatedBricksBlock(BlockBehaviour.Properties.of(Material.STONE))),
        COOPER_PLATED_BRICK_WALL_BLOCK = BLOCK_REGISTER.register("cooper_plated_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(COOPER_PLATED_BRICKS_BLOCK.get())));

  public static void register(IEventBus bus) {
    BLOCK_REGISTER.register(bus);
  }
}
