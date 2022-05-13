package com.sejapoe.techcolonies.entity;

import com.sejapoe.techcolonies.block.entity.SmelteryBlockEntity;
import com.sejapoe.techcolonies.entity.ai.DwarfJobs;
import com.sejapoe.techcolonies.entity.ai.IDwarfJob;
import com.sejapoe.techcolonies.registry.ModCapabilities;
import com.sejapoe.techcolonies.registry.ModItems;
import com.sejapoe.techcolonies.core.SavableContainer;
import com.sejapoe.techcolonies.core.properties.faceelement.BeardType;
import com.sejapoe.techcolonies.entity.ai.goal.FillInterfaceGoal;
import com.sejapoe.techcolonies.entity.ai.goal.MoveToControllerGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DwarfEntity extends PathfinderMob  {
  private static final EntityDataAccessor<CompoundTag> DATA_FACE = SynchedEntityData.defineId(DwarfEntity.class, EntityDataSerializers.COMPOUND_TAG);
  private IDwarfJob job;
  private String dwarfName; // TODO: Move to dwarf fundamentals
  private BlockPos controllerPos;
  private BlockPos inputContainerPos;
  private final int invSize = 1;
  private List<Goal> goalList = new ArrayList<>();

  private final SavableContainer inv = new SavableContainer(invSize);
  private LazyOptional<IItemHandlerModifiable> backpackHandler;

  public DwarfEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
    super(entityType, level);
    this.setCustomNameVisible(true);
  }

  @Nullable
  @Override
  public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor levelAccessor, @NotNull DifficultyInstance difficultyInstance, @NotNull MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
    this.setBeardType(BeardType.getRandom());
    this.setControllerPos(null);
    this.setDwarfName("Jack");
    return super.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(DATA_FACE, new CompoundTag());
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
    this.goalSelector.addGoal(20, new TemptGoal(this,1.25f, Ingredient.of(ModItems.STRANGE_WAND.get()), false));
    this.goalSelector.addGoal(21, new LookAtPlayerGoal(this, Player.class, 6F));
    this.goalSelector.addGoal(22, new RandomLookAroundGoal(this));
  }

  @Override
  public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
    if (capability == ModCapabilities.DWARF_ITEM_HANDLER_CAPABILITY) {
      if (this.backpackHandler == null)
        this.backpackHandler = LazyOptional.of(this::createHandler);
      return this.backpackHandler.cast();
    }
    return super.getCapability(capability, facing);
  }

  private @NotNull IItemHandlerModifiable createHandler() {
    return new InvWrapper(this.inv);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3f).add(Attributes.MAX_HEALTH, 20.0d);
  }

  @Override
  public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    super.readAdditionalSaveData(compoundTag);
    Tag beardType = compoundTag.get("BeardType");
    if (beardType != null) {
     this.setBeardType(BeardType.getByName(beardType.getAsString()));
    }
    CompoundTag controllerPos = compoundTag.getCompound("ControllerPos");
    this.controllerPos = controllerPos != null ? NbtUtils.readBlockPos(controllerPos) : null;
    CompoundTag inputContainerPos = compoundTag.getCompound("InputContainerPos");
    this.inputContainerPos = inputContainerPos != null ? NbtUtils.readBlockPos(inputContainerPos) : null;
    inv.loadAllItems(compoundTag);
    this.dwarfName = compoundTag.getString("Name");
    String jobKey = compoundTag.getString("Job");
    if (jobKey != "") {
      this.setJob(Arrays.stream(DwarfJobs.values()).filter(job -> job.getName().equals(jobKey)).findFirst().orElse(null));
    }
  }

  @Override
  public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    super.addAdditionalSaveData(compoundTag);
    compoundTag.putString("BeardType", this.getBeardType().getSerializedName());
    if (controllerPos != null) {
      compoundTag.put("ControllerPos", NbtUtils.writeBlockPos(controllerPos));
    }
    if (inputContainerPos != null) {
      compoundTag.put("InputContainerPos", NbtUtils.writeBlockPos(inputContainerPos));
    }
    inv.saveAllItems(compoundTag);
    compoundTag.putString("Name", this.dwarfName);
    if (this.job != null) {
      compoundTag.putString("Job", this.job.getName());
    }
  }

  public Component getBakedName() {
    TextComponent textComponent = new TextComponent(this.getDwarfName());
    if (this.getJob() != null) {
      textComponent.append(" ").append(this.getJob().getTranslatableName());
    }
    return textComponent;
  }

  public void updateCustomName() {
    this.setCustomName(this.getBakedName());
  }

  @Override
  public void setCustomName(@Nullable Component name) {
    if (name != null) {
      if (!name.getString().equals(this.getBakedName().getString())) {
        // TODO: Check allow renaming?
        this.dwarfName = name.getString();
        name = this.getBakedName();
      }
      super.setCustomName(name);
    }
  }

  private void updateGoals() {
    if (this.job == null) {
      for (Goal goal : goalList) {
        this.goalSelector.removeGoal(goal);
      }
      return;
    }
    if (this.job.hasControllerBlock()) {
      Goal g1 = new FillInterfaceGoal(this);
      Goal g2 = new MoveToControllerGoal(this, this.job.getControllerBlock());
      this.goalSelector.addGoal(1, g1);
      goalList.add(g1);
      this.goalSelector.addGoal(2, g2);
      goalList.add(g2);
    }
  }
  public void setControllerPos(BlockPos controllerPos) {
    this.controllerPos = controllerPos;
    if (controllerPos == null || level == null) {
      this.setJob(null);
      return;
    }
    BlockEntity blockEntity = level.getBlockEntity(controllerPos);
    if (blockEntity instanceof SmelteryBlockEntity) { // TODO: move this to BE
      this.setJob(DwarfJobs.MELTER);
    } else {
      this.setJob(null);
    }
  }

  public BlockPos getControllerPos() {
    return controllerPos;
  }

  public void setBeardType(BeardType beardType) {
    CompoundTag compoundTag = this.entityData.get(DATA_FACE);
    compoundTag.putString("BeardType", beardType.getSerializedName());
    this.entityData.set(DATA_FACE, compoundTag);
  }

  public BeardType getBeardType() {
    CompoundTag compoundTag = this.entityData.get(DATA_FACE);
    return BeardType.getByName(compoundTag.getString("BeardType"));
  }

  public void setInputContainerPos(BlockPos inputContainerPos) {
    this.inputContainerPos = inputContainerPos;
  }

  public BlockPos getInputContainerPos() {
    return inputContainerPos;
  }

  public SavableContainer getInv() {
    return inv;
  }

  public String getDwarfName() {
    return dwarfName;
  }

  public void setDwarfName(String dwarfName) {
    this.dwarfName = dwarfName;
    this.updateCustomName();
  }

  public IDwarfJob getJob() {
    return job;
  }

  public void setJob(IDwarfJob job) {
    this.job = job;
    this.updateCustomName();
    this.updateGoals();
  }
}
