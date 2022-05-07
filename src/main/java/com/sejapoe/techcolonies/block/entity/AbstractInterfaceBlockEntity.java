package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.properties.InterfaceDirection;
import com.sejapoe.techcolonies.core.properties.ModProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AbstractInterfaceBlockEntity extends AbstractStructureElementBlockEntity {
  private InterfaceDirection interfaceDirection;
  public AbstractInterfaceBlockEntity(BlockEntityType type, BlockPos blockPos, BlockState state) {
    super(type, blockPos, state);
    this.interfaceDirection = state.getValue(ModProperties.INTERFACE_DIRECTION);
  }

  public BlockPos getAccessibilityPos(PathfinderMob mob) {
    return getBlockPos().below();
    //    for (Direction direction : HorizontalDirectionalBlock.FACING.getPossibleValues()) {
//      BlockPos offsetPos = this.getBlockPos().offset(direction.getNormal());
//      if (level.getBlockState(offsetPos).isAir()) {
//        for (int y = offsetPos.getY() - 1; y > 0; --y) {
//          BlockPos checkPos = new BlockPos(offsetPos.getX(), y, offsetPos.getZ());
//          if (level.getBlockState(checkPos).entityCanStandOn(level, checkPos, mob)) {
//            TechColonies.LOGGER.debug("AccessPoint " + checkPos.toShortString());
//            return checkPos;
//          }
//        }
//      }
//    }
//    return null;
  }

  public boolean isInput() {
    return interfaceDirection == InterfaceDirection.INPUT;
  }

  public void setInterfaceDirection(InterfaceDirection interfaceDirection) {
    this.interfaceDirection = interfaceDirection;
  }

  @Override
  public void load(@NotNull CompoundTag compoundTag) {
    super.load(compoundTag);
    interfaceDirection = compoundTag.getBoolean("IsInput") ? InterfaceDirection.INPUT : InterfaceDirection.OUTPUT;
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag compoundTag) {
    super.saveAdditional(compoundTag);
    compoundTag.putBoolean("IsInput", interfaceDirection.equals(InterfaceDirection.INPUT));
  }
}
