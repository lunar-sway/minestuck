package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.Pose;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * Written to be used with {@link RendersAsItem}, but which somehow works without this.
 */
public class CustomSpriteRenderer<T extends Entity & RendersAsItem> extends EntityRenderer<T>
{
	private final FakeEntity fakeEntity;
	private final ModifiedSpriteRenderer renderer;
	
	protected CustomSpriteRenderer(EntityType<?> type, EntityRendererManager renderManager, ItemRenderer itemRenderer)
	{
		super(renderManager);
		fakeEntity = new FakeEntity(type);
		renderer = new ModifiedSpriteRenderer(renderManager, itemRenderer);
	}
	
	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		fakeEntity.setEntity(entity);
		renderer.doRender(fakeEntity, x, y, z, entityYaw, partialTicks);
	}
	
	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(T entity)
	{
		fakeEntity.setEntity(entity);
		return renderer.getTexture(fakeEntity);
	}
	
	private class FakeEntity extends Entity implements IRendersAsItem
	{
		private T entity;
		
		FakeEntity(EntityType<?> type)
		{
			//noinspection ConstantConditions
			super(type, null);
		}
		
		void setEntity(T entity)
		{
			this.entity = entity;
			posX = entity.posX;
			posY = entity.posY;
			posZ = entity.posZ;
		}
		
		@Override
		protected void registerData()
		{}
		
		@Override
		protected void readAdditional(CompoundNBT compound)
		{}
		
		@Override
		protected void writeAdditional(CompoundNBT compound)
		{}
		
		@Override
		public IPacket<?> createSpawnPacket()
		{
			return null;
		}
		
		@Override
		public ItemStack getItem()
		{
			return entity.getItem();
		}
		
		@Nullable
		@Override
		public Team getTeam()
		{
			return entity.getTeam();
		}
		
		@Override
		public boolean getAlwaysRenderNameTagForRender()
		{
			return entity.getAlwaysRenderNameTagForRender();
		}
		
		@Override
		public boolean hasCustomName()
		{
			return entity.hasCustomName();
		}
		
		@Override
		public ITextComponent getDisplayName()
		{
			return entity.getDisplayName();
		}
		
		@Override
		public Pose getPose()
		{
			return entity.getPose();
		}
	}
	
	private class ModifiedSpriteRenderer extends SpriteRenderer<FakeEntity>
	{
		
		ModifiedSpriteRenderer(EntityRendererManager renderManagerIn, ItemRenderer itemRendererIn)
		{
			super(renderManagerIn, itemRendererIn);
		}
		
		ResourceLocation getTexture(FakeEntity entity)
		{
			return getEntityTexture(entity);
		}
	}
}
