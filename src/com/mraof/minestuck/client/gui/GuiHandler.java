package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.tileentity.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class GuiHandler
{
	public static ResourceLocation MINI_CRUXTRUDER_ID = new ResourceLocation("minestuck", "mini_cruxtruder");
	public static ResourceLocation MINI_TOTEM_LATHE_ID = new ResourceLocation("minestuck", "mini_totem_lathe");
	public static ResourceLocation MINI_ALCHEMITER_ID = new ResourceLocation("minestuck", "mini_alchemiter");
	public static ResourceLocation MINI_PUNCH_DESIGNIX_ID = new ResourceLocation("minestuck", "mini_punch_designix");
	public static ResourceLocation GRIST_WIDGET_ID = new ResourceLocation("minestuck", "grist_widget");
	public static ResourceLocation URANIUM_COOKER_ID = new ResourceLocation("minestuck", "uranium_cooker");
	
	public static ResourceLocation CONSORT_MERCHANT_ID = new ResourceLocation("minestuck", "consort_merchant");
	
	/*@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if(id == GuiId.MACHINE.ordinal())
		{
			if (tileEntity instanceof TileEntitySburbMachine)
				return new ContainerSburbMachine(player.inventory, (TileEntitySburbMachine) tileEntity);
			else if(tileEntity instanceof TileEntityGristWidget)
				return new ContainerCrockerMachine(player.inventory, (TileEntityGristWidget) tileEntity);
			else if(tileEntity instanceof TileEntityUraniumCooker)
				return new ContainerUraniumCooker(player.inventory, (TileEntityUraniumCooker) tileEntity);
		} else if(id == GuiId.MERCHANT.ordinal())
			return new ContainerConsortMerchant(player);
		return null;
	}*/
	
	public static GuiScreen provideGuiContainer(FMLPlayMessages.OpenContainer message)
	{
		ResourceLocation id = message.getId();
		BlockPos pos = message.getAdditionalData().readBlockPos();
		Minecraft mc = Minecraft.getInstance();
		TileEntity tileEntity = mc.world.getTileEntity(pos);
		if(id == MINI_CRUXTRUDER_ID)
		{
			if(tileEntity instanceof MiniCruxtruderTileEntity)
				return new GuiMiniCruxtruder(mc.player.inventory, (MiniCruxtruderTileEntity) tileEntity);
		} else if(id == MINI_TOTEM_LATHE_ID)
		{
			if(tileEntity instanceof MiniTotemLatheTileEntity)
				return new GuiMiniTotemLathe(mc.player.inventory, (MiniTotemLatheTileEntity) tileEntity);
		} else if(id == MINI_ALCHEMITER_ID)
		{
			if(tileEntity instanceof MiniAlchemiterTileEntity)
				return new GuiMiniAlchemiter(mc.player.inventory, (MiniAlchemiterTileEntity) tileEntity);
		} else if(id == MINI_PUNCH_DESIGNIX_ID)
		{
			if(tileEntity instanceof MiniPunchDesignixTileEntity)
				return new GuiMiniPunchDesignix(mc.player.inventory, (MiniPunchDesignixTileEntity) tileEntity);
		} else if(id == GRIST_WIDGET_ID)
		{
			if(tileEntity instanceof GristWidgetTileEntity)
				return new GuiGristWidget(mc.player.inventory, (GristWidgetTileEntity) tileEntity);
		} else if(id == CONSORT_MERCHANT_ID)
		{
			return new GuiConsortShop(mc.player);
		}
		
		return null;
	}
}