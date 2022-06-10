package com.parzivail.blanketconpatches;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BlanketconPatches implements ModInitializer
{
	public static final Logger LOGGER = LoggerFactory.getLogger("BlanketCon Patches");

	public static boolean SHOULD_PATCH_KEYS = false;

	@Override
	public void onInitialize()
	{
		BlanketconPatches.LOGGER.info("BlanketconPatches ready");
	}
}
