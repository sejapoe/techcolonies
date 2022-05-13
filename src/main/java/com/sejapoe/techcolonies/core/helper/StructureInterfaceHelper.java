package com.sejapoe.techcolonies.core.helper;

import com.sejapoe.techcolonies.block.entity.FluidInterfaceBlockEntity;
import com.sejapoe.techcolonies.block.entity.ItemInterfaceBlockEntity;
import com.sejapoe.techcolonies.recipe.StructureRecipe;
import com.sejapoe.techcolonies.registry.ModCapabilities;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StructureInterfaceHelper {
  public static void extractRecipeIngredients(StructureRecipe recipe, List<ItemInterfaceBlockEntity> interfaceBlockEntityList) {
    List<Ingredient> ingredients = new ArrayList<>(recipe.getIngredients());
    for (ItemInterfaceBlockEntity itemInterface : interfaceBlockEntityList) {
      IItemHandler handler = itemInterface.getCapability(ModCapabilities.DWARF_ITEM_HANDLER_CAPABILITY).orElse(null);
      for (int i = 0; i < handler.getSlots(); i++) {
        ItemStack stack = handler.getStackInSlot(i);
        for (Ingredient ingredient : ingredients) {
          if (ingredient.test(stack)) {
            stack.shrink(1);
            ingredients.remove(ingredient);
            break;
          }
        }
        if (ingredients.isEmpty()) return;
      }
    }
  }

  public static void injectRecipeFluidResult(StructureRecipe recipe, List<FluidInterfaceBlockEntity> fluidInterfaceBlockEntities) {
    tryInjectRecipeFluidResult(recipe, fluidInterfaceBlockEntities, IFluidHandler.FluidAction.EXECUTE);
  }

  public static boolean hasSpaceForFluidResult(StructureRecipe recipe, List<FluidInterfaceBlockEntity> fluidInterfaceBlockEntities) {
    List<FluidStack> fluidResults = tryInjectRecipeFluidResult(recipe, fluidInterfaceBlockEntities, IFluidHandler.FluidAction.SIMULATE);
    return fluidResults.isEmpty();
  }

  @NotNull
  private static List<FluidStack> tryInjectRecipeFluidResult(StructureRecipe recipe, List<FluidInterfaceBlockEntity> fluidInterfaceBlockEntities, IFluidHandler.FluidAction simulate) {
    List<FluidStack> fluidResults = new ArrayList<>(recipe.getFluidResults());
    List<FluidStack> fluidResults1 = new ArrayList<>();
    for (FluidInterfaceBlockEntity fluidInterface : fluidInterfaceBlockEntities) {
      IFluidHandler handler = fluidInterface.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null);
      for (FluidStack fluidStack : fluidResults) {
        FluidStack fluidStack1 = fluidStack.copy();
        fluidStack1.shrink(handler.fill(fluidStack, simulate));
        if (!fluidStack1.isEmpty()) {
          fluidResults1.add(fluidStack1);
        }
      }
    }
    return fluidResults1;
  }
}
