package com.nobilia.autocomplete;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Ngram {

    private final static Map<Integer, Index> cache = new HashMap<>();
    private static String baseline;

    public static void main(String... args) throws IOException {
        baseline = baseline();

        run(2, "the");
        run(2, "lamb");
        run(3, "the lamb");
        run(4, "the lamb love");
    }

    private static void run(Integer nGramLength, String userInput) {
        if (!cache.containsKey(nGramLength)) {
            cache.put(nGramLength, Indexer.run(baseline, nGramLength));
        }

        System.out.println(
            cache.get(nGramLength)
                .predictNextWord(userInput)
                .map(Index.IndexItem::toString)
                .orElse(""));
    }

    private static String baseline() throws IOException {
        return IOUtils.toString(
            Objects.requireNonNull(Ngram.class.getClassLoader().getResourceAsStream("baseline.txt")),
            StandardCharsets.UTF_8.name());
    }
}
