package com.sejapoe.techcolonies.entity;

import com.sejapoe.techcolonies.core.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

public class DwarfEntity extends PathfinderMob {
  public DwarfEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
    super(entityType, level);
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
}
