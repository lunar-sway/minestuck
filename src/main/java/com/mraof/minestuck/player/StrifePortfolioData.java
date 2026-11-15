package com.mraof.minestuck.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.item.components.MSItemComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
	 * Ids of the specibus evolutions this player has already performed (see {@code StrifeEvolution}).
	 * Every evolution can only happen once per player.
	 */
	private final Set<String> completedEvolutions = new HashSet<>();
	/**
	 * How many strife cards this player has received from mob drops (used to cap the drops).
	 */
	private int droppedCards = 0;
	
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
	
	public static final Codec<StrifePortfolioData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			PortfolioSlot.CODEC.listOf().optionalFieldOf("portfolio", List.of()).forGetter(StrifePortfolioData::getPortfolioSlots),
			Codec.INT.optionalFieldOf("selected_specibus", -1).forGetter(d -> d.selectedSpecibusIndex),
			Codec.INT.optionalFieldOf("selected_weapon", -1).forGetter(d -> d.selectedWeaponIndex),
			Codec.BOOL.optionalFieldOf("armed", false).forGetter(d -> d.armed),
			Codec.BOOL.optionalFieldOf("switcher_unlocked", false).forGetter(d -> d.abstrataSwitcherUnlocked),
			Codec.STRING.listOf().optionalFieldOf("completed_evolutions", List.of()).forGetter(d -> new ArrayList<>(d.completedEvolutions)),
			Codec.INT.optionalFieldOf("dropped_cards", 0).forGetter(d -> d.droppedCards)
	).apply(instance, StrifePortfolioData::fromCodec));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, StrifePortfolioData> STREAM_CODEC = StreamCodec.of((buf, data) -> {
		List<PortfolioSlot> slots = data.getPortfolioSlots();
		ByteBufCodecs.INT.encode(buf, slots.size());
		for(PortfolioSlot slot : slots)
			PortfolioSlot.STREAM_CODEC.encode(buf, slot);
		ByteBufCodecs.INT.encode(buf, data.selectedSpecibusIndex);
		ByteBufCodecs.INT.encode(buf, data.selectedWeaponIndex);
		ByteBufCodecs.BOOL.encode(buf, data.armed);
		ByteBufCodecs.BOOL.encode(buf, data.abstrataSwitcherUnlocked);
		ByteBufCodecs.INT.encode(buf, data.completedEvolutions.size());
		for(String id : data.completedEvolutions)
			ByteBufCodecs.STRING_UTF8.encode(buf, id);
		ByteBufCodecs.INT.encode(buf, data.droppedCards);
	}, buf -> {
		int slotCount = ByteBufCodecs.INT.decode(buf);
		List<PortfolioSlot> slots = new ArrayList<>(slotCount);
		for(int i = 0; i < slotCount; i++)
			slots.add(PortfolioSlot.STREAM_CODEC.decode(buf));
		int selSpecibus = ByteBufCodecs.INT.decode(buf);
		int selWeapon = ByteBufCodecs.INT.decode(buf);
		boolean armed = ByteBufCodecs.BOOL.decode(buf);
		boolean switcherUnlocked = ByteBufCodecs.BOOL.decode(buf);
		int evolutionCount = ByteBufCodecs.INT.decode(buf);
		List<String> evolutions = new ArrayList<>(evolutionCount);
		for(int i = 0; i < evolutionCount; i++)
			evolutions.add(ByteBufCodecs.STRING_UTF8.decode(buf));
		int droppedCards = ByteBufCodecs.INT.decode(buf);
		return fromCodec(slots, selSpecibus, selWeapon, armed, switcherUnlocked, evolutions, droppedCards);
	});
	
	private static StrifePortfolioData fromCodec(List<PortfolioSlot> slots, int selSpecibus, int selWeapon, boolean armed, boolean switcherUnlocked, List<String> evolutions, int droppedCards)
	{
		StrifePortfolioData data = new StrifePortfolioData();
		for(PortfolioSlot slot : slots)
			if(slot.index() >= 0 && slot.index() < PORTFOLIO_SIZE) data.portfolio[slot.index()] = slot.specibus();
		data.selectedSpecibusIndex = selSpecibus;
		data.selectedWeaponIndex = selWeapon;
		data.armed = armed;
		data.abstrataSwitcherUnlocked = switcherUnlocked;
		data.completedEvolutions.addAll(evolutions);
		data.droppedCards = Math.max(0, droppedCards);
		data.removeDeadSlots();
		return data;
	}
	
	public boolean removeDeadSlots()
	{
		boolean changed = false;
		for(int i = 0; i < PORTFOLIO_SIZE; i++)
		{
			StrifeSpecibus sp = portfolio[i];
			if(sp == null) continue;
			
			boolean dead = !sp.isAssigned() && sp.getContents().isEmpty();
			for(int j = 0; j < i && !dead; j++)
				if(portfolio[j] == sp) dead = true;
			
			if(dead)
			{
				portfolio[i] = null;
				changed = true;
			}
		}
		
		if(selectedSpecibusIndex >= 0 && (selectedSpecibusIndex >= PORTFOLIO_SIZE || portfolio[selectedSpecibusIndex] == null))
		{
			selectedSpecibusIndex = -1;
			armed = false;
			changed = true;
		}
		return changed;
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
		return java.util.Arrays.stream(portfolio).filter(sp -> sp != null && sp.isAssigned() && (!sp.getContents().isEmpty() || (armed && sp == getSelectedSpecibus()))).toArray(StrifeSpecibus[]::new);
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
	 * Returns the slot index of the first specibus with the given abstratus name, or -1.
	 */
	public int findSpecibusIndex(@Nullable String abstratusName)
	{
		if(abstratusName == null) return -1;
		for(int i = 0; i < PORTFOLIO_SIZE; i++)
			if(portfolio[i] != null && abstratusName.equals(portfolio[i].getAbstratusName())) return i;
		return -1;
	}
	
	public boolean hasCompletedEvolution(String evolutionId)
	{
		return completedEvolutions.contains(evolutionId);
	}
	
	public void completeEvolution(String evolutionId)
	{
		completedEvolutions.add(evolutionId);
	}
	
	public int getDroppedCards()
	{
		return droppedCards;
	}
	
	public void addDroppedCard()
	{
		droppedCards++;
	}
	
	/**
	 * Copies the state that has to survive death even when the portfolio itself is dropped
	 * (unlocked switcher, completed evolutions and the mob-drop counter).
	 */
	public void copyPersistentStateFrom(StrifePortfolioData other)
	{
		this.abstrataSwitcherUnlocked = other.abstrataSwitcherUnlocked;
		this.completedEvolutions.clear();
		this.completedEvolutions.addAll(other.completedEvolutions);
		this.droppedCards = other.droppedCards;
	}
	
	/**
	 * While a weapon is armed it is held in the main hand and is <b>not</b> part of the deck list.
	 * This returns the deck the way the player experiences it: the armed weapon is put back at the slot it will return to.
	 * All weapon indexes that are sent to the server refer to this list.
	 */
	public List<ItemStack> getDeckWithArmed(StrifeSpecibus specibus, ItemStack mainHand)
	{
		List<ItemStack> deck = new ArrayList<>(specibus.getContents());
		if(armed && specibus == getSelectedSpecibus() && !mainHand.isEmpty() && mainHand.has(MSItemComponents.STRIFE_ASSIGNED.get()))
			deck.add(armedWeaponSlot(deck.size()), mainHand);
		return deck;
	}
	
	/**
	 * Converts an index into the raw deck list (without the armed weapon) to an index into {@link #getDeckWithArmed}.
	 */
	public int rawToDeckIndex(int rawIndex)
	{
		if(armed && rawIndex >= armedWeaponSlot(getSelectedSpecibus() == null ? 0 : getSelectedSpecibus().getContents().size()))
			return rawIndex + 1;
		return rawIndex;
	}
	
	/**
	 * The slot in the deck that the armed weapon returns to, clamped to the deck size.
	 */
	public int armedWeaponSlot(int deckSize)
	{
		return Math.max(0, Math.min(selectedWeaponIndex, deckSize));
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
	public boolean hasMatchingSpecibus(ItemStack stack)
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
		return selectedSpecibusIndex == other.selectedSpecibusIndex && selectedWeaponIndex == other.selectedWeaponIndex && armed == other.armed && abstrataSwitcherUnlocked == other.abstrataSwitcherUnlocked && droppedCards == other.droppedCards && completedEvolutions.equals(other.completedEvolutions) && java.util.Arrays.equals(portfolio, other.portfolio);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(selectedSpecibusIndex, selectedWeaponIndex, armed, abstrataSwitcherUnlocked, droppedCards, completedEvolutions, java.util.Arrays.hashCode(portfolio));
	}
}