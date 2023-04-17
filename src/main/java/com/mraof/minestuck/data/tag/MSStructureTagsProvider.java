package com.mraof.minestuck.data.tag;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.gen.structure.MSConfiguredStructures;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class MSStructureTagsProvider extends TagsProvider<Structure>
{
	public MSStructureTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(generator, BuiltinRegistries.STRUCTURES, Minestuck.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void addTags()
	{
		this.tag(MSTags.Structures.SCANNER_LOCATED).add(MSConfiguredStructures.FROG_TEMPLE);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Structure Tags";
	}
}
