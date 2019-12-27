package com.mraof.minestuck.world.storage;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.tracker.PlayerTracker;
import com.mraof.minestuck.util.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.Constants;

import java.util.Objects;

public final class PlayerData
{
	final IdentifierHandler.PlayerIdentifier player;
	
	private final PlayerSavedData savedData;
	
	private final Echeladder echeladder;
	
	private boolean givenModus;
	private Modus modus;
	
	private Title title;
	
	public GristSet gristCache;
	public int color = ColorCollector.DEFAULT_COLOR;
	public long boondollars;
	public boolean effectToggle;
	
	PlayerData(PlayerSavedData savedData, IdentifierHandler.PlayerIdentifier player, MinecraftServer mcServer)
	{
		this.savedData = savedData;
		this.player = player;
		echeladder = new Echeladder(savedData, player);
	}
	
	PlayerData(PlayerSavedData savedData, CompoundNBT nbt, MinecraftServer mcServer)
	{
		this.savedData = savedData;
		this.player = IdentifierHandler.load(nbt, "player");
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
		
		echeladder = new Echeladder(savedData, player);
		echeladder.loadEcheladder(nbt);
	}
	
	CompoundNBT writeToNBT()
	{
		CompoundNBT nbt = new CompoundNBT();
		player.saveToNBT(nbt, "player");
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
			PlayerTracker.updateTitle(player.getPlayer(savedData.mcServer));
		} else throw new IllegalStateException("Can't set title for player "+player.getUsername()+" because they already have one");
	}
}