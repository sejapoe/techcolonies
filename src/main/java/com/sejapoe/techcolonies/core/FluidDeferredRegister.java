package com.sejapoe.techcolonies.core;

import com.sejapoe.techcolonies.registry.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;
import net.minecraftforge.fluids.ForgeFlowingFluid.Source;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class FluidDeferredRegister {
  private static final ResourceLocation OVERLAY = new ResourceLocation("block/water_overlay");
  private static final ResourceLocation LIQUID = new ResourceLocation("block/lava_still");
  private static final ResourceLocation LIQUID_FLOW = new ResourceLocation("block/lava_flow");

  public static FluidAttributes.Builder getBaseBuilder() {
    return FluidAttributes.builder(LIQUID, LIQUID_FLOW).sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY).overlay(OVERLAY);
  }

  public static Item.Properties getBaseBucketProperties() {
    return ModItems.getBaseProperties().stacksTo(1).craftRemainder(Items.BUCKET);
  }

  private final DeferredRegister<Fluid> fluidDeferredRegister;
  private final DeferredRegister<Block> blockDeferredRegister;
  private final DeferredRegister<Item> itemDeferredRegister;

  public FluidDeferredRegister(String modID) {
    fluidDeferredRegister = DeferredRegister.create(ForgeRegistries.FLUIDS, modID);
    blockDeferredRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, modID);
    itemDeferredRegister = DeferredRegister.create(ForgeRegistries.ITEMS, modID);
  }

  public FluidRegistryObject<Source, Flowing, LiquidBlock, BucketItem> registerMoltenMetal(IMetalConstant constant) {
    int density = Math.round(constant.getLiquidDensity());
    return register("molten_" + constant.getName(), fluidAttributes -> fluidAttributes
              .color(constant.getColor())
              .temperature(Math.round(constant.getLiquidTemperature()))
              .density(density)
              .viscosity(density)
              .luminosity(Math.round(constant.getLiquidLuminosity())),
            properties -> properties.tickRate(30).slopeFindDistance(4).levelDecreasePerBlock(2));
  }

  public FluidRegistryObject<Source, Flowing, LiquidBlock, BucketItem> register(String name, UnaryOperator<FluidAttributes.Builder> builderUnaryOperator, UnaryOperator<ForgeFlowingFluid.Properties> propertiesUnaryOperator) {
    return register(name, builderUnaryOperator.apply(getBaseBuilder()), propertiesUnaryOperator);
  }

  public FluidRegistryObject<Source, Flowing, LiquidBlock, BucketItem> register(String name, FluidAttributes.Builder builder, UnaryOperator<ForgeFlowingFluid.Properties> propertiesUnaryOperator) {
    return register(name, builder, BucketItem::new, propertiesUnaryOperator);
  }

  public FluidRegistryObject<Source, Flowing, LiquidBlock, BucketItem> register(String name, FluidAttributes.Builder builder, BucketCreator bucketCreator) {
    return register(name, builder, bucketCreator, properties -> properties);
  }

  public FluidRegistryObject<Source, Flowing, LiquidBlock, BucketItem> register(String name, FluidAttributes.Builder builder, BucketCreator bucketCreator, UnaryOperator<ForgeFlowingFluid.Properties> propertiesUnaryOperator) {
    String flowingName = name + "_flowing";
    String bucketName = name + "_bucket";
    FluidRegistryObject<Source, Flowing, LiquidBlock, BucketItem> fluidRegistryObject = new FluidRegistryObject();
    ForgeFlowingFluid.Properties properties = propertiesUnaryOperator.apply(new ForgeFlowingFluid.Properties(fluidRegistryObject::getStillFluid, fluidRegistryObject::getFlowingFluid, builder)
            .bucket(fluidRegistryObject::getBucket).block(fluidRegistryObject::getBlock));
    fluidRegistryObject.setStillRegistryObject(fluidDeferredRegister.register(name, () -> new Source(properties)));
    fluidRegistryObject.setFlowingRegistryObject(fluidDeferredRegister.register(flowingName, () -> new Flowing(properties)));
    fluidRegistryObject.setBucketRegistryObject(itemDeferredRegister.register(bucketName, () -> bucketCreator.create(fluidRegistryObject::getStillFluid, getBaseBucketProperties())));
    fluidRegistryObject.setBlockRegistryObject(blockDeferredRegister.register(name, () -> new LiquidBlock(fluidRegistryObject::getStillFluid,
            BlockBehaviour.Properties.copy(Blocks.LAVA))));
    return fluidRegistryObject;
  }

  public void register(IEventBus bus) {
    fluidDeferredRegister.register(bus);
    blockDeferredRegister.register(bus);
    itemDeferredRegister.register(bus);
  }
  @FunctionalInterface
  public interface BucketCreator {
    BucketItem create(Supplier<? extends Fluid> suppplier, Item.Properties properties);
  }
  public class FluidRegistryObject<STILL extends Fluid, FLOWING extends Fluid, BLOCK extends LiquidBlock, BUCKET extends BucketItem> {
    private RegistryObject<STILL> stillRegistryObject;
    private RegistryObject<FLOWING> flowingRegistryObject;
    private RegistryObject<BLOCK> blockRegistryObject;
    private RegistryObject<BUCKET> bucketRegistryObject;

    public STILL getStillFluid() {
      return stillRegistryObject.get();
    }

    public FLOWING getFlowingFluid() {
      return flowingRegistryObject.get();
    }

    public BLOCK getBlock() {
      return blockRegistryObject.get();
    }

    public BUCKET getBucket() {
      return bucketRegistryObject.get();
    }


    public void setStillRegistryObject(RegistryObject<STILL> stillRegistryObject) {
      this.stillRegistryObject = stillRegistryObject;
    }

    public void setFlowingRegistryObject(RegistryObject<FLOWING> flowingRegistryObject) {
      this.flowingRegistryObject = flowingRegistryObject;
    }

    public void setBlockRegistryObject(RegistryObject<BLOCK> blockRegistryObject) {
      this.blockRegistryObject = blockRegistryObject;
    }

    public void setBucketRegistryObject(RegistryObject<BUCKET> bucketRegistryObject) {
      this.bucketRegistryObject = bucketRegistryObject;
    }
  }
}
