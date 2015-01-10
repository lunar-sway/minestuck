package com.mraof.minestuck.client.renderer.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mraof.minestuck.client.model.ModelTransportalizer;

public class RenderTransportalizer extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation("Minestuck:textures/machines/Transportalizer.png");
	private ModelTransportalizer transportalizer = new ModelTransportalizer();
	public void renderMachineTileEntity(TileEntity tileEntity, double d0, double d1, double d2, float f)
	{
		this.bindTexture(texture);

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef((float)d0, (float)d1, (float)d2 + 1.0F);
		GL11.glScalef(1.0F, -1.0F, -1.0F);

		transportalizer.render(0.0625F);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f, int i) 
	{
		this.renderMachineTileEntity(tileentity, d0, d1, d2, f);
	}


}
