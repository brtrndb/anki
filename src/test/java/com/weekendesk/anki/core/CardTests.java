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
public class CardTests
{
	@Test
	public void saveFormatTest()
	{
		Card card = new Card("Question ?", "Answer", BoxColor.ORANGE.name());
		assertThat(card.saveFormat()).isEqualTo("Question ?|Answer|ORANGE");
	}

	@Test
	public void promoteTest()
	{
		Card card0 = new Card("Question ?", "Answer", BoxColor.GREEN.name());
		Card card1 = new Card("Question ?", "Answer", BoxColor.ORANGE.name());
		Card card2 = new Card("Question ?", "Answer", BoxColor.RED.name());
		card0.promote();
		assertThat(card0).isEqualTo(card1);
		card1.promote();
		assertThat(card1).isEqualTo(card2);
	}
}
