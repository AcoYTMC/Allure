package com.example.template.impl;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

public class Template implements ModInitializer {
	public static final String MOD_ID = "template";
	public static final Logger LOGGER = LogUtils.getLogger();

	public void onInitialize() {
		//
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
