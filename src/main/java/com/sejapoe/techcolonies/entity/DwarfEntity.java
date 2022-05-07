package com.sejapoe.techcolonies.entity;

import com.sejapoe.techcolonies.core.ModItems;
import com.sejapoe.techcolonies.core.properties.faceelement.BeardType;
import net.minecraft.nbt.CompoundTag;
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
import org.jetbrains.annotations.Nullable;

public class DwarfEntity extends PathfinderMob {
  private static final EntityDataAccessor<CompoundTag> DATA_FACE = SynchedEntityData.defineId(DwarfEntity.class, EntityDataSerializers.COMPOUND_TAG);
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
    this.goalSelector.addGoal(1, new TemptGoal(this,1.25f, Ingredient.of(ModItems.STRANGE_WAND.get()), false));
    this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.f));
    this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6F));
    this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
//    this.goalSelector.addGoal();
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3f).add(Attributes.MAX_HEALTH, 20.0d);
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

  @Override
  public void readAdditionalSaveData(CompoundTag compoundTag) {
    super.readAdditionalSaveData(compoundTag);
    Tag tag = compoundTag.get("BeardType");
    if (tag != null) {
     String asString = tag.getAsString();
     BeardType beardType = BeardType.getByName(asString);
     this.setBeardType(beardType);
    }
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compoundTag) {
    super.addAdditionalSaveData(compoundTag);
    compoundTag.putString("BeardType", this.getBeardType().getSerializedName());
  }


}
