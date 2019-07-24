package tankgame;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Explosion extends Sprite {
  BufferedImage[] explosionImages;

  public Explosion( int x, int y ) throws IOException {
    super(x, y);
    initExplosion();
  }

  private void initExplosion() throws IOException {
    loadImage( "resources/Explosion_small_strip6.png" );
    getImageDimensions();
  }

  @Override
  protected void loadImage( String source ) throws IOException {
    super.loadImage( source );

    if( image == null ){
      System.out.println( "image is null" );
    }

    explosionImages = new BufferedImage[ 6 ];

    int w = image.getWidth( null ) / 6;
    int h = image.getHeight( null );
    int xCoord = 0;
    int yCoord = 0;

    for( int counter = 0; counter < 6; counter++ ) {
      explosionImages[ counter ] = image.getSubimage( xCoord, yCoord, w, h );
      xCoord +=w;
    }
  }
  
  @Override
  public BufferedImage getImage() {
    if( frame < 6 ) {
      return explosionImages[ frame ];
    } else {
      return null;
    }
  }
  
  public void nextFrame() throws InterruptedException {
    if( frame < explosionImages.length ) {
      frame++;
    }
  }
  
  public void printFrame() {
    System.out.println( frame );
  }
}
