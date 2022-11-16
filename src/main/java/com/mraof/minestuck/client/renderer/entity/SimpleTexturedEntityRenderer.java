package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.SimpleTexturedEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class SimpleTexturedEntityRenderer<T extends MobEntity, M extends EntityModel<T>> extends MobRenderer<T, M>
{
	private final ResourceLocation textureLocation;
	
	public SimpleTexturedEntityRenderer(EntityRendererManager manager, M model, float shadowRadius, EntityType<T> entityType)
	{
		this(manager, model, shadowRadius, textureFromType(entityType));
	}
	
	public SimpleTexturedEntityRenderer(EntityRendererProvider.Context context, M par1ModelBase, float par2)
	{
		super(context, par1ModelBase, par2);
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