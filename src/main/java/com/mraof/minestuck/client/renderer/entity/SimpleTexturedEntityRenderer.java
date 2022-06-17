package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class SimpleTexturedEntityRenderer<T extends MobEntity, M extends EntityModel<T>> extends MobRenderer<T, M>
{
	private final ResourceLocation textureLocation;
	
	public SimpleTexturedEntityRenderer(EntityRendererManager manager, M model, float shadowRadius, EntityType<T> entityType)
	{
		this(manager, model, shadowRadius, textureFromType(entityType));
	}
	
	public SimpleTexturedEntityRenderer(EntityRendererManager manager, M model, float shadowRadius, ResourceLocation textureLocation)
	{
		super(manager, model, shadowRadius);
		this.textureLocation = textureLocation;
	}
	
	@Override
	public ResourceLocation getTextureLocation(T entity)
	{
		return this.textureLocation;
	}
	
	public static ResourceLocation textureFromType(EntityType<?> entityType)
	{
		ResourceLocation entityName = Objects.requireNonNull(entityType.getRegistryName(), () -> "Getting texture for entity type without a registry name! " + entityType);
		
		return new ResourceLocation(entityName.getNamespace(), "textures/entity/" + entityName.getPath() + ".png");
	}
}