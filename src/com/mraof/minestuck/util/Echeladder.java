package com.mraof.minestuck.util;

import java.util.UUID;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class Echeladder
{
	
	public static final int RUNG_COUNT = 50;
	public static final byte UNDERLING_BONUS_OFFSET = 0;
	public static final byte ALCHEMY_BONUS_OFFSET = 15;
	public static final double MIN_PROGRESS_MODIFIER = 1/100D;
	
	private static final UUID echeladderHealthBoostModifierUUID = UUID.fromString("5b49a45b-ff22-4720-8f10-dfd745c3abb8");
	private static final UUID echeladderDamageBoostModifierUUID = UUID.fromString("a74176fd-bf4e-4153-bb68-197dbe4109b2");
	private static final int[] UNDERLING_BONUSES = new int[] {10, 120, 450, 2500};	//Bonuses for first time killing an underling
	private static final int[] ALCHEMY_BONUSES = new int[] {30, 400, 3000};
	
	public static void increaseProgress(EntityPlayerMP player, int progress)
	{
		MinestuckPlayerData.getData(player).echeladder.increaseProgress(progress);
	}
	
	private String name;
	private int rung;
	private int progress;
	
	private boolean[] underlingBonuses = new boolean[UNDERLING_BONUSES.length];
	private boolean[] alchemyBonuses = new boolean[ALCHEMY_BONUSES.length];
	
	public Echeladder(String name)
	{
		this.name = name;
	}
	
	private int getRungProgressReq()
	{
		return (int) (Math.pow(1.4, rung)*8);
	}
	
	public void increaseProgress(int exp)
	{
		SburbConnection c = SkaianetHandler.getMainConnection(name, true);
		int topRung = c != null && c.enteredGame() ? RUNG_COUNT : MinestuckConfig.preEntryRungLimit;
		int expReq = getRungProgressReq();
		if(rung >= topRung || exp < expReq*MIN_PROGRESS_MODIFIER){Debug.print("Skipped progress: "+exp);
			return;}
		
		int prevRung = rung;
		int prevExp = exp;
		
		increasment:
		{
			while(progress + exp >= expReq)
			{
				rung++;
				exp -= (expReq - progress);
				progress = 0;
				expReq = getRungProgressReq();
				if(rung >= topRung)
					break increasment;
				if(rung > prevRung + 1)
					exp = (int) (exp/1.5);
			}
			if(exp >= expReq/50)
				progress += exp;
		}
		Debug.print("Gained progress: "+prevExp+", rung increased by " +(rung - prevRung));
		
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
		if(type >= UNDERLING_BONUS_OFFSET && type < UNDERLING_BONUS_OFFSET + underlingBonuses.length && !underlingBonuses[type - UNDERLING_BONUS_OFFSET])
		{
			underlingBonuses[type - UNDERLING_BONUS_OFFSET] = true;
			increaseProgress(UNDERLING_BONUSES[type - UNDERLING_BONUS_OFFSET]);
		} else if(type >= ALCHEMY_BONUS_OFFSET && type < ALCHEMY_BONUS_OFFSET + alchemyBonuses.length && !alchemyBonuses[type - ALCHEMY_BONUS_OFFSET])
		{
			alchemyBonuses[type - ALCHEMY_BONUS_OFFSET] = true;
			increaseProgress(ALCHEMY_BONUSES[type - ALCHEMY_BONUS_OFFSET]);
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
	
	public float getUnderlingDamageModifier()
	{
		return 1 + rung*0.1F;
	}
	
	public float getUnderlingProtectionModifier()
	{
		return 1/(rung*0.05F + 1);
	}
	
	public void updateEcheladderBonuses(EntityPlayer player)
	{
		int healthBonus = healthBoost(rung);
		float damageBonus = attackBonus(rung);
		
		Debug.printf("Health bonus for rung %d: %d", rung + 1, healthBonus);
		updateAttribute(player.getEntityAttribute(SharedMonsterAttributes.maxHealth), new AttributeModifier(echeladderHealthBoostModifierUUID, "Echeladder Health Boost", healthBonus, 0).setSaved(false));
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
		
		byte[] bonuses = new byte[ALCHEMY_BONUS_OFFSET + alchemyBonuses.length];	//Booleans would be stored as bytes anyways
		for(int i = 0; i < underlingBonuses.length; i++)
			bonuses[i + UNDERLING_BONUS_OFFSET] = (byte) (underlingBonuses[i] ? 1 : 0);
		for(int i = 0; i < alchemyBonuses.length; i++)
			bonuses[i + ALCHEMY_BONUS_OFFSET] = (byte) (alchemyBonuses[i] ? 1 : 0);
		nbt.setByteArray("rungBonuses", bonuses);
	}
	
	protected void loadEcheladder(NBTTagCompound nbt)
	{
		rung = nbt.getInteger("rung");
		progress = nbt.getInteger("rungProgress");
		
		byte[] bonuses = nbt.getByteArray("rungBonuses");
		for(int i = 0; i < underlingBonuses.length && i + UNDERLING_BONUS_OFFSET < bonuses.length; i++)
			underlingBonuses[i] = bonuses[i + UNDERLING_BONUS_OFFSET] != 0;
		for(int i = 0; i < alchemyBonuses.length && i + ALCHEMY_BONUS_OFFSET < bonuses.length; i++)
			alchemyBonuses[i] = bonuses[i + ALCHEMY_BONUS_OFFSET] != 0;
	}
	
	public static float attackBonus(int rung)
	{
		return rung*0.05F;
	}
	
	public static int healthBoost(int rung)
	{
		return (int) (40*(rung/(float) Echeladder.RUNG_COUNT));	//At max rung, the player will have three rows of hearts
	}
}