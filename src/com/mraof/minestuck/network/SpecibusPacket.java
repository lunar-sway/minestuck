package com.mraof.minestuck.network;

import com.mraof.minestuck.inventory.specibus.StrifePortfolioHandler;
import com.mraof.minestuck.inventory.specibus.StrifeSpecibus;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.network.PacketBuffer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SpecibusPacket implements PlayToBothPacket
{

	public static final byte PORTFOLIO = 0;
	public static final byte SPECIBUS_ADD = 1;
	public static final byte SPECIBUS_REMOVE = 2;
	public static final byte DECK_ADD = 3;
	public static final byte DECK_REMOVE = 4;
	
	private final byte type;
	private int itemIndex;
	private CompoundNBT nbt;
	private int specibusId;
	
	private SpecibusPacket(byte type)
	{
		this.type = type;
	}
	
	public static SpecibusPacket sendPortfolioData(CompoundNBT nbt)
	{
		SpecibusPacket packet = new SpecibusPacket(PORTFOLIO);
		packet.nbt = nbt;
		return packet;
	}
	
	public static SpecibusPacket addSpecibus(CompoundNBT nbt)
	{
		SpecibusPacket packet = new SpecibusPacket(SPECIBUS_ADD);
		packet.nbt = nbt;
		return packet;
	}
	
	public static SpecibusPacket removeSpecibus(int specibusId)
	{
		SpecibusPacket packet = new SpecibusPacket(SPECIBUS_REMOVE);
		packet.specibusId = specibusId;
		return packet;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeByte(type);
		switch(type)
		{
			case DECK_REMOVE:
				buffer.writeInt(itemIndex);
			case SPECIBUS_REMOVE:
				buffer.writeInt(specibusId);
				break;
			default:
			//case PORTFOLIO: case SPECIBUS_ADD:
			{
				if(nbt != null)
				{
					try
					{
						ByteArrayOutputStream bytes = new ByteArrayOutputStream();
						CompressedStreamTools.writeCompressed(nbt, bytes);
						buffer.writeBytes(bytes.toByteArray());
					} catch(IOException e)
					{
						throw new IllegalStateException(e);
					}
				}
			}
			break;
		}
	}
	
	public static SpecibusPacket decode(PacketBuffer buffer)
	{
		SpecibusPacket packet = new SpecibusPacket(buffer.readByte());
		
		switch(packet.type)
		{
			case DECK_REMOVE:
				packet.itemIndex = buffer.readInt();
			case SPECIBUS_REMOVE:
				packet.specibusId = buffer.readInt();
				break;
			default:
				if(buffer.readableBytes() > 0)
				{
					byte[] bytes = new byte[buffer.readableBytes()];
					buffer.readBytes(bytes);
					try
					{
						packet.nbt = CompressedStreamTools.readCompressed(new ByteArrayInputStream(bytes));
					} catch(IOException e)
					{
						e.printStackTrace();
						return null;
					}
				}
				break;
		}
		return packet;
	}
	
	@Override
	public void execute()
	{
		if(type == PORTFOLIO)
		{
			ArrayList<StrifeSpecibus> portfolio = StrifePortfolioHandler.createPortfolio(nbt);
			ClientPlayerData.playerPortfolio = portfolio;
		}
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		switch(type)
		{
			case PORTFOLIO:
			{
				//System.out.println("portfolio");
				ArrayList<StrifeSpecibus> portfolio = StrifePortfolioHandler.createPortfolio(nbt);
				//TODO some safety is needed here
				//MinestuckPlayerData.setStrifePortfolio(IdentifierHandler.encode(player), portfolio);
			}
			break;
			case SPECIBUS_ADD:
			{
				//System.out.println("add");
				//TODO safety needed
				StrifeSpecibus specibus = new StrifeSpecibus(nbt);
				PlayerSavedData.getData(player).addSpecibus(specibus);
			}
			break;
			case SPECIBUS_REMOVE:
				//System.out.println("remove");
				StrifeSpecibus specibus = StrifePortfolioHandler.getSpecibus(player, specibusId);
				//TODO
				//MinestuckPlayerData.removeStrifeSpecibus(IdentifierHandler.encode(player), specibusId);
				
				if(specibus != null)
					StrifePortfolioHandler.spawnItem(player, StrifePortfolioHandler.createSpecibusItem(specibus));
				break;
			case DECK_ADD:
			{
				//System.out.println("deck add");
				StrifePortfolioHandler.addItemToDeck(player);
			}
			break;
			case DECK_REMOVE:
				//System.out.println("deck remove");
				StrifePortfolioHandler.retrieveItem(player, specibusId, itemIndex);
				break;
		}
	}
	
}
