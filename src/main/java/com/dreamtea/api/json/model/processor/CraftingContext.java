package com.dreamtea.api.json.model.processor;

import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CraftingContext implements Map<String, Object> {
  private final HashMap<String, Object> variables  = new HashMap<>();

  @Override
  public int size() {
    return variables.size();
  }

  @Override
  public boolean isEmpty() {
    return variables.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    if(key instanceof String skey){
      return this.containsKey(skey);
    }
    return false;
  }

  public boolean containsKey(String key) {
     return variables.containsKey(key.toLowerCase());
  }

  /**
   * The crafting context serves as a store of variables, looking them up by value is unsupported
   */
  @Override
  public boolean containsValue(Object value) {
    throw new UnsupportedOperationException("The crafting context serves as a store of variables, looking them up by value is unsupported");
  }

  @Override
  public Object get(Object key) {
    if(key instanceof String skey){
      this.get(skey);
    }
    throw new UnsupportedOperationException("variables are stored as Strings. Pass in the name of the variable (case insensitive)");
  }

  public Object get(String key) {
      var output = variables.get(key.toLowerCase());
      if(output == null) {
        throw new IllegalStateException("No field of "+ key + "(case insensitive) has been stored");
      }
      return output;
  }

  @Nullable
  @Override
  public Object put(String key, Object value) {
    if(value == null){
      throw new IllegalArgumentException("variables can be empty but they cannot be null");
    }
    return variables.put(key.toLowerCase(), value);
  }

  @Override
  public NbtElement remove(Object key) {
    throw new UnsupportedOperationException("Cannot remove a variable");
  }

  public void putAll(@NotNull Map<? extends String, ?> m) {
    m.forEach(this::put);
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException("Cannot remove a variable");
  }

  @NotNull
  @Override
  public Set<String> keySet() {
    return variables.keySet();
  }

  @NotNull
  @Override
  public Collection<Object> values() {
    throw new UnsupportedOperationException("Cannot remove collect variables by value");
  }

  @NotNull
  @Override
  public Set<Entry<String, Object>> entrySet() {
    return variables.entrySet();
  }

}
