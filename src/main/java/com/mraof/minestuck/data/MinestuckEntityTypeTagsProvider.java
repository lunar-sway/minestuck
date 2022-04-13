package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraft.entity.EntityType;
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
	protected void addTags()
	{
		tag(UNDERLINGS).add(IMP, OGRE, BASILISK, LICH, GICLOPS);
		tag(CONSORTS).add(SALAMANDER, NAKAGATOR, IGUANA, TURTLE);
		tag(CARAPACIANS).addTags(DERSITE_CARAPACIANS, PROSPITIAN_CARAPACIANS);
		tag(DERSITE_CARAPACIANS).add(DERSITE_PAWN, DERSITE_BISHOP, DERSITE_ROOK);
		tag(PROSPITIAN_CARAPACIANS).add(PROSPITIAN_PAWN, PROSPITIAN_BISHOP, PROSPITIAN_ROOK);
		tag(PAWNS).add(DERSITE_PAWN, PROSPITIAN_PAWN);
		tag(BISHOPS).add(DERSITE_BISHOP, PROSPITIAN_BISHOP);
		tag(ROOKS).add(DERSITE_ROOK, PROSPITIAN_ROOK);
		tag(MAGNET_RECEPTIVE).add(GRIST, VITALITY_GEL, EntityType.ITEM, EntityType.EXPERIENCE_ORB);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Entity Type Tags";
	}
}