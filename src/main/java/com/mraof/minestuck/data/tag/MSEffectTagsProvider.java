package com.mraof.minestuck.data.tag;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MSEffectTagsProvider extends IntrinsicHolderTagsProvider<MobEffect>
{
	public MSEffectTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(output, Registries.MOB_EFFECT, lookupProvider, MSEffectTagsProvider::keyForMobEffects, Minestuck.MOD_ID, existingFileHelper);
	}
	
	private static ResourceKey<MobEffect> keyForMobEffects(MobEffect landType)
	{
		return ForgeRegistries.MOB_EFFECTS.getResourceKey(landType).orElseThrow();
	}
	
	@SuppressWarnings("DataFlowIssue")
	@Override
	protected void addTags(HolderLookup.Provider provider)
	{
		//includes all Vanilla potion effects
		ForgeRegistries.MOB_EFFECTS.forEach(mobEffect ->
		{
			if(ForgeRegistries.MOB_EFFECTS.getKey(mobEffect).getNamespace().equals("minecraft"))
				this.tag(MSTags.Effects.SOPOR_SICKNESS_WHITELIST).add(mobEffect);
		});
		
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Effect Tags";
	}
}