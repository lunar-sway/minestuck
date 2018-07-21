package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.inventory.ContainerHandler;
import com.mraof.minestuck.util.AlchemyRecipeHandler;

public class MinestuckConfigPacket extends MinestuckPacket
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
	boolean easyDesignix;
	boolean disableGristWidget;
	boolean dataChecker;
	boolean preEntryEcheladder;
	boolean hardMode;
	boolean[] deployValues;
	
	@Override
	public MinestuckPacket generatePacket(Object... dat)
	{
		boolean mode = (Boolean) dat[0];
		data.writeBoolean(mode);
		if(mode)	//Values that shouldn't be changed while the game is running, and should therefore only be sent once
		{
			data.writeInt(MinestuckConfig.overworldEditRange);
			data.writeInt(MinestuckConfig.landEditRange);
			data.writeInt(ContainerHandler.windowIdStart);
			data.writeBoolean(MinestuckConfig.giveItems);
			data.writeBoolean(MinestuckConfig.hardMode);
			
			for(int i = 0; i < MinestuckConfig.deployConfigurations.length; i++)
				data.writeBoolean(MinestuckConfig.deployConfigurations[i]);
		} else
		{
			data.writeInt(MinestuckConfig.cardCost);
			data.writeInt(MinestuckConfig.alchemiterMaxStacks);
			data.writeBoolean(MinestuckConfig.disableGristWidget);
			data.writeByte(MinestuckConfig.treeModusSetting);
			data.writeByte(MinestuckConfig.hashmapChatModusSetting);
			data.writeBoolean((Boolean) dat[1]);
			data.writeBoolean(MinestuckConfig.preEntryRungLimit <= 0);
		}
		
		return this;
	}
	
	@Override
	public MinestuckPacket consumePacket(ByteBuf data)
	{
		mode = data.readBoolean();
		
		if(mode)
		{
			overWorldEditRange = data.readInt();
			landEditRange = data.readInt();
			windowIdStart = data.readInt();
			giveItems = data.readBoolean();
			hardMode = data.readBoolean();
			
			deployValues = new boolean[MinestuckConfig.deployConfigurations.length];
			for(int i = 0; i < deployValues.length; i++)
				deployValues[i] = data.readBoolean();
		} else
		{
			cardCost = data.readInt();
			alchemiterStacks = data.readInt();
			disableGristWidget = data.readBoolean();
			treeModusSetting = data.readByte();
			hashmapModusSetting = data.readByte();
			dataChecker = data.readBoolean();
			preEntryEcheladder = data.readBoolean();
		}
		
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		if(mode)
		{
			MinestuckConfig.clientOverworldEditRange = overWorldEditRange;
			MinestuckConfig.clientLandEditRange = landEditRange;
			MinestuckConfig.clientGiveItems = giveItems;
			ContainerHandler.clientWindowIdStart = windowIdStart;
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

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}

}
