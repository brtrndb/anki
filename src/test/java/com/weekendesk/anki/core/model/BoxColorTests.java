/**
 *
 */
package com.weekendesk.anki.core.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

/**
 * @author bertrand
 */
public class BoxColorTests
{
	@Test
	public void fromStringTestOK()
	{
		final BoxColor color = BoxColor.fromString("RED");
		assertThat(color).isEqualTo(BoxColor.RED);
	}

	@Test(expectedExceptions = { IllegalArgumentException.class })
	public void fromStringTestKO()
	{
		BoxColor.fromString("BLUE");
	}
}
