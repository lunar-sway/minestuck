package com.mraof.minestuck.data.dialogue;

import com.mraof.minestuck.data.dialogue.DialogueProvider.MessageProducer;
import com.mraof.minestuck.entity.dialogue.DialogueMessage;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.List;
import java.util.function.Function;

/**
 * A helper class for creating {@link MessageProducer}s and register text for it to the language provider with one function call.
 */
public final class DialogueLangHelper
{
	private final String modid;
	private final LanguageProvider languageProvider;
	
	public DialogueLangHelper(String modid, LanguageProvider languageProvider)
	{
		this.modid = modid;
		this.languageProvider = languageProvider;
	}
	
	public MessageProducer defaultKeyMsg(String text, DialogueMessage.Argument... arguments)
	{
		return id -> registerAndBuild(languageKeyBase(id), text, arguments);
	}
	
	public MessageProducer subMsg(String key, String text, DialogueMessage.Argument... arguments)
	{
		return id -> registerAndBuild(languageKeyBase(id) + "." + key, text, arguments);
	}
	
	public MessageProducer msg(ResourceLocation id, String key, String text, DialogueMessage.Argument... arguments)
	{
		return msg(registerAndBuild(languageKeyBase(id) + "." + key, text, arguments));
	}
	
	public MessageProducer msg(String key, String text, DialogueMessage.Argument... arguments)
	{
		return msg(registerAndBuild(languageKeyBase(new ResourceLocation(modid, key)), text, arguments));
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
