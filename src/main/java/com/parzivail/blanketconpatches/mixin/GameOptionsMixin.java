package com.parzivail.blanketconpatches.mixin;

import com.parzivail.blanketconpatches.BlanketconPatches;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(GameOptions.class)
public class GameOptionsMixin
{
	@Final
	@Shadow
	private File optionsFile;

	@Inject(method = "load()V", at = @At("HEAD"))
	public void load(CallbackInfo ci)
	{
		BlanketconPatches.SHOULD_PATCH_KEYS = !optionsFile.exists();
	}

	@Inject(method = "write()V", at = @At("HEAD"))
	public void write(CallbackInfo ci)
	{
		if (!BlanketconPatches.SHOULD_PATCH_KEYS)
		{
			BlanketconPatches.LOGGER.info("Detected existing options.txt, not modifying keybinds");
			return;
		}

		// Options file doesn't exist, setting sensible defaults
		BlanketconPatches.LOGGER.info("Writing new options.txt, setting sensible keybind defaults");

		var mc = MinecraftClient.getInstance();
		var keybindingsByTranslationKey = Arrays
				.stream(mc.options.allKeys)
				.collect(Collectors.toMap(
						KeyBinding::getTranslationKey,
						keyBinding -> keyBinding
				));

		final int NO_KEY = -1;

		// Give us a few more keys to work with
		setKeybind(keybindingsByTranslationKey, "key.saveToolbarActivator", NO_KEY);
		setKeybind(keybindingsByTranslationKey, "key.loadToolbarActivator", NO_KEY);
		setKeybind(keybindingsByTranslationKey, "key.socialInteractions", NO_KEY);

		// Sorry Iris, no shaderpacks are shipped by default so users with shaders can set these up
		setKeybind(keybindingsByTranslationKey, "iris.keybind.reload", NO_KEY);
		setKeybind(keybindingsByTranslationKey, "iris.keybind.toggleShaders", NO_KEY);
		setKeybind(keybindingsByTranslationKey, "iris.keybind.shaderPackSelection", NO_KEY);

		// Set mod-specific keys
		setKeybind(keybindingsByTranslationKey, "lambdamap.keybind.map", InputUtil.GLFW_KEY_J);
		setKeybind(keybindingsByTranslationKey, "key.voice_chat", InputUtil.GLFW_KEY_Y);
		setKeybind(keybindingsByTranslationKey, "key.campanion.open_backpack", InputUtil.GLFW_KEY_SEMICOLON);
		setKeybind(keybindingsByTranslationKey, "key.haema.expand_mist_form", InputUtil.GLFW_KEY_P);
		setKeybind(keybindingsByTranslationKey, "key.haema.dash", InputUtil.GLFW_KEY_COMMA);
		setKeybind(keybindingsByTranslationKey, "key.halfdoors.door_flip", InputUtil.GLFW_KEY_PERIOD);
		setKeybind(keybindingsByTranslationKey, "key.bewitchment.transformation_ability", InputUtil.GLFW_KEY_APOSTROPHE);
		setKeybind(keybindingsByTranslationKey, "bingbingwahoo.key.throw_cap", InputUtil.GLFW_KEY_LEFT_BRACKET);
		setKeybind(keybindingsByTranslationKey, "key.portalcubed.grab", InputUtil.GLFW_KEY_RIGHT_BRACKET);
		setKeybind(keybindingsByTranslationKey, "key.arcanus.openSpellInv", InputUtil.GLFW_KEY_BACKSLASH);
		setKeybind(keybindingsByTranslationKey, "key.pswg.species_select", InputUtil.GLFW_KEY_I);
	}

	private void setKeybind(Map<String, KeyBinding> keyBindingMap, String target, int newKeyCode)
	{
		var key = keyBindingMap.get(target);
		if (key == null)
		{
			BlanketconPatches.LOGGER.warn("Failed to rewrite unknown keybinding " + target);
			return;
		}

		var oldKey = key.getDefaultKey();
		var newKey = InputUtil.Type.KEYSYM.createFromCode(newKeyCode);

		BlanketconPatches.LOGGER.info(String.format(
				"Rewriting keybind %s: %s -> %s",
				target,
				oldKey.getTranslationKey(),
				newKey.getTranslationKey()
		));
		key.setBoundKey(newKey);
	}
}
