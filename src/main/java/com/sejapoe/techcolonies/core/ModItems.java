package com.sejapoe.techcolonies.core;

import com.sejapoe.techcolonies.TechColonies;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
  public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, TechColonies.MOD_ID);

//  public static final RegistryObject<Item>
//  EXAMPLE_ITEM = ITEM_REGISTER.register("registry_name", () -> new ExampleItem(...))

  public static void register(IEventBus bus) {
    for (RegistryObject<Block> registryObject : ModBlocks.BLOCK_REGISTER.getEntries()) {
      ITEM_REGISTER.register(registryObject.getId().getPath(), () -> {
        Block block = registryObject.get();
        return new BlockItem(block, new Item.Properties());
      });
    }
    ITEM_REGISTER.register(bus);
  }
}
