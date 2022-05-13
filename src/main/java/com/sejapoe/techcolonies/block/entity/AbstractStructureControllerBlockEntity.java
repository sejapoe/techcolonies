package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.recipe.StructureRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class AbstractStructureControllerBlockEntity<T extends StructureRecipe> extends AbstractStructureElementBlockEntity{
  private List<BlockPos> interfaces;
  private int processingTime = 0;
  private T activeRecipe;

  public AbstractStructureControllerBlockEntity(BlockEntityType type, BlockPos blockPos, BlockState state) {
    super(type, blockPos, state);
    this.interfaces = new ArrayList<>();
  }

  @Override
  public void load(@NotNull CompoundTag compoundTag) {
    super.load(compoundTag);
    this.interfaces = new ArrayList<>();
    CompoundTag interfacesCompoundTag = compoundTag.getCompound("Interfaces");
    for (String key : interfacesCompoundTag.getAllKeys()) {
      interfaces.add(NbtUtils.readBlockPos(interfacesCompoundTag.getCompound(key)));
    }
    this.processingTime = compoundTag.getInt("ProcessingTime");
    ResourceLocation recipeId = new ResourceLocation(compoundTag.getString("RecipeId"));
    if (level != null) {
      this.activeRecipe = (T) level.getRecipeManager().byKey(recipeId).filter(recipe -> recipe instanceof StructureRecipe).orElse(null);
    }
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag compoundTag) {
    super.saveAdditional(compoundTag);
    CompoundTag interfacesCompoundTag = new CompoundTag();
    AtomicInteger i = new AtomicInteger();
    this.interfaces.forEach(pos -> {
      interfacesCompoundTag.put("Pos" + i.getAndIncrement(), NbtUtils.writeBlockPos(pos));
    });
    compoundTag.put("Interfaces", interfacesCompoundTag);
    compoundTag.putInt("ProcessingTime", this.processingTime);
    if (this.activeRecipe != null) {
      compoundTag.putString("RecipeId", this.activeRecipe.getId().toString());
    }
  }

  @Override
  public void updateStatus(boolean isComplete, int structureLevel) {
    super.updateStatus(isComplete, structureLevel);
  }

  public void updateInterfaces() {
    for (BlockPos pos : this.getInterfaces()) {
      BlockEntity interfaceEntity = level.getBlockEntity(pos);
      if (interfaceEntity instanceof AbstractStructureElementBlockEntity) {
        ((AbstractStructureElementBlockEntity) interfaceEntity).updateStatus(isComplete(), getStructureLevel());
      }
    }
  }

  public void setInterfaces(List<BlockPos> interfaces) {
    this.interfaces = interfaces;
  }

  public List<BlockPos> getInterfaces() {
    return interfaces;
  }

  public List<AbstractInterfaceBlockEntity> getSerializedInterfaces() {
    return interfaces.stream().map(blockPos -> (AbstractInterfaceBlockEntity) level.getBlockEntity(blockPos)).collect(Collectors.toList());
  }

  public List<FluidInterfaceBlockEntity> getFluidInterfaces() {
    List<FluidInterfaceBlockEntity> fluidInterfaceBlockEntities = new ArrayList<>();
    getSerializedInterfaces().forEach(can -> {
      if (can instanceof FluidInterfaceBlockEntity) {
        fluidInterfaceBlockEntities.add((FluidInterfaceBlockEntity) can);
      }
    });
    return fluidInterfaceBlockEntities;
  }

  @NotNull
  public List<FluidInterfaceBlockEntity> getFluidOutputInterfaces() {
    return getFluidInterfaces().stream().filter(interfaceBlockEntity -> !interfaceBlockEntity.isInput()).collect(Collectors.toList());
  }

  public List<ItemInterfaceBlockEntity> getItemInterfaces() {
    List<ItemInterfaceBlockEntity> itemInterfaceBlockEntities = new ArrayList<>();
    getSerializedInterfaces().forEach(can -> {
      if (can instanceof ItemInterfaceBlockEntity) {
        itemInterfaceBlockEntities.add((ItemInterfaceBlockEntity) can);
      }
    });
    return itemInterfaceBlockEntities;
  }

  @NotNull
  public List<ItemInterfaceBlockEntity> getItemInputInterfaces() {
    return getItemInterfaces().stream().filter(AbstractInterfaceBlockEntity::isInput).collect(Collectors.toList());
  }

  public List<ItemStack> getSummaryItemInput() {
    List<ItemInterfaceBlockEntity> inputInterfaces = getItemInputInterfaces();
    List<ItemStack> summary = new ArrayList<>();
    inputInterfaces.forEach(inputInterface -> {
      summary.addAll(inputInterface.getItems());
    });
    return summary;
  }

  public boolean isValidItemInput(ItemStack inserted) {
    List<T> recipes = getAllRecipes();
    return recipes.stream().anyMatch(recipe -> recipe.getIngredients().stream().anyMatch(ingredient -> ((Ingredient) ingredient).test(inserted)) && recipe.getRequiredStructureLevel() <= this.getStructureLevel());
  }

  public List<T> getAllRecipes() {
    List<T> recipes = new ArrayList<>();
    level.getRecipeManager().getRecipes().forEach(recipe -> {
      if (recipe instanceof StructureRecipe && recipe.getType().equals(getRecipeType())) {
        recipes.add((T) recipe);
      }
    });
    return recipes;
  }

  protected abstract RecipeType<? extends StructureRecipe> getRecipeType();

  public int getProcessingTime() {
    return processingTime;
  }

  public void setProcessingTime(int processingTime) {
    this.processingTime = processingTime;
  }

  public T getActiveRecipe() {
    return activeRecipe;
  }

  public void setActiveRecipe(T activeRecipe) {
    this.activeRecipe = activeRecipe;
  }
}
