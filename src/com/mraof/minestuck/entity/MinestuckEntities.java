package com.mraof.minestuck.entity;

import com.mraof.minestuck.entity.item.EntityCrewPoster;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.carapacian.EntityBlackBishop;
import com.mraof.minestuck.entity.carapacian.EntityBlackPawn;
import com.mraof.minestuck.entity.carapacian.EntityBlackRook;
import com.mraof.minestuck.entity.carapacian.EntityWhiteBishop;
import com.mraof.minestuck.entity.carapacian.EntityWhitePawn;
import com.mraof.minestuck.entity.carapacian.EntityWhiteRook;
import com.mraof.minestuck.entity.consort.EntityIguana;
import com.mraof.minestuck.entity.consort.EntityNakagator;
import com.mraof.minestuck.entity.consort.EntitySalamander;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.entity.item.EntityMetalBoat;
import com.mraof.minestuck.entity.item.EntityVitalityGel;
import com.mraof.minestuck.entity.underling.EntityBasilisk;
import com.mraof.minestuck.entity.underling.EntityGiclops;
import com.mraof.minestuck.entity.underling.EntityImp;
import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.entity.underling.EntityWyrm;

public final class MinestuckEntities
{
	public static int currentEntityIdOffset = 0;

	public static void registerEntities()
	{
		//register entities
		registerEntity(EntitySalamander.class, "Salamander", "salamander");
		registerEntity(EntityNakagator.class, "Nakagator", "nakagator");
		registerEntity(EntityIguana.class, "Iguana", "iguana");
		registerEntity(EntityImp.class, "Imp", "imp");
		registerEntity(EntityOgre.class, "Ogre", "ogre");
		registerEntity(EntityBasilisk.class, "Basilisk", "basilisk");
		registerEntity(EntityGiclops.class, "Giclops", "giclops");
		registerEntity(EntityWyrm.class, "Wyrm", "wyrm");
		registerEntity(EntityBlackPawn.class, "DersitePawn", "dersite_pawn");
		registerEntity(EntityWhitePawn.class, "ProspitianPawn", "prospitian_pawn");
		registerEntity(EntityBlackBishop.class, "DersiteBishop", "dersite_bishop");
		registerEntity(EntityWhiteBishop.class, "ProspitianBishop", "prospitian_bishop");
		registerEntity(EntityBlackRook.class, "DersiteRook", "dersite_rook");
		registerEntity(EntityWhiteRook.class, "ProspitianRook", "prospitian_rook");
		registerEntity(EntityDecoy.class, "PlayerDecoy", "player_decoy");
		registerEntity(EntityMetalBoat.class, "MetalBoat", "metal_boat");
		registerEntity(EntityGrist.class, "Grist", "grist", 512, 1, true);
		registerEntity(EntityVitalityGel.class, "VitalityGel", "vitality_gel", 512, 1, true);
		registerEntity(EntityCrewPoster.class, "MidnightCrewPoster", "midnight_crew_poster");
	}
	
	//registers entity with forge and minecraft, and increases currentEntityIdOffset by one in order to prevent id collision
	public static void registerEntity(Class<? extends Entity> entityClass, String name, String registryName)
	{
		registerEntity(entityClass, name, registryName, 80, 3, true);
	}

	public static void registerEntity(Class<? extends Entity> entityClass, String name, String registryName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(new ResourceLocation("minestuck", registryName), entityClass, name, currentEntityIdOffset, Minestuck.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
		currentEntityIdOffset++;
	}

}
