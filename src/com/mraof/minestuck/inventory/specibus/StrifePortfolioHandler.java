package com.mraof.minestuck.inventory.specibus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.SpecibusPacket;
import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.KindAbstratusType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;

public class StrifePortfolioHandler 
{
	//private static String[] metaConvert = {};
	private static List<KindAbstratusType> abstrataList = KindAbstratusList.getTypeList();
	
	public static StrifeSpecibus createSpecibus(int id, Side side)
	{
		StrifeSpecibus specibus = new StrifeSpecibus(id);
		specibus.side = side;
		return specibus;
	}
	
	public static KindAbstratusType getType(int id)
	{
		return abstrataList.get(id);
	}

	public static void addSpecibus(EntityPlayer player, StrifeSpecibus specibus) 
	{
		String typeName = specibus.getAbstratus().getDisplayName();
		player.sendStatusMessage(new TextComponentTranslation("The " + typeName + " specibus has been added to your strife portfolio."), false);						
		MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.PORTFOLIO, SpecibusPacket.SPECIBUS_ADD, writeToNBT(specibus));
		MinestuckChannelHandler.sendToServer(packet);
	}
	
	public static void setPortfolio(EntityPlayer player, ArrayList<StrifeSpecibus> portfolio) 
	{
		MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.PORTFOLIO, SpecibusPacket.PORTFOLIO, writeToNBT(portfolio));
		MinestuckChannelHandler.sendToServer(packet);
	}
	
	public static NBTTagCompound writeToNBT(StrifeSpecibus specibus)
	{
		return specibus.writeToNBT(new NBTTagCompound());
	}
	
	public static NBTTagCompound writeToNBT(ArrayList<StrifeSpecibus> portfolio)
	{
		NBTTagCompound nbt = new NBTTagCompound();
	
		int i = 0;
		for(StrifeSpecibus specibus : portfolio)
		{
			nbt.setTag("specibus"+i, specibus.writeToNBT(new NBTTagCompound()));
			i++;
		}
	
		
		return nbt;
	}
	
	public static ArrayList<StrifeSpecibus> createPortfolio(NBTTagCompound nbt)
	{
		ArrayList<StrifeSpecibus> portfolio = new ArrayList<StrifeSpecibus>();
		for(int i = 0; i < nbt.getSize(); i++)
		{
			String name = "specibus"+i;
			
			if(nbt.hasKey(name))
			{
				portfolio.add(new StrifeSpecibus(nbt.getCompoundTag(name)));
			}
		}
		return portfolio;
	}
}
