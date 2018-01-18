/**
 * 
 */
package com.weekendesk.anki.core;

/**
 * @author bertrand
 *
 */
public class AnkiException extends Exception
{
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 355991565269108963L;

	public AnkiException(String message)
	{
		super(message);
	}

	public AnkiException(String message, Throwable t)
	{
		super(message, t);
	}
}
