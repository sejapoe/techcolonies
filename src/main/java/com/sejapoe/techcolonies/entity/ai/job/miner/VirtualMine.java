package com.sejapoe.techcolonies.entity.ai.job.miner;

import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.core.helper.BlockHelper;
import com.sejapoe.techcolonies.entity.ai.base.goal.AbstractBreakerAI;
import com.sejapoe.techcolonies.level.VirtualLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import slimeknights.tconstruct.tools.TinkerTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VirtualMine {
  private final ServerLevel parentLevel;
  private VirtualLevel virtualLevel;
  private final List<Consumer<VirtualMine>> consumers = new ArrayList<>();
  private final int chunkCount;
  private final int minHeight;
  private final int maxHeight;
  private final List<BlockState> blocksToMine = new ArrayList<>();
  private final List<BlockState> ores = new ArrayList<>();
  private long miningTime = 0;


  public VirtualMine(ServerLevel parentLevel, int chunkCount, int minHeight, int maxHeight, ItemStack tool) {
    this(parentLevel, chunkCount, minHeight, maxHeight);
    this.addListener(virtualMine -> virtualMine.getMiningTime(tool));
  }

  public VirtualMine(ServerLevel parentLevel, int chunkCount, int minHeight, int maxHeight) {
    this.parentLevel = parentLevel;
    this.chunkCount = chunkCount;
    this.minHeight = minHeight;
    this.maxHeight = maxHeight;
    Thread virtualLevelCreatingThread = new Thread(() -> {
      try {
        this.virtualLevel = new VirtualLevel(this.parentLevel);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      for (Consumer<VirtualMine> consumer : this.consumers) {
        consumer.accept(this);
      }
      TechColonies.LOGGER.info("Virtual level created");
    });
    virtualLevelCreatingThread.start();
  }

  public void addListener(Consumer<VirtualMine> consumer) {
    consumers.add(consumer);
  }

  public List<BlockState> getOres() {
    if (this.ores.isEmpty()) {
      this.ores.addAll(this.getBlocksToMine().stream().filter(blockState -> blockState.is(Tags.Blocks.ORES)).collect(Collectors.toList()));
    }
    return this.ores;
  }

  private List<BlockState> getBlocksToMine() {
    if (this.blocksToMine.isEmpty() && isVirtualLevelCreated()) {
      for (int i = 0; i < chunkCount; i++) {
        ChunkAccess chunk = virtualLevel.getChunk(0, i);
        Stream<BlockState> blockStates = chunk.getBlockStates(new AABB(0, minHeight, 0, 16, maxHeight, 16));
        this.blocksToMine.addAll(blockStates.filter(blockState -> !(blockState.getBlock() instanceof LiquidBlock)).collect(Collectors.toList()));
      }
    }
    return this.blocksToMine;
  }

  public long getMiningTime(ItemStack tool) {
    if (this.miningTime == 0 && isVirtualLevelCreated()) {
      int max = 0;
      for (BlockState blockState : this.getBlocksToMine()) {
        int breakingTimeForTool = BlockHelper.getBreakingTimeForTool(this.virtualLevel, blockState, tool);
        if (breakingTimeForTool > max) {
          max = breakingTimeForTool;
        }
        this.miningTime += breakingTimeForTool;
      }
      if (tool.getItem().equals(TinkerTools.sledgeHammer.asItem())) {
        this.miningTime /= 9;
      }
    }
    return this.miningTime;
  }

  public boolean isVirtualLevelCreated() {
    return this.virtualLevel != null;
  }

  public List<ItemStack> calculateResult(ServerLevel level, ItemStack tool) {
  List<ItemStack> itemStackList = new ArrayList<>();
    for (BlockState blockState : this.getOres()) {
      itemStackList.addAll(blockState.getDrops(new LootContext.Builder(level).withRandom(level.random)
              .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(BlockPos.ZERO))
              .withParameter(LootContextParams.TOOL, tool)));
//              .withOptionalParameter(LootContextParams.THIS_ENTITY, fakePlayer)
//              .withOptionalParameter(LootContextParams.BLOCK_ENTITY, WorldUtils.getTileEntity(getWorldNN(), pos))));
    }
    return itemStackList;
  }
}
