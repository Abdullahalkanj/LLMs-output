import java.util.*;

public class HANOI {
    /**
     * Returns a list of moves to solve the Tower of Hanoi:
     * each move is a Pair<fromPeg, toPeg>.
     */
    public static List<Pair<Integer,Integer>> hanoi(int height, int start, int end) {
        List<Pair<Integer,Integer>> steps = new ArrayList<>();

        if (height > 0) {
            // Compute the helper peg directly:
            int helper = 6 - start - end;


            steps.addAll(hanoi(height - 1, start, helper));

            steps.add(new Pair<>(start, end)); 

            steps.addAll(hanoi(height - 1, helper, end));
        }

        return steps;
    }

    // Simple generic Pair class
    public static class Pair<F, S> {
        private F first;
        private S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        public F getFirst() { return first; }
        public S getSecond() { return second; }
        public void setFirst(F first) { this.first = first; }
        public void setSecond(S second) { this.second = second; }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
    }

    // Optional: simple test harness
    public static void main(String[] args) {
        List<Pair<Integer,Integer>> moves = hanoi(3, 1, 3);
        moves.forEach(System.out::println);
    }
}
