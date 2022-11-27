package com.dreamtea.api.json.model.processor;

import com.dreamtea.api.json.model.outputs.Field;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtElement;

import java.util.function.BiPredicate;

public class Comparison extends ToxProcessor<Boolean> {

  private final BiPredicate<Object, Object> comparator;

  public Comparison(Field<?> primary, Field<?> secondary, BiPredicate<NbtElement, NbtElement> comparator) {
    super(primary, secondary);
    this.comparator = comparator;
  }

  @Override
  public Boolean process(CraftingContext context) {
    return comparator.test(fields[0].get(context), fields[1].get(context));
  }

  public static Comparison AND(Field<?> primary, Field<?> secondary) {
    BiPredicate<NbtElement, NbtElement> comparator = (a, b) ->{
      if(a.getType() == 1 && b.getType() == 1 ){
        return ((NbtByte) a).byteValue() == 1 && ((NbtByte) b).byteValue() == 1;
      }
      return false;
    };
    return new Comparison(primary, secondary, comparator);
  }

  public static Comparison OR(Field<?> primary, Field<?> secondary) {
    BiPredicate<NbtElement, NbtElement> comparator = (a, b) ->{
      if(a.getType() == 1 && b.getType() == 1 ){
        return ((NbtByte) a).byteValue() == 1 || ((NbtByte) b).byteValue() == 1;
      }
      return false;
    };
    return new Comparison(primary, secondary, comparator);
  }
}
