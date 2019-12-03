package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

/**
 * Container for land dimension information between info being loaded,
 * and the dimensions actually being registered.
 */
public class LandInfoContainer
{
	public final IdentifierHandler.PlayerIdentifier identifier;
	private final LandTypePair.LazyInstance landAspects;
	private final ResourceLocation dimensionName;
	private final boolean useReverseOrder;
	private final int terrainNameIndex, titleNameIndex;
	@Nullable
	private BlockPos gatePos = null;
	@Nullable
	private DimensionType cachedDimension;
	@Nullable
	private LandTypePair cachedAspects;
	
	public LandInfoContainer(IdentifierHandler.PlayerIdentifier identifier, LandTypePair landTypes, DimensionType dimensionType, Random random)
	{
		this.identifier = Objects.requireNonNull(identifier);
		cachedAspects = Objects.requireNonNull(landTypes);
		this.landAspects = landTypes.createLazy();
		cachedDimension = Objects.requireNonNull(dimensionType);
		dimensionName = DimensionType.getKey(dimensionType);
		useReverseOrder = random.nextBoolean();
		terrainNameIndex = random.nextInt(landTypes.terrain.getNames().length);
		titleNameIndex = random.nextInt(landTypes.title.getNames().length);
	}
	
	private LandInfoContainer(SkaianetHandler handler, IdentifierHandler.PlayerIdentifier identifier, LandTypePair.LazyInstance landAspects, ResourceLocation dimensionType, boolean reverseOrder, int terrainNameIndex, int titleNameIndex)
	{
		this.identifier = identifier;
		this.landAspects = landAspects;
		dimensionName = dimensionType;
		useReverseOrder = reverseOrder;
		this.terrainNameIndex = terrainNameIndex;
		this.titleNameIndex = titleNameIndex;
	}
	
	public ITextComponent landAsTextComponent()
	{
		ITextComponent aspect1 = new TranslationTextComponent("land."+landName1());
		ITextComponent aspect2 = new TranslationTextComponent("land."+landName2());
		return new TranslationTextComponent(LandTypePair.FORMAT, aspect1, aspect2);
	}
	
	public String landName1()
	{
		if(!useReverseOrder)
			return getLandAspects().terrain.getNames()[terrainNameIndex];
		else return getLandAspects().title.getNames()[titleNameIndex];
	}
	
	public String landName2()
	{
		if(useReverseOrder)
			return getLandAspects().terrain.getNames()[terrainNameIndex];
		else return getLandAspects().title.getNames()[titleNameIndex];
	}
	
	@Nullable
	public BlockPos getGatePos()
	{
		return gatePos;
	}
	
	public void setGatePos(BlockPos pos)
	{
		gatePos = pos;
	}
	
	/**
	 * Should NOT be called during a very early loading stage (such as when reading data through {@link com.mraof.minestuck.MSWorldPersistenceHook}).
	 * Because world persistence is loaded alongside world-specific registries, there's not a guarrantee that it is loaded and ready before skaianet is loading data.
	 * (Though the only thing that might change in the registry would be missing land aspects that may get a dummy lanspect created for them)
	 */
	public LandTypePair getLandAspects()
	{
		if(cachedAspects == null)
			cachedAspects = landAspects.create();
		return cachedAspects;
	}
	
	public LandTypePair.LazyInstance getLazyLandAspects()
	{
		return landAspects;
	}
	
	/**
	 * Should NOT be called during a very early loading stage (such as when reading data through {@link com.mraof.minestuck.MSWorldPersistenceHook}).
	 * Because world persistence is loaded alongside the dimension type registry, there's not a guarrantee that it is loaded and ready before skaianet is loading data.
	 * Note: It has indeed happened that Skaianet has loaded before world-specific dimension types.
	 */
	public DimensionType getDimensionType()
	{
		if(cachedDimension == null)
		{
			cachedDimension = DimensionType.byName(dimensionName);
			if(cachedDimension == null)
				throw new IllegalStateException("Unable to load dimenison "+dimensionName+". Either the name is wrong, or this is called before dimensions have been loaded.");
		}
		return cachedDimension;
	}
	
	public ResourceLocation getDimensionName()
	{
		return dimensionName;
	}
	
	/**
	 * Saves the info container to nbt, except for the identifier
	 */
	public CompoundNBT write(CompoundNBT nbt)
	{
		landAspects.write(nbt);
		nbt.putString("dim_type", dimensionName.toString());
		nbt.putBoolean("reverse_order", useReverseOrder);
		nbt.putInt("terrain_name_index", terrainNameIndex);
		nbt.putInt("title_name_index", titleNameIndex);
		if(gatePos != null)
		{
			nbt.putInt("gate_x", gatePos.getX());
			nbt.putInt("gate_y", gatePos.getY());
			nbt.putInt("gate_z", gatePos.getZ());
		}
		return nbt;
	}
	
	public static LandInfoContainer read(CompoundNBT nbt, SkaianetHandler handler, IdentifierHandler.PlayerIdentifier identifier)
	{
		LandTypePair.LazyInstance aspects = LandTypePair.LazyInstance.read(nbt);
		ResourceLocation dimName = new ResourceLocation(nbt.getString("dim_type"));
		boolean reverse = nbt.getBoolean("reverse_order");
		int terrainIndex = nbt.getInt("terrain_name_index");
		int titleIndex = nbt.getInt("title_name_index");
		
		LandInfoContainer info = new LandInfoContainer(handler, identifier, aspects, dimName, reverse, terrainIndex, titleIndex);
		
		if(nbt.contains("gate_x", Constants.NBT.TAG_ANY_NUMERIC))
		{
			info.gatePos = new BlockPos(nbt.getInt("gate_x"), nbt.getInt("gate_y"), nbt.getInt("gate_z"));
		}
		
		return info;
	}
}