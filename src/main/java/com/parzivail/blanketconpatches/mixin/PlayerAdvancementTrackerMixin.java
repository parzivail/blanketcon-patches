package com.parzivail.blanketconpatches.mixin;

import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.network.packet.s2c.play.AdvancementUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementTrackerMixin
{
	@Shadow
	private boolean dirty;
	
	@Unique private boolean sentFunnyPackets = false;

	@Inject(method = "sendUpdate(Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At("HEAD"), cancellable = true)
	public void sendUpdate(ServerPlayerEntity serverPlayerEntity, CallbackInfo ci)
	{
		// Do not the cat
		dirty = false;
		
		// At least once, send two advancement packets anyway, because Patchouli loads books clientside upon receiving the second advancement update packet.
		// The packets don't actually have to contain any advancements.
		// See https://github.com/parzivail/blanketcon-patches/issues/1
		if (!sentFunnyPackets)
		{
			serverPlayerEntity.networkHandler.sendPacket(new AdvancementUpdateS2CPacket(false, Collections.emptyList(), Collections.emptySet(), Collections.emptyMap()));
			serverPlayerEntity.networkHandler.sendPacket(new AdvancementUpdateS2CPacket(false, Collections.emptyList(), Collections.emptySet(), Collections.emptyMap()));
			sentFunnyPackets = true;
		}
		
		ci.cancel();
	}
}
