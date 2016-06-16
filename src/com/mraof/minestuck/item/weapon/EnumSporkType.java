package com.mraof.minestuck.item.weapon;

public enum EnumSporkType 
{
	SPOON_WOOD(59, 2.0D, 5, true, "woodenSpoon"),
	SPOON_SILVER(250, 2.5D, 12, true, "silverSpoon"),
	CROCKER(512, 4.0D, 15, true, "crockerSpork"),
	SKAIA(2048, 8.5D, 10, false, "skaiaFork"),
	FORK(100, 4.0D, 3, false, "fork"),
	SPORK(120, 4.5D, -2.3D, 5, true, "spork");
	
	public static final double SPOON_ATTACK_SPEED = -2.2D;
	public static final double FORK_ATTACK_SPEED = -2.6D;
	private final int maxUses;
	private final double damageVsEntity;
	private final double attackSpeed;
	private final int enchantability;
	private final boolean isSpoon;
	private final String name;
	
	private EnumSporkType(int maxUses, double damageVsEntity, int enchantability, boolean isSpoon, String name)
	{
		this(maxUses, damageVsEntity, isSpoon ? SPOON_ATTACK_SPEED : FORK_ATTACK_SPEED, enchantability, isSpoon, name);
	}
	
	private EnumSporkType(int maxUses, double damageVsEntity, double attackSpeed, int enchantability, boolean isSpoon, String name)
	{
		this.maxUses = maxUses;
		this.damageVsEntity = damageVsEntity;
		this.attackSpeed = attackSpeed;
		this.enchantability = enchantability;
		this.isSpoon = isSpoon;
		this.name = name;
	}
	
	public int getMaxUses() 
	{
		return maxUses;
	}
	
	public double getDamageVsEntity() 
	{
		return damageVsEntity;
	}
	
	public double getAttackSpeed()
	{
		return attackSpeed;
	}
	
	public int getEnchantability() 
	{
		return enchantability;
	}
	public boolean getIsSpoon()
	{
		return isSpoon;
	}
	
	public String getUnlocalizedName()
	{
		return name;
	}
}