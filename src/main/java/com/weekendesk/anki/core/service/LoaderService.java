package com.weekendesk.anki.core.service;

import com.weekendesk.anki.core.model.AnkiException;
import com.weekendesk.anki.core.model.Game;

public interface LoaderService
{
	public Game loadGame(String inputFile) throws AnkiException;

	public void saveGame(Game session, String outputFile) throws AnkiException;
}
