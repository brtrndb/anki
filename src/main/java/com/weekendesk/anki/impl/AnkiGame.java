package com.weekendesk.anki.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.weekendesk.anki.core.model.AnkiException;
import com.weekendesk.anki.core.model.BoxColor;
import com.weekendesk.anki.core.model.Card;
import com.weekendesk.anki.core.model.Game;
import com.weekendesk.anki.core.service.GameService;
import com.weekendesk.anki.core.service.LoaderService;
import com.weekendesk.anki.core.service.MessagerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AnkiGame implements GameService
{
	private final String			gameFilePath;
	private final LoaderService		loader;
	private final MessagerService	messager;
	private Game					game;

	public AnkiGame(final String sessionFilePath)
	{
		this.gameFilePath = sessionFilePath;
		this.loader = new AnkiLoader();
		this.messager = new AnkiMessager();
		this.game = null;
	}

	@Override
	public void startGame() throws AnkiException
	{
		log.debug("Preparing game.");
		this.buildGame();
		this.playGame();
		this.endGame();
	}

	private void buildGame() throws AnkiException
	{
		this.game = this.loader.loadGame(this.gameFilePath);
		this.prepareGameFromPreviousSession();
		log.debug("Game is ready to play.");
	}

	private void prepareGameFromPreviousSession()
	{
		final Map<BoxColor, List<Card>> boxes = new HashMap<>();
		final List<Card> redCards = this.getCardsFromBox(BoxColor.RED);
		final List<Card> orangeCards = this.getCardsFromBox(BoxColor.ORANGE);
		final List<Card> greenCards = this.getCardsFromBox(BoxColor.GREEN);

		redCards.forEach(c -> c.setColor(c.getColor().next()));
		orangeCards.forEach(c -> c.setColor(c.getColor().next()));
		greenCards.forEach(c -> c.setColor(c.getColor().next()));

		boxes.put(BoxColor.RED, redCards);
		boxes.get(BoxColor.RED).addAll(orangeCards);
		boxes.put(BoxColor.ORANGE, greenCards);
		boxes.put(BoxColor.GREEN, new ArrayList<>());

		this.game.setBoxes(boxes);
		log.debug("Game prepared: {}", this.game);
	}

	private void playGame() throws AnkiException
	{
		final List<Card> cardsToStudy = this.getCardsFromBox(BoxColor.RED);

		log.debug("Starting game.");

		this.messager.message("Welcome to Anki !");

		if (cardsToStudy.isEmpty())
		{
			this.messager.message("You've already mastered all questions !");
			return;
		}

		this.messager.message("Today, there are " + cardsToStudy.size() + " questions to memorise.");

		try (InputStreamReader isr = new InputStreamReader(System.in); BufferedReader br = new BufferedReader(isr))
		{
			String answer;
			for (final Card card : cardsToStudy)
			{
				this.messager.question("Question: " + card.getQuestion());
				this.messager.answer("Answer: ");
				answer = br.readLine();

				if (answer.toLowerCase().equals(card.getAnswer().toLowerCase()))
					this.promoteCard(card, BoxColor.GREEN);
				else
					this.promoteCard(card, BoxColor.ORANGE);
			}

			this.messager.message("No more question for today.");
			this.game.getBox(BoxColor.RED).clear();
		}
		catch (final IOException e)
		{
			throw (new AnkiException("Cannot read answer", e));
		}
	}

	@Override
	public List<Card> getCardsFromBox(final BoxColor color)
	{
		return (this.game.getBox(color));
	}

	@Override
	public Card promoteCard(final Card card, final BoxColor color)
	{
		final Card promoted = new Card(card.getQuestion(), card.getAnswer(), color);
		this.getCardsFromBox(color).add(promoted);
		this.messager.message("Card moved to box " + color.name() + ".");
		return (promoted);
	}

	@Override
	public void endGame() throws AnkiException
	{
		final List<Card> redCards = this.getCardsFromBox(BoxColor.RED);
		final List<Card> orangeCards = this.getCardsFromBox(BoxColor.ORANGE);

		if (redCards.isEmpty() && orangeCards.isEmpty())
			this.messager.message("You've learn everything ! Good job !");
		else
			this.messager.message("See you tomorrow :)");

		this.loader.saveGame(this.game, null);
	}
}
