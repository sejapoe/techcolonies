package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.block.PlatedBrickWallBlock;
import com.sejapoe.techcolonies.block.PortalBlock;
import com.sejapoe.techcolonies.core.structures.PlatedBlockPattern;
import com.sejapoe.techcolonies.core.structures.Structures;
import com.sejapoe.techcolonies.entity.DwarfEntity;
import com.sejapoe.techcolonies.entity.SentDwarf;
import com.sejapoe.techcolonies.registry.ModBlockEntities;
import com.sejapoe.techcolonies.registry.ModBlocks;
import com.sejapoe.techcolonies.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WallSide;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PortalControllerBlockEntity extends AbstractStructureControllerBlockEntity implements ISentDwarfHolder {
  private final List<SentDwarf> entities = new ArrayList<>();
  private final Queue<SentDwarf> entitiesToExtract = new ArrayDeque<>();
  public PortalControllerBlockEntity(BlockPos blockPos, BlockState state) {
    super(ModBlockEntities.PORTAL_BE.get(), blockPos, state);
  }

  @Override
  protected PlatedBlockPattern.BlockPatternMatch checkStructure(Level level, BlockPos pos, BlockState state) {
    BlockPos leftFrontBot = pos.offset(2, 0, -2);
    return Structures.PORTAL_PATTERN.matches(level, leftFrontBot, Direction.NORTH);
  }

  @Override
  public void tick(@NotNull Level level, BlockPos blockPos, BlockState blockState) {
    if (this.isComplete()) {
      checkAndRepairPortal(level, blockPos);
    } else {
      checkAndClearPortal(level, blockPos);
    }
    for (SentDwarf entity : this.entities) {
      entity.tick();
    }
    extractEntities();
  }

  @Override
  public void load(@NotNull CompoundTag compoundTag) {
    super.load(compoundTag);
    this.entities.clear();
    for (Tag tag : compoundTag.getList("Entities", 10)) {
      CompoundTag compoundTag1 = (CompoundTag) tag;
      this.entities.add(new SentDwarf(compoundTag1, this));
    }
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag compoundTag) {
    super.saveAdditional(compoundTag);
    ListTag entities = new ListTag();
    for (SentDwarf entity : this.entities) {
      CompoundTag compoundTag1 = new CompoundTag();
      entity.save(compoundTag1);
      entities.add(compoundTag1);
    }
    compoundTag.put("Entities", entities);
  }

  public void insertEntity(DwarfEntity entity, int time) {
    SentDwarf entity1 = new SentDwarf(entity, time, this);
    this.entities.add(entity1);
    entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
    this.setChanged();
  }

  public void extractEntity(SentDwarf entity) {
    this.entitiesToExtract.add(entity);
  }

  public void trueExtractEntity(SentDwarf entity) {
    DwarfEntity entity1 = ModEntities.DWARF_ENTITY.get().create(Objects.requireNonNull(level));
    assert entity1 != null;
    entity1.load(entity.getCompoundTag());
    entity1.moveTo(this.getBlockPos().above(2), 0, 0);
    this.entities.remove(entity);
    ((ServerLevel) level).addDuringTeleport(entity1);
    this.setChanged();
  }

  public void extractEntities() {
    while (!entitiesToExtract.isEmpty()) {
      trueExtractEntity(entitiesToExtract.remove());
    }
  }

  public List<SentDwarf> getEntities() {
    return entities;
  }

  private void checkAndRepairPortal(Level level, BlockPos blockPos) {
    Block portalBlock = ModBlocks.PORTAL_BLOCK.get();
    for (int i = 1; i < 4; ++i) {
      BlockPos blockPos1 = blockPos.above(i);
      BlockState pState = portalBlock.defaultBlockState().setValue(PortalBlock.AXIS, Direction.Axis.Y);
      if (!level.getBlockState(blockPos1).equals(pState)) {
        level.setBlockAndUpdate(blockPos1, pState);
      }
      for (Direction direction : Direction.Plane.HORIZONTAL) {
        BlockPos blockPos2 = blockPos1.offset(direction.getNormal());
        BlockState pState1 = portalBlock.defaultBlockState().setValue(PortalBlock.AXIS, direction.getAxis());
        if (!level.getBlockState(blockPos2).equals(pState1)) {
          level.setBlockAndUpdate(blockPos2, pState1);
        }
        BlockPos blockPos3 = blockPos2.offset(direction.getNormal());
        if (level.getBlockState(blockPos3).getValue(PlatedBrickWallBlock.getPropertyFromDirection(direction.getOpposite())) != WallSide.TALL) {
          level.setBlockAndUpdate(blockPos3, level.getBlockState(blockPos3).setValue(PlatedBrickWallBlock.getPropertyFromDirection(direction.getOpposite()), WallSide.TALL));
        }
      }
    }
  }

  private void checkAndClearPortal(Level level, BlockPos blockPos) {
    Block portalBlock = ModBlocks.PORTAL_BLOCK.get();
    for (int i = 1; i < 4; ++i) {
      BlockPos blockPos1 = blockPos.above(i);
      if (level.getBlockState(blockPos1).is(portalBlock)) {
        level.setBlockAndUpdate(blockPos1, Blocks.AIR.defaultBlockState());
      }
      for (Direction direction : Direction.Plane.HORIZONTAL) {
        BlockPos blockPos2 = blockPos1.offset(direction.getNormal());
        if (level.getBlockState(blockPos2).is(portalBlock)) {
          level.setBlockAndUpdate(blockPos2, Blocks.AIR.defaultBlockState());
        }
      }
    }
  }
}
