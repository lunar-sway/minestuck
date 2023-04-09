package com.mraof.minestuck.data.tag;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeRegistryTagsProvider;
import org.jetbrains.annotations.Nullable;

import static com.mraof.minestuck.util.MSTags.TitleLandTypes.*;

@MethodsReturnNonnullByDefault
public final class TitleLandTypeTagsProvider extends ForgeRegistryTagsProvider<TitleLandType>
{
	public TitleLandTypeTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(generator, LandTypes.TITLE_REGISTRY.get(), Minestuck.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void addTags()
	{
		this.tag(MONSTERS).add(LandTypes.MONSTERS.get(), LandTypes.UNDEAD.get());
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Title Land Type Tags";
	}
}
