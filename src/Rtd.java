import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.*;

public class Rtd {
    private static class Position {
    	int x;
    	int y;

    	Position(int x, int y) {
    		this.x = x;
    		this.y = y;
    	}

        @Override
        public boolean equals(Object o) {
            if (o instanceof Position) {
                Position pos = (Position) o;
                return x == pos.x && y == pos.y;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + x;
            result = 31 * result + y;
            return result;
       }
    }

    private static class DieState {
    	int down;
    	int forward;
        int right;

    	DieState(int down, int forward, int right) {
    		this.down = down;
    		this.forward = forward;
            this.right = right;
    	}
    }

    private static class Pair<F, S> {
    	F f;
    	S s;

    	Pair(F f, S s) {
    		this.f = f;
    		this.s = s;
    	}
    }

    private static class DijkstraState {
        Position pos;
        DieState state;
        Integer cost;

        DijkstraState(Position _pos, DieState _state, Integer _cost) {
            pos = _pos;
            state = _state;
            cost =  _cost;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof DijkstraState) {
                DijkstraState ds = (DijkstraState) o;
                return pos == ds.pos && state == ds.state && cost == ds.cost;
            }
            return false;
        }
    }

	static class Task {
		public static final String INPUT_FILE = "rtd.in";
		public static final String OUTPUT_FILE = "rtd.out";

		int n, m, k;
        Position start, end;
        int[][] map;
        int[] die;

		private void readInput() {
			try {
				Scanner sc = new Scanner(new BufferedReader(new FileReader(
										INPUT_FILE)));
				n = sc.nextInt();
				m = sc.nextInt();

                map = new int[n + 1][m + 1];

                start = new Position(sc.nextInt(), sc.nextInt());
                end = new Position(sc.nextInt(), sc.nextInt());
                k = sc.nextInt();
                die = new int[7];

                for (int i = 1; i <= 6; i++) {
                    die[i] = sc.nextInt();
                }

                for (int i = 1; i <= k; i++) {
                    map[sc.nextInt()][sc.nextInt()] = 1;
                }

				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(int result) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
								OUTPUT_FILE)));
				pw.println(result);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

        private ArrayList<Pair<Position, DieState>> getNeighbors(Position p, DieState state, int map[][]) {
            ArrayList<Pair<Position, DieState>> result = new ArrayList<>(4);
            int x = p.x;
            int y = p.y;

            if (x != 1 && map[x - 1][y] != 1) {
                Position newPos = new Position(x - 1, y);
                DieState newSt = new DieState(7 - state.forward, 7 - state.down, state.right);
                result.add(new Pair<>(newPos, newSt));
            }

            if (x != n && map[x + 1][y] != 1) {
                Position newPos = new Position(x + 1, y);
                DieState newSt = new DieState(state.forward, 7 - state.down, state.right);
                result.add(new Pair<>(newPos, newSt));
            }

            if (y != 1 && map[x][y - 1] != 1) {
                Position newPos = new Position(x, y - 1);
                DieState newSt = new DieState(7 - state.right, state.forward, state.down);
                result.add(new Pair<>(newPos, newSt));
            }

            if (y != m && map[x][y + 1] != 1) {
                Position newPos = new Position(x, y + 1);
                DieState newSt = new DieState(state.right, state.forward, 7 - state.down);
                result.add(new Pair<>(newPos, newSt));
            }

            return result;
        }

        private int encodeDie(int down, int forward) {
            return (down - 1) * 4 + (forward - 1) % 4;
        }

        private int encodeDie(DieState state) {
            return encodeDie(state.down, state.forward);
        }

        private int[][][] Dijkstra(Position start, Position end, int[][] map) {
            Queue<DijkstraState> queue = new PriorityQueue<>(new Comparator<DijkstraState>() {
                @Override
                public int compare(DijkstraState s1, DijkstraState s2) {
                   return s1.cost - s2.cost;
                }
            });

            int[][][] sum = new int[n + 1][m + 1][24];

            // die_code = down * 4 + forward % 4
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= m; j++) {
                    for (int k = 0; k < 24; k++) {
                        sum[i][j][k] = Integer.MAX_VALUE;
                    }
                }
            }

            DieState state = new DieState(1, 5, 3);
            sum[start.x][start.y][encodeDie(state)] = die[state.down];
            queue.add(new DijkstraState(start, state, die[state.down]));

            while (!queue.isEmpty()) {
                DijkstraState crtState = queue.poll();
                Position crtPos = crtState.pos;
                int crtDieCode = encodeDie(crtState.state);

                for (Pair<Position, DieState> next : getNeighbors(crtPos, crtState.state, map)) {
                    Position nextPos = next.f;
                    DieState nextState = next.s;
                    int code = encodeDie(nextState);
                    int x = nextPos.x;
                    int y = nextPos.y;

    				if (sum[x][y][code] > sum[crtPos.x][crtPos.y][crtDieCode] + die[nextState.down]) {
                        queue.remove(new DijkstraState(nextPos, nextState, sum[x][y][code]));
                        sum[x][y][code] = sum[crtPos.x][crtPos.y][crtDieCode] + die[nextState.down];
                        queue.add(new DijkstraState(nextPos, nextState, sum[x][y][code]));
    				}
    			}
    		}

            return sum;
        }

        private int getResult() {
            int[][][] sum = Dijkstra(start, end, map);
            int min = Integer.MAX_VALUE;

            for (int i = 0; i < 24; i++) {
                if (min > sum[end.x][end.y][i]) {
                    min = sum[end.x][end.y][i];
                }
            }

            // for (int i = 1; i <= n; i++) {
            //     for (int j = 1; j<= m; j++) {
            //         int minimum = Integer.MAX_VALUE;
            //         for (int k = 0; k < 24 ;k++) {
            //             if (minimum > sum[i][j][k]) {
            //                 minimum = sum[i][j][k];
            //             }
            //         }
            //         System.out.print(minimum + " ");
            //     }
            //     System.out.println();
            // }

            System.out.println("Min = " + min);
            return min;
        }

		public void solve() {
			readInput();
			writeOutput(getResult());
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
