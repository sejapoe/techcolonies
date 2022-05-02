package com.sejapoe.techcolonies.datagen.server;

import com.sejapoe.techcolonies.TechColonies;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider {
  public ModItemTagsProvider(DataGenerator generator, BlockTagsProvider blocks, @Nullable ExistingFileHelper existingFileHelper) {
    super(generator, blocks, TechColonies.MOD_ID, existingFileHelper);
  }

  @Override
  protected void addTags() {

  }
}
