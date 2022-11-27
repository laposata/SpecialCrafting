package com.dreamtea.api.json.nbt.processor;

import com.dreamtea.api.json.model.outputs.Field;
import com.dreamtea.api.json.model.processor.ToxProcessor;
import com.dreamtea.api.json.nbt.NbtObject;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class NbtField<T extends NbtElement> implements NbtObject<Field<T>> {
  public static final String NAME_KEY = "var_name";
  public static final String VALUE_KEY = "value";
  public static final String PROCESS_KEY = "process";
  private final String name;
  private final NbtElement value;
  private final NbtProcessor<T> processor;

  public NbtField(String name, NbtElement value, NbtProcessor<T> processor) {
    this.processor = processor;
    this.name = name;
    this.value = value;
  }

  @Override
  public NbtCompound toNbt() {
    NbtCompound compound = new NbtCompound();
    if (name != null) {
      compound.putString(NAME_KEY, name);
    }
    if (value != null) {
      compound.put(VALUE_KEY, value);
    }
    if (processor != null) {
      compound.put(PROCESS_KEY, processor.toNbt());
    }
    return compound;
  }

  @Override
  public Field<T> toModel() {
    return new Field<>(name, value, processor.toModel());
  }

  public static ToxProcessor<?> fromNbt(NbtCompound compound) {

  }
}
