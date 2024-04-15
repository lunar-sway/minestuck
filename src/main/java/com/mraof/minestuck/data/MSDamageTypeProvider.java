package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSDamageSources;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class MSDamageTypeProvider
{
	public static final String SPIKE_MSG = "minestuck.spike";
	public static final String DECAPITATION_MSG = "minestuck.decapitation";
	
	public static void register(BootstapContext<DamageType> context)
	{
		context.register(MSDamageSources.SPIKE, new DamageType(SPIKE_MSG, DamageScaling.ALWAYS, 0.1F));
		context.register(MSDamageSources.DECAPITATION, new DamageType(DECAPITATION_MSG, 0));
		context.register(MSDamageSources.ARMOR_PIERCE, new DamageType("player", 0.1F));
	}
	
	public static class Tags extends DamageTypeTagsProvider
	{
		public Tags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper)
		{
			super(output, provider, Minestuck.MOD_ID, existingFileHelper);
		}
		
		@Override
		protected void addTags(HolderLookup.Provider provider)
		{
			this.tag(DamageTypeTags.BYPASSES_ARMOR).add(MSDamageSources.DECAPITATION);
			this.tag(DamageTypeTags.BYPASSES_INVULNERABILITY).add(MSDamageSources.DECAPITATION);
			this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(MSDamageSources.DECAPITATION);
			this.tag(DamageTypeTags.BYPASSES_RESISTANCE).add(MSDamageSources.DECAPITATION);
			
			this.tag(DamageTypeTags.BYPASSES_ARMOR).add(MSDamageSources.ARMOR_PIERCE);
		}
	}
}
