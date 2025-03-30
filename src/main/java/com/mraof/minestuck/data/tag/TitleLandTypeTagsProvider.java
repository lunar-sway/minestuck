package com.mraof.minestuck.data.tag;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

import static com.mraof.minestuck.util.MSTags.TitleLandTypes.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class TitleLandTypeTagsProvider extends IntrinsicHolderTagsProvider<TitleLandType>
{
	public TitleLandTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(output, LandTypes.TITLE_KEY, lookupProvider, TitleLandTypeTagsProvider::keyForLandType, Minestuck.MOD_ID, existingFileHelper);
	}
	
	private static ResourceKey<TitleLandType> keyForLandType(TitleLandType landType)
	{
		return LandTypes.TITLE_REGISTRY.getResourceKey(landType).orElseThrow();
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider)
	{
		this.tag(MONSTERS).add(LandTypes.MONSTERS.get(), LandTypes.UNDEAD.get());
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Title Land Type Tags";
	}
}
