package com.sejapoe.techcolonies.datagen.server;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class ModStandartRecipeProvider extends RecipeProvider {
  public ModStandartRecipeProvider(DataGenerator generator) {
    super(generator);
  }

  @Override
  protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(ModItems.STRANGE_WAND.get(), 1)
            .define('x', Items.STICK)
            .define('y', Tags.Items.DYES_GREEN)
            .pattern(" yx")
            .pattern("yxy")
            .pattern("xy ")
            .unlockedBy("has_copper", has(Items.COPPER_INGOT))
            .save(consumer, new ResourceLocation(TechColonies.MOD_ID, Objects.requireNonNull(ModItems.STRANGE_WAND.get().getRegistryName()).getPath()));
  }
}
