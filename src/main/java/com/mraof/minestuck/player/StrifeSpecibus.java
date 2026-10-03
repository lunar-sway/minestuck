package com.mraof.minestuck.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Ported from Minestuck Universe (1.12.2)
 */
public class StrifeSpecibus
{
	@Nullable
	private String abstratusName; // unlocalizedName of the KindAbstratusType; null = unassigned empty slot
	private final LinkedList<ItemStack> weapons = new LinkedList<>();
	private String customName = "";
	
	public static final Codec<StrifeSpecibus> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.optionalFieldOf("kind_abstratus").forGetter(s -> Optional.ofNullable(s.abstratusName)), ItemStack.CODEC.listOf().optionalFieldOf("weapons", List.of()).forGetter(s -> new ArrayList<>(s.weapons)), Codec.STRING.optionalFieldOf("custom_name", "").forGetter(s -> s.customName)).apply(instance, (name, items, customName) -> {
		StrifeSpecibus sp = new StrifeSpecibus(name.orElse(null));
		sp.weapons.addAll(items);
		sp.customName = customName;
		return sp;
	}));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, StrifeSpecibus> STREAM_CODEC = StreamCodec.of((buf, sp) -> {
		// abstratusName as optional string
		buf.writeBoolean(sp.abstratusName != null);
		if(sp.abstratusName != null) ByteBufCodecs.STRING_UTF8.encode(buf, sp.abstratusName);
		// weapon list
		ByteBufCodecs.INT.encode(buf, sp.weapons.size());
		for(ItemStack stack : sp.weapons)
			ItemStack.STREAM_CODEC.encode(buf, stack);
		// custom name
		ByteBufCodecs.STRING_UTF8.encode(buf, sp.customName);
	}, buf -> {
		String name = buf.readBoolean() ? ByteBufCodecs.STRING_UTF8.decode(buf) : null;
		int count = ByteBufCodecs.INT.decode(buf);
		StrifeSpecibus sp = new StrifeSpecibus(name);
		for(int i = 0; i < count; i++)
			sp.weapons.add(ItemStack.STREAM_CODEC.decode(buf));
		sp.customName = ByteBufCodecs.STRING_UTF8.decode(buf);
		return sp;
	});
	
	public StrifeSpecibus(@Nullable String abstratusName)
	{
		this.abstratusName = abstratusName;
	}
	
	public static StrifeSpecibus empty()
	{
		return new StrifeSpecibus(null);
	}
	
	@Nullable
	public KindAbstratusType getKindAbstratus()
	{
		if(abstratusName == null) return null;
		return KindAbstratusList.getTypeFromName(abstratusName);
	}
	
	@Nullable
	public String getAbstratusName()
	{
		return abstratusName;
	}
	
	/**
	 * True if this slot has been assigned a weapon type.
	 */
	public boolean isAssigned()
	{
		return abstratusName != null;
	}
	
	/**
	 * Changes the weapon category of this specibus.
	 * Any weapons in the deck that are incompatible with the new type are removed
	 * and returned so the caller can drop them.
	 */
	public List<ItemStack> switchKindAbstratus(@Nullable String newAbstratusName)
	{
		if(java.util.Objects.equals(abstratusName, newAbstratusName)) return List.of();
		
		this.abstratusName = newAbstratusName;
		
		if(newAbstratusName == null)
		{
			List<ItemStack> dropped = new ArrayList<>(weapons);
			weapons.clear();
			return dropped;
		}
		
		KindAbstratusType newType = KindAbstratusList.getTypeFromName(newAbstratusName);
		if(newType == null) return List.of();
		
		List<ItemStack> removed = new ArrayList<>();
		weapons.removeIf(stack -> {
			if(!newType.partOf(stack))
			{
				removed.add(stack);
				return true;
			}
			return false;
		});
		return removed;
	}
	
	/**
	 * Attempts to add a weapon to this specibus deck.
	 * Returns true on success.  Fails if the slot is unassigned or the item
	 * doesn't match the abstratustype.
	 */
	public boolean putItemStack(ItemStack stack)
	{
		if(stack.isEmpty() || abstratusName == null) return false;
		KindAbstratusType type = getKindAbstratus();
		if(type == null || !type.partOf(stack)) return false;
		weapons.add(stack.copy());
		return true;
	}
	
	/**
	 * Removes the weapon at the given index. Returns true if the index was valid.
	 */
	public boolean unassign(int index)
	{
		if(index < 0 || index >= weapons.size()) return false;
		weapons.remove(index);
		return true;
	}
	
	/**
	 * Returns a copy of the weapon at the given index, or EMPTY if out of bounds.
	 */
	public ItemStack retrieveStack(int index)
	{
		if(index < 0 || index >= weapons.size()) return ItemStack.EMPTY;
		return weapons.get(index).copy();
	}
	
	public LinkedList<ItemStack> getContents()
	{
		return weapons;
	}
	
	public String getCustomName()
	{
		return customName;
	}
	
	public void setCustomName(String name)
	{
		this.customName = (name == null) ? "" : name.trim();
	}
	
	public boolean hasCustomName()
	{
		return customName != null && !customName.isEmpty();
	}
	
	public Component getDisplayName()
	{
		if(hasCustomName()) return Component.literal(customName);
		KindAbstratusType type = getKindAbstratus();
		return type != null ? type.getDisplayName() : Component.empty();
	}
	
	/**
	 * Short display name for rendering on the card graphic (max 12 chars).
	 */
	public String getDisplayNameForCard()
	{
		String name = hasCustomName() ? customName : (getKindAbstratus() != null ? getKindAbstratus().getDisplayName().getString() : "");
		name = name.toLowerCase();
		return name.length() > 12 ? name.substring(0, 9) + "..." : name;
	}
	
	@Override
	public String toString()
	{
		return abstratusName + " " + weapons;
	}
}