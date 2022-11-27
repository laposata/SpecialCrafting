package com.dreamtea.api.json.model;

import com.dreamtea.api.json.model.inputs.ToxIngredient;
import com.dreamtea.api.json.model.outputs.ToxOutput;
import com.dreamtea.api.json.model.processor.Comparison;
import com.dreamtea.api.json.model.processor.CraftingContext;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;

import java.util.List;

public abstract class ToxRecipe<C extends Inventory> implements Recipe<C> {
  protected RecipeType<?> recipeType;
  protected List<ToxIngredient> ingredients;
  protected List<ToxOutput> outputs;
  protected Comparison validator;

  public boolean consume(List<ItemStack> itemsBeingConsumed, CraftingContext context){
    if(ingredients.size() != itemsBeingConsumed.size()) return false;
    for(int i = 0; i < ingredients.size(); i ++){
      if(!ingredients.get(i).test(itemsBeingConsumed.get(i))){
        return false;
      }
    }
    for(int i = 0; i < ingredients.size(); i ++){
      ingredients.get(i).getValues(itemsBeingConsumed.get(i),context);
    }
    return validator.process(context);
  }

}
