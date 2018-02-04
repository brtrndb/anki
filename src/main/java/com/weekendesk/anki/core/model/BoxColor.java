package com.weekendesk.anki.core.model;

public enum BoxColor
{
	RED("RED", null),
	ORANGE("ORANGE", RED),
	GREEN("GREEN", ORANGE);

	private final String	color;
	private final BoxColor	next;

	private BoxColor(String color, BoxColor next)
	{
		this.color = color;
		this.next = next;
	}

	public BoxColor next()
	{
		if (this == BoxColor.RED)
			return (BoxColor.RED);
		return (this.next);
	}

	public static BoxColor fromString(String str)
	{
		for (BoxColor color : BoxColor.values())
			if (color.color.equals(str))
				return color;
		throw new IllegalArgumentException("Invalid color " + str);
	}
}
