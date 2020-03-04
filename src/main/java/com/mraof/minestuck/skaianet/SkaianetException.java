package com.mraof.minestuck.skaianet;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SkaianetException extends Exception
{
	private final ITextComponent textComponent;
	
	public SkaianetException(String translationKey)
	{
		this(new TranslationTextComponent(translationKey));
	}
	
	public SkaianetException(ITextComponent textComponent)
	{
		this.textComponent = textComponent;
	}
	
	public SkaianetException(ITextComponent textComponent, String message)
	{
		super(message);
		this.textComponent = textComponent;
	}
	
	public ITextComponent getTextComponent()
	{
		return textComponent;
	}
	
	@FunctionalInterface
	public interface SkaianetConsumer<T>
	{
		void consume(T t) throws SkaianetException;
	}
}