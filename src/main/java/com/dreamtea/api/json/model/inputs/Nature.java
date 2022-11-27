package com.dreamtea.api.json.model.inputs;

import com.dreamtea.api.json.model.processor.CraftingContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtType;

public class Nature {

  private final NbtElement defaultValue;
  private final String name;
  private final boolean required;
  private final NbtType<?> type;

  protected Nature(boolean required){
    defaultValue = null;
    name = null;
    this.required = required;
    type = null;
  }

  protected void addToContext(NbtCompound nbt, CraftingContext context){
    if(name != null){
      if(type.equals(nbt.getNbtType())){
        context.put(name, nbt);
      } else {
        context.put(name, defaultValue);
      }
    }
  }

  protected void addToContext(NbtElement nbt, CraftingContext context){
    if(name != null){
      if(type.equals(nbt.getNbtType())){
        context.put(name, nbt);
      } else {
        context.put(name, defaultValue);
      }
    }
  }
  protected boolean validate(NbtCompound nbt){
    if(nbt.isEmpty()){
      return !required;
    }
    return type.equals(nbt.getNbtType());
  }
  protected boolean validate(NbtElement nbt){
    if(nbt == null){
      return !required;
    }
    return type.equals(nbt.getNbtType());
  }

  protected Nature(NbtElement defaultValue, String name, boolean required, NbtType<?> type){
    if(defaultValue != null){
      if(name == null){
        throw new IllegalArgumentException("This value is not being stored. It cannot have a default value");
      }
      if(required){
        throw new IllegalArgumentException("Required values cannot have a default value");
      }
      if(!type.equals(defaultValue.getNbtType())){
        throw new IllegalArgumentException("Default value must be of type");
      }
    }
    if(defaultValue == null && !required){
      throw new IllegalArgumentException("Optional values must have default");
    }
    this.defaultValue = defaultValue;
    this.required = required;
    this.name = name;
    this.type = type;
  }

  public boolean isRequired(){
    return this.required;
  }

}
