package com.nobilia.autocomplete;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Index {

    public static class Prediction implements Comparable<Prediction> {

        private static final NumberFormat formatter = new DecimalFormat("#0.000");

        private final String value;
        private final Double weight;

        public Prediction(String value, Double occurrence, Double domainSize) {
            this.value = value;
            this.weight = occurrence / domainSize;
        }

        @Override
        public String toString() {
            return value + "," + formatter.format(weight);
        }

        @Override
        public int compareTo(Prediction o) {
            return weight.compareTo(o.weight) == 0 ? value.compareTo(o.value) : weight.compareTo(o.weight);
        }
    }

    public static class IndexItem {
        private final Set<Prediction> predictions;

        public IndexItem(Map<String, Double> input) {
            double domainSize = Integer.valueOf(input.size()).doubleValue();

            predictions = input.entrySet()
                .stream()
                .map(e -> new Prediction(e.getKey(), e.getValue(), domainSize))
                .collect(Collectors.toCollection(TreeSet::new))
                .descendingSet();
        }

        @Override
        public String toString() {
            return predictions.stream()
                .map(Prediction::toString)
                .collect(Collectors.joining(";"));
        }
    }

    private final Map<String, IndexItem> items = new HashMap<>();

    public Index(Map<String, Map<String, Double>> input) {
        input.forEach((key, value) -> items.put(key, new IndexItem(value)));
    }

    public Optional<IndexItem> predictNextWord(String sequence) {
        return Optional.ofNullable(items.getOrDefault(sequence, null));
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");

        items.forEach((key, value) -> joiner.add(key + " -> " + value.toString()));

        return joiner.toString();
    }
}
