/**
 * 
 */
package com.weekendesk.anki.core;

/**
 * @author bertrand
 *
 */
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
		return (this.next);
	}

	public static BoxColor fromString(String str)
	{
		for (BoxColor color : values())
			if (color.color.equals(str))
				return color;
		throw new IllegalArgumentException("Invalid color " + str);
	}
}
