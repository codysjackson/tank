package tankgame;

import java.io.IOException;

public class SolidWall extends Wall {

  public SolidWall( int x, int y ) throws IOException {
    super( x, y );
    initWall();
  }
  
  private void initWall() throws IOException {
    loadImage( "resources/Blue_wall1.png" );  
    getImageDimensions();
  }
}
