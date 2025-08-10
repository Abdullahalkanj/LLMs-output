import java.util.*;

public class HANOI {
    /**
     * Returns the list of moves to solve Towers of Hanoi.
     *
     * @param height number of disks
     * @param start  peg number to move from (1–3)
     * @param end    peg number to move to   (1–3)
     * @return       sequence of (from, to) moves
     */
    public static List<Pair<Integer,Integer>> hanoi(int height, int start, int end) {
        List<Pair<Integer,Integer>> steps = new ArrayList<>();

        if (height > 0) {
            // Find the helper peg in a simpler way:
            int helper = 6 - start - end;  // because 1+2+3 = 6

            // 1) Move top (height-1) disks start → helper
            steps.addAll(hanoi(height - 1, start, helper));

            // 2) Move the bottom disk start → end
            steps.add(new Pair<>(start, end));

            // 3) Move (height-1) disks helper → end
            steps.addAll(hanoi(height - 1, helper, end));
        }

        return steps;
    }


    public static class Pair<F, S> {
        private F first; //first member of pair
        private S second; //second member of pair

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
