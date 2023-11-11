package com.mraof.minestuck.network;

import com.mraof.minestuck.item.armor.JetPackItem;
import net.minecraft.network.FriendlyByteBuf;

public class JetPackPacket implements PlayToClientPacket
{
	private final JetPackItem.Animation animation;
	
	public static JetPackPacket createPacket(JetPackItem.Animation animation)
	{
		return new JetPackPacket(animation);
	}
	
	private JetPackPacket(JetPackItem.Animation animation)
	{
		this.animation = animation;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(animation.ordinal());
	}
	
	public static JetPackPacket decode(FriendlyByteBuf buffer)
	{
		JetPackItem.Animation animation = JetPackItem.Animation.values()[buffer.readInt()];
		
		return new JetPackPacket(animation);
	}
	
	@Override
	public void execute()
	{
	///	JetPackItem.setAnimationFromPacket(animation);
	}
}

