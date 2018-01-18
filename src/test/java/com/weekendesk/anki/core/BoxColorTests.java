/**
 * 
 */
package com.weekendesk.anki.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

/**
 * @author bertrand
 *
 */
public class BoxColorTests
{
	@Test
	public void fromStringTestOK()
	{
		BoxColor color = BoxColor.fromString("RED");
		assertThat(color).isEqualTo(BoxColor.RED);
	}

	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void fromStringTestKO()
	{
		BoxColor.fromString("BLUE");
	}
}
