package com.mraof.minestuck.entity;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.carapacian.*;
import com.mraof.minestuck.entity.consort.*;
import com.mraof.minestuck.entity.item.*;
import com.mraof.minestuck.entity.underling.*;

public final class MinestuckEntities
{
	public static int currentEntityIdOffset = 0;

	public static void registerEntities()
	{
		//register entities
		registerEntity(EntitySalamander.class, "Salamander");
		registerEntity(EntityNakagator.class, "Nakagator");
		registerEntity(EntityIguana.class, "Iguana");
		registerEntity(EntityTurtle.class, "Turtle");
		registerEntity(EntityImp.class, "Imp");
		registerEntity(EntityOgre.class, "Ogre");
		registerEntity(EntityBasilisk.class, "Basilisk");
		registerEntity(EntityGiclops.class, "Giclops");
		registerEntity(EntityWyrm.class, "Wyrm");
		registerEntity(EntityBlackPawn.class, "DersitePawn");
		registerEntity(EntityWhitePawn.class, "ProspitianPawn");
		registerEntity(EntityBlackBishop.class, "DersiteBishop");
		registerEntity(EntityWhiteBishop.class, "ProspitianBishop");
		registerEntity(EntityBlackRook.class, "DersiteRook");
		registerEntity(EntityWhiteRook.class, "ProspitianRook");
		registerEntity(EntityDecoy.class, "PlayerDecoy");
		registerEntity(EntityMetalBoat.class, "MetalBoat");
		registerEntity(EntityGrist.class, "Grist", 512, 1, true);
		registerEntity(EntityVitalityGel.class, "VitalityGel", 512, 1, true);
	}

	//registers entity with forge and minecraft, and increases currentEntityIdOffset by one in order to prevent id collision
	public static void registerEntity(Class<? extends Entity> entityClass, String name)
	{
		registerEntity(entityClass, name, 80, 3, true);
	}

	public static void registerEntity(Class<? extends Entity> entityClass, String name, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(entityClass, name, currentEntityIdOffset, Minestuck.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
		currentEntityIdOffset++;
	}

}
