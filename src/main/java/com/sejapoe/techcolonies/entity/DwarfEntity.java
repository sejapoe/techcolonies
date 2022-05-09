package com.sejapoe.techcolonies.entity;

import com.sejapoe.techcolonies.core.ModCapabilities;
import com.sejapoe.techcolonies.core.ModItems;
import com.sejapoe.techcolonies.core.SavableContainer;
import com.sejapoe.techcolonies.core.properties.faceelement.BeardType;
import com.sejapoe.techcolonies.entity.ai.goal.FillInterfaceGoal;
import com.sejapoe.techcolonies.entity.ai.goal.MoveToControllerGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

public class DwarfEntity extends PathfinderMob  {
  private static final EntityDataAccessor<CompoundTag> DATA_FACE = SynchedEntityData.defineId(DwarfEntity.class, EntityDataSerializers.COMPOUND_TAG);
  private BlockPos controllerPos;
  private BlockPos inputContainerPos;
  private int invSize = 1;

  private final SavableContainer inv = new SavableContainer(invSize);
  private LazyOptional<IItemHandlerModifiable> backpackHandler;

  public DwarfEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
    super(entityType, level);
  }

  @Nullable
  @Override
  public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
    this.setBeardType(BeardType.getRandom());
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
    this.goalSelector.addGoal(1, new FillInterfaceGoal(this));
    this.goalSelector.addGoal(2, new MoveToControllerGoal(this));
    this.goalSelector.addGoal(3, new TemptGoal(this,1.25f, Ingredient.of(ModItems.STRANGE_WAND.get()), false));
    this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6F));
    this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
    if (capability == ModCapabilities.DWARF_ITEM_HANDLER_CAPABILITY) {
      if (this.backpackHandler == null)
        this.backpackHandler = LazyOptional.of(this::createHandler);
      return this.backpackHandler.cast();
    }
    return super.getCapability(capability, facing);
  }

  private IItemHandlerModifiable createHandler() {
    return new InvWrapper(this.inv);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3f).add(Attributes.MAX_HEALTH, 20.0d);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compoundTag) {
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
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compoundTag) {
    super.addAdditionalSaveData(compoundTag);
    compoundTag.putString("BeardType", this.getBeardType().getSerializedName());
    if (controllerPos != null) {
      compoundTag.put("ControllerPos", NbtUtils.writeBlockPos(controllerPos));
    }
    if (inputContainerPos != null) {
      compoundTag.put("InputContainerPos", NbtUtils.writeBlockPos(inputContainerPos));
    }
    inv.saveAllItems(compoundTag);
  }


  public void setControllerPos(BlockPos controllerPos) {
    this.controllerPos = controllerPos;
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
}
