package com.mraof.minestuck.client.renderer;

import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.GL11.*;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.util.FBO;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;

@SideOnly(Side.CLIENT)
public class RenderCard implements IItemRenderer {
	
	public static IIcon cardIcon;
	
	public RenderItem itemRender = new RenderItem();
	
	public FBO itemBuffer = new FBO((int)(8*Math.pow(2,Minestuck.cardResolution)),(int)(8*Math.pow(2,Minestuck.cardResolution)));
	public FBO cardBuffer;
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return Minestuck.specialCardRenderer && item.getItem().equals(Minestuck.captchaCard) &&
				(type != ItemRenderType.ENTITY);
	}
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return helper == ItemRendererHelper.ENTITY_BOBBING || helper == ItemRendererHelper.ENTITY_ROTATION;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack card, Object... data) {
		TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
		ItemStack item = getDisplayItem(card);
		Tessellator t = Tessellator.instance;
		
		if(cardBuffer != null && (cardBuffer.height < cardIcon.getIconHeight() || cardBuffer.width < cardIcon.getIconWidth())) {
			cardBuffer.dispose();
			cardBuffer = null;
		}
		if(cardBuffer == null)
			cardBuffer = new FBO((int) Math.max(cardIcon.getIconWidth(), 16*Math.pow(2,Minestuck.cardResolution)),
					(int) Math.max(cardIcon.getIconHeight(), 16*Math.pow(2,Minestuck.cardResolution)));
		
		glPushMatrix();
		
		itemBuffer.bind();	//glClear affects the other buffers glClear in some way.
		
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		glOrtho(0, 16, 0, 16, -1000, 1000);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_ALPHA_TEST);
		glDisable(GL_CULL_FACE);
		
		if(item != null)
			if(item.getItem().equals(Minestuck.captchaCard)) {
				glDisable(GL_LIGHTING);
				textureManager.bindTexture(TextureMap.locationItemsTexture);
				renderIcon(0, 0, item.getIconIndex(), 16, 16);
			} else {
				glEnable(GL_LIGHTING);
				if (!ForgeHooksClient.renderInventoryItem((RenderBlocks)data[0], textureManager, item, itemRender.renderWithColor, itemRender.zLevel, 0, 0))
					itemRender.renderItemIntoGUI(null, textureManager, item, 0, 0, false);
				glDisable(GL_LIGHTING);
			}
		
		itemBuffer.unbind();
		
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
		
		renderContentToBuffer(card, item != null);
		
		glPopMatrix();
		
		if(OpenGlHelper.isFramebufferEnabled())
			Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(false);
		
		glBindTexture(GL_TEXTURE_2D, cardBuffer.texId);
		switch(type) {
		case EQUIPPED: case EQUIPPED_FIRST_PERSON:
			
//			TextureUtil.func_147950_a(false, false);
//			render3DCard(card);
//			TextureUtil.func_147945_b();
			break;
			
		case INVENTORY:
			
			glColor3f(1F, 1F, 1F);
			glDisable(GL_LIGHTING);
			
			
			t.startDrawingQuads();
			t.addVertexWithUV(0, 16, 0F, 0, 1);
			t.addVertexWithUV(16, 16, 0F, 1, 1);
			t.addVertexWithUV(16, 0, 0F, 1, 0);
			t.addVertexWithUV(0, 0, 0F, 0, 0);
			t.draw();
			
			break;
		}
		
	}
	
	public void renderContentToBuffer(ItemStack card, boolean renderItem) {
		TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
		Tessellator t = Tessellator.instance;
		
		cardBuffer.bind();
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		glOrtho(0, 16, 0, 16, -1000, 1000);
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		glLoadIdentity();
		
		glDisable(GL_CULL_FACE);
		
		textureManager.bindTexture(TextureMap.locationItemsTexture);
		renderIcon(0, 0, cardIcon, 16, 16);
		
		if(renderItem) {
			glBindTexture(GL_TEXTURE_2D, itemBuffer.texId);
			t.startDrawingQuads();
			t.addVertexWithUV(3, 12, 50F, 0, 1);
			t.addVertexWithUV(11, 12, 50F, 1, 1);
			t.addVertexWithUV(11, 4, 50F, 1, 0);
			t.addVertexWithUV(3, 4, 50F, 0, 0);
			t.draw();
		}
		
//		if(card.hasTagCompound() && card.getTagCompound().getBoolean("punched")) {
//			glTranslatef(0F, 0F, 100F);
//			renderPunchHoles(card);
//		}
		
		cardBuffer.unbind();
		
		glPopMatrix();
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
	}
	
	public static void renderIcon(int x, int y, IIcon icon, int width, int height) {
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.addVertexWithUV(x, y + height, 0F, icon.getMinU(), icon.getMaxV());
		t.addVertexWithUV(x + width, y + height, 0F, icon.getMaxU(), icon.getMaxV());
		t.addVertexWithUV(x + width, y, 0F, icon.getMaxU(), icon.getMinV());
		t.addVertexWithUV(x, y, 0F, icon.getMinU(), icon.getMinV());
		t.draw();
	}
	
	public static void renderPunchHoles(ItemStack card) {
		
		Tessellator t = Tessellator.instance;
		glDisable(GL_TEXTURE_2D);
		t.startDrawingQuads();
		t.setColorOpaque_I(0);
		int hashCode = card.getTagCompound().getString("contentID").hashCode()+card.getTagCompound().getInteger("contentMeta");
		if(card.getTagCompound().getString("contentID").equals(Item.itemRegistry.getNameForObject(Minestuck.blockStorage))
				&& card.getTagCompound().getInteger("contentMeta") == 1)
			hashCode = -1;
		for(int i = 0; i < 12; i++) {
			if((hashCode & (1 << i)) != 0)
				continue;
			int x = 3+(i%3)*3;
			int y = 5+(i/3)*2;
			t.addVertex(x  , y+1, 0);
			t.addVertex(x+2, y+1, 0);
			t.addVertex(x+2, y  , 0);
			t.addVertex(x  , y  , 0);
		}
		t.draw();
		glEnable(GL_TEXTURE_2D);
		
	}
	
	//ItemRenderer.renderItemIn2D(t, cardIcon.getMaxU(), cardIcon.getMinV(), cardIcon.getMinU(), cardIcon.getMaxV(),
	//cardIcon.getIconWidth(), cardIcon.getIconHeight(), 0.0625F);
	public void render3DCard(ItemStack card) {
		Tessellator t = Tessellator.instance;
		glBindTexture(GL_TEXTURE_2D, cardBuffer.texId);
		t.startDrawingQuads();
		t.setNormal(0.0F, 0.0F, -1.0F);
		t.addVertexWithUV(0, 1, -0.0625F, 1, 0);
		t.addVertexWithUV(1, 1, -0.0625F, 0, 0);
		t.addVertexWithUV(1, 0, -0.0625F, 0, 1);
		t.addVertexWithUV(0, 0, -0.0625F, 1, 1);
		t.draw();
		float f5 = 0.5F / cardIcon.getIconWidth();
		float f6 = 0.5F / cardIcon.getIconHeight();
		t.startDrawingQuads();
		t.setNormal(-1.0F, 0.0F, 0.0F);
		for (int k = 0; k < cardIcon.getIconWidth(); k++) {
			float f7 = k / cardIcon.getIconWidth();
			float f8 = 1 - f7 - f5;
			t.addVertexWithUV(f7, 0, -0.0625F, f8, 1);
			t.addVertexWithUV(f7, 0, 0, f8, 1);
			t.addVertexWithUV(f7, 1, 0, f8, 0);
			t.addVertexWithUV(f7, 1, -0.0625F, f8, 0);
		}
		t.draw();
		t.startDrawingQuads();
		t.setNormal(1.0F, 0.0F, 0.0F);
		for (int k = 0; k < cardIcon.getIconWidth(); k++)
		{
			float f7 = k / cardIcon.getIconWidth();
			float f8 = 1 - f7 - f5;
			float f9 = f7 + 1.0F / cardIcon.getIconWidth();
			t.addVertexWithUV(f9, 1, -0.0625F, f8, 0);
			t.addVertexWithUV(f9, 1, 0, f8, 0);
			t.addVertexWithUV(f9, 0, 0, f8, 1);
			t.addVertexWithUV(f9, 0, -0.0625F, f8, 1);
		}
		t.draw();
		t.startDrawingQuads();
		t.setNormal(0.0F, 1.0F, 0.0F);
		for (int k = 0; k < cardIcon.getIconHeight(); k++)
		{
			float f7 = k / cardIcon.getIconHeight();
			float f8 = 1 - f7 - f6;
			float f9 = f7 + 1.0F / cardIcon.getIconHeight();
			t.addVertexWithUV(0, f9, 0, 1, f8);
			t.addVertexWithUV(1, f9, 0, 0, f8);
			t.addVertexWithUV(1, f9, 0.0F - 0.0625F, 0, f8);
			t.addVertexWithUV(0, f9, 0.0F - 0.0625F, 1, f8);
		}
		t.draw();
		t.startDrawingQuads();
		t.setNormal(0.0F, -1.0F, 0.0F);
		for (int k = 0; k < cardIcon.getIconHeight(); k++)
		{
			float f7 = k / cardIcon.getIconHeight();
			float f8 = 1 - f7 - f6;
			t.addVertexWithUV(1, f7, 0, 0, f8);
			t.addVertexWithUV(0, f7, 0, 1, f8);
			t.addVertexWithUV(0, f7, -0.0625F, 1, f8);
			t.addVertexWithUV(1, f7, -0.0625F, 0, f8);
		}
		t.draw();
		
		renderContentToBuffer(card, false);
		if(OpenGlHelper.isFramebufferEnabled())
			Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(false);
		t.startDrawingQuads();
		t.setNormal(0.0F, 0.0F, 1.0F);
		t.addVertexWithUV(0, 0, 0, 1, 1);
		t.addVertexWithUV(1, 0, 0, 0, 1);
		t.addVertexWithUV(1, 1, 0, 0, 0);
		t.addVertexWithUV(0, 1, 0, 1, 0);
		t.draw();
	}
	
	
	public static ItemStack getDisplayItem(ItemStack card) {
		NBTTagCompound nbt = card.getTagCompound();
		String source = nbt == null?"":nbt.getBoolean("punched")?"display":"content";
		if(nbt == null || !nbt.hasKey(source+"ID"))
			return null;
		else {
			ItemStack stack = new ItemStack((Item) Item.itemRegistry.getObject(nbt.getString(source+"ID")),1 , nbt.getInteger(source+"Meta"));
			if(nbt.hasKey(source+"Data"))
				stack.stackTagCompound = nbt.getCompoundTag(source+"displayData");
			return stack;
		}
	}
	
}
