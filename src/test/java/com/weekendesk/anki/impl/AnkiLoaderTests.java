package com.weekendesk.anki.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.weekendesk.anki.core.model.AnkiException;
import com.weekendesk.anki.core.model.BoxColor;
import com.weekendesk.anki.core.model.Game;
import com.weekendesk.anki.core.service.LoaderService;

public class AnkiLoaderTests
{
	public static final String	QUESTIONS_OK	= "./src/test/resources/questionsOk.txt";
	public static final String	QUESTIONS_KO	= "./src/test/resources/questionsKo.txt";
	public static final String	SESSION_OK		= "./src/test/resources/sessionOk.txt";
	public static final String	SESSION_KO		= "./src/test/resources/sessionKo.txt";
	public static final String	SESSION_TMP		= "./src/test/resources/sessionTmp.txt";

	private final LoaderService	loader			= new AnkiLoader();

	@Test
	public void loadGameTestWithGoodFile() throws AnkiException
	{
		final Game game = this.loader.loadGame(QUESTIONS_OK);
		assertThat(game.getBox(BoxColor.RED).size()).isEqualTo(3);
		assertThat(game.getBox(BoxColor.ORANGE).size()).isEqualTo(0);
		assertThat(game.getBox(BoxColor.GREEN).size()).isEqualTo(0);
	}

	@Test(expectedExceptions = { ArrayIndexOutOfBoundsException.class })
	public void loadGameTestWithBadFile() throws AnkiException
	{
		this.loader.loadGame(QUESTIONS_KO);
	}

	@Test
	public void loadGameWithGoodSessionFile() throws AnkiException
	{
		final Game game = this.loader.loadGame(SESSION_OK);
		assertThat(game.getBox(BoxColor.RED).size()).isEqualTo(0);
		assertThat(game.getBox(BoxColor.ORANGE).size()).isEqualTo(2);
		assertThat(game.getBox(BoxColor.GREEN).size()).isEqualTo(1);
	}

	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void loadGameWithBadSessionFile() throws AnkiException
	{
		this.loader.loadGame(SESSION_KO);
	}

	@Test
	public void saveGameTest() throws AnkiException
	{
		final Game game = this.loader.loadGame(SESSION_OK);
		this.loader.saveGame(game, SESSION_TMP);
		final Game gameSaved = this.loader.loadGame(SESSION_TMP);
		assertThat(game).isEqualTo(gameSaved);
	}
}
