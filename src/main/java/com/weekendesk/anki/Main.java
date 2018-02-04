package com.weekendesk.anki;

import com.weekendesk.anki.core.model.AnkiException;
import com.weekendesk.anki.core.service.GameService;
import com.weekendesk.anki.impl.AnkiGame;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Main
{
	public static void main(final String[] args)
	{
		try
		{
			log.debug("Starting Anki.");
			final GameService game = buildGame(args);
			game.startGame();
			log.debug("Ending Anki.");
		}
		catch (final AnkiException e)
		{
			log.error("An error occurs...", e);
		}
		catch (final Exception e)
		{
			log.error("Oups... Something went wrong...", e);
		}
	}

	private static GameService buildGame(final String[] args)
	{
		GameService game;

		if (args.length == 0)
			game = new AnkiGame(null);
		else
			game = new AnkiGame(args[0]);

		if (1 < args.length)
			log.warn("There is more than one parameter. Only first will be used, others are ignored.");

		log.debug("Game built.");

		return (game);
	}
}
