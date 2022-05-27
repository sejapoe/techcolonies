package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.core.structures.PlatedBlockPattern;
import com.sejapoe.techcolonies.recipe.StructureRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class AbstractStructureControllerBlockEntity extends AbstractStructureElementBlockEntity{

  public AbstractStructureControllerBlockEntity(BlockEntityType type, BlockPos blockPos, BlockState state) {
    super(type, blockPos, state);
  }
  @Override
  public void updateStatus(boolean isComplete, int structureLevel) {
    super.updateStatus(isComplete, structureLevel);
  }

  protected void updateStructureStatus(Level level, BlockPos blockPos, BlockState blockState) {
    PlatedBlockPattern.BlockPatternMatch match = this.checkStructure(level, blockPos, blockState);
    if (match != null) {
      this.updateStatus(true, match.getLowestMaterial().getLevel());
    } else {
      this.updateStatus(false, 0);
    }
  }

  protected abstract PlatedBlockPattern.BlockPatternMatch checkStructure(Level level, BlockPos pos, BlockState state);
}
