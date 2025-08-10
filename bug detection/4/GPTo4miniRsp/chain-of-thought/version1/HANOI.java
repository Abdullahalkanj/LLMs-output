import java.util.*;

public class HANOI {

    public static List<Pair<Integer,Integer>> hanoi(int height, int start, int end) {
        List<Pair<Integer,Integer>> steps = new ArrayList<>();
        if (height > 0) {
            int helper = 6 - start - end;  // simpler helper peg
            steps.addAll(hanoi(height-1, start, helper));
            steps.add(new Pair<>(start, end));            // fixed target
            steps.addAll(hanoi(height-1, helper, end));
        }
        return steps;
    }



    public static class Pair<F, S> {
        private F first; 
        private S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        public void setFirst(F first) {
            this.first = first;
        }

        public void setSecond(S second) {
            this.second = second;
        }

        public F getFirst() {
            return first;
        }

        public S getSecond() {
            return second;
        }

        @Override
        public String toString() {
            return "(" + String.valueOf(first) + ", " + String.valueOf(second) + ")";
        }
    }
}
