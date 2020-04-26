package com.nobilia.autocomplete;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class Indexer {

    public static Index run(String baseline, int nGramLength) {
        String[] words = cleanUp(baseline);

        return indexWords(words, nGramLength);
    }

    private static String[] cleanUp(String baseline) {
        return Arrays.stream(baseline.split("\n"))
            .flatMap(s -> Arrays.stream(s.split(" ")))
            .map(s -> s.replaceAll("[^A-Za-z0-9]", ""))
            .filter(s -> !s.trim().isEmpty())
            .map(s -> s.trim().toLowerCase())
            .toArray(String[]::new);
    }

    private static Index indexWords(String[] words, int nGramLength) {
        Map<String, Map<String, Double>> index = new HashMap<>();

        BaselineItem item;
        Map<String, Double> innerItem;

        for (int i = 0; i < words.length - nGramLength; i++) {
            item = sequence(i, words, nGramLength);

            if (index.containsKey(item.getSequence())) {
                innerItem = index.get(item.getSequence());
                innerItem.put(item.getNextWord(), innerItem.getOrDefault(item.getNextWord(), 0.0) + 1.0);
            } else {
                index.put(item.getSequence(), new HashMap<>(Map.of(item.getNextWord(), 1.0)));
            }
        }

        return new Index(index);
    }

    private static BaselineItem sequence(int lowerBound, String[] words, int nGramLength) {
        StringJoiner joiner = new StringJoiner(" ");

        for (int i = lowerBound; i < lowerBound + (nGramLength - 1); i++) {
            joiner.add(words[i]);
        }

        return new BaselineItem(joiner.toString(), words[lowerBound + nGramLength - 1]);
    }

    private static class BaselineItem {
        private String sequence;
        private String nextWord;

        public BaselineItem(String sequence, String nextWord) {
            this.sequence = sequence;
            this.nextWord = nextWord;
        }

        public String getSequence() {
            return sequence;
        }

        public String getNextWord() {
            return nextWord;
        }

        @Override
        public String toString() {
            return "BaselineItem{" +
                "sequence='" + sequence + '\'' +
                ", nextWord='" + nextWord + '\'' +
                '}';
        }
    }
}
