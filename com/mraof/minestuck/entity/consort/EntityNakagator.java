/**
 * 
 */
package com.mraof.minestuck.entity.consort;

import net.minecraft.entity.EntityCreature;
import net.minecraft.world.World;

/**
 * @author mraof
 *
 */
public class EntityNakagator extends EntityCreature 
{
	public EntityNakagator(World world) 
	{
		super(world);
		moveSpeed = 2.5F;
        setSize(0.6F, 1.5F);
        this.experienceValue = 1;
		texture = "/mods/Minestuck/textures/mobs/Nakagator.png";
	}
	@Override
	public int getMaxHealth() 
	{
		return 10;
	}

}
