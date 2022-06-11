package com.parzivail.blanketconpatches.mixin;

import com.parzivail.blanketconpatches.BlanketconPatches;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapState.class)
public class MapStateMixin {
        @Inject(method = "update", at = @At("HEAD"), cancellable = true)
        private void dontUpdateMaps(PlayerEntity playerEntity, ItemStack itemStack, CallbackInfo ci) {
                if (playerEntity.world.getGameRules().getBoolean(BlanketconPatches.BLOCK_MAPS)) {
                        ci.cancel();
                }
        }
}
