package com.mraof.minestuck.item;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ItemHammer extends ItemWeapon
{    
	private int weaponDamage;
	private final EnumHammerType hammerType;
    public float efficiencyOnProperMaterial = 4.0F;
    public static final Block[] blocksEffectiveAgainst = new Block[] {Block.cobblestone, Block.stoneDoubleSlab, Block.stoneSingleSlab, Block.stone, Block.sandStone, Block.cobblestoneMossy, Block.oreIron, Block.blockIron, Block.oreCoal, Block.blockGold, Block.oreGold, Block.oreDiamond, Block.blockDiamond, Block.ice, Block.netherrack, Block.oreLapis, Block.blockLapis, Block.oreRedstone, Block.oreRedstoneGlowing, Block.rail, Block.railDetector, Block.railPowered};

	
    public ItemHammer(int id, EnumHammerType hammerType)
	{
		super(id, blocksEffectiveAgainst);
		
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
    public boolean canHarvestBlock(Block block) 
    {
		return block != null && (block.blockMaterial == Material.iron || block.blockMaterial == Material.anvil || block.blockMaterial == Material.rock);
    }

    @Override
	public float getStrVsBlock(ItemStack itemStack, Block block)
	{
		return block != null && (block.blockMaterial == Material.iron || block.blockMaterial == Material.anvil || block.blockMaterial == Material.rock) ? this.efficiencyOnProperMaterial : super.getStrVsBlock(itemStack, block);
	}

    @Override
	public int getAttackDamage()
	{
	    return hammerType.equals(hammerType.POPAMATIC) ? (int) (Math.pow(( Math.random() * 5), 2)) : this.weaponDamage;
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
		return true;
	}

    @Override
	public boolean onBlockDestroyed(ItemStack itemStack, World world, int par3, int par4, int par5, int par6, EntityLivingBase par7EntityLiving)
	{
		if ((double)Block.blocksList[par3].getBlockHardness(world, par4, par5, par6) != 0.0D)
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
		if(world.getBlockId(x, y, z) != 0)
		{
			if(hammerType.equals(hammerType.POGO))
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
	public void registerIcons(IconRegister iconRegister) 
	{
		switch(hammerType)
		{
		case CLAW:
			itemIcon = iconRegister.registerIcon("Minestuck:ClawHammer");
			break;
		case SLEDGE:
			itemIcon = iconRegister.registerIcon("Minestuck:SledgeHammer");
			break;
		case POGO:
			itemIcon = iconRegister.registerIcon("Minestuck:PogoHammer");
			break;
		case TELESCOPIC:
			itemIcon = iconRegister.registerIcon("Minestuck:TelescopicSassacrusher");
			break;
		case FEARNOANVIL:
			itemIcon = iconRegister.registerIcon("Minestuck:FearNoAnvil");
			break	;
		case ZILLYHOO:
			itemIcon = iconRegister.registerIcon("Minestuck:ZillyhooHammer");
			break;
		case POPAMATIC:
			itemIcon = iconRegister.registerIcon("Minestuck:Vrillyhoo");
			break;
		case SCARLET:
			itemIcon = iconRegister.registerIcon("Minestuck:ScarletZillyhoo");
			break;
		}
	}

}
