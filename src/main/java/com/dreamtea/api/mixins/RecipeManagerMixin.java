package com.dreamtea.api.mixins;

import com.dreamtea.api.imixin.IRecipeManager;
import com.google.gson.JsonObject;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin implements IRecipeManager {
  @Redirect(
    at = @At(
      value ="INVOKE",
      target = "Lnet/minecraft/recipe/RecipeManager;deserialize(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/Recipe;"
    ),
    method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V"
  )
  public Recipe<?> addRecipes(Identifier id, JsonObject json){
    Recipe<?> special;
    if(getRecipes() != null && (special = getRecipes().recipeProvider.get(id)) != null){
      return special;
    }
    return RecipeManager.deserialize(id, json);
  }
}
