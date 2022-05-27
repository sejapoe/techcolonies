package com.sejapoe.techcolonies.registry;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.block.*;
import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class ModBlocks {
  public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, TechColonies.MOD_ID);

  public static final Map<PlatingMaterial, RegistryObject<Block>>
          PLATED_BRICKS_BLOCKS = registerPlatedGroup("plated_bricks", material -> new PlatedBricksBlock(material, BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS))),
          PLATED_BRICK_WALL_BLOCKS = registerPlatedGroup("plated_brick_wall", material -> new PlatedBrickWallBlock(material, BlockBehaviour.Properties.copy(PLATED_BRICKS_BLOCKS.get(PlatingMaterial.COPPER).get())));
  public static final RegistryObject<Block>
        SMELTERY_BLOCK = BLOCK_REGISTER.register("smeltery", () -> new SmelteryBlock(BlockBehaviour.Properties.copy(PLATED_BRICKS_BLOCKS.get(PlatingMaterial.COPPER).get()))),
        ITEM_INTERFACE_BLOCK = BLOCK_REGISTER.register("item_interface", () -> new ItemInterfaceBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS))),
        FLUID_INTERFACE_BLOCK = BLOCK_REGISTER.register("fluid_interface", () -> new FluidInterfaceBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS))),
        PORTAL_BLOCK = BLOCK_REGISTER.register("portal_controller", () -> new PortalBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));

  public static void register(IEventBus bus) {
    TechColonies.LOGGER.debug("HELLO FROM BLOCK REGISTER");
    BLOCK_REGISTER.register(bus);
  }
  private static Map<PlatingMaterial, RegistryObject<Block>> registerPlatedGroup(String baseName, Function<PlatingMaterial, Block> blockFunc) {
    Map<PlatingMaterial, RegistryObject<Block>> map = new HashMap<>();
    for (PlatingMaterial material : PlatingMaterial.values()) {
      map.put(material, BLOCK_REGISTER.register(material.getSerializedName() + "_" + baseName, () -> blockFunc.apply(material)));
    }
    return map;
  }
}
