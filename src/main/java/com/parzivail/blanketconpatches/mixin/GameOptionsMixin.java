package com.parzivail.blanketconpatches.mixin;

import com.parzivail.blanketconpatches.BlanketconPatches;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(GameOptions.class)
public class GameOptionsMixin
{
	@Final
	@Shadow
	private File optionsFile;

	@Final
	@Shadow
	public KeyBinding[] allKeys;

	@Unique
	private HashMap<String, Integer> _queuedChanges;

	@Inject(method = "load()V", at = @At("HEAD"))
	public void load(CallbackInfo ci)
	{
		if (optionsFile.exists())
		{
			BlanketconPatches.LOGGER.info("Detected existing options.txt, not modifying keybinds");
			return;
		}

		_queuedChanges = new HashMap<>();

		final int NO_KEY = -1;

		// Give us a few more keys to work with
		_queuedChanges.put("key.saveToolbarActivator", NO_KEY);
		_queuedChanges.put("key.loadToolbarActivator", NO_KEY);
		_queuedChanges.put("key.socialInteractions", NO_KEY);

		// Sorry Iris, no shaderpacks are shipped by default so users with shaders can set these up
		_queuedChanges.put("iris.keybind.reload", NO_KEY);
		_queuedChanges.put("iris.keybind.toggleShaders", NO_KEY);
		_queuedChanges.put("iris.keybind.shaderPackSelection", NO_KEY);

		// Set mod-specific keys
		_queuedChanges.put("lambdamap.keybind.map", InputUtil.GLFW_KEY_J);
		_queuedChanges.put("key.voice_chat", InputUtil.GLFW_KEY_Y);
		_queuedChanges.put("key.campanion.open_backpack", InputUtil.GLFW_KEY_SEMICOLON);
		_queuedChanges.put("key.haema.expand_mist_form", InputUtil.GLFW_KEY_P);
		_queuedChanges.put("key.haema.dash", InputUtil.GLFW_KEY_COMMA);
		_queuedChanges.put("key.halfdoors.door_flip", InputUtil.GLFW_KEY_PERIOD);
		_queuedChanges.put("key.bewitchment.transformation_ability", InputUtil.GLFW_KEY_APOSTROPHE);
		_queuedChanges.put("bingbingwahoo.key.throw_cap", InputUtil.GLFW_KEY_LEFT_BRACKET);
		_queuedChanges.put("key.portalcubed.grab", InputUtil.GLFW_KEY_RIGHT_BRACKET);
		_queuedChanges.put("key.arcanus.openSpellInv", InputUtil.GLFW_KEY_BACKSLASH);
		_queuedChanges.put("key.pswg.species_select", InputUtil.GLFW_KEY_I);
	}

	@Inject(method = "write()V", at = @At("HEAD"))
	private void write(CallbackInfo ci)
	{
		if (_queuedChanges == null)
			return;

		// Options file doesn't exist, setting sensible defaults
		BlanketconPatches.LOGGER.info("Writing new options.txt, setting sensible keybind defaults");

		var keybindingsByTranslationKey = Arrays
				.stream(allKeys)
				.collect(Collectors.toMap(
						KeyBinding::getTranslationKey,
						keyBinding -> keyBinding
				));

		for (var pair : _queuedChanges.entrySet())
			setKeybind(keybindingsByTranslationKey, pair.getKey(), pair.getValue());

		_queuedChanges.clear();
		_queuedChanges = null;
	}

	@Unique
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
