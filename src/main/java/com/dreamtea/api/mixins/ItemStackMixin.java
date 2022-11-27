package com.dreamtea.api.mixins;

import com.dreamtea.api.imixin.IAlternateState;
import com.dreamtea.api.item.AlternateItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin implements IAlternateState {
  AlternateItemStack alternateItemStack;
  int hasAlternate = 0;

  @Inject(method = "isFood", at = @At("RETURN"), cancellable = true)
  public void isFood(CallbackInfoReturnable<Boolean> cir){
    if(getAlternate() != null && alternateItemStack.getFood() != null){
      cir.setReturnValue(alternateItemStack.getFood());
    }
  }

  @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
  public Item use(ItemStack instance){
    if(getAlternate() != null){
      return alternateItemStack.getAlternate();
    }
    return instance.getItem();
  }

  @Redirect(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
  public Item useOnBlock(ItemStack instance){
    if(getAlternate() != null){
      return alternateItemStack.getAlternate();
    }
    return instance.getItem();
  }

  @Redirect(method = "useOnEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
  public Item useOnEntity(ItemStack instance){
    if(getAlternate() != null){
      return alternateItemStack.getAlternate();
    }
    return instance.getItem();
  }
  @Override
  public void storeAlternate(AlternateItemStack alt) {
    this.alternateItemStack = alt;
    hasAlternate = 1;
  }

  @Override
  public AlternateItemStack getAlternate() {
    if(hasAlternate == 0){
      if(AlternateItemStack.hasAlternate((ItemStack)(Object)this)){
        storeAlternate(AlternateItemStack.getFrom((ItemStack)(Object)this));
      } else {
        hasAlternate = -1;
      }
    }
    return alternateItemStack;
  }
}
