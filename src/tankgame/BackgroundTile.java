package tankgame;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


public class BackgroundTile {
  BufferedImage image;

  public BackgroundTile( int x, int y ) throws IOException {
    initBackground();
  }
  
  private void initBackground() throws IOException {
    loadImage( "resources/Background.png" );
  }
  
  protected void loadImage( String imageName ) throws IOException {
    image = ImageIO.read( Sprite.class.getResource( imageName ));
  }
}
