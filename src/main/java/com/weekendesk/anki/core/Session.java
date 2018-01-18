/**
 * 
 */
package com.weekendesk.anki.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bertrand
 *
 */
public class Session
{
	private static final Logger			log					= LoggerFactory.getLogger(Session.class);

	private static final String			SESSION_FILENAME	= "session.txt";

	private String						sessionFilePath;
	private Map<BoxColor, List<Card>>	boxes;

	/**
	 * Create a session from saved file.
	 */
	public Session()
	{
		this(SESSION_FILENAME);
	}

	/**
	 * Create a session from a new set of cards.
	 */
	public Session(String sessionFilePath)
	{
		this.sessionFilePath = sessionFilePath;
	}

	/**
	 * Load the session.
	 * 
	 * @throws AnkiException
	 */
	public void load() throws AnkiException
	{
		log.debug("Building cards and boxes for this session.");
		this.loadFile();
		this.state();
		log.debug("Cards built.");
	}

	/**
	 * Load the files and build questions.
	 * 
	 * @throws AnkiException
	 */
	private void loadFile() throws AnkiException
	{
		try (Stream<String> stream = Files.lines(Paths.get(this.sessionFilePath)).skip(1))
		{
			this.boxes = stream.map(this::lineToCard).collect(Collectors.groupingBy(Card::getColor));
			for (BoxColor color : BoxColor.values()) // Fill empty boxes.
				if (!this.boxes.containsKey(color))
					this.boxes.put(color, new ArrayList<>());
		}
		catch (IOException e)
		{
			throw (new AnkiException("Cannot parse file", e));
		}
	}

	/**
	 * Build a question from a line.
	 * 
	 * @param line
	 * @return The question.
	 */
	private Card lineToCard(String line)
	{
		Card card;
		String[] data = line.split("\\|");
		if (data.length < 2 || 3 < data.length)
			throw (new ArrayIndexOutOfBoundsException("Array size expected 2 or 3, got " + data.length));

		// In a session file, if a color box is missing for a question, it will be put in the red box.
		if (data.length == 2)
			card = new Card(data[0], data[1]);
		else
			card = new Card(data[0], data[1], data[2]);

		log.debug("{}.", card);
		return (card);
	}

	public List<Card> getBoxContent(BoxColor color)
	{
		return (this.boxes.get(color));
	}

	public void addToBox(Card card, BoxColor color)
	{
		card.setColor(color);
		this.boxes.get(color).add(card);
	}

	/**
	 * End the session.
	 */
	public void end()
	{
		this.boxes.get(BoxColor.RED).clear();
		if (!boxes.get(BoxColor.ORANGE).isEmpty())
			this.boxes.values()
					.stream()
					.flatMap(List::stream)
					.forEach(Card::promote);
	}

	/**
	 * Save the session to a file.
	 * 
	 * @throws AnkiException
	 */
	public void save() throws AnkiException
	{
		try
		{
			this.state();
			List<String> lines = this.boxes.values()
					.stream()
					.flatMap(List::stream)
					.map(Card::saveFormat)
					.collect(Collectors.toList());
			lines.add(0, "question|answer|box");
			Files.write(Paths.get(SESSION_FILENAME), lines, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		}
		catch (IOException e)
		{
			throw (new AnkiException("Cannot parse file", e));
		}
	}

	/**
	 * Log the state of the session.
	 */
	private void state()
	{
		List<Card> red = this.boxes.get(BoxColor.RED);
		List<Card> orange = this.boxes.get(BoxColor.ORANGE);
		List<Card> green = this.boxes.get(BoxColor.GREEN);
		log.debug("Red: {} | Orange: {} | Green: {}.", red.size(), orange.size(), green.size());
	}
}
