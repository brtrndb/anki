package com.weekendesk.anki.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Card
{
	@Getter
	private String		question;
	@Getter
	private String		answer;
	@Getter
	@Setter
	private BoxColor	color;

	public Card(String question, String answer)
	{
		this(question, answer, BoxColor.RED.name());
	}

	public Card(String question, String answer, BoxColor color)
	{
		this.question = question;
		this.answer = answer;
		this.color = color;
	}

	public Card(String question, String answer, String level)
	{
		this(question, answer, BoxColor.fromString(level));
	}

	public void promote()
	{
		this.color = this.color.next();
	}
}
