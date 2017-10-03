package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.inventory.ContainerCrockerMachine;
import com.mraof.minestuck.inventory.ContainerPunchDesignix;
import com.mraof.minestuck.inventory.ContainerSburbMachine;
import com.mraof.minestuck.tileentity.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler 
{
	public static enum GuiId
	{
		MACHINE,
		COMPUTER,
		TRANSPORTALIZER,
		COLOR,
	}
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if(id == GuiId.MACHINE.ordinal())
			if(tileEntity instanceof TileEntitySburbMachine)
				return new ContainerSburbMachine(player.inventory, (TileEntitySburbMachine) tileEntity);
			else if(tileEntity instanceof TileEntityCrockerMachine)
				return new ContainerCrockerMachine(player.inventory, (TileEntityCrockerMachine) tileEntity); 
			else if(tileEntity instanceof TileEntityPunchDesignix)
				return new ContainerPunchDesignix(player.inventory,(TileEntityPunchDesignix) tileEntity);
				
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
			else if(tileEntity instanceof TileEntityPunchDesignix)
				return new GuiPunchDesignix(player.inventory,(TileEntityPunchDesignix) tileEntity);
		if(tileEntity instanceof TileEntityComputer && id == GuiId.COMPUTER.ordinal())
			return new GuiComputer(Minecraft.getMinecraft(),(TileEntityComputer) tileEntity);
		
		if(id == GuiId.TRANSPORTALIZER.ordinal() && tileEntity instanceof TileEntityTransportalizer)
			return new GuiTransportalizer(Minecraft.getMinecraft(), (TileEntityTransportalizer) tileEntity);
		
		if(id == GuiId.COLOR.ordinal())
			return new GuiColorSelector(false);
		
		return null;
		
	}
	
}
