package com.dreamtea.api.json.nbt.processor;

import com.dreamtea.api.json.model.outputs.Field;
import com.dreamtea.api.json.model.processor.ToxProcessor;
import com.dreamtea.api.json.nbt.NbtObject;
import com.dreamtea.api.utils.Utils;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtType;
import net.minecraft.nbt.NbtTypes;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.function.Function;

public class NbtProcessor<T> implements NbtObject<ToxProcessor<T>> {
  public static String ING_KEY = "ingredients";
  public static String ACTION_KEY = "do";

  @Override
  public NbtCompound toNbt() {
    NbtCompound nbtCompound = new NbtCompound();
    nbtCompound.put(ING_KEY, inputs);
    nbtCompound.putString(ACTION_KEY, type.name().toLowerCase());
    return nbtCompound;
  }

  @Override
  public ToxProcessor<T> toModel() {
    return (ToxProcessor<T>) type.build(inputs);
  }

  public static NbtProcessor<?> fromNbt(NbtCompound compound) {
    NbtList inputs = null;
    ProcessTypes type = null;
    if(compound.contains(ING_KEY)){
      inputs = compound.getList(ING_KEY, NbtElement.COMPOUND_TYPE);
    }
    if(compound.contains(ACTION_KEY)){
      String t = compound.getString(ACTION_KEY);
      try{
        type = ProcessTypes.valueOf(t.toUpperCase());
      } catch(Exception e){
        throw new IllegalStateException(
          String.format("Processor is not of acceptable type. \n\t\tReceived: %s \n\t\tLooking for one of: %s",
            t, Utils.listEnum(ProcessTypes.values())
          ));
      }
    }
    return inputs != null && type != null ? new NbtProcessor<>(inputs, type) : null;
  }

  public enum ProcessTypes{
    AND(NbtByte.BYTE_TYPE, NbtProcessors.AND::new),
    OR(NbtByte.BYTE_TYPE),
    NOT(NbtByte.BYTE_TYPE),
    XOR(NbtByte.BYTE_TYPE),
    EQUALS(NbtByte.BYTE_TYPE),
    GREATER_THAN(NbtByte.BYTE_TYPE),
    LESS_THAN(NbtByte.BYTE_TYPE),
    GREATER_THAN_EQ(NbtByte.BYTE_TYPE),
    LESS_THAN_EQ(NbtByte.BYTE_TYPE),
    BETWEEN(NbtByte.BYTE_TYPE),
    SAME(NbtByte.BYTE_TYPE),
    UNIQUE(NbtByte.BYTE_TYPE),
    IN(NbtByte.BYTE_TYPE),
    CONTAIN(NbtByte.BYTE_TYPE),

    ADD(NbtByte.BYTE_TYPE),
    SUBTRACT(NbtByte.NUMBER_TYPE),
    MULTIPLY(NbtByte.NUMBER_TYPE),
    DIVIDE(NbtByte.NUMBER_TYPE),
    POW(NbtByte.NUMBER_TYPE),

    MAP(NbtByte.COMPOUND_TYPE),;

    final int outputType;
    private Function<Field<?>[], ToxProcessor<?>> constructor;
    ProcessTypes(int outputType, Function<Field<?>[], ToxProcessor<?>> constructor){
      this.outputType = outputType;

    }

    public ToxProcessor<?> build(NbtList list){
      return constructor.apply();
    }
  }
  private final NbtList inputs;
  private final ProcessTypes type;

  NbtProcessor(NbtList list, ProcessTypes type){
    this.inputs = list;
    this.type = type;
  }

}
