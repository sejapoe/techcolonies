package com.sejapoe.techcolonies.entity.ai.job.miner;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

public class Mine {
  private BlockPos startPos;
  private Direction direction;
  private int miningLevel;
  private int miningRadius;

  public Mine(BlockPos startPos, Direction direction) {
    this(startPos, direction, 30, 32);
  }

  public Mine(BlockPos startPos, Direction direction, int miningLevel, int miningRadius) {
    this.startPos = startPos;
    this.direction = direction;
    this.miningLevel = miningLevel;
    this.miningRadius = miningRadius;
  }

  public static CompoundTag saveToNBT(Mine mine) {
    CompoundTag tag = new CompoundTag();
    tag.put("StartPos", NbtUtils.writeBlockPos(mine.startPos));
    tag.putInt("Direction", mine.direction.get2DDataValue());
    tag.putInt("MiningLevel", mine.miningLevel);
    tag.putInt("MiningRadius", mine.miningRadius);
    return tag;
  }

  public static Mine loadFromNBT(CompoundTag compoundTag) {
    if (compoundTag.isEmpty()) return null;
    return new Mine(NbtUtils.readBlockPos(compoundTag.getCompound("StartPos")),
            Direction.from2DDataValue(compoundTag.getInt("Direction")),
            compoundTag.getInt("MiningLevel"),
            compoundTag.getInt("MiningRadius"));
  }
}
