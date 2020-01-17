package com.mraof.minestuck.pesterchum;

import java.util.ArrayList;
import java.util.HashMap;

public class Chat
{
	public ArrayList<String> messages = new ArrayList<String>();
	public HashMap<String, User> users = new HashMap<String, User>();

	public void addMessage(String user, String message)
	{
		messages.add(0, message);
	}

	public void addUser(String user)
	{
		users.put(user, new User(user));
	}

	public void removeUser(String user)
	{
		users.remove(user);
	}

	public void setUserColor(String user, int color)
	{
		users.get(user).textColor = color;
	}
}
