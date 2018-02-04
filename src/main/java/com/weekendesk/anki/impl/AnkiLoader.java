package com.weekendesk.anki.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.weekendesk.anki.core.model.AnkiException;
import com.weekendesk.anki.core.model.BoxColor;
import com.weekendesk.anki.core.model.Card;
import com.weekendesk.anki.core.model.Game;
import com.weekendesk.anki.core.service.LoaderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AnkiLoader implements LoaderService
{
	private static final String	DEFAULT_IN_FILE		= "session.anki";
	private static final String	DEFAULT_OUT_FILE	= "session.anki";

	public AnkiLoader()
	{
	}

	@Override
	public Game loadGame(final String inputFile) throws AnkiException
	{
		final String path = (inputFile == null) || inputFile.isEmpty() ? DEFAULT_IN_FILE : inputFile;
		Map<BoxColor, List<Card>> boxes = new HashMap<>();

		try (Stream<String> stream = Files.lines(Paths.get(path)).skip(1))
		{
			boxes = stream.sorted()
					.map(this::lineToCard)
					.collect(Collectors.groupingBy(Card::getColor));

			for (final BoxColor color : BoxColor.values()) // Fill empty boxes.
				if (!boxes.containsKey(color))
					boxes.put(color, new ArrayList<>());

			final Game game = new Game(boxes);
			log.debug("Game loaded: {}", game);
			return (game);
		}
		catch (final IOException e)
		{
			throw (new AnkiException("Cannot parse file", e));
		}
	}

	private Card lineToCard(final String line)
	{
		Card card;
		BoxColor color;
		final String[] data = line.split("\\|");

		if ((data.length < 2) || (3 < data.length))
			throw (new ArrayIndexOutOfBoundsException("Array size expected 2 or 3, got " + data.length));

		// In a session file, if a color box is missing for a question, it will be put in the red box.
		if (data.length == 2)
			color = BoxColor.RED;
		else
			color = BoxColor.fromString(data[2]);

		card = new Card(data[0], data[1], color);

		log.debug("{}.", card);
		return (card);
	}

	@Override
	public void saveGame(final Game game, final String outputFile) throws AnkiException
	{
		try
		{
			final String path = (outputFile == null) || outputFile.isEmpty() ? DEFAULT_OUT_FILE : outputFile;
			final List<String> lines = game.getBoxes().values()
					.stream()
					.flatMap(List::stream)
					.map(this::cardToLine)
					.collect(Collectors.toList());
			lines.add(0, "question|answer|box");
			Files.write(Paths.get(path), lines, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
			log.debug("Game saved: {}", game);
		}
		catch (final IOException e)
		{
			throw (new AnkiException("Cannot parse file", e));
		}
	}

	private String cardToLine(final Card card)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append(card.getQuestion()).append("|")
				.append(card.getAnswer()).append("|")
				.append(card.getColor());
		return (builder.toString());
	}
}
