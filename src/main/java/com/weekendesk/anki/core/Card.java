package com.weekendesk.anki.core;

import java.util.Objects;

public class Card
{
	private String		question;
	private String		answer;
	private BoxColor	color;

	public Card(String question, String answer)
	{
		this(question, answer, BoxColor.RED.name());
	}

	public Card(String question, String answer, String level)
	{
		this.question = question;
		this.answer = answer;
		this.color = BoxColor.fromString(level);
	}

	public String getQuestion()
	{
		return question;
	}

	public String getAnswer()
	{
		return answer;
	}

	public BoxColor getColor()
	{
		return color;
	}

	public void setColor(BoxColor color)
	{
		this.color = color;
	}

	public String saveFormat()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(question).append("|")
				.append(answer).append("|")
				.append(color);
		return (builder.toString());
	}

	public void promote()
	{
		this.color = this.color.next();
	}

	@Override
	public int hashCode()
	{
		return (Objects.hash(question, answer, color));
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		else if (obj == null || obj.getClass() != getClass())
			return false;

		final Card other = (Card) obj;

		return (Objects.equals(this.question, other.question)
				&& Objects.equals(this.answer, other.answer)
				&& Objects.equals(this.color, other.color));
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("AnkiCard [")
				.append("question=").append(question).append(", ")
				.append("answer=").append(answer).append(", ")
				.append("color=").append(color)
				.append("]");
		return (builder.toString());
	}
}
