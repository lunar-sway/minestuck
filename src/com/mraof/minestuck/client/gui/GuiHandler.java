package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.inventory.ContainerConsortMerchant;
import com.mraof.minestuck.inventory.ContainerCrockerMachine;
import com.mraof.minestuck.inventory.ContainerSburbMachine;
import com.mraof.minestuck.inventory.ContainerUraniumCooker;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.tileentity.TileEntityCrockerMachine;
import com.mraof.minestuck.tileentity.TileEntitySburbMachine;
import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import com.mraof.minestuck.tileentity.TileEntityUraniumCooker;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler 
{
	public enum GuiId
	{
		MACHINE,
		COMPUTER,
		TRANSPORTALIZER,
		COLOR,
		MERCHANT,
	}
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if(id == GuiId.MACHINE.ordinal())
		{
			if (tileEntity instanceof TileEntitySburbMachine)
				return new ContainerSburbMachine(player.inventory, (TileEntitySburbMachine) tileEntity);
			else if(tileEntity instanceof TileEntityCrockerMachine)
				return new ContainerCrockerMachine(player.inventory, (TileEntityCrockerMachine) tileEntity);
			else if(tileEntity instanceof TileEntityUraniumCooker)
				return new ContainerUraniumCooker(player.inventory, (TileEntityUraniumCooker) tileEntity);
		} else if(id == GuiId.MERCHANT.ordinal())
			return new ContainerConsortMerchant(player);
		
		return null;
	}

	//returns an instance of the Gui you made earlier
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if(id == GuiId.MACHINE.ordinal())
			if(tileEntity instanceof TileEntitySburbMachine)
				return new GuiSburbMachine(player.inventory, (TileEntitySburbMachine) tileEntity);
			else if(tileEntity instanceof TileEntityCrockerMachine)
				return new GuiCrockerMachine(player.inventory, (TileEntityCrockerMachine) tileEntity);
			else if(tileEntity instanceof TileEntityUraniumCooker)
				return new GuiUraniumCooker(player.inventory, (TileEntityUraniumCooker) tileEntity);
		
		if(tileEntity instanceof TileEntityComputer && id == GuiId.COMPUTER.ordinal())
			return new GuiComputer(Minecraft.getMinecraft(),(TileEntityComputer) tileEntity);
		
		if(id == GuiId.TRANSPORTALIZER.ordinal() && tileEntity instanceof TileEntityTransportalizer)
			return new GuiTransportalizer(Minecraft.getMinecraft(), (TileEntityTransportalizer) tileEntity);
		
		if(id == GuiId.COLOR.ordinal())
			return new GuiColorSelector(false);
		
		if(id == GuiId.MERCHANT.ordinal())
			return new GuiConsortShop(player);
		
		return null;
		
	}
	
}
