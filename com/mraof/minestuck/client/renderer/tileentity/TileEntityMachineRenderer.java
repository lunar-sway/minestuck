package com.mraof.minestuck.client.renderer.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mraof.minestuck.client.model.ModelCruxtruder;
import com.mraof.minestuck.tileentity.TileEntityMachine;

public class TileEntityMachineRenderer extends TileEntitySpecialRenderer {

	private static final ResourceLocation cruxtruderTexture = new ResourceLocation("Minestuck:textures/machines/Cruxtruder.png");
	private ModelCruxtruder cruxtruder = new ModelCruxtruder();
	public void renderMachineTileEntity(TileEntityMachine tileEntityMachine, double d0, double d1, double d2, float f)
	{
		ResourceLocation texture = cruxtruderTexture;
		this.func_110628_a(texture);

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef((float)d0, (float)d1 + .2625F, (float)d2 + 1.0F);
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		short short1 = 0;

		GL11.glRotatef((float)short1, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);


		cruxtruder.render(0.03125F);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) 
	{
		this.renderMachineTileEntity((TileEntityMachine) tileentity, d0, d1, d2, f);
	}


}
