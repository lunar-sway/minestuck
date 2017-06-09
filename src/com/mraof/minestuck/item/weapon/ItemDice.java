package com.mraof.minestuck.item.weapon;
import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemDice extends ItemWeapon{
	public double MinimumDamageVsEntity;
	public double MaximumDamageVsEntity;
	public ItemDice(int maxUses, double minimumDamageVsEntity,double maximumDamageVsEntity , double weaponSpeed, int enchantability, int RequiredNumberOfDice, String name) {
		super(maxUses, minimumDamageVsEntity, weaponSpeed, enchantability, name);
		this.MaximumDamageVsEntity=maximumDamageVsEntity;
		this.MinimumDamageVsEntity=minimumDamageVsEntity;
		this.setMaxStackSize(RequiredNumberOfDice);
	}
	@Override
	protected double getAttackDamage(ItemStack stack){
		//done in a different method so that we change how multiple dice act in a different place than how a single dice acts..
		double totaldamage=0;
		int finaldamage;
		for (int i=0;i<this.maxStackSize;i++){
			totaldamage += getOneDieAttack();
		}
		totaldamage=totaldamage/this.maxStackSize;
		finaldamage=(int) Math.floor(totaldamage);
		return finaldamage;
	}
	protected boolean canAtack(ItemStack stack){
		if (stack.stackSize==this.maxStackSize){
			return true;
		}else{
			return false;
		}
	}
	protected double getOneDieAttack(){
		double Damage;
		double Rand;
		double DamageRange;
		DamageRange = MaximumDamageVsEntity-MinimumDamageVsEntity;
		Rand = Math.random()*(DamageRange);
		Damage =Rand+MinimumDamageVsEntity;
		return Damage;
	}
	protected double getOneDieAtackPreferLow(){
		double Damage;
		double Rand;
		double DamageRange;
		DamageRange = MaximumDamageVsEntity-MinimumDamageVsEntity;
		DamageRange = DamageRange*DamageRange;
		Rand =Math.sqrt(DamageRange*Math.random());
		Damage=Rand+MinimumDamageVsEntity;
		return Damage;
	}
	public boolean onEntitySwing(EntityLivingBase entityLiving,ItemStack stack){
		System.out.println(( entityLiving.getForward().xCoord/2)*(Math.random()*4-2));
		System.out.println(( entityLiving.getForward().yCoord/2)*(Math.random()*4-2));
		System.out.println(( entityLiving.getForward().zCoord/2)*(Math.random()*4-2));
		if(stack.stackSize==this.maxStackSize){
			Random random;
			for(int i=0;i<this.maxStackSize;i++){
				if(!entityLiving.world.isRemote){
					entityLiving.entityDropItem(new ItemStack(stack.getItem(),1), 1).setVelocity((entityLiving.getForward().xCoord/2)+(Math.random()-.5),(entityLiving.getForward().yCoord/2)+(Math.random()-.5),( entityLiving.getForward().zCoord/2)+(Math.random())-.5);
				}
			}
			if (entityLiving instanceof EntityPlayer){
				((EntityPlayer) entityLiving).inventory.removeStackFromSlot(((EntityPlayer) entityLiving).inventory.currentItem);				
			}
		}
		
		//continue everything else
		return false;
	}
}
