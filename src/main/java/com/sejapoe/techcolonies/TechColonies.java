package com.sejapoe.techcolonies;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.sejapoe.techcolonies.core.FluidDeferredRegister;
import com.sejapoe.techcolonies.registry.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TechColonies.MOD_ID)
public class TechColonies {
  public static final String MOD_ID = "techcolonies";
  // Directly reference a slf4j logger
  public static final Logger LOGGER = LogUtils.getLogger();
  public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
          .disableHtmlEscaping()
          .create();

  public TechColonies() {
    GeckoLib.initialize();

    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

    // Register fluids
    ModFluids.register(bus);
    // Register blocks
    ModBlocks.register(bus);
    // Register items
    ModItems.register(bus);
    // Register blockentities
    ModBlockEntities.register(bus);
    // Register entities
    ModEntities.register(bus);
    /// Register capabilities
    bus.addListener(ModCapabilities::register);
    // Register recipes
    bus.addGenericListener(RecipeSerializer.class, ModRecipeTypes::register);

    // Register the setup method for modloading
    bus.addListener(this::setup);
    // Register client setup
    bus.addListener(this::clientSetup);
    // Register the enqueueIMC method for modloading
    bus.addListener(this::enqueueIMC);
    // Register the processIMC method for modloading
    bus.addListener(this::processIMC);

    // Register ourselves for server and other game events we are interested in
    MinecraftForge.EVENT_BUS.register(this);
  }

  private void setup(final FMLCommonSetupEvent event) {
    // some preinit code
    LOGGER.info("HELLO FROM PREINIT");
    LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
  }
  @OnlyIn(Dist.CLIENT)
  private void clientSetup(final FMLCommonSetupEvent event) {
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.SMELTERY_BLOCK.get(), RenderType.cutout());
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.ITEM_INTERFACE_BLOCK.get(), RenderType.cutout());
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_INTERFACE_BLOCK.get(), RenderType.cutout());
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.PORTAL_CONTROLLER_BLOCK.get(), RenderType.cutout());
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.PORTAL_BLOCK.get(), RenderType.cutout());
    for (FluidDeferredRegister.FluidRegistryObject<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, LiquidBlock, BucketItem> fluidRegistryObject : ModFluids.FLUID_REGISTER.getEntries()) {
      ItemBlockRenderTypes.setRenderLayer(fluidRegistryObject.getBlock(), RenderType.translucent());
      ItemBlockRenderTypes.setRenderLayer(fluidRegistryObject.getStillFluid(), RenderType.translucent());
      ItemBlockRenderTypes.setRenderLayer(fluidRegistryObject.getFlowingFluid(), RenderType.translucent());
    }
  }

  private void enqueueIMC(final InterModEnqueueEvent event) {
    // Some example code to dispatch IMC to another mod
    InterModComms.sendTo("techcolonies", "helloworld", () -> {
      LOGGER.info("Hello world from the MDK");
      return "Hello world";
    });
  }

  private void processIMC(final InterModProcessEvent event) {
    // Some example code to receive and process InterModComms from other mods
    LOGGER.info("Got IMC {}", event.getIMCStream().
            map(m -> m.messageSupplier().get()).
            collect(Collectors.toList()));
  }

  // You can use SubscribeEvent and let the Event Bus discover methods to call
  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {
    // Do something when the server starts
    LOGGER.info("HELLO from server starting");
  }
}
