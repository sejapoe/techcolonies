package com.sejapoe.techcolonies.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sejapoe.techcolonies.TechColonies;
import com.sejapoe.techcolonies.entity.ai.job.miner.Mine;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class MineMapScreen extends Screen {
  private static final ResourceLocation LOCATION = new ResourceLocation(TechColonies.MOD_ID, "textures/gui/mine_map.png");
  private final Mine mine;

  public final Level level;
  private final Player player;
  private final ItemStack stack;
  private ForgeSlider miningLevel;
  private ForgeSlider miningRadius;
  protected int imageWidth = 250;
  protected int imageHeight = 166;

  public MineMapScreen(Level level, Player player, ItemStack stack) {
    super(new TranslatableComponent("gui.techcolonies.mine_map"));
    this.level = level;
    this.player = player;
    this.stack = stack;
    this.mine = Mine.loadFromNBT(stack.getOrCreateTag().getCompound("Mine"));
  }

  @Override
  protected void init() {
    int i = (this.width - this.imageWidth) / 2;
    int j = (this.height - this.imageHeight) / 2;
    this.miningLevel = new ForgeSlider(i + 14, j + 34, 222, 20, new TextComponent("Mining level: "), new TextComponent(""), -64, 320, this.mine.getMiningLevel(), true);
    this.miningRadius = new ForgeSlider(i + 14, j + 74, 222, 20, new TextComponent("Radius: "), new TextComponent(""), 16, 128, this.mine.getMiningRadius(), true);
    this.addWidget(this.miningLevel);
    this.addWidget(this.miningRadius);
  }

  @Override
  public void onClose() {
    super.onClose();
    this.updateMine();
    this.stack.getOrCreateTag().put("Mine", Mine.saveToNBT(mine));
  }

  private void updateMine() {
    this.mine.setMiningLevel(this.miningLevel.getValueInt());
    this.mine.setMiningRadius(this.miningRadius.getValueInt());
  }

  @Override
  public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
    this.renderBg(pPoseStack, pPartialTick);
    super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    this.renderFg(pPoseStack,  pMouseX, pMouseY, pPartialTick);
  }

  private void renderFg(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
    this.miningLevel.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    this.miningRadius.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
  }

  private void renderBg(PoseStack pPoseStack, float pPartialTick) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, LOCATION);
    int i = (this.width - this.imageWidth) / 2;
    int j = (this.height - this.imageHeight) / 2;
    this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
  }
}
