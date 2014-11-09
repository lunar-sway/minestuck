package com.mraof.minestuck.client.renderer.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mraof.minestuck.client.model.ModelAlchemiter;
import com.mraof.minestuck.client.model.ModelCruxtruder;
import com.mraof.minestuck.client.model.ModelGristWidget;
import com.mraof.minestuck.client.model.ModelMachine;
import com.mraof.minestuck.client.model.ModelPunchDesignix;
import com.mraof.minestuck.client.model.ModelTotemLathe;
import com.mraof.minestuck.tileentity.TileEntityMachine;

public class RenderMachine extends TileEntitySpecialRenderer {

	private static final ResourceLocation cruxtruderTexture = new ResourceLocation("Minestuck:textures/machines/Cruxtruder.png");
	private static final ResourceLocation punchDesignixTexture = new ResourceLocation("Minestuck:textures/machines/PunchDesignix.png");
	private static final ResourceLocation totemLatheTexture = new ResourceLocation("Minestuck:textures/machines/TotemLathe.png");
	private static final ResourceLocation alchemiterTexture = new ResourceLocation("Minestuck:textures/machines/Alchemiter.png");
	private static final ResourceLocation gristWidgetTexture = new ResourceLocation("Minestuck:textures/machines/GristWidget.png");
	private ModelCruxtruder cruxtruder = new ModelCruxtruder();
	private ModelPunchDesignix punchDesignix = new ModelPunchDesignix();
	private ModelTotemLathe totemLathe = new ModelTotemLathe();
	private ModelAlchemiter alchemiter = new ModelAlchemiter();
	private ModelGristWidget gristWidget = new ModelGristWidget();
	public void renderMachineTileEntity(TileEntityMachine tileEntityMachine, double d0, double d1, double d2, float f)
	{
        byte i = 0;

        if (tileEntityMachine.hasWorldObj())
            i = tileEntityMachine.rotation;
        
		ResourceLocation texture;
		ModelMachine machine;
		switch(tileEntityMachine.getMetadata())
		{
		default:
		case 0:
			machine = cruxtruder;
			texture = cruxtruderTexture;
			break;
		case 1:
			machine = punchDesignix;
			texture = punchDesignixTexture;
			break;
		case 2:
			machine = totemLathe;
			texture = totemLatheTexture;
			break;
		case 3:
			machine = alchemiter;
			texture = alchemiterTexture;
			break;
		case 4:
			machine = gristWidget;
			texture = gristWidgetTexture;
			break;
		}
		this.bindTexture(texture);

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef((float)d0, (float)d1, (float)d2 + 1.0F);
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		
        short short1 = (short) (i * 90);

		GL11.glRotatef((float)short1, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);


		machine.render(0.03125F);
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
