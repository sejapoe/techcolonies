package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.registry.ModBlockEntities;
import com.sejapoe.techcolonies.registry.ModRecipeTypes;
import com.sejapoe.techcolonies.core.helper.StructureInterfaceHelper;
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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SmelteryBlockEntity extends AbstractProcessingStructureControllerBlockEntity<SmelteryRecipe> {
  public SmelteryBlockEntity(BlockPos blockPos, BlockState state) {
    super(ModBlockEntities.SMELTERY_BE.get(), blockPos, state);
  }
  protected void process(Level level, BlockPos blockPos, BlockState blockState) {
    boolean isChanged = false;

    if (!this.isComplete()) {
      // Pause, NOT RESET, recipe if structure incomplete;
      this.setLit(false);
      return;
    }

    if (this.getActiveRecipe() != null) {
      if (this.getStructureLevel() < this.getActiveRecipe().getRequiredStructureLevel()) {
        // Reset recipe if structure level not enough;
        this.setProcessingTime(0);
        this.setActiveRecipe(null);
        this.setLit(false);
        isChanged = true;
      } else {
        this.setProcessingTime(this.getProcessingTime() + 1);
        this.setLit(true);
        if (this.getProcessingTime() >= this.getActiveRecipe().getProcessingDuration()) {
          // Complete recipe;
          StructureInterfaceHelper.injectRecipeFluidResult(this.getActiveRecipe(), this.getFluidOutputInterfaces());
          this.setProcessingTime(0);
          this.setActiveRecipe(null);
          this.setLit(false);
          isChanged = true;
        }
      }
    }

    if (this.getActiveRecipe() == null) {
      List<ItemStack> input = this.getSummaryItemInput();
      List<FluidStack> fluidInput = new ArrayList<>();
      List<SmelteryRecipe> recipes = this.getAllRecipes();
      SmelteryRecipe recipe = recipes.stream().filter(smelteryRecipe -> smelteryRecipe.matches(input, fluidInput)).findFirst().orElse(null);
      if (recipe != null && StructureInterfaceHelper.hasSpaceForFluidResult(recipe, this.getFluidOutputInterfaces())) {
        // Start process recipe;
        this.setActiveRecipe(recipe);
        StructureInterfaceHelper.extractRecipeIngredients(recipe, this.getItemInputInterfaces());
        this.setProcessingTime(0);
        isChanged = true;
      } else {
        // Confirm that isLit false, if no recipe;
        this.setLit(false);
      }
    }

    if (isChanged) {
      setChanged(level, blockPos, blockState);
    }
  }

  @Nullable
  @Override
  protected PlatedBlockPattern.BlockPatternMatch checkStructure(Level level, BlockPos pos, BlockState state) {
    Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
    BlockPos leftBotFront = pos.offset(facing.getClockWise().getNormal());
    return Structures.SMELTERY_PATTERN.matches(level, leftBotFront, facing);
  }

  @Override
  public void tick(@NotNull Level level, BlockPos blockPos, BlockState blockState) {

  }

  @Override
  protected RecipeType<? extends StructureRecipe> getRecipeType() {
    return ModRecipeTypes.SMELTING.getType();
  }
}
