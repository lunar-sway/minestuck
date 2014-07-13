package com.mraof.minestuck.pesterchum;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageParser implements Runnable
{
	IRC irc;
	ArrayList<Chat> chats = new ArrayList<Chat>();
	LinkedBlockingQueue<String> messages;
	String chumhandleBase;
	int chumhandleSuffix = 0;

	public MessageParser(IRC irc)
	{
		this.irc = irc;
		messages = new LinkedBlockingQueue<String>();
		this.chumhandleBase = this.irc.chumhandle;
	}

	@Override
	public void run()
	{
		while (irc.running)
			try 
			{
				onRecieved(messages.take());
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
	}

	public void add(String message)
	{
		messages.add(message);
	}

	public void onRecieved(String message)
	{
		if(message.charAt(0) == ':')
		{
			message = splitFirst(message)[1];
		}
		String[] splitString = splitFirst(message);
		String command = splitString[0].toUpperCase();
		message = splitString[1];
		if(command.equals("PING"))
		{
			this.send("PONG " + message);
		}
		else if(command.equals("001"))
		{
			//Registered
		}
		else if(command.equals("433"))
		{
			this.chumhandleSuffix++;
			this.irc.chumhandle = chumhandleBase + chumhandleSuffix;
			this.send("NICK " + this.irc.chumhandle);
		}
		else if(command.equals("PRIVMSG"))
		{
		}

	}

	public void onMessage(String user, String destination, String message)
	{
	}

	public void setNick(String nick)
	{
		this.chumhandleBase = nick;
		this.chumhandleSuffix = 0;
		this.irc.chumhandle = nick;
	}

	public void message(String destination, String message)
	{
		send("PRIVMSG " + destination + " :" + message);
	}

	public void send(String message)
	{
		this.irc.output.println(message);
	}

	public static String[] splitFirst(String string)
	{
		return splitFirst(string, " ");
	}

	public static String[] splitFirst(String string, String delimiter)
	{
		int splitIndex = string.indexOf(delimiter);
		if(splitIndex != -1)
		{
			return new String[] {string.substring(0, splitIndex), string.substring(splitIndex + 1)};
		}
		else
		{
			return new String[] {string, ""};
		}
	}
		
}
