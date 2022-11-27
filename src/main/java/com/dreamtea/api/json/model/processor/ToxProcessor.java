package com.dreamtea.api.json.model.processor;

import com.dreamtea.api.json.model.outputs.Field;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtType;

import java.util.List;

public abstract class ToxProcessor<T> {
  protected final Field<?>[] fields;
  private final List<Class<?>> types;
  private final boolean varArgs;
  public ToxProcessor(List<Class<?>> types, Field<?>... fields)
  {
    this.fields = fields;
    this.types = types;
    this.varArgs = false;
  }
  public ToxProcessor(List<Class<?>> types, boolean varArgs, Field<?>... fields)
  {
    this.fields = fields;
    this.types = types;
    this.varArgs = varArgs;
  }
  public abstract T process(CraftingContext context);
  public List<Class<?>> inputs(){
    return types;
  }

  public boolean varArgs(){
    return varArgs;
  }
}

