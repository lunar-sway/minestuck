package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

import static com.mraof.minestuck.entity.MSEntityTypes.*;
import static com.mraof.minestuck.util.MSTags.EntityTypes.*;

public class MinestuckEntityTypeTagsProvider extends EntityTypeTagsProvider
{
	public MinestuckEntityTypeTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(generator, Minestuck.MOD_ID, existingFileHelper);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void registerTags()
	{
		getOrCreateBuilder(UNDERLINGS).add(IMP, OGRE, BASILISK, LICH, GICLOPS);
		getOrCreateBuilder(CONSORTS).add(SALAMANDER, NAKAGATOR, IGUANA, TURTLE);
		getOrCreateBuilder(CARAPACIANS).addTags(DERSITE_CARAPACIANS, PROSPITIAN_CARAPACIANS);
		getOrCreateBuilder(DERSITE_CARAPACIANS).add(DERSITE_PAWN, DERSITE_BISHOP, DERSITE_ROOK);
		getOrCreateBuilder(PROSPITIAN_CARAPACIANS).add(PROSPITIAN_PAWN, PROSPITIAN_BISHOP, PROSPITIAN_ROOK);
		getOrCreateBuilder(PAWNS).add(DERSITE_PAWN, PROSPITIAN_PAWN);
		getOrCreateBuilder(BISHOPS).add(DERSITE_BISHOP, PROSPITIAN_BISHOP);
		getOrCreateBuilder(ROOKS).add(DERSITE_ROOK, PROSPITIAN_ROOK);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Entity Type Tags";
	}
}