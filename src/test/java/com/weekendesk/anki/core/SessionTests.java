/**
 * 
 */
package com.weekendesk.anki.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.testng.annotations.Test;

/**
 * @author bertrand
 *
 */
public class SessionTests
{
	@Test
	public void loadNewTestOk() throws AnkiException
	{
		Session session = new Session("./src/test/resources/questionsOk.txt");
		session.load();
		assertThat(session.getBoxContent(BoxColor.RED).size()).isEqualTo(3);
		assertThat(session.getBoxContent(BoxColor.ORANGE).size()).isEqualTo(0);
		assertThat(session.getBoxContent(BoxColor.GREEN).size()).isEqualTo(0);
	}

	@Test(expectedExceptions = { ArrayIndexOutOfBoundsException.class })
	public void loadNewTestKo() throws AnkiException
	{
		Session session = new Session("./src/test/resources/questionsKo.txt");
		session.load();
	}

	@Test
	public void loadResumeTestOk() throws AnkiException
	{
		Session session = new Session("./src/test/resources/sessionOk.txt");
		session.load();
		assertThat(session.getBoxContent(BoxColor.RED).size()).isEqualTo(0);
		assertThat(session.getBoxContent(BoxColor.ORANGE).size()).isEqualTo(2);
		assertThat(session.getBoxContent(BoxColor.GREEN).size()).isEqualTo(1);
	}

	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void loadResumeTestKo() throws AnkiException
	{
		Session session = new Session("./src/test/resources/sessionKo.txt");
		session.load();
	}

	@Test
	public void saveSessionTest() throws AnkiException, IOException
	{
		List<String> lines0;
		List<String> lines1;

		try (Stream<String> stream = Files.lines(Paths.get("./src/test/resources/sessionOk.txt")).skip(1))
		{
			lines0 = stream.sorted().collect(Collectors.toList());
		}

		Session session = new Session("./src/test/resources/sessionOk.txt");
		session.load();
		session.save();

		try (Stream<String> stream = Files.lines(Paths.get("session.txt")).skip(1))
		{
			lines1 = stream.sorted().collect(Collectors.toList());
		}

		assertThat(lines0).isEqualTo(lines1);
	}
}
