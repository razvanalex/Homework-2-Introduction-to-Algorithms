import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Revedges {

	public static void floyd(int[][] mat, int n, int p, BufferedWriter bw, int[] query1, int[] query2) throws IOException {
	
		int dist[][] = new int[n+1][n+1];
		int i, j, k;
		
		for (i = 1; i <= n; i++)
			for (j = 1; j <= n; j++)
				dist[i][j] = mat[i][j];
		
		for (k = 1; k <= n; k++)
        {
            for (i = 1; i <= n; i++)
            {
                
                for (j = 1; j <= n; j++)
                {
                    
                    if (dist[i][k] + dist[k][j] < dist[i][j])
                        dist[i][j] = dist[i][k] + dist[k][j];
                }
            }
        }
		
		for(i = 1; i <= n; i++) {
			dist[i][i] = 0;
		}
		
		for(i=1; i<=p; i++) {
			
			bw.write(String.valueOf(dist[query1[i]][query2[i]]));
			if(i < p)
				bw.write("\n");
		}
		
		
		bw.close();
	
	}
	

	public static void main(String[] args) throws IOException {

			int n = 0, m = 0, p = 0;
		
		int[] query1 = new int[100000];
		int[] query2 = new int[100000];
		
		File in = new File("revedges.in");
		
		File fout = new File("revedges.out");
		
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		
		int[][] mat = new int[1000][1000]; // matrice de adiacenta

		try { // citire din fisier si completare matrice ca in laborator

			Scanner sc = new Scanner(in);

			n = sc.nextInt();
			m = sc.nextInt();
			p = sc.nextInt();

			
			for (int i = 1; i <= n; i++)
				for (int j = 1; j <= n; j++)
					mat[i][j] = 100000;
				
			for (int i = 1; i <= m; i++) {

				int x, y;
				x = sc.nextInt();
				y = sc.nextInt();
				mat[x][y] = 0;
				mat[y][x] = 1;

			}
			
			
			for(int i = 1; i <= p; i++)
			{
				query1[i] = sc.nextInt();
				query2[i] = sc.nextInt();
			}

			sc.close();

		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
		}
		
		
		floyd(mat, n, p, bw, query1, query2);	// apel functie
		
	}
}

