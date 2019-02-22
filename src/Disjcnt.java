import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.*;

public class Disjcnt {

	private static class Edge {
		int x;
		int y;
		boolean removed;

		Edge(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Edge) {
				return ((x == ((Edge) o).x) && (y == ((Edge) o).y))
					|| ((x == ((Edge) o).y) && (y == ((Edge) o).x));
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


	static class Task {
		public static final String INPUT_FILE = "disjcnt.in";
		public static final String OUTPUT_FILE = "disjcnt.out";

		int n;
		int m;
		ArrayList<Edge> adj[];

		@SuppressWarnings("unchecked")
		private void readInput() {
			try {
				Scanner sc = new Scanner(new BufferedReader(new FileReader(
										INPUT_FILE)));
				n = sc.nextInt();
				m = sc.nextInt();
				adj = new ArrayList[n + 1];

				for (int i = 1; i <= n; i++)
					adj[i] = new ArrayList<>();
				for (int i = 1; i <= m; i++) {
					int x, y;
					x = sc.nextInt();
					y = sc.nextInt();
					adj[x].add(new Edge(x, y));
					adj[y].add(new Edge(y, x));
				}
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(long result) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
								OUTPUT_FILE)));
				pw.println(result);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		long[] low;
		long[] lvl;

		private void DFS(Integer u) {
			low[u] = lvl[u];
			HashMap<Edge, Long> sons = new HashMap<>();
			for (Edge edge : adj[u]) {
				Integer v = edge.y;
				Long crtSons = sons.get(new Edge(u, v));
				if (crtSons == null) {
					sons.put(new Edge(u, v), 0L);
				}
			}

			for (Edge edge : adj[u]) {
				Integer v = edge.y;
				Long crtSons = sons.get(new Edge(u, v));
				sons.put(new Edge(u, v), crtSons + 1L);

				if(lvl[v] == 0) {
					lvl[v] = lvl[u] + 1;
					DFS(v);
					if (low[v] >= lvl[v]) {
						edge.removed = true;
						adj[v].get(adj[v].indexOf(new Edge(u, v))).removed = true;
					}
					low[u] = Math.min(low[u], low[v]);
				} else if (lvl[v] < lvl[u] - 1) {
					low[u] = Math.min(low[u], lvl[v]);
				}
			}
		}

		private void removeBridges() {
			low = new long[n + 1];
			lvl = new long[n + 1];

			for (int u = 1; u <= n; u++) {
				lvl[u] = 1;
				DFS(u);
			}
		}

		public void dfs(ArrayList<Edge> adj[], int v, int[] color, Stack<Integer> S) {
			color[v] = 1;
			for (Edge edge : adj[v]) {
				Integer u = edge.y;
				if (color[u] == 0 && !edge.removed)
					dfs(adj, u, color, S);
			}
			S.push(v);
			color[v] = 2;
		}

		public void dfsT(ArrayList<Edge> adjt[], int v, int[] color, ArrayList<Integer> T) {
			color[v] = 1;
			for (Edge edge : adj[v]) {
				Integer u = edge.y;
				if (color[u] == 0 && !edge.removed)
					dfsT(adjt, u, color, T);
			}
			T.add(v);
			color[v] = 2;
		}

		public void ctc(ArrayList<ArrayList<Integer>> result) {
				Stack<Integer> S = new Stack<>();
				int[] color = new int[n + 1];

				for (int v = 1; v <= n; v++) {
					if (color[v] == 0) {
						dfs(adj, v, color, S);
					}
				}

				color = new int[n + 1];
				while (!S.isEmpty()) {
					ArrayList<Integer> t = new ArrayList<>();
					Integer v = S.pop();
					if (color[v] == 0) {
						dfsT(adj, v, color, t);
						result.add(t);
						adj[v] = null;
					}
				}
		}

		@SuppressWarnings("unchecked")
		private long getResult() {
			long counter = 0;

			removeBridges();
			ArrayList<ArrayList<Integer>> ctcComps = new ArrayList<>();
			ctc(ctcComps);

			for (ArrayList<Integer> com : ctcComps) {
				long size = com.size();
				counter += (size * (size - 1)) / 2;
			}

			return counter;
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
