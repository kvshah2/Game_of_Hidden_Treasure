import java.awt.Color;
import javax.swing.*;

/**
 * The class GridSquare takes the x and y coordinates of the square selected by the each player and sets the color accordingly.
 */
public class GridSquare extends JButton
{
	private int xcoord, ycoord;     // location of this square
    
	// constructor takes the x and y coordinates of this square
	public GridSquare( int xcoord, int ycoord)
	{
		super();
		this.xcoord = xcoord;
		this.ycoord = ycoord;
	}
	
	// if its player 1's turn the gridsquare will be set to blue color, otherwise yellow
	public void setColor( int playerID)
	{
		Color color = (playerID == 1) ? Color.blue : Color.yellow;
		this.setBackground(color);
	}

	public void resetColor(){
		this.setBackground(null);
	}

    
    // simple setters and getters
    public void setXcoord(int value)    { xcoord = value; }
    public void setYcoord(int value)    { ycoord = value; }
    public int getXcoord()              { return xcoord; }
    public int getYcoord()              { return ycoord; }
}
