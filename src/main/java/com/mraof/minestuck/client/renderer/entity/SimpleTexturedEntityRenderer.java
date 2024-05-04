package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

import java.util.Objects;

public class SimpleTexturedEntityRenderer<T extends Mob, M extends EntityModel<T>> extends MobRenderer<T, M>
{
	private final ResourceLocation textureLocation;
	
	public SimpleTexturedEntityRenderer(EntityRendererProvider.Context context, M model, float shadowRadius, EntityType<T> entityType)
	{
		this(context, model, shadowRadius, textureFromType(entityType));
	}
	
	public SimpleTexturedEntityRenderer(EntityRendererProvider.Context context, M model, float shadowRadius, ResourceLocation textureLocation)
	{
		super(context, model, shadowRadius);
		this.textureLocation = textureLocation;
	}
	
	@Override
	public ResourceLocation getTextureLocation(T entity)
	{
		return this.textureLocation;
	}
	
	public static ResourceLocation textureFromType(EntityType<?> entityType)
	{
		ResourceLocation entityName = Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(entityType), () -> "Getting texture for entity type without a registry name! " + entityType);
		
		return new ResourceLocation(entityName.getNamespace(), "textures/entity/" + entityName.getPath() + ".png");
	}
}