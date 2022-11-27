package com.dreamtea.api.mixins;

import com.dreamtea.api.imixin.IAlternateState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Ingredient.class)
public class IngredientsMixin {

  @Redirect(method = "test(Lnet/minecraft/item/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
  public boolean test(ItemStack instance, Item item, ItemStack itemStack) {
   if((Object)itemStack instanceof IAlternateState ias){
     if(ias.getAlternate() != null){
       return instance.isOf(ias.getAlternate().getAlternate());
     }
   }
   return instance.isOf(item);
  }
}
