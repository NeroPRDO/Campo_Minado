/**
 *
 * @author Pedro Eduardo Dall Agnol GRR - 20240844
 */

package ds131.pedro.campominado;

import java.util.Random;

public class CampoMinado {
    public final Cell[][] grid;
    private final int rows, cols, totalMinas;

    public CampoMinado(Nivel nivel) {
        this.cols = nivel.cols;
        this.rows = nivel.rows;
        this.totalMinas = nivel.minas;
        grid = new Cell[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = new Cell();
        sortearMinas();
        calcularVizinhas();
    }

    private void sortearMinas() {
        Random rnd = new Random();
        int colocadas = 0;
        while (colocadas < totalMinas) {
            int r = rnd.nextInt(rows), c = rnd.nextInt(cols);
            if (!grid[r][c].temMina) {
                grid[r][c].temMina = true;
                colocadas++;
            }
        }
    }

    private void calcularVizinhas() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c].temMina) continue;
                int count = 0;
                for (int dr = -1; dr <= 1; dr++) for (int dc = -1; dc <= 1; dc++) {
                    int nr = r + dr, nc = c + dc;
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                        && grid[nr][nc].temMina) {
                        count++;
                    }
                }
                grid[r][c].vizinhas = count;
            }
        }
    }
}
