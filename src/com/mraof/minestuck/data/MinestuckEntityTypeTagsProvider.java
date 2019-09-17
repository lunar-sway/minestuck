package com.mraof.minestuck.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;

import static com.mraof.minestuck.entity.MSEntityTypes.*;
import static com.mraof.minestuck.util.MSTags.EntityTypes.*;

public class MinestuckEntityTypeTagsProvider extends EntityTypeTagsProvider
{
	public MinestuckEntityTypeTagsProvider(DataGenerator generator)
	{
		super(generator);
	}
	
	@Override
	protected void registerTags()
	{
		getBuilder(UNDERLINGS).add(IMP, OGRE, BASILISK, LICH, GICLOPS);
		getBuilder(CONSORTS).add(SALAMANDER, NAKAGATOR, IGUANA, TURTLE);
		getBuilder(CARAPACIANS).add(DERSITE_CARAPACIANS, PROSPITIAN_CARAPACIANS);
		getBuilder(DERSITE_CARAPACIANS).add(DERSITE_PAWN, DERSITE_BISHOP, DERSITE_ROOK);
		getBuilder(PROSPITIAN_CARAPACIANS).add(PROSPITIAN_PAWN, PROSPITIAN_BISHOP, PROSPITIAN_ROOK);
		getBuilder(PAWNS).add(DERSITE_PAWN, PROSPITIAN_PAWN);
		getBuilder(BISHOPS).add(DERSITE_BISHOP, PROSPITIAN_BISHOP);
		getBuilder(ROOKS).add(DERSITE_ROOK, PROSPITIAN_ROOK);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Entity Type Tags";
	}
}