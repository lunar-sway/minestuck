package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.inventory.ContainerHandler;

public class MinestuckConfigPacket extends MinestuckPacket
{
	
	boolean mode;
	
	int overWorldEditRange;
	int landEditRange;
	int cardCost;
	int alchemiterStacks;
	int windowIdStart;
	int oreMultiplier;
	byte treeModusSetting;
	byte hashmapModusSetting;
	
	boolean giveItems;
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
			data.writeInt(MinestuckConfig.oreMultiplier);
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
			oreMultiplier = data.readInt();
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
			
			if(MinestuckConfig.oreMultiplier != oreMultiplier)
				player.sendMessage(new TextComponentString("[Minestuck] Ore multiplier config doesn't match the server value. (server: "+oreMultiplier+", you: "+MinestuckConfig.oreMultiplier+") Grist costs will likely not match their actual cost!").setStyle(new Style().setColor(TextFormatting.YELLOW)));
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
