package com.sejapoe.techcolonies.core.helper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sejapoe.techcolonies.TechColonies;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ItemHelper {
  public static JsonElement serialize(ItemStack stack) {
    JsonObject json = new JsonObject();
    ResourceLocation resourceLocation = stack.getItem().getRegistryName();
    json.addProperty("item", Objects.requireNonNull(resourceLocation).toString());
    int count = stack.getCount();
    if (count != 1)
      json.addProperty("count", count);
    if (stack.hasTag())
      json.add("nbt", JsonParser.parseString(Objects.requireNonNull(stack.getTag()).toString()));
    return json;
  }

  public static ItemStack deserialize(JsonElement je) {
    if (!je.isJsonObject())
      throw new JsonSyntaxException("ProcessingOutput must be a json object");

    JsonObject json = je.getAsJsonObject();
    String itemId = GsonHelper.getAsString(json, "item");
    int count = GsonHelper.getAsInt(json, "count", 1);
    ItemStack itemstack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)), count);

    if (GsonHelper.isValidNode(json, "nbt")) {
      try {
        JsonElement element = json.get("nbt");
        itemstack.setTag(TagParser.parseTag(
                element.isJsonObject() ? TechColonies.GSON.toJson(element) : GsonHelper.convertToString(element, "nbt")));
      } catch (CommandSyntaxException e) {
        e.printStackTrace();
      }
    }

    return itemstack;
  }
}
