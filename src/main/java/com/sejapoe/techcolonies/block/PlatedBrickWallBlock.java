package com.sejapoe.techcolonies.block;

import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.WallSide;

public class PlatedBrickWallBlock extends WallBlock implements IPlatedBlock{
  private final PlatingMaterial platingMaterial;

  public PlatedBrickWallBlock(PlatingMaterial material, Properties properties) {
    super(properties);
    this.platingMaterial = material;
  }

  @Override
  public PlatingMaterial getPlatingMaterial() {
    return platingMaterial;
  }

  public static EnumProperty<WallSide> getPropertyFromDirection(Direction direction) {
    return switch (direction) {
      case EAST -> BlockStateProperties.EAST_WALL;
      case WEST -> BlockStateProperties.WEST_WALL;
      case NORTH -> BlockStateProperties.NORTH_WALL;
      case SOUTH -> BlockStateProperties.SOUTH_WALL;
      default -> null;
    };
  }
}
