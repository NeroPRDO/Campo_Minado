package ds131.pedro.campominado;

public enum Nivel {
    FACIL   (9,  9,  10),
    MEDIO   (16, 16, 40),
    AVANCADO(30, 16, 99);

    public final int cols, rows, minas;
    Nivel(int cols, int rows, int minas) {
        this.cols = cols;
        this.rows = rows;
        this.minas = minas;
    }
}
