package com.dreamtea.api.json.model.inputs;

import com.dreamtea.api.json.model.processor.CraftingContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.Map;

public class Examiner extends Nature {

  private final Map<String, Nature> dataStore;

  protected Examiner(Map<String, Nature> dataStore) {
    super(checkIsRequired(dataStore));
    this.dataStore = dataStore;
  }

  private static boolean checkIsRequired(Map<String, Nature> dataStore){
    return dataStore.values().stream().anyMatch(Nature::isRequired);
  }

  public boolean validate(ItemStack stack){
    return validate(stack.getOrCreateNbt());
  }

  @Override
  protected boolean validate(NbtCompound nbt){
    if(nbt.isEmpty()){
      return !isRequired();
    }
    return dataStore.entrySet().stream().allMatch(pair -> {
      if(pair.getValue().isRequired()){
        return nbt.contains(pair.getKey()) && validate(nbt.get(pair.getKey()));
      }
      return true;
    });
  }

  @Override
  protected boolean validate(NbtElement nbt){
    return !isRequired();
  }

  public void addToContext(NbtCompound nbt, CraftingContext context) {
    dataStore.forEach((name, nature) ->{
      if(nbt.contains(name)){
        nature.addToContext(nbt.get(name), context);
      }
    });
  }

  protected void addToContext(NbtElement nbt, Map<String, String> context){

  }
}
