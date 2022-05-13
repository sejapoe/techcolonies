package com.sejapoe.techcolonies.registry;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.items.IItemHandler;

public final class ModCapabilities {
  public static final Capability<IDwarfItemHandler> DWARF_ITEM_HANDLER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

  public static void register(RegisterCapabilitiesEvent event) {
    event.register(IDwarfItemHandler.class);
  }

  protected interface IDwarfItemHandler extends IItemHandler {}
}
