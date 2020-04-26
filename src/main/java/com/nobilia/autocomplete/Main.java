package com.nobilia.autocomplete;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Main {

    private final static Map<Integer, Index> cache = new HashMap<>();
    private static String baseline;

    public static void main(String... args) throws IOException {
        baseline = baseline();

        System.out.println("the");
        run(2, "the");
        System.out.println("did");
        run(2, "did");
        System.out.println("lamb");
        run(2, "lamb");
        System.out.println("reply");
        run(2, "reply");
        System.out.println("the lamb");
        run(3, "the lamb");
        System.out.println("the lamb love");
        run(4, "the lamb love");
        System.out.println("The teacher did");
        run(4, "The teacher did");
    }

    private static void run(Integer nGramLength, String userInput) {
        if (!cache.containsKey(nGramLength)) {
            cache.put(nGramLength, Indexer.run(baseline, nGramLength));
        }

        System.out.println(
            cache.get(nGramLength)
                .predictNextWord(userInput.trim().toLowerCase())
                .map(Index.IndexItem::toString)
                .orElse(""));
    }

    private static String baseline() throws IOException {
        return IOUtils.toString(
            Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("baseline.txt")),
            StandardCharsets.UTF_8.name());
    }
}
