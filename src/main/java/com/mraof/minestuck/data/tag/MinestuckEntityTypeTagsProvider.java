package com.mraof.minestuck.data.tag;

import com.mraof.minestuck.Minestuck;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

import static com.mraof.minestuck.entity.MSEntityTypes.*;
import static com.mraof.minestuck.util.MSTags.EntityTypes.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MinestuckEntityTypeTagsProvider extends EntityTypeTagsProvider
{
	public MinestuckEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(output, lookupProvider, Minestuck.MOD_ID, existingFileHelper);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void addTags(HolderLookup.Provider provider)
	{
		tag(UNDERLINGS).add(IMP.get(), OGRE.get(), BASILISK.get(), LICH.get(), GICLOPS.get());
		tag(CONSORTS).add(SALAMANDER.get(), NAKAGATOR.get(), IGUANA.get(), TURTLE.get());
		tag(CARAPACIANS).addTags(DERSITE_CARAPACIANS, PROSPITIAN_CARAPACIANS);
		tag(DERSITE_CARAPACIANS).add(DERSITE_PAWN.get(), DERSITE_BISHOP.get(), DERSITE_ROOK.get());
		tag(PROSPITIAN_CARAPACIANS).add(PROSPITIAN_PAWN.get(), PROSPITIAN_BISHOP.get(), PROSPITIAN_ROOK.get());
		tag(PAWNS).add(DERSITE_PAWN.get(), PROSPITIAN_PAWN.get());
		tag(BISHOPS).add(DERSITE_BISHOP.get(), PROSPITIAN_BISHOP.get());
		tag(ROOKS).add(DERSITE_ROOK.get(), PROSPITIAN_ROOK.get());
		tag(MAGNET_RECEPTIVE).add(GRIST.get(), VITALITY_GEL.get(), EntityType.ITEM, EntityType.EXPERIENCE_ORB);
		tag(REMOTE_OBSERVER_BLACKLIST).add(EntityType.COMMAND_BLOCK_MINECART, EntityType.SPAWNER_MINECART, PLAYER_DECOY.get());
		tag(BOSS_MOB).add(EntityType.ENDER_DRAGON, EntityType.WITHER, EntityType.RAVAGER, EntityType.ELDER_GUARDIAN, EntityType.WARDEN, GICLOPS.get());
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Entity Type Tags";
	}
}