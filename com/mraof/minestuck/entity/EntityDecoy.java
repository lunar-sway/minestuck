package com.mraof.minestuck.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityDecoy extends EntityMinestuck{

	
	public EntityDecoy(World world){
		super(world);
	}
	
	public EntityDecoy(World par1World, EntityPlayer player) {
		super(par1World);
		this.posX = player.posX;
		this.posY = player.posY;
		this.posZ = player.posZ;
		this.setCustomNameTag(player.username);
	}

	@Override
	protected float getMaximumHealth() {
		return 20;
	}

	@Override
	public String getTexture() {
		return "";
	}

}
