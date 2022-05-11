package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.block.SmelteryBlock;
import com.sejapoe.techcolonies.registry.ModBlockEntities;
import com.sejapoe.techcolonies.registry.ModRecipeTypes;
import com.sejapoe.techcolonies.core.StructureInterfaceHelper;
import com.sejapoe.techcolonies.core.structures.PlatedBlockPattern;
import com.sejapoe.techcolonies.core.structures.Structures;
import com.sejapoe.techcolonies.recipe.SmelteryRecipe;
import com.sejapoe.techcolonies.recipe.StructureRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SmelteryBlockEntity extends AbstractStructureControllerBlockEntity<SmelteryRecipe> {
  private boolean isLit;
  public SmelteryBlockEntity(BlockPos blockPos, BlockState state) {
    super(ModBlockEntities.SMELTERY_BE.get(), blockPos, state);
  }
  public static void tick(Level level, BlockPos blockPos, BlockState blockState, SmelteryBlockEntity blockEntity) {
    if (level.isClientSide()) return;
    blockEntity.isLit = blockState.getValue(SmelteryBlock.LIT);
    updateStructureStatus(level, blockPos, blockState, blockEntity);
    blockState = level.getBlockState(blockPos);
    process(level, blockPos, blockState, blockEntity);
    if (blockEntity.isLit != blockState.getValue(SmelteryBlock.LIT)) {
      level.setBlockAndUpdate(blockPos, blockState.setValue(SmelteryBlock.LIT, blockEntity.isLit));
    }
  }

  private static void process(Level level, BlockPos blockPos, BlockState blockState, SmelteryBlockEntity blockEntity) {
    boolean isChanged = false;

    if (!blockEntity.isComplete()) {
      blockEntity.isLit = false;
      return;
    }

    if (blockEntity.getActiveRecipe() != null) {
      blockEntity.setProcessingTime(blockEntity.getProcessingTime() + 1);
      blockEntity.isLit = true;
      if (blockEntity.getProcessingTime() >= blockEntity.getActiveRecipe().getProcessingDuration()) {
        StructureInterfaceHelper.injectRecipeFluidResult(blockEntity.getActiveRecipe(), blockEntity.getFluidOutputInterfaces());
        blockEntity.setProcessingTime(0);
        blockEntity.setActiveRecipe(null);
        blockEntity.isLit = false;
        isChanged = true;
      }
    }

    if (blockEntity.getActiveRecipe() == null) {
      List<ItemStack> input = blockEntity.getSummaryItemInput();
      List<FluidStack> fluidInput = new ArrayList<>();
      List<SmelteryRecipe> recipes = blockEntity.getAllRecipes();
      SmelteryRecipe recipe = recipes.stream().filter(smelteryRecipe -> smelteryRecipe.matches(input, fluidInput)).findFirst().orElse(null);
      if (recipe != null && StructureInterfaceHelper.hasSpaceForFluidResult(recipe, blockEntity.getFluidOutputInterfaces())) {
        blockEntity.setActiveRecipe(recipe);
        StructureInterfaceHelper.extractRecipeIngredients(recipe, blockEntity.getItemInputInterfaces());
        blockEntity.setProcessingTime(0);
        isChanged = true;
      } else {
        blockEntity.isLit = false;
      }
    }

    if (isChanged) {
      setChanged(level, blockPos, blockState);
    }
  }

  private static void updateStructureStatus(Level level, BlockPos blockPos, BlockState blockState, SmelteryBlockEntity blockEntity) {
    PlatedBlockPattern.BlockPatternMatch match = checkStructure(level, blockPos, blockState);
    if (match != null) {
      blockEntity.updateStatus(true, match.getLowestMaterial().getLevel());
      blockEntity.setInterfaces(match.getInterfaces());
      blockEntity.updateInterfaces();
    } else {
      blockEntity.updateStatus(false, 0);
      blockEntity.updateInterfaces();
      blockEntity.setInterfaces(new ArrayList<>());
    }
  }

  @Nullable
  private static PlatedBlockPattern.BlockPatternMatch checkStructure(Level level, BlockPos pos, BlockState state) {
    Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
    BlockPos leftBotFront = pos.offset(facing.getClockWise().getNormal());
    return Structures.SMELTERY_PATTERN.matches(level, leftBotFront, facing);
  }

  @Override
  protected RecipeType<? extends StructureRecipe> getRecipeType() {
    return ModRecipeTypes.SMELTING.getType();
  }
}
