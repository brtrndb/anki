package com.weekendesk.anki.impl;

import com.weekendesk.anki.core.service.MessagerService;

public class AnkiMessager implements MessagerService
{
	public AnkiMessager()
	{
	}

	@Override
	public void question(final String question)
	{
		System.out.println(question);
	}

	@Override
	public void answer(final String message)
	{
		System.out.print(message);
	}

	@Override
	public void message(final String message)
	{
		System.out.println(message);
	}
}
