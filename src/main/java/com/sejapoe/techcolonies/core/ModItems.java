package com.sejapoe.techcolonies.core;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import com.sejapoe.techcolonies.items.PlatedBlockItem;
import com.sejapoe.techcolonies.items.StrangeWandItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
  public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, TechColonies.MOD_ID);

  public static final RegistryObject<Item>
          STRANGE_WAND = ITEM_REGISTER.register("strange_wand", () -> new StrangeWandItem(new Item.Properties().tab(ModCreativeModeTab.TECH_COLONIES)));
//  EXAMPLE_ITEM = ITEM_REGISTER.register("registry_name", () -> new ExampleItem(...))

  public static void register(IEventBus bus) {
    TechColonies.LOGGER.debug("HELLO FROM ITEM REGISTER");

    for (RegistryObject<Block> registryObject : ModBlocks.BLOCK_REGISTER.getEntries()) {
      ITEM_REGISTER.register(registryObject.getId().getPath(), () -> new BlockItem(registryObject.get(), new Item.Properties().tab(ModCreativeModeTab.TECH_COLONIES)));
    }
    ITEM_REGISTER.register(bus);
  }
}
