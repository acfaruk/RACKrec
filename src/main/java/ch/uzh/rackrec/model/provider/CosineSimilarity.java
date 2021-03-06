package ch.uzh.rackrec.model.provider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CosineSimilarity {
    private CosineSimilarity() {

    }

    protected static double compute(HashMap<String, Double> vector1, HashMap<String, Double> vector2) {
        Set<String> sharedKeys = new HashSet<>(vector1.keySet());
        Set<String> keysVector1 = vector1.keySet();
        Set<String> keysVector2 = vector2.keySet();

        sharedKeys.retainAll(vector2.keySet());
        double sumproduct = 0;
        double a = 0;
        double b = 0;

        sumproduct = sharedKeys.stream()
                .mapToDouble(key -> vector1.get(key) * vector2.get(key))
                .sum();

        a = keysVector1.stream()
                .mapToDouble(key -> vector1.get(key) * vector1.get(key))
                .sum();

        b = keysVector2.stream()
                .mapToDouble(key -> vector2.get(key) * vector2.get(key))
                .sum();

        return sumproduct / Math.sqrt(a * b);
    }
}
