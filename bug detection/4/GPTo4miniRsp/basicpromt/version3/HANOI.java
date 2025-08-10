import java.util.*;

public class HANOI {
    public static List<Pair<Integer,Integer>> hanoi(int height, int start, int end) {
        List<Pair<Integer,Integer>> steps = new ArrayList<>();

        if (height > 0) {
            int helper = 6 - start - end;

            steps.addAll(hanoi(height - 1, start, helper));

            steps.add(new Pair<>(start, end));

            steps.addAll(hanoi(height - 1, helper, end));
        }

        return steps;
    }

    public static class Pair<F, S> {
        private final F first;
        private final S second;
        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }
        public F getFirst() { return first; }
        public S getSecond() { return second; }
        @Override public String toString() {
            return "(" + first + " → " + second + ")";
        }
    }

    public static void main(String[] args) {
        int n = 3;
        List<Pair<Integer,Integer>> moves = hanoi(n, 1, 3);
        for (int i = 0; i < moves.size(); i++) {
            System.out.printf("Move %2d: %s%n", i+1, moves.get(i));
        }
    }
}
