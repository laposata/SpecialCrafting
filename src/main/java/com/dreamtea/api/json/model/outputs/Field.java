package com.dreamtea.api.json.model.outputs;

import com.dreamtea.api.json.model.processor.CraftingContext;
import com.dreamtea.api.json.model.processor.ToxProcessor;
import net.minecraft.nbt.NbtElement;

public record Field<T>(String name, T value, ToxProcessor<T> process) {

  public T get(CraftingContext context){
    if(process != null){
      return process.process(context);
    }
    if(name != null && context.containsKey(name)){
      return (T) context.get(name);
    }
    return value;
  }

}
