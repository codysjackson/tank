package tankgame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JComponent;


public class Background extends JComponent {
  BufferedImage background;
  
  public Background( BufferedImage background ) throws IOException {
    this.background = background;
  }
  
  @Override
  protected void paintComponent( Graphics graphics ) {
    super.paintComponent( graphics );
    graphics.drawImage( background, 0, 0, this );
  }
}
