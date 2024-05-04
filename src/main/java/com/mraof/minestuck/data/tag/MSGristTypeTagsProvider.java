package com.mraof.minestuck.data.tag;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypeSpawnCategory;
import com.mraof.minestuck.api.alchemy.GristTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

import static com.mraof.minestuck.api.alchemy.GristTypes.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class MSGristTypeTagsProvider extends IntrinsicHolderTagsProvider<GristType>
{
	public MSGristTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(output, REGISTRY_KEY, lookupProvider, MSGristTypeTagsProvider::keyForGristType, Minestuck.MOD_ID, existingFileHelper);
	}
	
	private static ResourceKey<GristType> keyForGristType(GristType gristType)
	{
		return GristTypes.REGISTRY.getResourceKey(gristType).orElseThrow();
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider)
	{
		tag(GristTypeSpawnCategory.COMMON.getTagKey())
				.add(AMBER.get(), CAULK.get(), CHALK.get(), IODINE.get(), SHALE.get(), TAR.get())
				.add(COBALT.get(), MARBLE.get(), MERCURY.get(), QUARTZ.get(), SULFUR.get());
		tag(GristTypeSpawnCategory.UNCOMMON.getTagKey())
				.add(COBALT.get(), MARBLE.get(), MERCURY.get(), QUARTZ.get(), SULFUR.get())
				.add(AMETHYST.get(), GARNET.get(), RUBY.get(), RUST.get())
				.add(DIAMOND.get(), GOLD.get(), URANIUM.get());
		//noinspection unchecked
		tag(GristTypeSpawnCategory.ANY.getTagKey()).addTags(GristTypeSpawnCategory.COMMON.getTagKey(), GristTypeSpawnCategory.UNCOMMON.getTagKey());
		
		tag(AMBER.get().getSecondaryTypesTag()).add(RUST.get(), SULFUR.get());
		tag(CAULK.get().getSecondaryTypesTag()).add(IODINE.get(), CHALK.get());
		tag(CHALK.get().getSecondaryTypesTag()).add(SHALE.get(), MARBLE.get());
		tag(IODINE.get().getSecondaryTypesTag()).add(AMBER.get(), CHALK.get());
		tag(SHALE.get().getSecondaryTypesTag()).add(MERCURY.get(), TAR.get());
		tag(TAR.get().getSecondaryTypesTag()).add(AMBER.get(), COBALT.get());
		
		tag(COBALT.get().getSecondaryTypesTag()).add(RUBY.get(), AMETHYST.get());
		tag(MARBLE.get().getSecondaryTypesTag()).add(CAULK.get(), AMETHYST.get());
		tag(MERCURY.get().getSecondaryTypesTag()).add(COBALT.get(), RUST.get());
		tag(QUARTZ.get().getSecondaryTypesTag()).add(MARBLE.get(), URANIUM.get());
		tag(SULFUR.get().getSecondaryTypesTag()).add(IODINE.get(), TAR.get());
		
		tag(AMETHYST.get().getSecondaryTypesTag()).add(QUARTZ.get(), GARNET.get());
		tag(GARNET.get().getSecondaryTypesTag()).add(RUBY.get(), GOLD.get());
		tag(RUBY.get().getSecondaryTypesTag()).add(QUARTZ.get(), DIAMOND.get());
		tag(RUST.get().getSecondaryTypesTag()).add(SHALE.get(), GARNET.get());
		
		tag(DIAMOND.get().getSecondaryTypesTag()).add(GOLD.get());
		tag(GOLD.get().getSecondaryTypesTag()).add(URANIUM.get());
		tag(URANIUM.get().getSecondaryTypesTag()).add(DIAMOND.get());
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Grist Type Tags";
	}
}
