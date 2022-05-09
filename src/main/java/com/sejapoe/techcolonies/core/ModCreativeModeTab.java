package com.sejapoe.techcolonies.core;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ModCreativeModeTab {
  public static final CreativeModeTab TECH_COLONIES = (new CreativeModeTab("techcolonies") {
    @Override
    @MethodsReturnNonnullByDefault
    public @NotNull ItemStack makeIcon() {
      return new ItemStack(ModItems.STRANGE_WAND.get());
    }
  });
}
