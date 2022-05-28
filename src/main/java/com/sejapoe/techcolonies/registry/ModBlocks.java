package com.sejapoe.techcolonies.registry;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.block.*;
import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import net.minecraft.world.item.BlockItem;
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
import java.util.function.Supplier;

public final class ModBlocks {
  public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, TechColonies.MOD_ID);

  public static final Map<PlatingMaterial, RegistryObject<Block>>
          PLATED_BRICKS_BLOCKS = registerPlatedGroup("plated_bricks", material -> new PlatedBricksBlock(material, BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS))),
          PLATED_BRICK_WALL_BLOCKS = registerPlatedGroup("plated_brick_wall", material -> new PlatedBrickWallBlock(material, BlockBehaviour.Properties.copy(PLATED_BRICKS_BLOCKS.get(PlatingMaterial.COPPER).get())));
  public static final RegistryObject<Block>
        SMELTERY_BLOCK = registerWithItem("smeltery", () -> new SmelteryBlock(BlockBehaviour.Properties.copy(PLATED_BRICKS_BLOCKS.get(PlatingMaterial.COPPER).get()))),
        ITEM_INTERFACE_BLOCK = registerWithItem("item_interface", () -> new ItemInterfaceBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS))),
        FLUID_INTERFACE_BLOCK = registerWithItem("fluid_interface", () -> new FluidInterfaceBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS))),
        PORTAL_CONTROLLER_BLOCK = registerWithItem("portal_controller", () -> new PortalControllerBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS))),
        PORTAL_BLOCK = register("portal", () -> new PortalBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_PORTAL)));

  public static void register(IEventBus bus) {
    TechColonies.LOGGER.debug("HELLO FROM BLOCK REGISTER");
    BLOCK_REGISTER.register(bus);
  }
  private static Map<PlatingMaterial, RegistryObject<Block>> registerPlatedGroup(String baseName, Function<PlatingMaterial, Block> blockFunc) {
    Map<PlatingMaterial, RegistryObject<Block>> map = new HashMap<>();
    for (PlatingMaterial material : PlatingMaterial.values()) {
      map.put(material, registerWithItem(material.getSerializedName() + "_" + baseName, () -> blockFunc.apply(material)));
    }
    return map;
  }

  private static RegistryObject<Block> registerWithItem(String name, Supplier<Block> supplier) {
    RegistryObject<Block> blockRegistryObject = register(name, supplier);
    ModItems.ITEM_REGISTER.register(blockRegistryObject.getId().getPath(), () -> new BlockItem(blockRegistryObject.get(), ModItems.getBaseProperties()));
    return blockRegistryObject;
  }

  private static RegistryObject<Block> register(String name, Supplier<Block> supplier) {
    return BLOCK_REGISTER.register(name, supplier);
  }
}
