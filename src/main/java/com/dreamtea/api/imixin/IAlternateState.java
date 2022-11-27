package com.dreamtea.api.imixin;

import com.dreamtea.api.item.AlternateItemStack;

public interface IAlternateState {
  public void storeAlternate(AlternateItemStack alt);
  public AlternateItemStack getAlternate();
}
