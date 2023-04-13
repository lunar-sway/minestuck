package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.inventory.musicplayer.IMusicPlaying;
import com.mraof.minestuck.inventory.musicplayer.MusicPlaying;
import com.mraof.minestuck.util.MSCapabilities;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EditToolsCapabilityProvider implements ICapabilityProvider
{
	private final LazyOptional<IEditTools> lazyInitSupplierEditTools = LazyOptional.of(this::getCachedEditTools);
	private EditTools editTools;
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(MSCapabilities.EDIT_TOOLS_CAPABILITY == cap)
			return (lazyInitSupplierEditTools).cast();
		return LazyOptional.empty();
	}
	
	private @Nonnull
	EditTools getCachedEditTools()
	{
		if(editTools == null)
		{
			editTools = new EditTools();
		}
		return editTools;
	}
}
