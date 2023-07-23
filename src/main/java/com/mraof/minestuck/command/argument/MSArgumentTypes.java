package com.mraof.minestuck.command.argument;

import com.mraof.minestuck.Minestuck;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MSArgumentTypes
{
	public static final DeferredRegister<ArgumentTypeInfo<?, ?>> REGISTER = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, Minestuck.MOD_ID);
	
	static {
		REGISTER.register("grist_type", () -> ArgumentTypeInfos.registerByClass(GristTypeArgument.class,
				SingletonArgumentInfo.contextFree(GristTypeArgument::gristType)));
		REGISTER.register("grist_set", () -> ArgumentTypeInfos.registerByClass(GristSetArgument.class, new GristSetArgument.Info()));
		REGISTER.register("terrain_land", () -> ArgumentTypeInfos.registerByClass(TerrainLandTypeArgument.class,
				SingletonArgumentInfo.contextFree(TerrainLandTypeArgument::terrainLandType)));
		REGISTER.register("title_land", () -> ArgumentTypeInfos.registerByClass(TitleLandTypeArgument.class,
				SingletonArgumentInfo.contextFree(TitleLandTypeArgument::titleLandType)));
		REGISTER.register("land_type_pair", () -> ArgumentTypeInfos.registerByClass(LandTypePairArgument.class,
				SingletonArgumentInfo.contextFree(LandTypePairArgument::nullablePairs)));
		REGISTER.register("title", () -> ArgumentTypeInfos.registerByClass(TitleArgument.class,
				SingletonArgumentInfo.contextFree(TitleArgument::title)));
		//noinspection unchecked,rawtypes
		REGISTER.register("list", () -> ArgumentTypeInfos.registerByClass(ListArgument.class, new ListArgument.Info()));
	}
}
