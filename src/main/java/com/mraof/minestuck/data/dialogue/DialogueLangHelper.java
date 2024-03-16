package com.mraof.minestuck.data.dialogue;

import com.mraof.minestuck.entity.dialogue.DialogueMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.List;
import java.util.function.Function;

public final class DialogueLangHelper
{
	private final LanguageProvider languageProvider;
	
	public DialogueLangHelper(LanguageProvider languageProvider)
	{
		this.languageProvider = languageProvider;
	}
	
	@Deprecated
	public static Function<ResourceLocation, DialogueMessage> defaultKeyMsg(DialogueMessage.Argument... arguments)
	{
		return id -> msg(languageKeyBase(id), arguments);
	}
	
	public Function<ResourceLocation, DialogueMessage> defaultKeyMsg(String text, DialogueMessage.Argument... arguments)
	{
		return id -> msg(languageKeyBase(id), text, arguments);
	}
	
	public Function<ResourceLocation, DialogueMessage> subMsg(String key, String text, DialogueMessage.Argument... arguments)
	{
		return id -> msg(languageKeyBase(id) + "." + key, text, arguments);
	}
	
	@Deprecated
	public static DialogueMessage msg(String key, DialogueMessage.Argument... arguments)
	{
		return new DialogueMessage(key, List.of(arguments));
	}
	
	public DialogueMessage msg(String key, String text, DialogueMessage.Argument... arguments)
	{
		this.languageProvider.add(key, text);
		return new DialogueMessage(key, List.of(arguments));
	}
	
	public Function<ResourceLocation, String> subText(String subKey, String text)
	{
		return id -> {
			String key = languageKeyBase(id) + "." + subKey;
			this.languageProvider.add(key, text);
			return key;
		};
	}
	
	private static String languageKeyBase(ResourceLocation id)
	{
		return id.getNamespace() + ".dialogue." + id.getPath().replace("/", ".");
	}
}
