package com.dreamtea.api.recipes;

import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

import java.util.Map;

public abstract class NbtRecipeManager {
  public final Map<Identifier, Recipe<?>> recipeProvider = recipeCollection();

  protected abstract Map<Identifier, Recipe<?>> recipeCollection();
}
