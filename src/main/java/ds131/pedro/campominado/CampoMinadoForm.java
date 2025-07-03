package ds131.pedro.campominado;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Arrays;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class CampoMinadoForm extends javax.swing.JFrame {
    // --- campos de lógica e recursos ---
    private Nivel nivel;
    private CampoMinado campo;
    private JButton[][] btnGrid;
    private ImageIcon tileIcon;
    private ImageIcon mineIcon;
    private ImageIcon flagIcon;
    private final ImageIcon smileIcon;
    private final ImageIcon sadIcon;
    private int btnSize;
    private ImageIcon blankIcon;

    public CampoMinadoForm() {
        initComponents();  // chama o método GERADO

        // carrega e escala ícones para 32×32px
        // tileIcon  = loadIcon("/images/tile.png",  32,32);
        // mineIcon  = loadIcon("/images/mine.png",  32,32);
        // flagIcon  = loadIcon("/images/flag.png",  32,32);
        smileIcon = loadIcon("/images/smile.png", 32,32);
        sadIcon   = loadIcon("/images/sad.png",   32,32);

        // configura menu de níveis
        menuFacil.addActionListener(e -> changeLevel(Nivel.FACIL));
        menuMedio.addActionListener(e -> changeLevel(Nivel.MEDIO));
        menuAvancado.addActionListener(e -> changeLevel(Nivel.AVANCADO));

        // botão face reinicia o jogo
        btnFace.addActionListener(e -> resetGame());

        // layout do statusPanel em 1×3
        statusPanel.setLayout(new GridLayout(1,3,10,5));
        lblMinesLeft.setPreferredSize(new Dimension(50,30));
        btnFace.setPreferredSize(new Dimension(32,32));
        lblStatus.setPreferredSize(new Dimension(150,30));

        // inicia no nível Fácil
        changeLevel(Nivel.FACIL);
    }

    /** Carrega uma imagem do classpath e escala */
    private ImageIcon loadIcon(String path, int w, int h) {
        URL loc = getClass().getResource(path);
        ImageIcon orig = new ImageIcon(loc);
        Image img = orig.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void changeLevel(Nivel n) {
        this.nivel = n;
        resetGame();
    }

   private void resetGame() {
    campo = new CampoMinado(nivel);

    // 1) decide tamanho do botão
    btnSize = (nivel == Nivel.AVANCADO) ? 20 : 32;

    // 2) escala ícones conforme btnSize
    tileIcon = loadIcon("/images/tile.png", btnSize, btnSize);
    mineIcon = loadIcon("/images/mine.png", btnSize, btnSize);
    flagIcon = loadIcon("/images/flag.png", btnSize, btnSize);
    // gera um ícone transparente para o zero
    Image blankImg = new BufferedImage(btnSize, btnSize, BufferedImage.TYPE_INT_ARGB);
    blankIcon = new ImageIcon(blankImg);

    // 3) refaz o painel
    boardPanel.removeAll();
    boardPanel.setLayout(new GridLayout(nivel.rows, nivel.cols, 1, 1));
    btnGrid = new JButton[nivel.rows][nivel.cols];

    for (int r = 0; r < nivel.rows; r++) {
        for (int c = 0; c < nivel.cols; c++) {
            JButton b = new JButton(tileIcon);
            b.setPreferredSize(new Dimension(btnSize, btnSize));
            b.setOpaque(true);
            b.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));
            b.setBackground(new Color(240,240,240));
            final int rr = r, cc = c;
            b.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) toggleFlag(rr, cc);
                    else if (SwingUtilities.isLeftMouseButton(e)) handleOpen(rr, cc);
                }
            });
            boardPanel.add(b);
            btnGrid[r][c] = b;
        }
    }

    updateMinesLeft();
    btnFace.setIcon(smileIcon);

    pack();
    setLocationRelativeTo(null);
}

    private void toggleFlag(int r, int c) {
        Cell cell = campo.grid[r][c];
        if (cell.aberto) return;
        cell.marcado = !cell.marcado;
        btnGrid[r][c].setIcon(cell.marcado ? flagIcon : tileIcon);
        updateMinesLeft();
        checkWin();
    }

    private void handleOpen(int r, int c) {
    Cell cell = campo.grid[r][c];
    if (cell.marcado || cell.aberto) return;
    cell.aberto = true;
    JButton b = btnGrid[r][c];

    if (cell.temMina) {
        b.setIcon(mineIcon);
        gameOver(false);
        return;
    }

    int v = cell.vizinhas;
    if (v > 0) {
        ImageIcon numIcon = loadIcon("/images/numbers/" + v + ".png", btnSize, btnSize);
        b.setIcon(numIcon);
    } else {
        // revelação de zero sem encolher o botão
        b.setIcon(blankIcon);
        floodFill(r, c);
    }

    checkWin();
}

