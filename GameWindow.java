import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


/**
 * The GameWindow class is the main class of the application that creates the GUIs as well as has
 * all the event handlers for the components of the GUI. Moreover, the class also implements MouseListener
 * class that handles the events when the user plays the game by clicking the integer buttons.
 *
 * @author Kenil Shah (201957701)
 */
public class GameWindow extends JFrame implements ActionListener, MouseListener {

    // The components of the GUI that needed to be used inside the class.
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JLabel instructionLabel;
    private JButton topButton;
    private GridSquare[][] gridSquares;
    private int rows;
    private int columns;
    private int playerID;
    private int treasure_x;
    private int treasure_y;


    public GameWindow(int rows, int columns) {
        Random rand = new Random();
        this.treasure_x = rand.nextInt(5);
        this.treasure_y = rand.nextInt(5);
        this.rows = rows;
        this.columns = columns;
        this.setSize(600, 600);
        this.createMenuBar();
        this.topPanel = new JPanel();
        this.bottomPanel = new JPanel();
        this.topPanel.setLayout(new FlowLayout());
        this.bottomPanel.setLayout(new GridLayout(rows, columns));
        this.bottomPanel.setSize(800, 500);
        this.instructionLabel = new JLabel("Find the treasure. Click 'New game' to begin.");
        this.topButton = new JButton("New Game");
        this.topButton.addActionListener(this);
        this.topPanel.add(this.topButton);
        this.topPanel.add(this.instructionLabel);
        this.gridSquares = new GridSquare[rows][columns];

        for(int x = 0; x < rows; x++) {
            for(int y = 0; y < columns; y++) {
                this.gridSquares[x][y] = new GridSquare(x, y);
                this.gridSquares[x][y].setSize(20, 20);
                this.gridSquares[x][y].setEnabled(false);
                this.bottomPanel.add(this.gridSquares[x][y]);
            }
        }

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(this.topPanel, "North");
        this.getContentPane().add(this.bottomPanel, "Center");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    /**
     * Make the menu bar and its associated menus, also add the menu items and register their action listeners.
     */
    public void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu file_menu = new JMenu("File");
        menuBar.add(file_menu);
        JMenuItem reset = new JMenuItem("Reset");
        file_menu.add(reset);
        reset.addActionListener((var1x) -> {
            this.newGame();
        });
        JMenuItem exit = new JMenuItem("Exit");
        file_menu.add(exit);
        exit.addActionListener((var1x) -> {
            this.exit();
        });

        JMenu aboutMenu = new JMenu("About");
        menuBar.add(aboutMenu);

        JMenuItem versionInfo = new JMenuItem("Version Info");
        aboutMenu.add(versionInfo);
        versionInfo.addActionListener(e -> showVersionInfo());
    }

    /**
     * Reset the text of all the buttons when the user clicks the New Game Button.
     * Also note that the game is reset back to the new game upon the clicking of New Game by the user.
     * The System also randomly assigns turn to player 1 or 2 to start the game.
     */
    private void newGame() {
        Random rand = new Random();
        this.setPlayerID( rand.nextInt(2) + 1);
        this.instructionLabel.setText("Player " + this.getPlayerID() + "'s turn...");

        for(int row = 0; row < this.gridSquares.length; row++) {
            for(int column = 0; column < this.gridSquares.length; column++) {
                this.gridSquares[row][column].setText("");
                this.gridSquares[row][column].setFont(new Font("Tahoma", Font.BOLD, 20));
                this.gridSquares[row][column].setEnabled(true);
                this.gridSquares[row][column].setContentAreaFilled(false);
                this.gridSquares[row][column].resetColor();
                this.gridSquares[row][column].addMouseListener(this);
                this.gridSquares[row][column].setXcoord(row);
                this.gridSquares[row][column].setYcoord(column);
            }
        }
        System.out.println(treasure_x + ", " + treasure_y);
    }

    /**
     * Exit the application with an Exit status 0 when the user clicks the Exit option in the file menu.
     */
    private void exit() {
        System.exit(0);
    }

    /**
     * Mutator for changing the player after each turn.
     * @param id is the player number.
     */
    public void setPlayerID(int id) {
        this.playerID = id;
    }

    /**
     * Accessor for the player ID that has the current turn.
     * @return the player number.
     */
    public int getPlayerID() {
        return this.playerID;
    }

    /**
     * Show the version information of the application when the user clicks the version info option from the
     * about menu of the application.
     */
    private void showVersionInfo() {
        JOptionPane.showMessageDialog(topPanel, "Version : 1.0\n" + "Author   : Kenil V Shah",
                "Hidden Treasure", JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * Implementing the actionPerformed events for our buttons panel.
     *
     */
    public void actionPerformed(ActionEvent var1) {
        Object var2 = var1.getSource();
        if (var2.equals(this.topButton)) {
            this.newGame();
        }

    }

    /**
     * Implementing the mouseClicked events for our buttons panel.
     * Note that upon initial launch of the game, the buttons are disabled and clicking on them will not trigger any MouseEvent.
     *
     */
    public void mouseClicked(MouseEvent mevt) {
        Object selected =  mevt.getSource();
        if (selected instanceof GridSquare) {
            GridSquare square = (GridSquare) selected;
            int x1 = square.getXcoord();
            int y1 = square.getYcoord();
            this.gridSquares[x1][y1].setContentAreaFilled(true);
            square.setEnabled(false);
            square.setColor(this.playerID);
            square.removeMouseListener(this);

            if (x1 == this.treasure_x && y1 == this.treasure_y) {
                square.setText("X");
                this.instructionLabel.setText("Player " + this.playerID + " won!!!");
                this.gameOver();

            } else {
                this.changePlayerID();
                this.instructionLabel.setText("Player " + this.playerID + "'s turn.");
                square.setText(String.valueOf(Math.abs(x1 - this.treasure_x) + Math.abs(y1 - this.treasure_y)));
            }
        }
    }

    /**
     * Implementing the mouseClicked events for our buttons panel.
     * Note that as soon as the treasure is discovered the ga me win print the winner and the buttons are disabled and clicking on them will not trigger any MouseEvent.
     *
     */
    private void gameOver(){
        for(int row = 0; row < this.gridSquares.length; row++) {
            for (int column = 0; column < this.gridSquares.length; column++) {
                this.gridSquares[row][column].setEnabled(false);
                this.gridSquares[row][column].removeMouseListener(this);

            }
        }


    }

    private void changePlayerID(){
        if (this.playerID == 1){
            this.playerID = 2;
        }else{
            this.playerID = 1;
        }
    }


    // Necessary function implementation to complete the implementation of MouseEvent class.
    public void mousePressed(MouseEvent var1) {}
    public void mouseReleased(MouseEvent var1) {}
    public void mouseEntered(MouseEvent var1) {}
    public void mouseExited(MouseEvent var1) {}
}
