package com.dreamtea.api.json.nbt;

import com.dreamtea.api.json.model.processor.ToxProcessor;
import net.minecraft.nbt.NbtCompound;

public interface NbtObject<T> {
  public NbtCompound toNbt();
  public T toModel();
}
