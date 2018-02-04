package com.weekendesk.anki.core.service;

public interface MessagerService
{
	public void question(String question);

	public void answer(String message);

	public void message(String message);
}
