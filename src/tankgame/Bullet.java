package tankgame;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bullet extends Sprite {
  private final int BOARD_WIDTH = 2000;
  private final double BULLET_SPEED = 2;
  private final double dx, dy;
  private int explosionCoordX, explosionCoordY;
  private boolean isExplode;
  BufferedImage[] bulletImages;
  int angle;

  public Bullet( int x, int y, double dx, double dy, int angle ) throws IOException {
    super( x, y );
    this.dx = dx;
    this.dy = dy;
    this.angle = angle;

    initBullet();
  }

  private void initBullet() throws IOException {
    isExplode = false;
    loadImage( "resources/Shell_basic_strip60.png" );
    getImageDimensions();
  }

  public void move() {
    xCoordinate += BULLET_SPEED * dx;
    yCoordinate += BULLET_SPEED * dy;

    if ( xCoordinate > BOARD_WIDTH ) {
        isVisible = false;
    }
  }

  @Override
  protected void loadImage( String source ) throws IOException {
    super.loadImage( source );

    if( image == null ){
      System.out.println( "image is null" );
    }

    bulletImages = new BufferedImage[ 60 ];

    int w = image.getWidth( null ) / 60;
    int h = image.getHeight( null );
    int x = 0;
    int y = 0;

    for( int i = 0; i < 60; i++ ) {
      bulletImages[ i ] = image.getSubimage( x, y, w, h );
      x +=w;
    }
  }

  @Override
  public BufferedImage getImage() {
    return bulletImages[ this.angle / 6 ];
  }

  public void checkBulletCollision( Bullet bulletObject, SolidWall[][] solidWall,
          DestructibleWall[][] destructibleWall ) throws IOException {
    Rectangle bullet =  new Rectangle(( int )bulletObject.getX(), ( int )bulletObject.getY(),
            bulletObject.getImage().getWidth( null ), bulletObject.getImage().getHeight( null ));
    for( int i = 0; i < solidWall.length; i++ ) {
      for( int j = 0; j < solidWall.length; j++ ) {
        if( solidWall[ i ][ j ] != null ) {
          if( bullet.intersects( solidWall[ i ][ j ].getBounds())) {
            isVisible = false;
            SoundEffects effect = new SoundEffects();
            effect.playSound();
          }
        }
        if(destructibleWall[ i ][ j ] != null) {
          if( bullet.intersects( destructibleWall[ i ][ j ].getBounds())) {
            SoundEffects effect = new SoundEffects();
            effect.playSound();
            setIsExplode( true );
            setExplosionCoord(( int )destructibleWall[ i ][ j ].getX(),( int ) destructibleWall[ i ][ j ].getY());
            isVisible = false;
            destructibleWall[ i ][ j ].destroyWall( destructibleWall, i, j );
          }
        }
      }
    }
  }

  public void setIsExplode( boolean isExplode ) {
    this.isExplode = isExplode;
  }

  public boolean getIsExplode() {
    return isExplode;
  }

  public void setExplosionCoord( int x, int y ) {
    this.explosionCoordX = x;
    this.explosionCoordY = y;
  }

  public int getExplosionCoordX() {
    return this.explosionCoordX;
  }

  public int getExplosionCoordY() {
    return this.explosionCoordY;
  }

  public void checkTankHit( Bullet bulletObject, Tank tank2 ) throws IOException {
    Rectangle bullet =  new Rectangle(( int )bulletObject.getX(),( int ) bulletObject.getY(),
            bulletObject.getImage().getWidth( null ), bulletObject.getImage().getHeight( null ));
    Rectangle secondTank = new Rectangle(( int )tank2.getX() + 8, ( int )tank2.getY() + 8,
            tank2.getImage().getWidth( null ) - 16, tank2.getImage().getHeight( null ) - 16 );

    if( bullet.intersects( secondTank )) {
      SoundEffects effect = new SoundEffects();
      effect.playSound();
      isVisible = false;
      tank2.damageTank();
      if( tank2.getHealth() <= 0 ) {
        setIsExplode( true );
        setExplosionCoord(( int )secondTank.getX(), ( int )secondTank.getY());
        tank2.resetTank();
      }
    }
  }
}