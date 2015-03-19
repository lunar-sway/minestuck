package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.inventory.ContainerHandler;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.UsernameHandler;

public class MinestuckConfigPacket extends MinestuckPacket
{
	
	int overWorldEditRange;
	int landEditRange;
	int cardCost;
	int windowIdStart;
	byte treeModusSetting;
	
	boolean hardMode;
	boolean giveItems;
	boolean easyDesignix;
	boolean cardRecipe;
	boolean[] deployValues;
	
	String lanHost;

	public MinestuckConfigPacket()
	{
		super(Type.CONFIG);
	}

	@Override
	public MinestuckPacket generatePacket(Object... dat)
	{
		data.writeInt(MinestuckConfig.overworldEditRange);
		data.writeInt(MinestuckConfig.landEditRange);
		data.writeInt(MinestuckConfig.cardCost);
		data.writeInt(ContainerHandler.windowIdStart);
		data.writeBoolean(MinestuckConfig.hardMode);
		data.writeBoolean(MinestuckConfig.giveItems);
		data.writeBoolean(MinestuckConfig.cardRecipe);
		data.writeByte(MinestuckConfig.treeModusSetting);
		
		for(int i = 0; i < MinestuckConfig.deployConfigurations.length; i++)
			data.writeBoolean(MinestuckConfig.deployConfigurations[i]);
		if(UsernameHandler.host != null)
			writeString(data,UsernameHandler.host);
		
		return this;
	}
	
	@Override
	public MinestuckPacket consumePacket(ByteBuf data)
	{
		overWorldEditRange = data.readInt();
		landEditRange = data.readInt();
		cardCost = data.readInt();
		windowIdStart = data.readInt();
		hardMode = data.readBoolean();
		giveItems = data.readBoolean();
		cardRecipe = data.readBoolean();
		treeModusSetting = data.readByte();
		
		deployValues = new boolean[MinestuckConfig.deployConfigurations.length];
		for(int i = 0; i < deployValues.length; i++)
			deployValues[i] = data.readBoolean();
		lanHost = readLine(data);
		if(lanHost.isEmpty())
			lanHost = null;
		Debug.print("Recived packet! Host is "+lanHost);
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		
		MinestuckConfig.clientOverworldEditRange = this.overWorldEditRange;
		MinestuckConfig.clientLandEditRange = this.landEditRange;
		MinestuckConfig.clientCardCost = this.cardCost;
		MinestuckConfig.clientHardMode = this.hardMode;
		MinestuckConfig.clientGiveItems = this.giveItems;
		MinestuckConfig.clientTreeAutobalance = treeModusSetting;
		ContainerHandler.clientWindowIdStart = windowIdStart;
		
		if(!Minestuck.isServerRunning)
		{
			UsernameHandler.host = lanHost;
			DeployList.applyConfigValues(deployValues);
			AlchemyRecipeHandler.addOrRemoveRecipes(cardRecipe);
		}
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}

}
