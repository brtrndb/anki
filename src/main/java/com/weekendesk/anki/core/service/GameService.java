package com.weekendesk.anki.core.service;

import java.util.List;

import com.weekendesk.anki.core.model.AnkiException;
import com.weekendesk.anki.core.model.BoxColor;
import com.weekendesk.anki.core.model.Card;

public interface GameService
{
	public void startGame() throws AnkiException;

	public List<Card> getCardsFromBox(BoxColor color);

	public Card promoteCard(Card card, BoxColor color);

	public void endGame() throws AnkiException;
}
