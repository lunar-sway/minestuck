package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.DeployList;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Arrays;
import java.util.function.Supplier;

public class ModConfigPacket
{
	
	boolean mode;
	
	int overWorldEditRange;
	int landEditRange;
	int cardCost;
	int alchemiterStacks;
	int windowIdStart;
	byte treeModusSetting;
	byte hashmapModusSetting;
	
	boolean giveItems;
	boolean disableGristWidget;
	boolean dataChecker;
	boolean preEntryEcheladder;
	boolean hardMode;
	boolean[] deployValues;
	
	/**
	 * For configs that shouldn't change during runtime
	 */
	public ModConfigPacket()
	{
		mode = true;
		overWorldEditRange = MinestuckConfig.overworldEditRange;
		landEditRange = MinestuckConfig.landEditRange;
		//windowIdStart = ContainerHandler.windowIdStart;
		giveItems = MinestuckConfig.giveItems;
		hardMode = MinestuckConfig.hardMode;
		
		deployValues = Arrays.copyOf(MinestuckConfig.deployConfigurations, MinestuckConfig.deployConfigurations.length);
	}
	
	/**
	 * For configs that could be changed during runtime
	 * @param dataChecker if the player has access to and thus should see the data checker
	 */
	public ModConfigPacket(boolean dataChecker)
	{
		mode = false;
		cardCost = MinestuckConfig.cardCost;
		alchemiterStacks = MinestuckConfig.alchemiterMaxStacks;
		disableGristWidget = MinestuckConfig.disableGristWidget;
		treeModusSetting = MinestuckConfig.treeModusSetting;
		hashmapModusSetting = MinestuckConfig.hashmapChatModusSetting;
		this.dataChecker = dataChecker;
		preEntryEcheladder = MinestuckConfig.preEntryRungLimit <= 0;
	}
	
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBoolean(mode);
		if(mode)	//Values that shouldn't be changed while the game is running, and should therefore only be sent once
		{
			buffer.writeInt(overWorldEditRange);
			buffer.writeInt(landEditRange);
			//buffer.writeInt(windowIdStart);
			buffer.writeBoolean(giveItems);
			buffer.writeBoolean(hardMode);
			
			for(int i = 0; i < deployValues.length; i++)
				buffer.writeBoolean(deployValues[i]);
		} else
		{
			buffer.writeInt(cardCost);
			buffer.writeInt(alchemiterStacks);
			buffer.writeBoolean(disableGristWidget);
			buffer.writeByte(treeModusSetting);
			buffer.writeByte(hashmapModusSetting);
			buffer.writeBoolean(dataChecker);
			buffer.writeBoolean(preEntryEcheladder);
		}
	}
	
	public static ModConfigPacket decode(PacketBuffer buffer)
	{
		ModConfigPacket packet = new ModConfigPacket();
		
		packet.mode = buffer.readBoolean();
		
		if(packet.mode)
		{
			packet.overWorldEditRange = buffer.readInt();
			packet.landEditRange = buffer.readInt();
			//packet.windowIdStart = buffer.readInt();
			packet.giveItems = buffer.readBoolean();
			packet.hardMode = buffer.readBoolean();
			
			packet.deployValues = new boolean[MinestuckConfig.deployConfigurations.length];
			for(int i = 0; i < packet.deployValues.length; i++)
				packet.deployValues[i] = buffer.readBoolean();
		} else
		{
			packet.cardCost = buffer.readInt();
			packet.alchemiterStacks = buffer.readInt();
			packet.disableGristWidget = buffer.readBoolean();
			packet.treeModusSetting = buffer.readByte();
			packet.hashmapModusSetting = buffer.readByte();
			packet.dataChecker = buffer.readBoolean();
			packet.preEntryEcheladder = buffer.readBoolean();
		}
		
		return packet;
	}
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
			ctx.get().enqueueWork(this::execute);
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute()
	{
		if(mode)
		{
			MinestuckConfig.clientOverworldEditRange = overWorldEditRange;
			MinestuckConfig.clientLandEditRange = landEditRange;
			MinestuckConfig.clientGiveItems = giveItems;
			//ContainerHandler.clientWindowIdStart = windowIdStart;
			MinestuckConfig.clientHardMode = hardMode;
			
			if(!Minestuck.isServerRunning)
			{
				DeployList.applyConfigValues(deployValues);
			}
		} else
		{
			MinestuckConfig.clientCardCost = cardCost;
			MinestuckConfig.clientAlchemiterStacks = alchemiterStacks;
			MinestuckConfig.clientDisableGristWidget = disableGristWidget;
			MinestuckConfig.clientTreeAutobalance = treeModusSetting;
			MinestuckConfig.clientHashmapChat = hashmapModusSetting;
			MinestuckConfig.dataCheckerAccess = dataChecker;
			MinestuckConfig.preEntryEcheladder = preEntryEcheladder;
		}
	}
}