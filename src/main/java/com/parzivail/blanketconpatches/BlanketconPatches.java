package com.parzivail.blanketconpatches;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BlanketconPatches implements ModInitializer
{
	public static final Logger LOGGER = LoggerFactory.getLogger("BlanketCon Patches");

	public static boolean SHOULD_PATCH_KEYS = false;

	public static final GameRules.Key<GameRules.BooleanRule> BLOCK_MAPS = GameRuleRegistry.register("blockMapUpdates", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

	@Override
	public void onInitialize()
	{
		BlanketconPatches.LOGGER.info("BlanketconPatches ready");
	}
}
