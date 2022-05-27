package com.sejapoe.techcolonies.level;

import com.sejapoe.techcolonies.TechColonies;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VirtualChunkProgressListener implements ChunkProgressListener {
  @Override
  public void updateSpawnPos(@NotNull ChunkPos pCenter) {}

  @Override
  public void onStatusChange(@NotNull ChunkPos pChunkPosition, @Nullable ChunkStatus pNewStatus) {}

  @Override
  public void start() {}

  @Override
  public void stop() {}
}
