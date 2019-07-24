package tankgame;

import java.io.IOException;

public class DestructibleWall extends Wall {

  public DestructibleWall( int x, int y ) throws IOException {
    super( x, y );
    initWall();
  }
  
  private void initWall() throws IOException {
    loadImage( "resources/Blue_wall2.png" );  
    getImageDimensions();
  }
  
  public void destroyWall( DestructibleWall wall[][], int i, int j ) {
    wall[ i ][ j ] = null;
  }
}
