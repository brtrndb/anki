package com.weekendesk.anki.core.model;

import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Game
{
	@Getter
	@Setter
	private Map<BoxColor, List<Card>> boxes;

	public Game(Map<BoxColor, List<Card>> boxes)
	{
		this.boxes = boxes;
	}

	public List<Card> getBox(BoxColor color)
	{
		return (this.boxes.get(color));
	}
}