private void floodFill(int r, int c) {
    for (int dr = -1; dr <= 1; dr++) {
        for (int dc = -1; dc <= 1; dc++) {
            int nr = r + dr, nc = c + dc;
            if (nr < 0 || nr >= nivel.rows || nc < 0 || nc >= nivel.cols) continue;
            Cell neighbor = campo.grid[nr][nc];
            if (!neighbor.aberto && !neighbor.temMina) {
                handleOpen(nr, nc);
            }
        }
    }
}

    private void updateMinesLeft() {
        long flags = Arrays.stream(campo.grid)
            .flatMap(Arrays::stream)
            .filter(c -> c.marcado)
            .count();
        lblMinesLeft.setText(String.format("%03d", nivel.minas - flags));
    }

    private void checkWin() {
        boolean win = Arrays.stream(campo.grid)
            .flatMap(Arrays::stream)
            .allMatch(c -> c.temMina ? c.marcado : c.aberto);
        if (win) gameOver(true);
    }

    private void gameOver(boolean win) {
        btnFace.setIcon(win ? smileIcon : sadIcon);
        for (int r = 0; r < nivel.rows; r++) {
            for (int c = 0; c < nivel.cols; c++) {
                JButton b = btnGrid[r][c];
                if (!win && campo.grid[r][c].temMina) {
                    b.setIcon(mineIcon);
                }
                b.setEnabled(false);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        lblMinesLeft = new javax.swing.JLabel();
        btnFace = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        boardPanel = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        menuJogo = new javax.swing.JMenu();
        menuFacil = new javax.swing.JMenuItem();
        menuMedio = new javax.swing.JMenuItem();
        menuAvancado = new javax.swing.JMenuItem();

        jButton1.setText("jButton1");

        jButton7.setText("jButton7");

        jButton2.setText("jButton2");

        jButton3.setText("jButton3");

        jButton4.setText("jButton4");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("jButton5");

        jButton6.setText("jButton6");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 0, 153));

        statusPanel.setBackground(new java.awt.Color(102, 102, 102));
        statusPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        statusPanel.setLayout(new java.awt.GridLayout(1, 3, 75, 5));

        lblMinesLeft.setBackground(new java.awt.Color(51, 51, 51));
        lblMinesLeft.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        lblMinesLeft.setForeground(new java.awt.Color(204, 0, 0));
        lblMinesLeft.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblMinesLeft.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        statusPanel.add(lblMinesLeft);

        btnFace.setBackground(new java.awt.Color(204, 204, 204));
        btnFace.setMaximumSize(new java.awt.Dimension(7, 7));
        btnFace.setMinimumSize(new java.awt.Dimension(7, 7));
        btnFace.setPreferredSize(new java.awt.Dimension(7, 7));
        statusPanel.add(btnFace);

        lblStatus.setBackground(new java.awt.Color(51, 51, 51));
        lblStatus.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(204, 0, 0));
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        statusPanel.add(lblStatus);

        boardPanel.setBackground(new java.awt.Color(51, 51, 51));
        boardPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout boardPanelLayout = new javax.swing.GroupLayout(boardPanel);
        boardPanel.setLayout(boardPanelLayout);
        boardPanelLayout.setHorizontalGroup(
            boardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 456, Short.MAX_VALUE)
        );
        boardPanelLayout.setVerticalGroup(
            boardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 212, Short.MAX_VALUE)
        );

        menuBar.setBackground(new java.awt.Color(204, 51, 255));
        menuBar.setForeground(new java.awt.Color(51, 51, 51));

        menuJogo.setText("Jogo");

        menuFacil.setText("Fácil");
        menuJogo.add(menuFacil);

        menuMedio.setText("Médio");
        menuJogo.add(menuMedio);

        menuAvancado.setText("Avançado");
        menuJogo.add(menuAvancado);

        menuBar.add(menuJogo);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(boardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(boardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CampoMinadoForm().setVisible(true);
        });
    }




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel boardPanel;
    private javax.swing.JButton btnFace;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel lblMinesLeft;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JMenuItem menuAvancado;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuFacil;
    private javax.swing.JMenu menuJogo;
    private javax.swing.JMenuItem menuMedio;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
}
