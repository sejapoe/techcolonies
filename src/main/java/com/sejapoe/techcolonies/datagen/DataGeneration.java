package com.sejapoe.techcolonies.datagen;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.datagen.client.ModBlockStateProvider;
import com.sejapoe.techcolonies.datagen.client.ModItemModelProvider;
import com.sejapoe.techcolonies.datagen.server.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = TechColonies.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration {
  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    ExistingFileHelper helper = event.getExistingFileHelper();

    if (event.includeClient()) {
      // Client Data Generation
      generator.addProvider(new ModBlockStateProvider(generator, helper));
      generator.addProvider(new ModItemModelProvider(generator, helper));
    }

    if (event.includeServer()) {
      // Server Data Generation
      ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(generator, helper);

      generator.addProvider(new ModRecipeProvider(generator));
      generator.addProvider(blockTagsProvider);
      generator.addProvider(new ModItemTagsProvider(generator, blockTagsProvider, helper));
      generator.addProvider(new ModFluidTagsProvider(generator, helper));
      generator.addProvider(new ModLootTableProvider(generator));
    }
  }
}
