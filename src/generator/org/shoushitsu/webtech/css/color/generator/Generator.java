package org.shoushitsu.webtech.css.color.generator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Generator {

	private static final String STYLE = "style";

	private static final int STYLE_PREFIX_LENGTH = "background:".length();

	public static void main(String[] args) throws IOException {
		Document specDocument = loadSpec(Paths.get(args[0]));
		Elements rows = specDocument.select("table.colortable tr:not(:has(th))");
		Set<String> knownNames = new HashSet<>();
		try (BufferedWriter out = Files.newBufferedWriter(Paths.get(args[1]), StandardCharsets.UTF_8)) {
			for (Element row : rows) {
				String colorName = row.child(0).attr(STYLE).substring(STYLE_PREFIX_LENGTH).toLowerCase(Locale.ENGLISH);
				if (!knownNames.contains(colorName)) {
					out.write(colorName);
					out.write("\t");
					out.write(row.child(1).attr(STYLE).substring(STYLE_PREFIX_LENGTH + 1));
					out.newLine();
					knownNames.add(colorName);
				}
			}
		}
		System.out.println("Done: processed " + rows.size() + " color definitions, " + knownNames.size() + " unique");
	}

	private static Document loadSpec(Path specFile) throws IOException {
		Document specDocument;
		try (BufferedInputStream in = new BufferedInputStream(Files.newInputStream(specFile))) {
			specDocument = Jsoup.parse(in, StandardCharsets.UTF_8.name(), "");
		}
		return specDocument;
	}

}
