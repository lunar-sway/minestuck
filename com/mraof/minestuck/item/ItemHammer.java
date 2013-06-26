package com.mraof.minestuck.item;

import com.mraof.minestuck.CommonProxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;


public class ItemHammer extends ItemTool
{    
	private int weaponDamage;
	private final EnumHammerType hammerType;
    public float efficiencyOnProperMaterial = 4.0F;
    public static final Block[] blocksEffectiveAgainst = new Block[] {Block.cobblestone, Block.stoneDoubleSlab, Block.stoneSingleSlab, Block.stone, Block.sandStone, Block.cobblestoneMossy, Block.oreIron, Block.blockIron, Block.oreCoal, Block.blockGold, Block.oreGold, Block.oreDiamond, Block.blockDiamond, Block.ice, Block.netherrack, Block.oreLapis, Block.blockLapis, Block.oreRedstone, Block.oreRedstoneGlowing, Block.rail, Block.railDetector, Block.railPowered};

	
    public ItemHammer(int id, EnumHammerType hammerType)
	{
		super(id, 3, EnumToolMaterial.GOLD, blocksEffectiveAgainst);
		
		this.hammerType = hammerType;
		this.maxStackSize = 1;
		this.setMaxDamage(hammerType.getMaxUses());
		this.setCreativeTab(CreativeTabs.tabCombat);
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
//			this.setIconIndex(5);
			break;
		case POPAMATIC:
			this.setUnlocalizedName("popamaticVrillyhoo");
//			this.setIconIndex(6);
			break;
		case SCARLET:
			this.setUnlocalizedName("scarletZillyhoo");
//			this.setIconIndex(7);
			break;
		}
		this.weaponDamage = 3 + hammerType.getDamageVsEntity();
	}
	
    @Override
    public boolean canHarvestBlock(Block block) 
    {
		return block != null && (block.blockMaterial == Material.iron || block.blockMaterial == Material.anvil || block.blockMaterial == Material.rock);
    }

	public float getStrVsBlock(ItemStack itemStack, Block block)
	{
		return block != null && (block.blockMaterial == Material.iron || block.blockMaterial == Material.anvil || block.blockMaterial == Material.rock) ? this.efficiencyOnProperMaterial : super.getStrVsBlock(itemStack, block);
	}
	
	public int getDamageVsEntity(Entity par1Entity)
	{
	    return hammerType.equals(hammerType.POPAMATIC) ? (int) (Math.pow(( Math.random() * 5), 2)) : this.weaponDamage;
	}
	
	public int getItemEnchantability()
	{
		return this.hammerType.getEnchantability();
	}
	 
	public boolean hitEntity(ItemStack itemStack, EntityLiving target, EntityLiving player)
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

	public boolean onBlockDestroyed(ItemStack itemStack, World world, int par3, int par4, int par5, int par6, EntityLiving par7EntityLiving)
	{
		if ((double)Block.blocksList[par3].getBlockHardness(world, par4, par5, par6) != 0.0D)
		{
			itemStack.damageItem(2, par7EntityLiving);
		}
		
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
		if(world.getBlockId(x, y, z) != 0){
			if(hammerType.equals(hammerType.POGO))
			{
				player.motionY = Math.abs(player.motionY) + 0.5;
				player.fallDistance = 0;
				stack.damageItem(1, player);
				return true;
			} 
			else return false;
		}
		else return false;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) 
	{
		switch(hammerType)
		{
		case CLAW:
			itemIcon = iconRegister.registerIcon("Minestuck:ClawHammer");
//			this.setIconIndex(0);
			break;
		case SLEDGE:
			itemIcon = iconRegister.registerIcon("Minestuck:SledgeHammer");
//			this.setIconIndex(1);
			break;
		case POGO:
			itemIcon = iconRegister.registerIcon("Minestuck:PogoHammer");
//			this.setIconIndex(2);
			break;
		case TELESCOPIC:
			itemIcon = iconRegister.registerIcon("Minestuck:TelescopicSassacrusher");
//			this.setIconIndex(3);
			break;
		case FEARNOANVIL:
			itemIcon = iconRegister.registerIcon("Minestuck:FearNoAnvil");
//			this.setIconIndex(4);
			break	;
		case ZILLYHOO:
			itemIcon = iconRegister.registerIcon("Minestuck:ZillyhooHammer");
//			this.setIconIndex(5);
			break;
		case POPAMATIC:
			itemIcon = iconRegister.registerIcon("Minestuck:Vrillyhoo");
//			this.setIconIndex(6);
			break;
		case SCARLET:
			itemIcon = iconRegister.registerIcon("Minestuck:ScarletZillyhoo");
//			this.setIconIndex(7);
			break;
		}
	}

}
