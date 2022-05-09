package com.sejapoe.techcolonies.core;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.item.StrangeWandItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public final class ModItems {
  public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, TechColonies.MOD_ID);

  public static final RegistryObject<Item>
          STRANGE_WAND = ITEM_REGISTER.register("strange_wand", () -> new StrangeWandItem(getBaseProperties()));


  public static void register(IEventBus bus) {
    TechColonies.LOGGER.debug("HELLO FROM ITEM REGISTER");

    for (RegistryObject<Block> registryObject : ModBlocks.BLOCK_REGISTER.getEntries()) {
      ITEM_REGISTER.register(registryObject.getId().getPath(), () -> new BlockItem(registryObject.get(), getBaseProperties()));
    }
    ITEM_REGISTER.register(bus);
  }
  @NotNull
  public static Item.Properties getBaseProperties() {
    return new Item.Properties().tab(ModCreativeModeTab.TECH_COLONIES);
  }
}
