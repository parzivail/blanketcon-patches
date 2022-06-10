package com.parzivail.blanketconpatches.mixin;

import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementTrackerMixin
{
	@Shadow
	private boolean dirty;

	@Inject(method = "sendUpdate(Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At("HEAD"), cancellable = true)
	public void sendUpdate(ServerPlayerEntity serverPlayerEntity, CallbackInfo ci)
	{
		// Do not the cat
		dirty = false;
		ci.cancel();
	}
}
