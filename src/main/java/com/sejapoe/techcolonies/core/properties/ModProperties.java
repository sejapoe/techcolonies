package com.sejapoe.techcolonies.core.properties;

import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ModProperties {
  public static final EnumProperty<PlatingMaterial> PLATING_MATERIAL = EnumProperty.create("plating_material", PlatingMaterial.class);
  public static final EnumProperty<InterfaceDirection> INTERFACE_DIRECTION = EnumProperty.create("interface_direction", InterfaceDirection.class);
}
