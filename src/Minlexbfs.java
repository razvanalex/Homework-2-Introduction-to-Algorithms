import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.*;

public class Minlexbfs {
	static class Task {
		public static final String INPUT_FILE = "minlexbfs.in";
		public static final String OUTPUT_FILE = "minlexbfs.out";
		public static final int NMAX = 100005; // 10^5
		public enum Color { ALB, GRI, NEGRU }
		int n;
		int m;

		@SuppressWarnings("unchecked")
		ArrayList<Integer> adj[] = new ArrayList[NMAX];

		private void readInput() {
			try {
				Scanner sc = new Scanner(new BufferedReader(new FileReader(
								INPUT_FILE)));
				n = sc.nextInt();
				m = sc.nextInt();

				for (int i = 1; i <= n; i++)
					adj[i] = new ArrayList<>();
				for (int i = 1; i <= m; i++) {
					int x, y;
					x = sc.nextInt();
					y = sc.nextInt();
					adj[x].add(y);
					adj[y].add(x);
				}
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(int[] result) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
								OUTPUT_FILE)));
				for (int i = 1; i <= n; i++) {
					pw.printf("%d%c", result[i], i == n ? '\n' : ' ');
				}
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}


		private ArrayList<Integer> BFS(int start, ArrayList<Integer>adj[]) {
			ArrayList<Integer> bfs = new ArrayList<>();

			Queue<Integer> queue = new LinkedList<>();
			boolean[] visited = new boolean[n + 1];

			// Add start node in queue and mark it as being visited
			queue.add(start);
			visited[start] = true;

			while (!queue.isEmpty()) {
				// Get the top of the queue
				Integer u = queue.peek();

				for (Integer v : adj[u]) {
					if (!visited[v]) {
						// Add neighbor to queue
						queue.add(v);
						visited[v] = true;
					}
				}

				// Add node to list (here stdout)
				bfs.add(u);

				// Remove the top of the queue
				queue.poll();
			}

			return bfs;
		}


		private int[] getResult() {
			for (int i = 1; i <= n; i++) {
				Collections.sort(adj[i]);
			}

			ArrayList<Integer> bfs = BFS(1, adj);

			int[] d = new int[n + 1];
			int i = 1;
			for (Integer u : bfs) {
				d[i++] = u;
			}

			return d;
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
