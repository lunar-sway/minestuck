package com.mraof.minestuck.network;

import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;

public class PassiveEffectTogglePacket implements PlayToServerPacket
{
	public static final String ON = "minestuck.aspect_effects.on";
	public static final String OFF = "minestuck.aspect_effects.off";
	public static final String WARNING = "minestuck.aspect_effects.warning";
	
	@Override
	public void encode(PacketBuffer buffer)
	{
	}
	
	public static PassiveEffectTogglePacket decode(PacketBuffer buffer)
	{
		return new PassiveEffectTogglePacket();
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		PlayerData data = PlayerSavedData.getData(player);
		data.passiveEffectToggle(!data.passiveEffectToggle());
		if(data.passiveEffectToggle())
		{
			if(data.getTitle() != null && (data.getTitle().getHeroClass() != EnumClass.HEIR && data.getTitle().getHeroClass() != EnumClass.BARD))
				player.sendStatusMessage(new TranslationTextComponent(WARNING), true);
			else
				player.sendStatusMessage(new TranslationTextComponent(ON), true);
		} else
		{
			player.sendStatusMessage(new TranslationTextComponent(OFF), true);
		}
	}
}