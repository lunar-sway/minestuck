package com.mraof.minestuck.client.gui.computer;

import com.mraof.minestuck.computer.ProgramType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author Kirderf1
 */
public interface ProgramGui
{
	/**
	 * Called when the gui is created or if the player pressed the switch
	 * program button.
	 */
	default void onInit(ComputerScreen gui)
	{
	}
	
	/**
	 * Called when some related data have changed that may affect the program.
	 */
	default void onUpdate(ComputerScreen gui)
	{
	}
	
	/**
	 * Called when the gui is to be rendered.
	 */
	void render(GuiGraphics guiGraphics, ComputerScreen gui);
	
	static void drawHeaderMessage(Component message, GuiGraphics guiGraphics, ComputerScreen gui)
	{
		guiGraphics.drawString(gui.getMinecraft().font, message,
				(gui.width - ComputerScreen.xSize) / 2 + 15, (gui.height - ComputerScreen.ySize) / 2 + 45,
				gui.getTheme().data().textColor(), false);
	}
	
	final class Registry
	{
		
		private static final HashMap<ProgramType, Supplier<? extends ProgramGui>> programs = new HashMap<>();
		
		/**
		 * Should only be used client-side
		 */
		public static void register(ProgramType programType, Supplier<? extends ProgramGui> factory)
		{
			if(programs.containsKey(programType))
				throw new IllegalArgumentException("Program type " + programType + " is already registered!");
			programs.put(programType, factory);
		}
		
		/**
		 * Creates and returns a new computer program gui for the given type.
		 * Should only be used in a client-side context due to gui sidedness!
		 */
		public static ProgramGui createGuiInstance(ProgramType programType)
		{
			return Objects.requireNonNull(programs.get(programType)).get();
		}
	}
}
