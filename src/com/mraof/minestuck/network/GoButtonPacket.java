package com.mraof.minestuck.network;

import com.mraof.minestuck.inventory.*;
import com.mraof.minestuck.tileentity.TileEntityMachineProcess;
import com.mraof.minestuck.util.Debug;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class GoButtonPacket
{
	
	public boolean newMode;
	public boolean overrideStop;
	
	public GoButtonPacket(boolean newMode, boolean overrideStop)
	{
		this.newMode = newMode;
		this.overrideStop = overrideStop;
	}
	
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBoolean(newMode);
		buffer.writeBoolean(overrideStop);
	}
	
	public static GoButtonPacket decode(PacketBuffer buffer)
	{
		boolean newMode = buffer.readBoolean();
		boolean overrideStop = buffer.readBoolean();
		
		return new GoButtonPacket(newMode, overrideStop);
	}
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute(EntityPlayer player)
	{
		TileEntityMachineProcess te;
		if(player.openContainer instanceof ContainerMiniAlchemiter)
				te = ((ContainerMiniAlchemiter) player.openContainer).tileEntity;
		else if(player.openContainer instanceof ContainerMiniCruxtruder)
			te = ((ContainerMiniCruxtruder) player.openContainer).tileEntity;
		else if(player.openContainer instanceof ContainerMiniPunchDesignix)
			te = ((ContainerMiniPunchDesignix) player.openContainer).tileEntity;
		else if(player.openContainer instanceof ContainerMiniTotemLathe)
			te = ((ContainerMiniTotemLathe) player.openContainer).tileEntity;
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
}