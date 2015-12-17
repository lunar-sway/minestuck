package com.mraof.minestuck.util;

import net.minecraft.nbt.NBTTagCompound;

public class Echeladder
{
	
	public static final int RUG_COUNT = 50;
	public static final byte UNDERLING_BONUS_OFFSET = 0;
	
	private static final int[] UNDERLING_BONUSES = new int[] {10, 200, 400, 1200};	//Bonuses for first time killing an underling
	
	private int rug;
	private int progress;
	
	private boolean[] underlingBonuses = new boolean[UNDERLING_BONUSES.length];
	
	private int getRugProgressReq()
	{
		return (int) (Math.pow(1.4, rug)*5);
	}
	
	public void increaseXP(int xp)
	{
		int max = getRugProgressReq();
		if(rug >= RUG_COUNT || xp < max/50)
			return;
		
		if(progress + xp >= max)
		{
			rug++;
			xp -= (max - progress);
			progress = 0;
			
			if(xp >= getRugProgressReq()/50)
				progress = xp;
		} else progress += xp;
		
		//Notify player
	}
	
	public void checkBonus(byte type)
	{
		if(type >= UNDERLING_BONUS_OFFSET && type < UNDERLING_BONUS_OFFSET + underlingBonuses.length && !underlingBonuses[type])
		{
			underlingBonuses[type] = true;
			increaseXP(UNDERLING_BONUSES[type]);
		}
	}
	
	public int getRug()
	{
		return rug;
	}
	
	public float getProgress()
	{
		return ((float) progress)/getRugProgressReq();
	}
	
	protected void saveEcheladder(NBTTagCompound nbt)
	{
		nbt.setInteger("rug", rug);
		nbt.setInteger("rugProgress", progress);
		
		byte[] bonuses = new byte[underlingBonuses.length];	//Booleans would be stored as bytes anyways
		for(int i = 0; i < underlingBonuses.length; i++)
			bonuses[i] = (byte) (underlingBonuses[i] ? 1 : 0);
		nbt.setByteArray("rugBonuses", bonuses);
	}
	
	protected void loadEcheladder(NBTTagCompound nbt)
	{
		rug = nbt.getInteger("rug");
		progress = nbt.getInteger("rugProgress");
		
		byte[] bonuses = nbt.getByteArray("rugBonuses");
		for(int i = 0; i < underlingBonuses.length && i < bonuses.length; i++)
			underlingBonuses[i] = bonuses[i] != 0;
	}
}