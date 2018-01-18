/**
 * 
 */
package com.weekendesk.anki.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bertrand
 *
 */
public class AnkiGame
{
	private static final Logger	log	= LoggerFactory.getLogger(AnkiGame.class);

	private String				sessionFilePath;
	private Session				session;

	/**
	 * 
	 * @param sessionFilePath
	 *            Path to questions file.
	 */
	public AnkiGame(String sessionFilePath)
	{
		this.sessionFilePath = sessionFilePath;
	}

	/**
	 * Start the game.
	 */
	public void start()
	{
		log.debug("Preparing game.");
		try
		{
			buildSession();
			startGame();
			endGame();
		}
		catch (AnkiException e)
		{
			log.error("An error occurs...", e);
		}
	}

	private void buildSession() throws AnkiException
	{
		if (sessionFilePath == null)
			this.session = new Session();
		else
			this.session = new Session(this.sessionFilePath);

		this.session.load();
		log.debug("Session built.");
	}

	private void startGame() throws AnkiException
	{
		List<Card> cardsToStudy = this.session.getBoxContent(BoxColor.RED);

		log.debug("Starting game.");

		if (cardsToStudy.isEmpty())
		{
			System.out.println("You've already mastered all questions !");
			return;
		}

		try (InputStreamReader isr = new InputStreamReader(System.in); BufferedReader br = new BufferedReader(isr))
		{
			String answer;
			for (Card card : cardsToStudy)
			{
				System.out.println("Question: " + card.getQuestion());
				System.out.print("Answer: ");
				answer = br.readLine();

				if (answer.toLowerCase().equals(card.getAnswer().toLowerCase()))
					this.putCardIntoBox("Well done !", BoxColor.GREEN, card);
				else
					this.putCardIntoBox("Wrong... Correct answer is: " + card.getAnswer(), BoxColor.ORANGE, card);
			}

			System.out.println("No more question for today.");
		}
		catch (IOException e)
		{
			throw (new AnkiException("Cannot read answer", e));
		}
	}

	private void putCardIntoBox(String message, BoxColor color, Card card)
	{
		System.out.println(message);
		log.debug("Put down card into the {} box.", color.name());
		this.session.addToBox(card, color);
	}

	private void endGame() throws AnkiException
	{
		this.session.end();
		if (this.session.getBoxContent(BoxColor.RED).isEmpty() && this.session.getBoxContent(BoxColor.ORANGE).isEmpty())
			System.out.println("You've learn everything ! Good job !");
		else
			System.out.println("See you tomorrow :)");
		this.session.save();
	}
}
