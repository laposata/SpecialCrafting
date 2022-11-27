package com.dreamtea.api.json.nbt.processor;

import com.dreamtea.api.json.model.outputs.Field;
import com.dreamtea.api.json.model.processor.CraftingContext;
import com.dreamtea.api.json.model.processor.ToxProcessor;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtEnd;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtType;

import java.util.Arrays;
import java.util.List;

public class NbtProcessors {

  public static class AND extends ToxProcessor<Boolean>{
    public AND(Field<?> ... fields){
      super(List.of(Boolean.class), true, fields);
    }
    @Override
    public Boolean process(CraftingContext context) {
      return Arrays.stream(fields).allMatch(e -> ((Boolean)e.get(context)));
    }
  }
  public static class OR extends ToxProcessor<Boolean>{
    public OR(Field<?> ... fields){
      super(List.of(Boolean.class), true, fields);
    }
    @Override
    public Boolean process(CraftingContext context) {
      return Arrays.stream(fields).anyMatch(e -> ((Boolean)e.get(context)));
    }
  }

  public static class NOT extends ToxProcessor<Boolean>{
    public NOT(Field<?> ... fields){
      super(List.of(Boolean.class), fields);
    }
    @Override
    public Boolean process(CraftingContext context) {
      return !(Boolean) fields[0].get(context);
    }
  }

  public static class XOR extends ToxProcessor<Boolean>{
    public XOR(Field<?> ... fields){
      super(List.of(Boolean.class, Boolean.class), fields);
    }
    @Override
    public Boolean process(CraftingContext context) {
      return fields[0].get(context) != fields[1].get(context);
    }
  }
  public static class EQUALS extends ToxProcessor<Boolean>{
    public EQUALS(Field<?> ... fields){
      super(List.of(Class.class), true, fields);
    }
    @Override
    public Boolean process(CraftingContext context) {
      return Arrays.stream(fields).distinct().count() == 1;
    }
  }

  public static class GREATER_THAN extends ToxProcessor<Boolean>{
    public GREATER_THAN(Field<?> ... fields){
      super(List.of(Float.class, Float.TYPE), fields);
    }
    @Override
    public Boolean process(CraftingContext context) {
      return (Float)fields[0].get(context) > (Float)fields[1].get(context);
    }
  }
  public static class LESS_THAN extends ToxProcessor<Boolean>{
    public LESS_THAN(Field<?> ... fields){
      super(List.of(Float.class, Float.TYPE), fields);
    }
    @Override
    public Boolean process(CraftingContext context) {
      return (Float)fields[0].get(context) < (Float)fields[1].get(context);
    }
  }
  public static class GREATER_THAN_EQ extends ToxProcessor<Boolean>{
    public GREATER_THAN_EQ(Field<?> ... fields){
      super(List.of(Float.class, Float.TYPE), fields);
    }
    @Override
    public Boolean process(CraftingContext context) {
      return (Float)fields[0].get(context) >= (Float)fields[1].get(context);
    }
  }
  public static class LESS_THAN_EQ extends ToxProcessor<Boolean>{
    public LESS_THAN_EQ(Field<?> ... fields){
      super(List.of(Float.class, Float.TYPE), fields);
    }
    @Override
    public Boolean process(CraftingContext context) {
      return (Float)fields[0].get(context) <= (Float)fields[1].get(context);
    }
  }
}
