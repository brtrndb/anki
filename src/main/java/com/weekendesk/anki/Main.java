package com.weekendesk.anki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weekendesk.anki.core.AnkiGame;

public final class Main
{
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args)
	{
		log.debug("Welcome.");
		try
		{
			AnkiGame game = buildGame(args);
			game.start();
		}
		catch (Exception e)
		{
			log.error("Oups... Something went wrong...", e);
		}
	}

	private static AnkiGame buildGame(String[] args)
	{
		AnkiGame game;

		if (args.length == 0)
			game = new AnkiGame(null);
		else
		{
			if (args.length > 1)
				log.warn("There is more than one parameter. Only first will be used, others are ignored.");
			game = new AnkiGame(args[0]);
		}

		log.debug("Game built.");

		return (game);
	}
}
