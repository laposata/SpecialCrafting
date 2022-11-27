package com.dreamtea.api.recipes;

import com.dreamtea.api.item.AlternateItemStack;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class AbstractCustomShapedCrafting extends ShapedRecipe {
  public static final String ALT_ID = "fragile_wing";
  public final ItemStack output;
  public final String group;
  public int width = 3;
  public int height = 3;
  public final List<ItemStack> recipe;

  protected AbstractCustomShapedCrafting(Identifier id, List<ItemStack> recipe, ItemStack output, int width, int height, String group) {
    super(id, group, width, height, ingredientsFromItemStacks(recipe), output);
    this.width = width;
    this.height = height;
    this.recipe = recipe;
    this.output = output;
    this.group = group;
  }


  @Override
  public boolean isIgnoredInRecipeBook() {
    return false;
  }

  @Override
  public ItemStack craft(CraftingInventory inventory) {
    return getOutput().copy();
  }

  @Override
  public boolean fits(int width, int height) {
    return width == this.width && height == this.height;
  }
  public void export(@NonNull Identifier id, @NonNull Consumer<RecipeJsonProvider> exporter){
    ShapedRecipeJsonBuilder has_item = ShapedRecipeJsonBuilder.create(output.getItem())
      .group(group)
      .criterion("has_item", RecipeProvider.conditionsFromItem(output.getItem()));
    recipeToJsonPattern(has_item, recipe);
    has_item.offerTo(exporter, id);
  }

  public void create(Identifier id, Map<Identifier, Recipe<?>> specials){
    specials.put(id, this);
  }

  public ShapedRecipeJsonBuilder recipeToJsonPattern(ShapedRecipeJsonBuilder builder, List<ItemStack> recipe){
    DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(9, Ingredient.EMPTY);
    Map<Item, String> items = new HashMap<>();
    items.put(null, " ");
    items.put(Items.AIR, " ");
    StringBuilder pattern = new StringBuilder();
    recipe.forEach(ing -> {
      var item = ing.getItem();
      if(!items.containsKey(item)){
        items.put(item, Integer.toString(items.size()));
        builder.input(items.get(item).charAt(0), item);

      }
      pattern.append(items.get(item));
    });
    pattern.append("          ");
    String patternString = pattern.toString();
    for(int i = 0; i < width * height; i += width){
      builder.pattern(patternString.substring(i, i + width));
    }
    return builder;
  }
  @Override
  public RecipeSerializer<?> getSerializer() {
    return RecipeSerializer.SHAPED;
  }

  @Override
  public boolean matches(CraftingInventory inventory, World world) {
    for (int i = 0; i < inventory.getWidth(); ++i) {
      for (int j = 0; j < inventory.getHeight(); ++j) {
        if (!checkAtSlot(i, j, inventory)){
          return false;
        }
      }
    }
    return true;
  }

  protected boolean checkAtSlot(int i, int j, CraftingInventory inventory){
    int ind = i + j * inventory.getWidth();
    if(ind < 0 || ind >= recipe.size()){
      return true;
    }
    ItemStack ingredient = recipe.get(ind);
    ItemStack inv = inventory.getStack(ind);
    return AlternateItemStack.match(ingredient, inv);
  }

  public static DefaultedList<Ingredient> ingredientsFromItemStacks(List<ItemStack> recipe){
    DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(9, Ingredient.EMPTY);
    for (int k = 0; k < defaultedList.size() && k < recipe.size(); ++k) {
      defaultedList.set(k, Ingredient.ofStacks(recipe.get(k)));
    }
    return defaultedList;
  }

  public static List<ItemStack> itemsToItemStacks(List<Item> items){
    return items.stream().map(Item::getDefaultStack).collect(Collectors.toList());
  }

}
