package com.mraof.minestuck.network;

import com.mraof.minestuck.inventory.ContainerSburbMachine;
import com.mraof.minestuck.tileentity.TileEntitySburbMachine;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.alchemy.GristType;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class MachineStatePacket extends MinestuckPacket {

	public int xCoord;
	public int yCoord;
	public int zCoord;
	public GristType gristType;
	
	@Override
	public MinestuckPacket generatePacket(Object... dat) 
	{
		data.writeInt(GristType.REGISTRY.getID((GristType) dat[0]));
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) 
	{
		gristType = GristType.REGISTRY.getValue(data.readInt());
		
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		if(!(player.openContainer instanceof ContainerSburbMachine))
			return;
		
		TileEntitySburbMachine te = ((ContainerSburbMachine) player.openContainer).tileEntity;
		
		if (te == null)
		{
			Debug.warnf("Invalid TE in container for player %s!", player.getName());
		}
		else
		{
			te.selectedGrist = gristType;
		}
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.CLIENT);
	}

}
