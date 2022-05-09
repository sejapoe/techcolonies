/*
MIT License

Copyright (c) 2019 simibubi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.sejapoe.techcolonies.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sejapoe.techcolonies.TechColonies;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class FluidHelper {
  public static JsonElement serializeFluidStack(FluidStack stack) {
    JsonObject json = new JsonObject();
    json.addProperty("fluid", Objects.requireNonNull(stack.getFluid()
                    .getRegistryName())
            .toString());
    json.addProperty("amount", stack.getAmount());
    if (stack.hasTag())
      json.addProperty("nbt", stack.getTag()
              .toString());
    return json;
  }

  public static FluidStack deserializeFluidStack(JsonObject json) {
    ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json, "fluid"));
    Fluid fluid = ForgeRegistries.FLUIDS.getValue(id);
    if (fluid == null)
      throw new JsonSyntaxException("Unknown fluid '" + id + "'");
    int amount = GsonHelper.getAsInt(json, "amount");
    FluidStack stack = new FluidStack(fluid, amount);

    if (!json.has("nbt"))
      return stack;

    try {
      JsonElement element = json.get("nbt");
      stack.setTag(TagParser.parseTag(
              element.isJsonObject() ? TechColonies.GSON.toJson(element) : GsonHelper.convertToString(element, "nbt")));

    } catch (CommandSyntaxException e) {
      e.printStackTrace();
    }

    return stack;
  }
}
