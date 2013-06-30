package com.mraof.minestuck.client.gui;


import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;

import com.mraof.minestuck.entity.item.EntityGrist;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiGristCache extends Gui
{
	private static final int guiWidth = 226;
	private static final int guiHeight = 232;
	private static final int yOffset = 20;
	private static final String cacheMessage = "Grist Cache";
	
	private static final int gristIconX = 21;
	private static final int gristIconY = 76;
	private static final int gristIconXOffset = 66;
	private static final int gristIconYOffset = 21;
	
	private static final int gristCountX = 44;
	private static final int gristCountY = 80;
	private static final int gristCountXOffset = 66;
	private static final int gristCountYOffset = 21;
	
	private Minecraft mc;
	private FontRenderer fontRenderer;
    public static boolean visible = false;

    private String title = "CLASS of ASPECT";
    private String titleMessage = "";
    
    
    public GuiGristCache(Minecraft mc)
	{
		super();
		
		this.mc = mc;
        this.fontRenderer = mc.fontRenderer;
	}
	//name of function doesn't actually matter
	@ForgeSubscribe(priority = EventPriority.NORMAL)
	public void onRenderGameOverlay(RenderGameOverlayEvent event)
	{
	    if(event.isCancelable() || event.type != ElementType.ALL  || !this.visible)
	    {
	      return;
	    }
//	    for(int gristId = 0; gristId < EntityGrist.gristTypes.length; gristId++)
//	   		drawString(fontRenderer, EntityGrist.gristTypes[gristId] + " Grist: " + mc.thePlayer.getEntityData().getCompoundTag("Grist").getInteger(EntityGrist.gristTypes[gristId]), 0, 20 + gristId * 8, 0xddddee);

	    if (titleMessage.isEmpty()) {
	    	titleMessage = mc.thePlayer.username.toUpperCase() + " : " + title;
	    }
	    
        this.mc.renderEngine.bindTexture("/gui/GristCache.png");
        this.drawTexturedModalRect((mc.displayWidth/4)-(guiWidth/2), yOffset, 0, 0, 226, 232);
        fontRenderer.drawString(cacheMessage, (mc.displayWidth/4)-fontRenderer.getStringWidth(cacheMessage)/2, yOffset*2, 4210752);
        fontRenderer.drawString(titleMessage, (mc.displayWidth/4)-fontRenderer.getStringWidth(titleMessage)/2, yOffset*2+10, 4210752);
        
//        this.drawGristIcon((mc.displayWidth/4)-(guiWidth/2)+gristIconX, yOffset+gristIconY, "Build");
//        fontRenderer.drawString("0",(mc.displayWidth/4)-(guiWidth/2)+gristCountX, yOffset+gristCountY,0xddddee);
        
        for(int gristId = 0; gristId < EntityGrist.gristTypes.length; gristId++) {
        	int row = (int) (gristId/7);
        	int column = (int) (gristId%7);
        	 this.drawGristIcon((mc.displayWidth/4)-(guiWidth/2)+gristIconX+(gristIconXOffset*row-row), yOffset+gristIconY+(gristIconYOffset*column-column), EntityGrist.gristTypes[gristId]);
        	 fontRenderer.drawString(Integer.toString(mc.thePlayer.getEntityData().getCompoundTag("Grist").getInteger(EntityGrist.gristTypes[gristId])),(mc.displayWidth/4)-(guiWidth/2)+gristCountX+(gristCountXOffset*row-row), yOffset+gristCountY+(gristCountYOffset*column-column), 0xddddee);
        }
	}
	
	private void drawGristIcon(int x,int y,String gristType) {
		
		this.mc.renderEngine.bindTexture("/mods/Minestuck/textures/grist/" + gristType + ".png");
        
        float scale = (float) 1/16;
        
        int iconX = 16;
        int iconY = 16;
        int iconU = 0;
        int iconV = 0;
        
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x + 0), (double)(y +  iconY), (double)this.zLevel, (double)((float)(iconU + 0) * scale), (double)((float)(iconV +  iconY) * scale));
        tessellator.addVertexWithUV((double)(x + iconX), (double)(y +  iconY), (double)this.zLevel, (double)((float)(iconU + iconX) * scale), (double)((float)(iconV +  iconY) * scale));
        tessellator.addVertexWithUV((double)(x + iconX), (double)(y + 0), (double)this.zLevel, (double)((float)(iconU + iconX) * scale), (double)((float)(iconV + 0) * scale));
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((float)(iconU + 0) * scale), (double)((float)(iconV + 0) * scale));
        tessellator.draw();
	}
}
