package com.parzivail.blanketconpatches.mixin;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Inject(method = "getFirstMatch", at = @At("HEAD"), cancellable = true)
    public <C extends Inventory, T extends Recipe<C>> void onGetFirstMatch(RecipeType<T> type, C inventory, World world, CallbackInfoReturnable<Optional<T>> info) {
        if (type == RecipeType.CRAFTING && inventory.isEmpty()) {
            info.setReturnValue(Optional.empty());
        }
    }
}