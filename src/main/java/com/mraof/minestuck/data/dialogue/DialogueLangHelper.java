package com.mraof.minestuck.data.dialogue;

import com.mraof.minestuck.data.dialogue.DialogueProvider.MessageProducer;
import com.mraof.minestuck.entity.dialogue.DialogueMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.List;
import java.util.function.Function;

/**
 * A helper class for creating {@link MessageProducer}s and register text for it to the language provider with one function call.
 */
public final class DialogueLangHelper
{
	private final LanguageProvider languageProvider;
	
	public DialogueLangHelper(LanguageProvider languageProvider)
	{
		this.languageProvider = languageProvider;
	}
	
	@Deprecated
	public static MessageProducer defaultKeyMsg(DialogueMessage.Argument... arguments)
	{
		return id -> new DialogueMessage(languageKeyBase(id), List.of(arguments));
	}
	
	public MessageProducer defaultKeyMsg(String text, DialogueMessage.Argument... arguments)
	{
		return id -> registerAndBuild(languageKeyBase(id), text, arguments);
	}
	
	public MessageProducer subMsg(String key, String text, DialogueMessage.Argument... arguments)
	{
		return id -> registerAndBuild(languageKeyBase(id) + "." + key, text, arguments);
	}
	
	@Deprecated
	public static MessageProducer msg(String key, DialogueMessage.Argument... arguments)
	{
		return msg(new DialogueMessage(key, List.of(arguments)));
	}
	
	public MessageProducer msg(String key, String text, DialogueMessage.Argument... arguments)
	{
		return msg(registerAndBuild(key, text, arguments));
	}
	
	public static MessageProducer msg(DialogueMessage message)
	{
		return baseId -> message;
	}
	
	public Function<ResourceLocation, String> subText(String subKey, String text)
	{
		return id -> {
			String key = languageKeyBase(id) + "." + subKey;
			this.languageProvider.add(key, text);
			return key;
		};
	}
	
	private DialogueMessage registerAndBuild(String key, String text, DialogueMessage.Argument... arguments)
	{
		this.languageProvider.add(key, text);
		return new DialogueMessage(key, List.of(arguments));
	}
	
	private static String languageKeyBase(ResourceLocation id)
	{
		return id.getNamespace() + ".dialogue." + id.getPath().replace("/", ".");
	}
}
