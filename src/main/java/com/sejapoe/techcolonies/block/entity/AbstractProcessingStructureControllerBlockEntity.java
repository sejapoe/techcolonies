package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.block.SmelteryBlock;
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

public abstract class AbstractProcessingStructureControllerBlockEntity<T extends StructureRecipe> extends AbstractStructureControllerBlockEntity {
  private List<BlockPos> interfaces;
  private int processingTime = 0;
  private T activeRecipe;
  private boolean isLit;

  public AbstractProcessingStructureControllerBlockEntity(BlockEntityType type, BlockPos blockPos, BlockState state) {
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
    this.interfaces.forEach(pos -> interfacesCompoundTag.put("Pos" + i.getAndIncrement(), NbtUtils.writeBlockPos(pos)));
    compoundTag.put("Interfaces", interfacesCompoundTag);
    compoundTag.putInt("ProcessingTime", this.processingTime);
    if (this.activeRecipe != null) {
      compoundTag.putString("RecipeId", this.activeRecipe.getId().toString());
    }
  }

  public void updateInterfaces() {
    for (BlockPos pos : this.getInterfaces()) {
      BlockEntity interfaceEntity = Objects.requireNonNull(level).getBlockEntity(pos);
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
    return interfaces.stream().map(blockPos -> (AbstractInterfaceBlockEntity) Objects.requireNonNull(level).getBlockEntity(blockPos)).collect(Collectors.toList());
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
    inputInterfaces.forEach(inputInterface -> summary.addAll(inputInterface.getItems()));
    return summary;
  }

  public boolean isValidItemInput(ItemStack inserted) {
    List<T> recipes = getAllRecipes();
    return recipes.stream().anyMatch(recipe -> recipe.getIngredients().stream().anyMatch(ingredient -> ingredient.test(inserted)) && recipe.getRequiredStructureLevel() <= this.getStructureLevel());
  }

  public List<T> getAllRecipes() {
    List<T> recipes = new ArrayList<>();
    Objects.requireNonNull(level).getRecipeManager().getRecipes().forEach(recipe -> {
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

  public boolean isLit() {
    return isLit;
  }

  public void setLit(boolean lit) {
    isLit = lit;
  }

  public static void tick(@NotNull Level level, BlockPos blockPos, BlockState blockState, AbstractProcessingStructureControllerBlockEntity<?> blockEntity) {
    if (level.isClientSide()) return;
    blockEntity.isLit = blockState.getValue(SmelteryBlock.LIT);
    blockEntity.updateStructureStatus(level, blockPos, blockState);
    blockState = level.getBlockState(blockPos);
    blockEntity.process(level, blockPos, blockState);
    if (blockEntity.isLit != blockState.getValue(SmelteryBlock.LIT)) {
      level.setBlockAndUpdate(blockPos, blockState.setValue(SmelteryBlock.LIT, blockEntity.isLit));
    }
    blockEntity.tick(level, blockPos, blockState);
  }

  @Override
  protected void updateStructureStatus(Level level, BlockPos blockPos, BlockState blockState) {
    PlatedBlockPattern.BlockPatternMatch match = this.checkStructure(level, blockPos, blockState);
    if (match != null) {
      this.updateStatus(true, match.getLowestMaterial().getLevel());
      this.setInterfaces(match.getInterfaces());
      this.updateInterfaces();
    } else {
      this.updateStatus(false, 0);
      this.updateInterfaces();
      this.setInterfaces(new ArrayList<>());
    }
  }

  protected abstract void process(Level level, BlockPos blockPos, BlockState blockState);
}
