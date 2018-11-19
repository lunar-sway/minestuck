package com.mraof.minestuck.entity;

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
import com.mraof.minestuck.entity.consort.EntityTurtle;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.entity.item.EntityCrewPoster;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.entity.item.EntityHologram;
import com.mraof.minestuck.entity.item.EntityMetalBoat;
import com.mraof.minestuck.entity.item.EntitySbahjPoster;
import com.mraof.minestuck.entity.item.EntityShopPoster;
import com.mraof.minestuck.entity.item.EntityVitalityGel;
import com.mraof.minestuck.entity.underling.EntityBasilisk;
import com.mraof.minestuck.entity.underling.EntityGiclops;
import com.mraof.minestuck.entity.underling.EntityImp;
import com.mraof.minestuck.entity.underling.EntityLich;
import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.entity.underling.EntityWyrm;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public final class MinestuckEntities
{
	public static int currentEntityIdOffset = 0;

	public static void registerEntities()
	{
		//register entities
		registerEntity(EntityFrog.class, "frog", 1100060, 11656884);
		registerEntity(EntitySalamander.class, EnumConsort.SALAMANDER.getName());
		registerEntity(EntityNakagator.class, EnumConsort.NAKAGATOR.getName());
		registerEntity(EntityIguana.class, EnumConsort.IGUANA.getName());
		registerEntity(EntityTurtle.class, EnumConsort.TURTLE.getName());
		registerEntity(EntityImp.class, "imp");
		registerEntity(EntityOgre.class, "ogre");
		registerEntity(EntityBasilisk.class, "basilisk");
		registerEntity(EntityLich.class, "lich");
		registerEntity(EntityGiclops.class, "giclops");
		registerEntity(EntityWyrm.class, "wyrm");
		registerEntity(EntityBlackPawn.class, "dersitePawn", "dersite_pawn");
		registerEntity(EntityWhitePawn.class, "prospitianPawn", "prospitian_pawn");
		registerEntity(EntityBlackBishop.class, "dersiteBishop", "dersite_bishop");
		registerEntity(EntityWhiteBishop.class, "prospitianBishop", "prospitian_bishop");
		registerEntity(EntityBlackRook.class, "dersiteRook", "dersite_rook");
		registerEntity(EntityWhiteRook.class, "prospitianRook", "prospitian_rook");
		registerEntity(EntityDecoy.class, "playerDecoy",  "player_decoy");
		registerEntity(EntityMetalBoat.class, "metalBoat", "metal_boat");
		registerEntity(EntityGrist.class, "grist", "grist", 512, 1, true);
		registerEntity(EntityVitalityGel.class, "vitalityGel", "vitality_gel", 512, 1, true);
		registerEntity(EntityCrewPoster.class, "midnightCrewPoster", "midnight_crew_poster");
		registerEntity(EntitySbahjPoster.class, "sbahjPoster", "sbahj_poster");
		registerEntity(EntityShopPoster.class, "shopPoster", "shop_poster");
		registerEntity(EntityHologram.class, "holoItem", "holo_item");
		
	}
	
	
	public static void registerEntity(Class<? extends Entity> entityClass, String name)
	{
		registerEntity(entityClass, name, name, 80, 3, true);
	}
	
	public static void registerEntity(Class<? extends Entity> entityClass, String name, int eggPrimary, int eggSecondary)
	{
		registerEntity(entityClass, name, name, 80, 3, true, eggPrimary, eggSecondary);
	}
	
	//registers entity with forge and minecraft, and increases currentEntityIdOffset by one in order to prevent id collision
	public static void registerEntity(Class<? extends Entity> entityClass, String name, String registryName)
	{
		registerEntity(entityClass, name, registryName, 80, 3, true);
	}

	public static void registerEntity(Class<? extends Entity> entityClass, String name, String registryName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(new ResourceLocation("minestuck", registryName), entityClass, "minestuck." + name, currentEntityIdOffset, Minestuck.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
		currentEntityIdOffset++;
	}

	public static void registerEntity(Class<? extends Entity> entityClass, String name, String registryName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int eggPrimary, int eggSecondary)
	{
		EntityRegistry.registerModEntity(new ResourceLocation("minestuck", registryName), entityClass, "minestuck." + name, currentEntityIdOffset, Minestuck.instance, trackingRange, updateFrequency, sendsVelocityUpdates, eggPrimary, eggSecondary);
		currentEntityIdOffset++;
	}
	
}
