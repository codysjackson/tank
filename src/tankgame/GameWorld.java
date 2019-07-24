package tankgame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Timer;


public class GameWorld implements ActionListener {

  private final int ICRAFT_X = 100;
  private final int ICRAFT_Y = 120;
  private final int ICRAFT_X2 = 800;
  private final int ICRAFT_Y2 = 1000;
  private final int DELAY = 20;
  private Timer timer;
  private Tank tank, tank2;
  private Explosion explosion;
  SolidWall[][] solidWall = new SolidWall[ 100 ][ 100 ];
  DestructibleWall[][] destructibleWall = new DestructibleWall[ 100 ][ 100 ];
  private BufferedImage background = ImageIO.read(GameWorld.class.getResource( "resources/Background.png" ));

  public GameWorld() throws IOException, URISyntaxException {
    initBoard();
  }

  private void initBoard() throws IOException, URISyntaxException{
    tank = new Tank( ICRAFT_X, ICRAFT_Y );
    tank2 = new Tank( ICRAFT_X2, ICRAFT_Y2 );

    timer = new Timer( DELAY, this );
    timer.start();

    URL resource = GameWorld.class.getResource( "resources/level.txt" );
    File level = new File( resource.toURI());
    BufferedReader reader = new BufferedReader( new FileReader( level ));
    String inputStream;

    int i = 0;
    int j = 0;
    int index = 0;

    while(( inputStream = reader.readLine()) != null ) {
      while( index < inputStream.length()) {
        switch( inputStream.charAt( index )) {
          case '0':
            solidWall[ j ][ i ] = null;
            i++;
            break;
          case '1':
            solidWall[ j ][ i ] = new SolidWall( i * 32, j * 32 );
            i++;
            break;
          case '2':
            destructibleWall[ j ][ i ] = new DestructibleWall( i * 32, j * 32 );
            i++;
            break;
          default:
            solidWall[ j ][ i ] = null;
            i++;
            break;
        }
        index++;
      }
      i = 0;
      index = 0;
      j++;
    }
  }

  @Override
  public void actionPerformed( ActionEvent e ) {
    try {
      updateBullets();
    } catch( IOException ex ) {
      Logger.getLogger( GameWorld.class.getName()).log( Level.SEVERE, null, ex );
    }
    updateTank();
    try {
      updateExplosion();
    } catch( InterruptedException ex ) {
      Logger.getLogger( GameWorld.class.getName()).log( Level.SEVERE, null, ex );
    }
  }
  private void updateBullets() throws IOException {
    ArrayList firstTankBullets = tank.getBullets();
    ArrayList secondTankBullets = tank2.getBullets();

    for( int i = 0; i < firstTankBullets.size(); i++ ) {
      Bullet b1 = ( Bullet ) firstTankBullets.get( i );
      if( b1.isVisible()) {
        b1.move();
        b1.checkBulletCollision( b1, solidWall, destructibleWall );
        b1.checkTankHit( b1, tank2 );
        if( b1.getIsExplode() == true ) {
            explosion = new Explosion( b1.getExplosionCoordX(), b1.getExplosionCoordY());
        }
      } else {
        firstTankBullets.remove( i );
      }
    }

    for( int i = 0; i < secondTankBullets.size(); i++ ) {
      Bullet b2 = ( Bullet ) secondTankBullets.get( i );
      if( b2.isVisible()) {
        b2.move();
        b2.checkBulletCollision( b2, solidWall, destructibleWall );
        b2.checkTankHit( b2, tank );
        if( b2.getIsExplode() == true ) {
          explosion = new Explosion( b2.getExplosionCoordX(), b2.getExplosionCoordY());
        }
      } else {
          secondTankBullets.remove( i );
      }
    }
  }


  private void updateExplosion() throws InterruptedException {
    if( explosion != null ) {
      explosion.nextFrame();
    }
  }

  private void updateTank() {
    tank.move();
    tank2.move();
    tank.checkWallCollision( solidWall );
    tank.checkWallCollision( destructibleWall );
    tank2.checkWallCollision( solidWall );
    tank2.checkWallCollision( destructibleWall );
    tank.checkTankCollision( tank2 );
    tank2.checkTankCollision( tank );
  }
  
  public boolean isOver() {
    if( tank.getLives() == 0 || tank2.getLives() == 0 ) {
      return true;
    } else {
      return false;
    }
  }

  public int getICRAFT_Y() {
    return ICRAFT_Y;
  }

  public int getICRAFT_X2() {
    return ICRAFT_X2;
  }

  public int getICRAFT_Y2() {
    return ICRAFT_Y2;
  }

  public int getDELAY() {
    return DELAY;
  }

  public Timer getTimer() {
    return timer;
  }

  public void setTimer( Timer timer ) {
    this.timer = timer;
  }

  public Tank getTank() {
    return tank;
  }

  public void setTank( Tank tank ) {
    this.tank = tank;
  }

  public Tank getTank2() {
    return tank2;
  }

  public void setTank2( Tank tank2 ) {
    this.tank2 = tank2;
  }

  public SolidWall[][] getSolidWall() {
    return solidWall;
  }

  public void setSolidWall( SolidWall[][] solidWall ) {
    this.solidWall = solidWall;
  }

  public DestructibleWall[][] getDestructibleWall() {
    return destructibleWall;
  }

  public void setDestructibleWall( DestructibleWall[][] destructibleWall ) {
    this.destructibleWall = destructibleWall;
  }

  public BufferedImage getBack() {
    return background;
  }

  public void setBack( BufferedImage background ) {
    this.background = background;
  }

  public int getICRAFT_X() {
    return ICRAFT_X;
  }
  
  public Explosion getExplosion() {
    return explosion;
  }

  public void setExplosion( Explosion explosion ) {
    this.explosion = explosion;
  }
}