package com.mraof.minestuck.client.renderer.entity.frog;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.model.ModelFrog;
import com.mraof.minestuck.entity.EntityFrog;
import com.mraof.minestuck.entity.EntityMinestuck;

import com.mraof.minestuck.item.ItemFrog;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderFrog extends RenderLivingBase<EntityFrog>
{

	public RenderFrog(RenderManager manager, ModelBase par1ModelBase, float par2)
	{
		super(manager, new ModelFrog(), par2);
		this.addLayer(new LayerFrogSkin(this));
		this.addLayer(new LayerFrogEyes(this));
		this.addLayer(new LayerFrogBelly(this));
		
	}

	@Override
	protected void preRenderCallback(EntityFrog frog, float partialTickTime) 
	{
		float scale = frog.getFrogSize();
		GlStateManager.scalef(scale,scale,scale);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityFrog entity)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "textures/mobs/frog/base.png");
	}
	
	protected boolean canRenderName(EntityFrog entity)
    {
        return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.hasCustomName() && entity == this.renderManager.pointedEntity);
    }
	
    public static class FrogItemColor implements IItemColor
	{
		@Override
		public int getColor(ItemStack stack, int tintIndex)
		{
			ItemFrog item = ((ItemFrog)stack.getItem());
			int color = -1;
			int type = stack.hasTag() ? 0 : stack.getTag().getInt("Type");
			if(type == 0)
			{
				switch(tintIndex)
				{
					case 0: color = item.getSkinColor(stack); break;
					case 1: color = item.getEyeColor(stack); break;
					case 2: color = item.getBellyColor(stack); break;
				}
			}
			return color;
		}
	}
}
