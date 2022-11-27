package com.dreamtea.api.json.model.inputs;

import com.dreamtea.api.json.nbt.outputs.NbtOutput;
import com.dreamtea.api.json.model.processor.CraftingContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.recipe.Ingredient;

import java.util.Map;

public class ToxIngredient {
  private Examiner examiner;
  private Ingredient ingredient;
  private NbtOutput transformedTo;


  public boolean test(ItemStack stack){
    return examiner.validate(stack);
  }
  public boolean matchesIngredient(ItemStack stack){
    return ingredient.test(stack);
  }

  public Map<String, NbtElement> getValues(ItemStack stack, CraftingContext context){
    examiner.addToContext(stack.getOrCreateNbt(), context);
    return context;
  }


}
