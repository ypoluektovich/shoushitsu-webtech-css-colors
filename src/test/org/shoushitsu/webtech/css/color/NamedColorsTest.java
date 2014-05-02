package org.shoushitsu.webtech.css.color;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class NamedColorsTest {

	public static final String RED_NAME = "red";

	public static final Integer RED_CODE = NamedColors.ALPHA_MASK | (0xFF << 16);

	@Test
	public void redCodeByName() throws Exception {
		assertEquals(NamedColors.getCodeByName(RED_NAME), RED_CODE);
	}

	@Test
	public void redNameByCode() throws Exception {
		assertEquals(NamedColors.getNameByCode(RED_CODE), RED_NAME);
	}

}
