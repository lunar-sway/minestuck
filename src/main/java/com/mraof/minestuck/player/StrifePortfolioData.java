package com.mraof.minestuck.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Portfolio: up to PORTFOLIO_SIZE specibus slots, each containing a weapon type
 * and a deck of ItemStacks.  One slot is "selected" at a time; from the selected
 * slot one weapon can be "armed" (physically placed in the player's main hand).
 * Ported from Minestuck Universe (1.12.2).
 */
public class StrifePortfolioData
{
	public static final int PORTFOLIO_SIZE = 10;
	
	/**
	 * Nullable entries mean the slot is empty.
	 */
	private final StrifeSpecibus[] portfolio = new StrifeSpecibus[PORTFOLIO_SIZE];
	
	/**
	 * Index of the currently selected specibus slot, or -1.
	 */
	private int selectedSpecibusIndex = -1;
	/**
	 * Index of the currently selected weapon within the active specibus, or -1.
	 */
	private int selectedWeaponIndex = -1;
	/**
	 * Whether a weapon has been drawn from the deck into the player's hand.
	 */
	private boolean armed = false;
	/**
	 * Unlocked when the player's echeladder rung reaches the configured threshold.
	 */
	private boolean abstrataSwitcherUnlocked = false;
	
	/**
	 * A (slot-index, specibus) pair used for serialisation.
	 */
	public record PortfolioSlot(int index, StrifeSpecibus specibus)
	{
		public static final Codec<PortfolioSlot> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.INT.fieldOf("index").forGetter(PortfolioSlot::index), StrifeSpecibus.CODEC.fieldOf("specibus").forGetter(PortfolioSlot::specibus)).apply(instance, PortfolioSlot::new));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, PortfolioSlot> STREAM_CODEC = StreamCodec.of((buf, slot) -> {
			ByteBufCodecs.INT.encode(buf, slot.index());
			StrifeSpecibus.STREAM_CODEC.encode(buf, slot.specibus());
		}, buf -> new PortfolioSlot(ByteBufCodecs.INT.decode(buf), StrifeSpecibus.STREAM_CODEC.decode(buf)));
	}
	
	public static final Codec<StrifePortfolioData> CODEC = RecordCodecBuilder.create(instance -> instance.group(PortfolioSlot.CODEC.listOf().optionalFieldOf("portfolio", List.of()).forGetter(StrifePortfolioData::getPortfolioSlots), Codec.INT.optionalFieldOf("selected_specibus", -1).forGetter(d -> d.selectedSpecibusIndex), Codec.INT.optionalFieldOf("selected_weapon", -1).forGetter(d -> d.selectedWeaponIndex), Codec.BOOL.optionalFieldOf("armed", false).forGetter(d -> d.armed), Codec.BOOL.optionalFieldOf("switcher_unlocked", false).forGetter(d -> d.abstrataSwitcherUnlocked)).apply(instance, StrifePortfolioData::fromCodec));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, StrifePortfolioData> STREAM_CODEC = StreamCodec.of((buf, data) -> {
		List<PortfolioSlot> slots = data.getPortfolioSlots();
		ByteBufCodecs.INT.encode(buf, slots.size());
		for(PortfolioSlot slot : slots)
			PortfolioSlot.STREAM_CODEC.encode(buf, slot);
		ByteBufCodecs.INT.encode(buf, data.selectedSpecibusIndex);
		ByteBufCodecs.INT.encode(buf, data.selectedWeaponIndex);
		ByteBufCodecs.BOOL.encode(buf, data.armed);
		ByteBufCodecs.BOOL.encode(buf, data.abstrataSwitcherUnlocked);
	}, buf -> {
		int slotCount = ByteBufCodecs.INT.decode(buf);
		List<PortfolioSlot> slots = new ArrayList<>(slotCount);
		for(int i = 0; i < slotCount; i++)
			slots.add(PortfolioSlot.STREAM_CODEC.decode(buf));
		return fromCodec(slots, ByteBufCodecs.INT.decode(buf), ByteBufCodecs.INT.decode(buf), ByteBufCodecs.BOOL.decode(buf), ByteBufCodecs.BOOL.decode(buf));
	});
	
	private static StrifePortfolioData fromCodec(List<PortfolioSlot> slots, int selSpecibus, int selWeapon, boolean armed, boolean switcherUnlocked)
	{
		StrifePortfolioData data = new StrifePortfolioData();
		for(PortfolioSlot slot : slots)
			if(slot.index() >= 0 && slot.index() < PORTFOLIO_SIZE) data.portfolio[slot.index()] = slot.specibus();
		data.selectedSpecibusIndex = selSpecibus;
		data.selectedWeaponIndex = selWeapon;
		data.armed = armed;
		data.abstrataSwitcherUnlocked = switcherUnlocked;
		return data;
	}
	
	private List<PortfolioSlot> getPortfolioSlots()
	{
		List<PortfolioSlot> result = new ArrayList<>();
		for(int i = 0; i < PORTFOLIO_SIZE; i++)
			if(portfolio[i] != null) result.add(new PortfolioSlot(i, portfolio[i]));
		return result;
	}
	
	/**
	 * Returns the raw array.  Entries may be null (empty slots).
	 */
	public StrifeSpecibus[] getPortfolio()
	{
		return portfolio;
	}
	
	public boolean isPortfolioFull()
	{
		for(StrifeSpecibus sp : portfolio)
			if(sp == null) return false;
		return true;
	}
	
	public boolean isPortfolioEmpty()
	{
		for(StrifeSpecibus sp : portfolio)
			if(sp != null) return false;
		return true;
	}
	
	/**
	 * True if any slot is assigned to the given abstratusName.
	 */
	public boolean portfolioHasAbstratus(@Nullable String abstratusName)
	{
		if(abstratusName == null) return false;
		for(StrifeSpecibus sp : portfolio)
			if(sp != null && abstratusName.equals(sp.getAbstratusName())) return true;
		return false;
	}
	
	/**
	 * Returns all non-null, assigned specibus slots that actually contain weapons
	 * (or are fist-kind placeholders).
	 */
	public StrifeSpecibus[] getNonEmptyPortfolio()
	{
		return java.util.Arrays.stream(portfolio).filter(sp -> sp != null && sp.isAssigned() && !sp.getContents().isEmpty()).toArray(StrifeSpecibus[]::new);
	}
	
	/**
	 * Returns the slot index of the given specibus, or -1 if not found.
	 */
	public int getSpecibusIndex(StrifeSpecibus specibus)
	{
		for(int i = 0; i < PORTFOLIO_SIZE; i++)
			if(portfolio[i] == specibus) return i;
		return -1;
	}
	
	/**
	 * Adds a specibus to the first free slot.
	 * Returns false if the portfolio is full or a duplicate abstratustype already exists.
	 */
	public boolean addSpecibus(StrifeSpecibus specibus)
	{
		if(isPortfolioFull()) return false;
		if(specibus.isAssigned() && portfolioHasAbstratus(specibus.getAbstratusName())) return false;
		
		for(int i = 0; i < PORTFOLIO_SIZE; i++)
		{
			if(portfolio[i] == null)
			{
				portfolio[i] = specibus;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes and returns the specibus at the given slot index, or null.
	 * Clears selection/armed state if the removed slot was selected.
	 */
	@Nullable
	public StrifeSpecibus removeSpecibus(int index)
	{
		if(index < 0 || index >= PORTFOLIO_SIZE || portfolio[index] == null) return null;
		
		StrifeSpecibus result = portfolio[index];
		portfolio[index] = null;
		
		if(selectedSpecibusIndex == index)
		{
			selectedSpecibusIndex = -1;
			armed = false;
		}
		return result;
	}
	
	public void setSpecibus(StrifeSpecibus specibus, int index)
	{
		if(index >= 0 && index < PORTFOLIO_SIZE) portfolio[index] = specibus;
	}
	
	public void clearPortfolio()
	{
		java.util.Arrays.fill(portfolio, null);
	}
	
	public int getSelectedSpecibusIndex()
	{
		return selectedSpecibusIndex;
	}
	
	public int getSelectedWeaponIndex()
	{
		return selectedWeaponIndex;
	}
	
	public void setSelectedSpecibusIndex(int index)
	{
		if(selectedSpecibusIndex != index)
		{
			selectedWeaponIndex = 0;
			selectedSpecibusIndex = index;
		}
	}
	
	public void setSelectedWeaponIndex(int index)
	{
		selectedWeaponIndex = index;
	}
	
	public boolean isArmed()
	{
		return armed;
	}
	
	public void setArmed(boolean armed)
	{
		this.armed = armed;
	}
	
	public boolean abstrataSwitcherUnlocked()
	{
		return abstrataSwitcherUnlocked;
	}
	
	public void unlockAbstrataSwitcher(boolean unlocked)
	{
		abstrataSwitcherUnlocked = unlocked;
	}
	
	/**
	 * Returns the currently selected StrifeSpecibus, or null if none is selected
	 * or the index is out of range.
	 */
	@Nullable
	public StrifeSpecibus getSelectedSpecibus()
	{
		if(selectedSpecibusIndex < 0 || selectedSpecibusIndex >= PORTFOLIO_SIZE) return null;
		return portfolio[selectedSpecibusIndex];
	}
	
	/**
	 * Returns true if the portfolio contains any specibus whose weapon type
	 * matches the given ItemStack.
	 */
	public boolean hasMatchingSpecibus(net.minecraft.world.item.ItemStack stack)
	{
		for(StrifeSpecibus sp : portfolio)
		{
			if(sp == null || !sp.isAssigned()) continue;
			KindAbstratusType type = sp.getKindAbstratus();
			if(type != null && type.partOf(stack)) return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof StrifePortfolioData other)) return false;
		return selectedSpecibusIndex == other.selectedSpecibusIndex && selectedWeaponIndex == other.selectedWeaponIndex && armed == other.armed && abstrataSwitcherUnlocked == other.abstrataSwitcherUnlocked && java.util.Arrays.equals(portfolio, other.portfolio);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(selectedSpecibusIndex, selectedWeaponIndex, armed, abstrataSwitcherUnlocked, java.util.Arrays.hashCode(portfolio));
	}
}