package caro;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Setter
@Getter
public class CaroView extends JFrame {
    private CaroController caroController;
    private JButton[][] buttons;
    private int sizeButton = 40;
    private int[] preBotMove = {0, 0};
    private boolean isEndGame = false;
    private boolean isWaiting = false;
    private static CaroView caroView = null;
    private JLabel labelNotification = null;
    private CaroView() {
        caroController = CaroController.getInstance();
        int sizeMatrix = ( caroController).getSizeMatrix();

        this.setTitle("Caro");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        JPanel mainView = new JPanel();
        mainView.setLayout(new BorderLayout());
        buttons = new JButton[sizeMatrix + 1][sizeMatrix + 1];
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(sizeMatrix, sizeMatrix));
        panelButtons.setSize(sizeMatrix * sizeButton, sizeMatrix * sizeButton);
        for(int i = 1; i <= sizeMatrix; i++) {
            for(int j = 1; j <= sizeMatrix; j++) {
                JButton button = new JButton();
                Font font = new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, sizeButton - 10);
                button.setFont(font);
                button.setBackground(Color.WHITE);
                button.setMargin(new Insets(0, 0, 0, 0));
                button.setPreferredSize(new Dimension(sizeButton, sizeButton));
                int finalI = i;
                int finalJ = j;
                button.addActionListener(e -> {
                    if(buttons[finalI][finalJ].getText().length() == 0) {
                        if(!isEndGame) {
                            caroController.playerMove(finalI, finalJ);
                        }
                    }
                });
                panelButtons.add(button);
                buttons[i][j] = button;
            }
        }

        JPanel panelImage = new JPanel();
        panelImage.setLayout(new BorderLayout());
        ImageIcon image = new ImageIcon("./assets/caro.png");
        Image originalImage = image.getImage();
        int newWidth = (int) (originalImage.getWidth(null) * .5);
        int newHeight = (int) (originalImage.getHeight(null) * .5);
        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JPanel panelButtonStartGame = new JPanel();
        panelButtonStartGame.setLayout(new FlowLayout());
        JButton buttonStartGame = new JButton("Play");
        buttonStartGame.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        buttonStartGame.setBackground(Color.WHITE);
        buttonStartGame.setMargin(new Insets(0, 0, 0, 0));
        buttonStartGame.setPreferredSize(new Dimension(150, 50));
        buttonStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isEndGame = false;
                labelNotification.setText("Game in progress ...");
                caroController.restartGame();
                for(int i = 1; i <= sizeMatrix; i++) {
                    for(int j = 1; j <= sizeMatrix; j++) {
                        buttons[i][j].setText("");
                    }
                }
            }
        });
        panelButtonStartGame.add(buttonStartGame);

        panelImage.add(new JLabel(resizedIcon), BorderLayout.NORTH);
        panelImage.add(panelButtonStartGame);

        JPanel panelNotification = new JPanel();
        labelNotification = new JLabel();
        labelNotification.setFont(new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 25));
        labelNotification.setText("Game in progress ...");
        panelNotification.add(labelNotification);

        mainView.add(panelButtons, BorderLayout.CENTER);
        mainView.add(panelImage, BorderLayout.EAST);
        mainView.add(panelNotification, BorderLayout.SOUTH);

        this.add(mainView);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    public static CaroView getInstance() {
        if(caroView == null) {
            caroView = new CaroView();
        }
        return caroView;
    }
    public void playerMove(int x, int y) {
        buttons[x][y].setText("O");
        buttons[x][y].setForeground(new Color(0, 70, 140));
    }
    public void botMove(int x, int y) {
        buttons[x][y].setText("X");
        buttons[x][y].setForeground(new Color(200, 0, 0));
        preBotMove = new int[]{x, y};
    }
    public void endGame(String endGame) {
        isEndGame = true;
        labelNotification.setText(endGame);
    }
}
