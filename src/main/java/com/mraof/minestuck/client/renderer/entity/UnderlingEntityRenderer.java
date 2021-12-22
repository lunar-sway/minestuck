package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A renderer for underlings with a texture cache.
 * Creates the cached textures from the entity type, so it assumes that all entities that this instance is applied to has the same entity type.
 */
public class UnderlingEntityRenderer<T extends UnderlingEntity, M extends EntityModel<T>> extends MobRenderer<T, M>
{
	private final Map<GristType, ResourceLocation> textureMap = new HashMap<>();
	
	public UnderlingEntityRenderer(EntityRendererManager renderManager, M entityModel, float shadowSize)
	{
		super(renderManager, entityModel, shadowSize);
	}
	
	@Override
	public ResourceLocation getTextureLocation(T entity)
	{
		return textureMap.computeIfAbsent(entity.getGristType(), grist -> createTexture(entity, grist));
	}
	
	protected ResourceLocation createTexture(T entity, GristType grist)
	{
		ResourceLocation underlingName = Objects.requireNonNull(entity.getType().getRegistryName(), () -> "Getting texture for entity without a registry name! "+entity);
		ResourceLocation gristName = grist.getEffectiveName();
		
		return new ResourceLocation(underlingName.getNamespace(), String.format("textures/entity/underlings/%s/%s_%s.png", gristName.getNamespace(), gristName.getPath(), underlingName.getPath()));
	}
}