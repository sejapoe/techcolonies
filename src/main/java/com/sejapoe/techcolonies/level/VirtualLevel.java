package com.sejapoe.techcolonies.level;

import com.google.common.collect.ImmutableList;
import net.minecraft.Util;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.ServerLevelData;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class VirtualLevel extends ServerLevel {
  public VirtualLevel(ServerLevel parent) throws IOException {
    this(parent, parent.random.nextLong());
  }

  private VirtualLevel(ServerLevel parent, long seed) throws IOException {
    this(parent, seed, replaceSeed(seed, ((PrimaryLevelData) parent.getServer().getWorldData().overworldData()).worldGenSettings()));
  }

  private static WorldGenSettings replaceSeed(long seed, WorldGenSettings worldGenSettings) {
    return worldGenSettings.withSeed(false, OptionalLong.of(seed));
  }

  public VirtualLevel(ServerLevel parent, long seed, WorldGenSettings newOpts) throws IOException {
    super(parent.getServer(),
            Util.backgroundExecutor(),
            LevelStorageSource.createDefault(Files.createTempDirectory("TechColoniesVirtual")).createAccess("TechColoniesVirtual"),
            (ServerLevelData) parent.getLevelData(),
            parent.dimension(),
            new Holder.Direct<>(parent.dimensionType()),
            new VirtualChunkProgressListener(),
            Objects.requireNonNull(newOpts.dimensions().get(parent.dimension().location())).generator(),
            false,
            seed,
            ImmutableList.of(),
            false);
  }


}
