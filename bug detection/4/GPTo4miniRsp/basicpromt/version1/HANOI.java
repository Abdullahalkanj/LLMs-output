import java.util.*;

public class HANOI {
    
    public static List<Pair<Integer,Integer>> hanoi(int height, int start, int end) {
        ArrayList<Pair<Integer,Integer>> steps = new ArrayList<>();

        if (height > 0) {
            
            PriorityQueue<Integer> pegs = new PriorityQueue<>();
            pegs.add(1);
            pegs.add(2);
            pegs.add(3);
            pegs.remove(start);
            pegs.remove(end);
            int helper = pegs.poll();

            
            steps.addAll(hanoi(height - 1, start, helper));
            
            steps.add(new Pair<>(start, end));
            
            steps.addAll(hanoi(height - 1, helper, end));
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
        public F getFirst()  { return first; }
        public S getSecond() { return second; }
        @Override
        public String toString() {
            return "(" + first + " → " + second + ")";
        }
    }

    public static void main(String[] args) {
        List<Pair<Integer,Integer>> moves = hanoi(3, 1, 3);
        for (Pair<Integer,Integer> m : moves) {
            System.out.println(m);
        }
    }
}
