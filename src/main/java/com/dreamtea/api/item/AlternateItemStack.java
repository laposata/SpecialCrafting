package com.dreamtea.api.item;

import com.dreamtea.api.imixin.IAlternateState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.BooleanUtils;

import javax.annotation.Nonnull;
import java.util.Objects;

import static net.minecraft.item.ItemStack.DISPLAY_KEY;
import static net.minecraft.item.ItemStack.LORE_KEY;
import static net.minecraft.item.ItemStack.NAME_KEY;

public class AlternateItemStack {
  private final Item icon;
  private final Item functionally;
  private Text lore;
  private Text name;
  private final String identifier;
  private Boolean food;
  private boolean asFunc = false;

  private static final String ALT_KEY = "alternate_item";
  private static final String ALT_NAME_KEY = "alternate_name";
  private static final String ALT_LORE_KEY = "alternate_lore";
  private static final String ALT_ID_KEY = "alternate_identifier";
  private static final String ALT_FUNC_KEY = "alternate_func";
  private static final String ALT_SELF_KEY = "alternate_self";
  private static final String ALT_FOOD_KEY = "alternate_food";

  /**
   * Returns if a and b are of the same type.
   * If both are AlternateItemStacks then their identifiers are compared
   * If both are regular ItemStacks then {@link net.minecraft.item.ItemStack}.isOf(Item) is called instead
   * otherwise return false
   * @param a
   * @param b
   * @return
   */
  public static boolean match(ItemStack a, ItemStack b){
    AlternateItemStack alt_a = getFrom(a);
    AlternateItemStack alt_b = getFrom(b);
    if(alt_a == null && alt_b == null){
      return a.isOf(b.getItem());
    }
    if(alt_a == null || alt_b == null){
      return false;
    }
    return alt_a.getIdentifier().equals(alt_b.getIdentifier());
  }

  public static boolean hasAlternate(ItemStack stack) {
    return stack != null && stack.getSubNbt(ALT_KEY) != null;
  }

  public static AlternateItemStack getFrom(ItemStack stack) {
    if(!hasAlternate(stack)){
      return null;
    }
    NbtCompound alt = stack.getSubNbt(ALT_KEY);
    Item alternate = Registry.ITEM.get(Identifier.tryParse(alt.getString(ALT_FUNC_KEY)));
    Item icon = Registry.ITEM.get(Identifier.tryParse(alt.getString(ALT_SELF_KEY)));
    String id = alt.getString(ALT_ID_KEY);
    AlternateItemStack output = new AlternateItemStack(icon, alternate, id);
    output.asFunc(!stack.isOf(icon));
    if(alt.contains(ALT_NAME_KEY)){
      output.name(Text.Serializer.fromJson(alt.getString(ALT_NAME_KEY)));
    }
    if(alt.contains(ALT_LORE_KEY)){
      output.lore(Text.Serializer.fromJson(alt.getString(ALT_LORE_KEY)));
    }
    if(alt.contains(ALT_FOOD_KEY)){
      output.lore(Text.Serializer.fromJson(alt.getString(ALT_FOOD_KEY)));
    }
    if(((Object)stack) instanceof IAlternateState ias){
      ias.storeAlternate(output);
    }
    return output;
  }


  public static String getCustomName(ItemStack stack) {
    AlternateItemStack alt = getFrom(stack);
    if(alt == null){
      return getCustomNameOfStack(stack);
    }
    return alt.customName(stack);
  }

  public AlternateItemStack(Item icon, Item functionally, String id) {
    this.icon = icon;
    this.functionally = functionally;
    this.identifier = id;
  }

  public AlternateItemStack(Item icon, String id) {
    this.icon = icon;
    this.functionally = Items.POISONOUS_POTATO;
    this.identifier = id;
    this.food = false;
  }

  public ItemStack create() {
    ItemStack stack = new ItemStack(asFunc? functionally: icon);
    NbtCompound data = new NbtCompound();
    if(name != null) {
      data.putString(ALT_NAME_KEY, Text.Serializer.toJson(name));
      stack.setCustomName(name);
      NbtCompound display = stack.getOrCreateSubNbt(DISPLAY_KEY);
      display.putString(NAME_KEY, Text.Serializer.toJson(name));
      stack.getOrCreateNbt().put(DISPLAY_KEY, display);
    }

    if(lore != null) {
      data.putString(ALT_LORE_KEY, Text.Serializer.toJson(lore));
      NbtCompound display = stack.getOrCreateSubNbt(DISPLAY_KEY);
      NbtList loreList = new NbtList();
      loreList.add(NbtString.of(Text.Serializer.toJson(lore)));
      display.put(LORE_KEY, loreList);
      stack.getOrCreateNbt().put(DISPLAY_KEY, display);
    }

    if(food != null) {
      data.putBoolean(ALT_FOOD_KEY, BooleanUtils.isTrue(food));
    }
    data.putString(ALT_ID_KEY, identifier);
    Identifier itemId = Registry.ITEM.getId(functionally);
    Identifier iconId = Registry.ITEM.getId(icon);
    data.putString(ALT_FUNC_KEY, itemId == null ? "minecraft:air" : itemId.toString());
    data.putString(ALT_SELF_KEY, iconId == null ? "minecraft:air" : iconId.toString());
    stack.getOrCreateNbt().put(ALT_KEY, data);
    if((Object)stack instanceof IAlternateState ias){
      ias.storeAlternate(this);
    }
    return stack;
  }

  public AlternateItemStack food(Boolean food){
    this.food = food;
    return this;
  }

  public AlternateItemStack name(Text name){
    this.name = name;
    return this;
  }

  public AlternateItemStack lore(Text lore){
    this.lore = lore;
    return this;
  }

  public AlternateItemStack asFunc(boolean asFunc){
    this.asFunc = asFunc;
    return this;
  }

  public Text getName(){
    return name;
  }

  public Boolean getFood(){
    return food;
  }

  @Nonnull
  public String getIdentifier(){
    return identifier;
  }

  public Text getLore(){
    return lore;
  }

  @Nonnull
  public Item getAlternate(){
    return this.functionally;
  }

  private static String getCustomNameOfStack(ItemStack stack){
    NbtCompound nbtCompound = stack.getOrCreateSubNbt(DISPLAY_KEY);
    return nbtCompound.getString(NAME_KEY);
  }


  public String customName(ItemStack itemStack) {
    String stackName = getCustomNameOfStack(itemStack);
    if (name != null) {
      if (Objects.equals(stackName, Text.Serializer.toJson(name))) {
        return null;
      }
    }
    return stackName;
  }
}
