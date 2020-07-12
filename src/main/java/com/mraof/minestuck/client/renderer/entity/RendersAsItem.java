package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.item.ItemStack;

/**
 * An alternative to {@link net.minecraft.entity.IRendersAsItem} meant to get around it being client side only,
 * in order to make it safer for usage on common classes (such as entities)
 * Meant to be paired with {@link CustomSpriteRenderer} as an alternative to {@link net.minecraft.client.renderer.entity.SpriteRenderer},
 * but somehow works on its own.
 * If this class stops working on its own, try using it with {@link CustomSpriteRenderer}.
 */
public interface RendersAsItem
{
	ItemStack getItem();
}