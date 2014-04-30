package org.shoushitsu.webtech.css.color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NamedColors {

	private static final Map<String, Integer> CODE_BY_NAME = new HashMap<>();

	private static final Map<Integer, String> NAME_BY_CODE = new HashMap<>();

	static {
		InputStream stream = NamedColors.class.getResourceAsStream("colortable");
		if (stream != null) {
			try (BufferedReader in = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
				String line;
				while ((line = in.readLine()) != null) {
					String[] parts = line.split("\t", 2);
					String name = parts[0];
					try {
						Integer code = Integer.parseInt(parts[1], 16);
						CODE_BY_NAME.put(name, code);
						NAME_BY_CODE.put(code, name);
					} catch (NumberFormatException e) {
						// Can't do anything. Suppress the exception, ignore this definition.
					}
				}
			} catch (IOException e) {
				// Can't do anything. Suppress the exception, act like no colors were defined.
			}
		}
	}

	public static Integer getCodeByName(String name) {
		return CODE_BY_NAME.get(name.toLowerCase(Locale.ENGLISH));
	}

	public static String getNameByCode(Integer code) {
		return NAME_BY_CODE.get(code);
	}

}
