package com.sejapoe.techcolonies.core.structures;

import com.google.common.base.MoreObjects;
import com.google.common.cache.LoadingCache;
import com.sejapoe.techcolonies.block.AbstractInterfaceBlock;
import com.sejapoe.techcolonies.block.IPlatedBlock;
import com.sejapoe.techcolonies.core.properties.PlatingMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PlatedBlockPattern extends BlockPattern {

  public PlatedBlockPattern(BlockPattern pattern) {
    this(pattern.getPattern());
  }

  public PlatedBlockPattern(Predicate<BlockInWorld>[][][] predicates) {
    super(predicates);
  }

  // pos - left bot front
  // direction - controller facing
  public BlockPatternMatch matches(Level level, BlockPos pos, Direction direction) {
    LoadingCache<BlockPos, BlockInWorld> loadingCache = createLevelCache(level, false);
    return matches(pos, direction, loadingCache);
  }

  @Nullable
  private BlockPatternMatch matches(BlockPos pos, Direction direction, LoadingCache<BlockPos, BlockInWorld> loadingCache) {
    PlatingMaterial lowestMaterial = PlatingMaterial.values()[PlatingMaterial.values().length - 1];
    List<BlockPos> interfaces = new ArrayList<>();
    for(int i = 0; i < this.getWidth(); ++i) {
      for(int j = 0; j < this.getHeight(); ++j) {
        for(int k = 0; k < this.getDepth(); ++k) {
          int x = this.getWidth() - i - 1;
          int y = this.getHeight() - j - 1;
          int z = this.getDepth() - k - 1;
          BlockInWorld block = loadingCache.getUnchecked(pos
                  .offset(direction.getCounterClockWise(Direction.Axis.Y).getNormal().multiply(i)
                  .offset(Direction.UP.getNormal().multiply(j))
                  .offset(direction.getOpposite().getNormal().multiply(k))));
          if (!this.getPattern()[z][y][x].test(block)) {
            return null;
          } else {
            if (block.getState().getBlock() instanceof IPlatedBlock) {
              PlatingMaterial material = ((IPlatedBlock) block.getState().getBlock()).getPlatingMaterial();
              if (material.getLevel() < lowestMaterial.getLevel()) {
                lowestMaterial = material;
              }
            }

            if (block.getState().getBlock() instanceof AbstractInterfaceBlock) {
              interfaces.add(block.getPos());
            }
          }
        }
      }
    }
    return new BlockPatternMatch(pos, direction, Direction.UP, loadingCache, this.getWidth(), this.getHeight(), this.getDepth(), lowestMaterial, interfaces);
  }


  public static class BlockPatternMatch extends BlockPattern.BlockPatternMatch {
    private final PlatingMaterial lowestMaterial;
    private final List<BlockPos> interfaces;

    public BlockPatternMatch(BlockPos pos, Direction direction, Direction direction1, LoadingCache<BlockPos, BlockInWorld> loadingCache, int width, int height, int depth, PlatingMaterial lowestMaterial, List<BlockPos> interfaces) {
      super(pos, direction, direction1, loadingCache, width, height, depth);
      this.lowestMaterial = lowestMaterial;
      this.interfaces = interfaces;
    }

    public PlatingMaterial getLowestMaterial() {
      return lowestMaterial;
    }

    @Override
    public @NotNull String toString() {
      return MoreObjects.toStringHelper(this).add("up", this.getUp()).add("forwards", this.getForwards()).add("frontTopLeft", this.getFrontTopLeft()).add("material", this.getLowestMaterial().getSerializedName()).toString();
    }

    public List<BlockPos> getInterfaces() {
      return interfaces;
    }
  }
}
