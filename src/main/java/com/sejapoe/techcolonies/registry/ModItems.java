package com.sejapoe.techcolonies.registry;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.item.StrangeWandItem;
import com.sejapoe.techcolonies.item.ToolBeltItem;
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
          STRANGE_WAND = ITEM_REGISTER.register("strange_wand", () -> new StrangeWandItem(getBaseProperties())),
          TOOL_BELT = ITEM_REGISTER.register("tool_belt", () -> new ToolBeltItem(getBaseProperties().stacksTo(1)));

  public static void register(IEventBus bus) {
    TechColonies.LOGGER.debug("HELLO FROM ITEM REGISTER");
    ITEM_REGISTER.register(bus);
  }
  @NotNull
  public static Item.Properties getBaseProperties() {
    return new Item.Properties().tab(ModCreativeModeTab.TECH_COLONIES);
  }
}
