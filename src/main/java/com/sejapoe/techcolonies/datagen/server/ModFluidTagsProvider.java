package com.sejapoe.techcolonies.datagen.server;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.ModFluids;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModFluidTagsProvider extends FluidTagsProvider {
  public ModFluidTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
    super(generator, TechColonies.MOD_ID, existingFileHelper);
  }

  @Override
  protected void addTags() {
    tag(FluidTags.LAVA).add(ModFluids.MOLTEN_COPPER.getStillFluid());
    tag(FluidTags.LAVA).add(ModFluids.MOLTEN_COPPER.getFlowingFluid());
  }
}
