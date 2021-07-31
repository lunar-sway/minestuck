package com.mraof.minestuck.effects;

import com.mraof.minestuck.Minestuck;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;

public class MSEffect extends Effect
{
	private static final ResourceLocation TEXTURES = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/potions.png");
	private final int index;
	
	private static int currentIndex = 0;
	
	protected MSEffect(EffectType typeIn, int liquidColor, String name)
	{
		super(typeIn, liquidColor);
		this.index = currentIndex++;
		//setPotionName("effect.minestuckuniverse."+name);
		setRegistryName("effect.minestuck." + name);
	}
	
	@Override
	public void renderInventoryEffect(EffectInstance effect, DisplayEffectsScreen<?> gui, int x, int y, float z)
	{
		super.renderInventoryEffect(effect, gui, x, y, z);
	}
	
	@Override
	public boolean shouldRender(EffectInstance effect)
	{
		return false;
	}
	
	@Override
	public void renderHUDEffect(EffectInstance effect, AbstractGui gui, int x, int y, float z, float alpha)
	{
		super.renderHUDEffect(effect, gui, x, y, z, alpha);
	}
	
	/*@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
	{
		this.renderEffect(x + 6, y + 7, 1.0F);
	}*/
	
	
	
	/*@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(int x, int y, Effect effect, Minecraft mc, float alpha)
	{
		this.renderEffect(x + 3, y + 3, alpha);
	}*/
	
	
	
	/*@SideOnly(Side.CLIENT)
	protected void renderEffect(int x, int y, float alpha)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURES);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buf = tessellator.getBuffer();
		buf.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
		int textureX = this.index % 14 * 18;
		int textureY = this.index / 14 * 18;
		buf.pos((double)x, (double)(y + 18), 0.0D).tex((double)textureX * 0.00390625D, (double)(textureY + 18) * 0.00390625D).endVertex();
		buf.pos((double)(x + 18), (double)(y + 18), 0.0D).tex((double)(textureX + 18) * 0.00390625D, (double)(textureY + 18) * 0.00390625D).endVertex();
		buf.pos((double)(x + 18), (double)y, 0.0D).tex((double)(textureX + 18) * 0.00390625D, (double)textureY * 0.00390625D).endVertex();
		buf.pos((double)x, (double)y, 0.0D).tex((double)textureX * 0.00390625D, (double)textureY * 0.00390625D).endVertex();
		tessellator.draw();
	}*/
}
