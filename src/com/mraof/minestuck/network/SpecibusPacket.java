package com.mraof.minestuck.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

import com.mraof.minestuck.inventory.specibus.StrifePortfolioHandler;
import com.mraof.minestuck.inventory.specibus.StrifeSpecibus;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;

public class SpecibusPacket extends MinestuckPacket
{
	public static final byte SPECIBUS_ADD = 1;
	public static final byte SPECIBUS_REMOVE = 2;
	public static final byte PORTFOLIO = 3;
	
	public byte type;
	public NBTTagCompound nbt;
	public ArrayList<StrifeSpecibus> portfolio;
	
	@Override
	public MinestuckPacket generatePacket(Object... data) 
	{
		byte type = (Byte) data[0];
		this.data.writeByte(type);
		if(data.length > 1)
		{
			switch(type)
			{
				case PORTFOLIO: case SPECIBUS_ADD:
				{
					try
					{
						ByteArrayOutputStream bytes = new ByteArrayOutputStream();
						CompressedStreamTools.writeCompressed((NBTTagCompound)data[1], bytes);
						this.data.writeBytes(bytes.toByteArray());
					}
					catch (IOException e)
					{
						e.printStackTrace();
						return null;
					}
				}
				break;
			}
		}
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) 
	{
		this.type = data.readByte();
		if(data.readableBytes() > 0)
		{
			switch(type)
			{
			case PORTFOLIO: case SPECIBUS_ADD:
			{
				byte[] bytes = new byte[data.readableBytes()];
				data.readBytes(bytes);
				try
				{
					this.nbt = CompressedStreamTools.readCompressed(new ByteArrayInputStream(bytes));
				}
				catch(IOException e)
				{
					e.printStackTrace();
					return null;
				}
			}
			break;
			}
		}
		return this;
	}

	@Override
	public void execute(EntityPlayer player) 
	{
		if(player != null && player.world != null && !player.world.isRemote)
		{
			switch(type)
			{
			case PORTFOLIO:
			{
				portfolio = StrifePortfolioHandler.createPortfolio(nbt);
				MinestuckPlayerData.onPacketRecived(this);
				MinestuckPlayerData.setStrifePortfolio(IdentifierHandler.encode(player), portfolio);
				MinestuckPlayerData.setClientPortfolio(portfolio);
			}
			break;
			case SPECIBUS_ADD:
			{
				ArrayList<StrifeSpecibus> portfolio = MinestuckPlayerData.getStrifePortfolio(IdentifierHandler.encode(player));
				StrifeSpecibus specibus = new StrifeSpecibus(nbt);
				MinestuckPlayerData.setClientPortfolio(portfolio);
				MinestuckPlayerData.getStrifePortfolio(IdentifierHandler.encode(player)).add(specibus);

				EnumHand hand = (player.getHeldItemMainhand().isItemEqual(new ItemStack(MinestuckItems.strifeCard)) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
				ItemStack card = player.getHeldItem(hand);
				
				if(card.isItemEqual(new ItemStack(MinestuckItems.strifeCard)))
					card.shrink(1);
			}
			break;
			}
		}
		else
		{
			switch(type)
			{
			case PORTFOLIO:
			{
				portfolio = StrifePortfolioHandler.createPortfolio(nbt);
				MinestuckPlayerData.setClientPortfolio(portfolio);
			}
			break;
			}
		}
	}

	@Override
	public EnumSet<Side> getSenderSide() 
	{
		return EnumSet.allOf(Side.class);
	}
	
}
