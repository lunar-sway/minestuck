package com.mraof.minestuck.item.weapon;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.google.common.collect.Sets;
import com.mraof.minestuck.util.MinestuckAchievementHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ItemHammer extends ItemWeapon
{	
	private int weaponDamage;
	private final EnumHammerType hammerType;
	public float efficiencyOnProperMaterial = 4.0F;
	public static final Set<Block> blocksEffectiveAgainst = Sets.newHashSet(new Block[] {Blocks.cobblestone, Blocks.double_stone_slab, Blocks.stone_slab, Blocks.stone, Blocks.sandstone, Blocks.mossy_cobblestone, Blocks.iron_ore, Blocks.iron_block, Blocks.coal_ore, Blocks.coal_block, Blocks.gold_block, Blocks.gold_ore, Blocks.diamond_ore, Blocks.diamond_block, Blocks.ice, Blocks.netherrack, Blocks.lapis_ore, Blocks.lapis_block, Blocks.redstone_ore, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail});

	
	public ItemHammer(EnumHammerType hammerType)
	{
		super(blocksEffectiveAgainst);
		
		this.hammerType = hammerType;
		this.setMaxDamage(hammerType.getMaxUses());
		this.efficiencyOnProperMaterial = hammerType.getEfficiencyOnProperMaterial();
		switch(hammerType)
		{
		case CLAW:
			this.setUnlocalizedName("clawHammer");
			break;
		case SLEDGE:
			this.setUnlocalizedName("sledgeHammer");
			break;
		case POGO:
			//10 Build Grist, 16 Shale
			this.setUnlocalizedName("pogoHammer");
			break;
		case TELESCOPIC:
			this.setUnlocalizedName("telescopicSassacrusher");
			break;
		case FEARNOANVIL:
			this.setUnlocalizedName("fearNoAnvil");
			break;
		case ZILLYHOO:
			this.setUnlocalizedName("zillyhooHammer");
			break;
		case POPAMATIC:
			this.setUnlocalizedName("popamaticVrillyhoo");
			break;
		case SCARLET:
			this.setUnlocalizedName("scarletZillyhoo");
			break;
		}
		this.weaponDamage = 3 + hammerType.getDamageVsEntity();
	}
	
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		if(this.hammerType.equals(EnumHammerType.CLAW))
			player.triggerAchievement(MinestuckAchievementHandler.getHammer);
	}
	
	@Override
	public boolean canHarvestBlock(Block block) 
	{
		return block != null && (block.getMaterial() == Material.iron || block.getMaterial() == Material.anvil || block.getMaterial() == Material.rock);
	}

	@Override
	public float getStrVsBlock(ItemStack itemStack, Block block)
	{
		return block != null && (block.getMaterial() == Material.iron || block.getMaterial() == Material.anvil || block.getMaterial() == Material.rock) ? this.efficiencyOnProperMaterial : super.getStrVsBlock(itemStack, block);
	}

	@Override
	public int getAttackDamage()
	{
		return this.weaponDamage;
	}

	@Override
	public int getItemEnchantability()
	{
		return this.hammerType.getEnchantability();
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
	{
		itemStack.damageItem(1, player);
		if(hammerType.equals(EnumHammerType.POGO))
		{
			target.motionY = Math.abs(player.motionY) + target.motionY + 0.5;
			player.motionY = 0;
			player.fallDistance = 0;
		}
		else if(hammerType.equals(EnumHammerType.SCARLET))
			target.setFire(50);
		else if (hammerType.equals(EnumHammerType.POPAMATIC) )
			target.attackEntityFrom(DamageSource.magic , (float) (player.worldObj.rand.nextInt(6)+1) * (player.worldObj.rand.nextInt(6)+1) );
		else if (hammerType.equals(EnumHammerType.FEARNOANVIL) && player.worldObj.rand.nextGaussian() > 0.9)	//Just a suggestion, keep it if you like it.
			target.addPotionEffect(new PotionEffect(2,100,3));	//Would prefer it being triggered by a critical hit instead, if it can.
		return true;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack itemStack, World world, Block par3, int par4, int par5, int par6, EntityLivingBase par7EntityLiving)
	{
		if ((double)par3.getBlockHardness(world, par4, par5, par6) != 0.0D)
			itemStack.damageItem(2, par7EntityLiving);
		
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
	public int getMaxItemUseDuration(ItemStack itemStack)
	{
		return Integer.MAX_VALUE;
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) 
	{
		if(world.getBlock(x, y, z) != Blocks.air)
		{
			if (hammerType.equals(EnumHammerType.POGO))
			{
				player.motionY = Math.abs(player.motionY) + 0.5;
				player.fallDistance = 0;
				stack.damageItem(1, player);
				return true;
			} 
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) 
	{
		switch(hammerType)
		{
		case CLAW:
			itemIcon = iconRegister.registerIcon("minestuck:ClawHammer");
			break;
		case SLEDGE:
			itemIcon = iconRegister.registerIcon("minestuck:SledgeHammer");
			break;
		case POGO:
			itemIcon = iconRegister.registerIcon("minestuck:PogoHammer");
			break;
		case TELESCOPIC:
			itemIcon = iconRegister.registerIcon("minestuck:TelescopicSassacrusher");
			break;
		case FEARNOANVIL:
			itemIcon = iconRegister.registerIcon("minestuck:FearNoAnvil");
			break	;
		case ZILLYHOO:
			itemIcon = iconRegister.registerIcon("minestuck:ZillyhooHammer");
			break;
		case POPAMATIC:
			itemIcon = iconRegister.registerIcon("minestuck:Vrillyhoo");
			break;
		case SCARLET:
			itemIcon = iconRegister.registerIcon("minestuck:ScarletZillyhoo");
			break;
		}
	}

}
