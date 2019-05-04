package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.inventory.ContainerGristWidget;
import com.mraof.minestuck.inventory.ContainerSburbMachine;
import com.mraof.minestuck.tileentity.TileEntityMachineProcess;
import com.mraof.minestuck.util.Debug;

public class GoButtonPacket extends MinestuckPacket
{
	
	public boolean newMode;
	public boolean overrideStop;
	
	@Override
	public MinestuckPacket generatePacket(Object... dat) 
	{
		data.writeBoolean((Boolean) dat[0]);
		data.writeBoolean((Boolean) dat[1]);
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) 
	{
		newMode = data.readBoolean();
		overrideStop = data.readBoolean();
		
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		TileEntityMachineProcess te;
		if(player.openContainer instanceof ContainerSburbMachine)
				te = ((ContainerSburbMachine) player.openContainer).tileEntity;
		else if(player.openContainer instanceof ContainerGristWidget)
			te = ((ContainerGristWidget) player.openContainer).tileEntity;
		else return;
		
		if (te == null)
		{
			System.out.println("Invalid TE in container for player %s");
			Debug.warnf("Invalid TE in container for player %s!", player.getName());
		} else
		{
			System.out.println("Button pressed. Alchemiter going!");
			Debug.debug("Button pressed. Alchemiter going!");
			te.ready = newMode;
			te.overrideStop = overrideStop;
		}
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.CLIENT);
	}

}
