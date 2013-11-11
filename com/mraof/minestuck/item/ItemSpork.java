package com.mraof.minestuck.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

//I called it a spork because it includes both
public class ItemSpork extends ItemWeapon 
{
	private int weaponDamage;
	private final EnumSporkType sporkType;
	public float efficiencyOnProperMaterial = 4.0F;
	/**
	 * whether it's a spoon or a fork, unused for the crocker spork, as it depends on the meta.
	 */
	public boolean isSpoon;
	private Icon[] crockerTypes = new Icon[2];

	public ItemSpork(int id, EnumSporkType sporkType) 
	{
		super(id);
		this.isSpoon = sporkType.getIsSpoon();
		this.sporkType = sporkType;
		this.maxStackSize = 1;
		this.setMaxDamage(sporkType.getMaxUses());
		this.setCreativeTab(Minestuck.tabMinestuck);
		switch(sporkType)
		{
		case CROCKER:
			this.setUnlocalizedName("crockerSpork");
			//			this.setIconIndex(23);
			break;
		case SKAIA:
			this.setUnlocalizedName("skaiaFork");
			//			this.setIconIndex(25);
			break;
		}
		this.weaponDamage = 2 + sporkType.getDamageVsEntity();
	}
	
	@Override
	public int getAttackDamage() 
	{
		return isSpoon ? weaponDamage : weaponDamage + 2;
	}
	
	@Override
	public int getItemEnchantability()
	{
		return this.sporkType.getEnchantability();
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
	{
		if(sporkType.equals(sporkType.CROCKER) && !isSpoon(itemStack)){
			target.hurtResistantTime = 0;	//A somewhat hackish way, but I find attributes too complicated, for now.
			target.attackEntityFrom(DamageSource.causeMobDamage(player), 2F);
		}
			itemStack.damageItem(isSpoon(itemStack) ? 1 : 2, player);
		return true;
	}

	public boolean isSpoon(ItemStack itemStack) {
		if(sporkType.equals(sporkType.CROCKER))
			return itemStack.stackTagCompound == null?true:itemStack.stackTagCompound.getBoolean("isSpoon");
		else return isSpoon;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack itemStack, World world, int par3, int par4, int par5, int par6, EntityLivingBase par7EntityLiving)
	{
		if ((double)Block.blocksList[par3].getBlockHardness(world, par4, par5, par6) != 0.0D)
		{
			itemStack.damageItem(2, par7EntityLiving);
		}

		return true;
	}

	@Override	
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
	public int getMaxItemUseDuration(ItemStack itemStack)
	{
		return 72000;
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) 
	{
		if(!world.isRemote)
			if(sporkType.equals(sporkType.CROCKER)) {
				if(stack.stackTagCompound.getByte("delay") > 0)
					return stack;
				else stack.stackTagCompound.setByte("delay", (byte) 10);
				
				stack.stackTagCompound.setBoolean("isSpoon", !stack.stackTagCompound.getBoolean("isSpoon"));
				
				
				
				if(!stack.stackTagCompound.hasKey("AttributeModifiers"))
					stack.stackTagCompound.setTag("AttributeModifiers", new NBTTagList());
				NBTTagList list = stack.stackTagCompound.getTagList("AttributeModifiers");
				boolean found = false;
				for(int i = 0; i < list.tagCount(); i++) {
					NBTTagCompound nbt = (NBTTagCompound) list.tagAt(i);
					if(nbt.getString("AttributeName").equals(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName())) {
						nbt.setDouble("Amount", isSpoon(stack)?3:5);
						found = true;
						break;
					}
				}
				
				if(!found) {
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setString("AttributeName", SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
					nbt.setLong("UUIDMost", field_111210_e.getMostSignificantBits());
					nbt.setLong("UUIDLeast", field_111210_e.getLeastSignificantBits());
					nbt.setString("Name", "Tool Modifier");
					nbt.setDouble("Amount", isSpoon(stack)?3:5);
					nbt.setInteger("Operation", 0);
					list.appendTag(nbt);
				}
			}
		return stack;
	}
	
	@Override
	public Icon getIcon(ItemStack stack, int pass) {
		return getIconIndex(stack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconIndex(ItemStack stack) {
		if(sporkType.equals(sporkType.CROCKER))
			return crockerTypes[isSpoon(stack)?0:1];
		else return itemIcon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) 
	{
		switch(sporkType)
		{
		case CROCKER:
			crockerTypes[0] = iconRegister.registerIcon("minestuck:CrockerSpoon");
			crockerTypes[1] = iconRegister.registerIcon("minestuck:CrockerFork");
			break;
		case SKAIA:
			itemIcon = iconRegister.registerIcon("minestuck:SkaianFork");
			break;
		}
	}
	
	public void checkTagCompound(ItemStack stack) {
		if(stack.stackTagCompound == null)
			stack.stackTagCompound = new NBTTagCompound();
		if(!stack.stackTagCompound.hasKey("isSpoon"))
			stack.stackTagCompound.setBoolean("isSpoon", true);
		if(!stack.stackTagCompound.hasKey("delay"))
			stack.stackTagCompound.setByte("delay", (byte) 0);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if(this.sporkType != EnumSporkType.CROCKER)
			return getUnlocalizedName();
		else return "item."+(isSpoon(stack)?"crockerSpoon":"crockerFork");
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
		checkTagCompound(stack);
		
		if(stack.stackTagCompound.getByte("delay") > 0)
			stack.stackTagCompound.setByte("delay", (byte) (stack.stackTagCompound.getByte("delay")-1));
	}
	
}
