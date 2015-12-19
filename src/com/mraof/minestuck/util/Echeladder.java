package com.mraof.minestuck.util;

import java.util.UUID;

import com.mraof.minestuck.tracker.MinestuckPlayerTracker;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class Echeladder
{
	
	public static final int RUNG_COUNT = 50;
	public static final byte UNDERLING_BONUS_OFFSET = 0;
	
	private static final UUID echeladderHealthBoostModifierUUID = UUID.fromString("5b49a45b-ff22-4720-8f10-dfd745c3abb8");
	private static final UUID echeladderDamageBoostModifierUUID = UUID.fromString("a74176fd-bf4e-4153-bb68-197dbe4109b2");
	private static final int[] UNDERLING_BONUSES = new int[] {10, 120, 450, 1200};	//Bonuses for first time killing an underling
	
	private String name;
	private int rung;
	private int progress;
	
	private boolean[] underlingBonuses = new boolean[UNDERLING_BONUSES.length];
	
	public Echeladder(String name)
	{
		this.name = name;
	}
	
	private int getRungProgressReq()
	{
		return (int) (Math.pow(1.4, rung)*5);
	}
	
	public void increaseEXP(int exp)
	{
		int max = getRungProgressReq();
		if(rung >= RUNG_COUNT || exp < max/50)
			return;
		
		int prevRung = rung;
		increasment:
		{
			while(progress + exp >= max)
			{
				rung++;
				exp -= (max - progress);
				progress = 0;
				max = getRungProgressReq();
				if(rung >= RUNG_COUNT)
					break increasment;
			}
			if(exp >= max/50)
				progress += exp;
		}
		
		EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(UsernameHandler.decode(name));
		if(player != null)
		{
			MinestuckPlayerTracker.updateEcheladder(player);
			if(rung != prevRung)
				updateEcheladderBonuses(player);
		}
	}
	
	public void checkBonus(byte type)
	{
		if(type >= UNDERLING_BONUS_OFFSET && type < UNDERLING_BONUS_OFFSET + underlingBonuses.length && !underlingBonuses[type])
		{
			underlingBonuses[type] = true;
			increaseEXP(UNDERLING_BONUSES[type]);
		}
	}
	
	public int getRung()
	{
		return rung;
	}
	
	public float getProgress()
	{
		return ((float) progress)/getRungProgressReq();
	}
	
	public void updateEcheladderBonuses(EntityPlayer player)
	{
		int healthBonus = (int) (40*(rung/(float) Echeladder.RUNG_COUNT));	//At max rung, the player will have three rows of hearts
		float damageBonus = rung*0.1F;
		
		Debug.printf("Health bonus for rung %d: %d", rung + 1, healthBonus);
		updateAttribute(player.getEntityAttribute(SharedMonsterAttributes.maxHealth), new AttributeModifier(echeladderHealthBoostModifierUUID, "Echeladder Health Boost", healthBonus, 0));
		updateAttribute(player.getEntityAttribute(SharedMonsterAttributes.attackDamage), new AttributeModifier(echeladderDamageBoostModifierUUID, "Echeladder Damage Boost", damageBonus, 2).setSaved(false));
	}
	
	public void updateAttribute(IAttributeInstance attribute, AttributeModifier modifier)
	{
		if(attribute.func_180374_a(modifier))
			attribute.removeModifier(attribute.getModifier(modifier.getID()));
		attribute.applyModifier(modifier);
	}
	
	protected void saveEcheladder(NBTTagCompound nbt)
	{
		nbt.setInteger("rung", rung);
		nbt.setInteger("rungProgress", progress);
		
		byte[] bonuses = new byte[underlingBonuses.length];	//Booleans would be stored as bytes anyways
		for(int i = 0; i < underlingBonuses.length; i++)
			bonuses[i] = (byte) (underlingBonuses[i] ? 1 : 0);
		nbt.setByteArray("rungBonuses", bonuses);
	}
	
	protected void loadEcheladder(NBTTagCompound nbt)
	{
		rung = nbt.getInteger("rung");
		progress = nbt.getInteger("rungProgress");
		
		byte[] bonuses = nbt.getByteArray("rungBonuses");
		for(int i = 0; i < underlingBonuses.length && i < bonuses.length; i++)
			underlingBonuses[i] = bonuses[i] != 0;
	}
}