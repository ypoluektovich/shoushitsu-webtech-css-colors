package org.shoushitsu.webtech.css.color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The mapping between color names and their ARGB codes.
 */
public class NamedColors {

	private static final Map<String, Integer> CODE_BY_NAME = new HashMap<>();

	private static final Map<Integer, String> NAME_BY_CODE = new HashMap<>();

	private static final int ALPHA_MASK = 0xFF << 24;

	static {
		addColorDefinition("transparent", 0);
		InputStream stream = NamedColors.class.getResourceAsStream("colortable");
		if (stream != null) {
			try (BufferedReader in = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
				String line;
				while ((line = in.readLine()) != null) {
					String[] parts = line.split("\t", 2);
					String name = parts[0];
					try {
						Integer code = ALPHA_MASK | Integer.parseInt(parts[1], 16);
						addColorDefinition(name, code);
					} catch (NumberFormatException e) {
						// Can't do anything. Suppress the exception, ignore this definition.
					}
				}
			} catch (IOException e) {
				// Can't do anything. Suppress the exception, act like no colors were defined.
			}
		}
	}

	private static void addColorDefinition(String name, Integer code) {
		CODE_BY_NAME.put(name, code);
		NAME_BY_CODE.put(code, name);
	}

	/**
	 * Get ARGB code of a color by its name, if defined.
	 * @param name the name of the color, case-insensitive.
	 * @return the ARGB code of the color, or {@code null} if the specified name is not defined.
	 */
	public static Integer getCodeByName(String name) {
		return name == null ? null : CODE_BY_NAME.get(name.toLowerCase(Locale.ENGLISH));
	}

	/**
	 * Get name of a color by its ARGB code.
	 * @param code the code of the color.
	 * @return the name of the color, of {@code null} if there's no name defined for the code.
	 */
	public static String getNameByCode(Integer code) {
		return NAME_BY_CODE.get(code);
	}

}
