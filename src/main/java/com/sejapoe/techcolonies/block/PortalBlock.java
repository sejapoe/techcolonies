package com.sejapoe.techcolonies.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PortalBlock extends Block {
  public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
  protected static final int AABB_OFFSET = 2;
  protected static final VoxelShape X_AXIS_AABB = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
  protected static final VoxelShape Z_AXIS_AABB = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);

  public PortalBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
  }

//  @Override
//  public @NotNull BlockState updateShape(BlockState pState, Direction pFacing, @NotNull BlockState pFacingState, @NotNull LevelAccessor pLevel, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pFacingPos) {
//    Direction.Axis direction$axis = pFacing.getAxis();
//    Direction.Axis direction$axis1 = pState.getValue(AXIS);
//    boolean flag = direction$axis1 != direction$axis && direction$axis.isHorizontal();
//    return !flag && !pFacingState.is(this) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
//  }

  public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
    return switch (pState.getValue(AXIS)) {
      case Z -> Z_AXIS_AABB;
      case X -> X_AXIS_AABB;
      case Y -> Shapes.join(X_AXIS_AABB, Z_AXIS_AABB, BooleanOp.OR);
    };
  }

  public @NotNull ItemStack getCloneItemStack(@NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState) {
    return ItemStack.EMPTY;
  }

  public @NotNull BlockState rotate(@NotNull BlockState pState, @NotNull Rotation pRot) {
    return switch (pRot) {
      case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (pState.getValue(AXIS)) {
        case Z -> pState.setValue(AXIS, Direction.Axis.X);
        case X -> pState.setValue(AXIS, Direction.Axis.Z);
        default -> pState;
      };
      default -> pState;
    };
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    pBuilder.add(AXIS);
  }
}
