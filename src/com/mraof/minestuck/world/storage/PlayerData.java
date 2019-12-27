package com.mraof.minestuck.world.storage;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.PlayerDataPacket;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.tracker.PlayerTracker;
import com.mraof.minestuck.util.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public final class PlayerData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	final IdentifierHandler.PlayerIdentifier identifier;
	
	private final PlayerSavedData savedData;
	private final Echeladder echeladder;
	private int color = ColorCollector.DEFAULT_COLOR;
	
	private boolean givenModus;
	private Modus modus;
	
	private Title title;
	
	public GristSet gristCache;
	public long boondollars;
	public boolean effectToggle;
	
	private boolean hasLoggedIn;
	
	PlayerData(PlayerSavedData savedData, IdentifierHandler.PlayerIdentifier player, MinecraftServer mcServer)
	{
		this.savedData = savedData;
		this.identifier = player;
		echeladder = new Echeladder(savedData, player);
		gristCache = new GristSet(GristTypes.BUILD, 20);
		hasLoggedIn = false;
	}
	
	PlayerData(PlayerSavedData savedData, CompoundNBT nbt, MinecraftServer mcServer)
	{
		this.savedData = savedData;
		this.identifier = IdentifierHandler.load(nbt, "player");
		if (nbt.contains("grist_cache"))
		{
			this.gristCache = GristSet.read(nbt.getList("grist_cache", Constants.NBT.TAG_COMPOUND));
		}
		if (nbt.contains("titleClass"))
			this.title = new Title(EnumClass.getClassFromInt(nbt.getByte("titleClass")), EnumAspect.getAspectFromInt(nbt.getByte("titleAspect")));	//TODO Should be read in the title class
		if (nbt.contains("modus"))
		{
			this.modus = CaptchaDeckHandler.readFromNBT(nbt.getCompound("modus"), savedData);
			givenModus = true;
		}
		else givenModus = nbt.getBoolean("givenModus");
		if (nbt.contains("color"))
			this.color = nbt.getInt("color");
		boondollars = nbt.getLong("boondollars");
		effectToggle = nbt.getBoolean("effectToggle");
		
		echeladder = new Echeladder(savedData, identifier);
		echeladder.loadEcheladder(nbt);
		
		hasLoggedIn = true;
	}
	
	CompoundNBT writeToNBT()
	{
		CompoundNBT nbt = new CompoundNBT();
		identifier.saveToNBT(nbt, "player");
		if (this.gristCache != null)
		{
			nbt.put("grist_cache", gristCache.write(new ListNBT()));
		}
		if (this.title != null)
		{
			nbt.putByte("titleClass", (byte) this.title.getHeroClass().ordinal());	//TODO Should be written in the title object
			nbt.putByte("titleAspect", (byte) this.title.getHeroAspect().ordinal());
		}
		if (this.modus != null)
			nbt.put("modus", CaptchaDeckHandler.writeToNBT(modus));
		else nbt.putBoolean("givenModus", givenModus);
		nbt.putInt("color", this.color);
		nbt.putLong("boondollars", boondollars);
		nbt.putBoolean("effectToggle", effectToggle);
		
		echeladder.saveEcheladder(nbt);
		return nbt;
	}
	
	private void markDirty()
	{
		savedData.markDirty();
	}
	
	public Echeladder getEcheladder()
	{
		return echeladder;
	}
	
	public int getColor()
	{
		return color;
	}
	
	public void trySetColor(int color)
	{
		if(SburbHandler.canSelectColor(identifier, savedData.mcServer) && this.color != color)
		{
			this.color = color;
			markDirty();
			
			ServerPlayerEntity playerEntity = identifier.getPlayer(savedData.mcServer);
			if(playerEntity != null)
			{
				PlayerDataPacket packet = PlayerDataPacket.color(this.color);
				MSPacketHandler.sendToPlayer(packet, playerEntity);
			}
		}
	}
	
	public Modus getModus()
	{
		return modus;
	}
	
	public void setModus(Modus modus)
	{
		if(this.modus != modus)
		{
			this.modus = modus;
			if(modus != null)
				setGivenModus();
			markDirty();
		}
	}
	
	private void setGivenModus()
	{
		givenModus = true;
		markDirty();
	}
	
	public boolean hasGivenModus()
	{
		return givenModus;
	}
	
	public Title getTitle()
	{
		return title;
	}
	
	public void setTitle(Title newTitle)
	{
		if(title == null)
		{
			title = Objects.requireNonNull(newTitle);
			markDirty();
			sendTitle(identifier.getPlayer(savedData.mcServer));
		} else throw new IllegalStateException("Can't set title for player "+ identifier.getUsername()+" because they already have one");
	}
	
	public void onPlayerLoggedIn(ServerPlayerEntity player)
	{
		getEcheladder().updateEcheladderBonuses(player);
		
		if(getModus() == null && MinestuckConfig.defaultModusTypes.length > 0 && !hasGivenModus())
		{
			int index = player.world.rand.nextInt(MinestuckConfig.defaultModusTypes.length);
			Modus modus = CaptchaDeckHandler.createServerModus(new ResourceLocation(MinestuckConfig.defaultModusTypes[index]), savedData);
			if(modus != null)
			{
				modus.initModus(player, null, MinestuckConfig.initialModusSize.get());
				CaptchaDeckHandler.setModus(player, modus);
			} else LOGGER.warn("Couldn't create a modus by name {}.", MinestuckConfig.defaultModusTypes[index]);
		}
		
		if(getModus() != null)
		{
			Modus modus = getModus();
			MSPacketHandler.sendToPlayer(CaptchaDeckPacket.data(CaptchaDeckHandler.writeToNBT(modus)), player);
		}
		
		PlayerTracker.updateGristCache(player.getServer(), identifier);
		sendTitle(player);
		echeladder.sendDataPacket(player, true);
		MSPacketHandler.sendToPlayer(PlayerDataPacket.boondollars(PlayerSavedData.getData(player).boondollars), player);
		
		if(hasLoggedIn && !player.isSpectator())
			MSPacketHandler.sendToPlayer(PlayerDataPacket.color(), player);
		else
		{
			PlayerDataPacket packet = PlayerDataPacket.color(PlayerSavedData.getData(player).getColor());
			MSPacketHandler.sendToPlayer(packet, player);
		}
		
		hasLoggedIn = true;
	}
	
	private void sendTitle(ServerPlayerEntity player)
	{
		Title newTitle = getTitle();
		if(newTitle == null || player == null)
			return;
		PlayerDataPacket packet = PlayerDataPacket.title(newTitle);
		MSPacketHandler.sendToPlayer(packet, player);
	}
}