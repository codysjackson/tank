/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Tank extends Sprite {

  private int spawnX, spawnY;
  private double xCoordChange, yCoordChange;
  private double prevX, prevY;
  private double bulletDirectionX, bulletDirectionY;
  private int health, lives;
  private ArrayList bullets;
  BufferedImage[] tankImages;
  int angle;
  Set<Integer> pressed = new HashSet<Integer>();

  Set<Integer> pressed2 = new HashSet<Integer>();

  public Tank( int x, int y ) throws IOException {
    super( x, y );
    setSpawn( x, y );
    initTank();
  }

  private void initTank() throws IOException {
    bullets = new ArrayList();
    loadImage( "resources/Tank_blue_basic_strip60.png" );
    angle = 0;
    health = 130;
    lives = 3;
    getImageDimensions();
  }

  @Override
  protected void loadImage( String source ) throws IOException {
    super.loadImage( source );

    if( image == null ){
      System.out.println( "image is null" );
    }

    tankImages = new BufferedImage[ 60 ];

    int width = image.getWidth( null ) / 60;
    int height = image.getHeight( null );
    int xCoordinate = 0;
    int yCoordinate = 0;

    for( int i = 0; i < 60; i++ ) {
      tankImages[ i ] = image.getSubimage( xCoordinate, yCoordinate, width, height );
      xCoordinate += width;
    }
  }

  @Override
  public BufferedImage getImage() {
    return tankImages[ angle / 6 ];
  }

  public void updateAngle( int direction ) {
    if( direction == 1 ) {
      angle += 6;
    } else {
      angle -= 6;
    }

    if( angle < 0 ) {
      angle = 354;
    }
    else if( angle >= 360 ) {
      angle = 0;
    }
  }

  public int getAngle() {
    return angle;
  }

  public void move() {
    prevX = xCoordinate;
    prevY = yCoordinate;
    xCoordinate += xCoordChange;
    yCoordinate += yCoordChange;
  }

  public ArrayList getBullets() {
    return bullets;
  }

  public void checkWallCollision( Wall[][] wall ) {
    Rectangle playerTank = new Rectangle( getX() + 8, getY() + 8,
            getImage().getWidth( null ) - 16, getImage().getHeight( null ) - 16 );
    Rectangle[][] wallHitbox = new Rectangle[ 100 ][ 100 ];

    for( int i = 0; i < wall.length; i++ ) {
      for( int j = 0; j < wall[ i ].length; j++ ) {
        if( wall[ i ][ j ] != null ) {
          wallHitbox[ i ][ j ] = wall[ i][ j ].getBounds();
          if( playerTank.intersects( wallHitbox[ i ][ j ] )) {
            xCoordinate = ( int ) prevX;
            yCoordinate = ( int ) prevY;
          }
        }
      }
    }
  }

  public void checkTankCollision( Tank tank2 ) {
    Rectangle firstTank = new Rectangle( getX() + 8, getY() + 8,
            getImage().getWidth( null ) - 16, getImage().getHeight( null ) - 16 );
    Rectangle secondTank = new Rectangle( tank2.getX() + 8, tank2.getY() + 8,
            tank2.getImage().getWidth( null ) - 16, tank2.getImage().getHeight( null ) - 16 );

    if( firstTank.intersects( secondTank )) {
      xCoordinate = ( int ) prevX;
      yCoordinate = ( int ) prevY;
    }
  }

  private double getTankCenterX() {
    double centerX;
    centerX = ( this.getX() + ( this.getX() + this.width )) / 2;
    return centerX;
  }

  private double getTankCenterY() {
    double centerY;
    centerY = (this.getY() + (this.getY() + this.height)) / 2;
    return centerY;
  }

  public void damageTank() {
    health -= 40;
    if( lives < 0 ) {

    }
    System.out.println("Health: " + health + "\nLives: " + lives);
  }

  private void setSpawn( int x, int y ) {
    this.spawnX = x;
    this.spawnY = y;
  }

  public int getHealth() {
    return this.health;
  }
  public int getLives() {
    return this.lives;
  }

  public void resetTank() {
    this.lives--;
    this.health = 130;
    this.xCoordinate = spawnX;
    this.yCoordinate = spawnY;
    this.angle = 0;
  }

  public void keyCall( KeyEvent e ) throws IOException{
    pressed.add( e.getKeyCode());
    for( int i :pressed ){
      keyPressedHelper( i );
    }
  }

  private void keyPressedHelper( int key ) throws IOException{
    bulletDirectionX = 5 * Math.cos( Math.toRadians( angle ));
    bulletDirectionY = -5 * Math.sin( Math.toRadians( angle ));

    if( key == KeyEvent.VK_ENTER ) {
      fire();
    }

    if( key == KeyEvent.VK_LEFT ) {
      updateAngle( 1 );
    }

    if( key == KeyEvent.VK_RIGHT ) {
      updateAngle( -1 );
    }

    if( key == KeyEvent.VK_UP ) {
      xCoordChange = ( 5*Math.cos( Math.toRadians( angle )));
      yCoordChange = ( -5*Math.sin( Math.toRadians( angle )));
    }

    if( key == KeyEvent.VK_DOWN ) {
      xCoordChange = ( -5*Math.cos( Math.toRadians( angle )));
      yCoordChange = ( 5*Math.sin( Math.toRadians( angle )));
    }
  }

  public void keyPressed( KeyEvent e ) throws IOException {
    int key = e.getKeyCode();

    bulletDirectionX = 5*Math.cos(Math.toRadians( angle ));
    bulletDirectionY = -5*Math.sin(Math.toRadians( angle ));

    if( key == KeyEvent.VK_ENTER ) {
      fire();
    }

    if( key == KeyEvent.VK_LEFT ) {
      updateAngle( 1 );
    }

    if( key == KeyEvent.VK_RIGHT ) {
      updateAngle( -1 );
    }

    if( key == KeyEvent.VK_UP ) {
      xCoordChange = ( 5*Math.cos( Math.toRadians( angle )));
      yCoordChange = ( -5*Math.sin( Math.toRadians( angle )));
    }

    if( key == KeyEvent.VK_DOWN ) {
      xCoordChange = ( -5*Math.cos( Math.toRadians( angle )));
      yCoordChange = ( 5*Math.sin( Math.toRadians( angle )));
    }
  }

  public void keyPressedPlayerTwo( KeyEvent e ) throws IOException {

    int key = e.getKeyCode();

    bulletDirectionX = 5*Math.cos(Math.toRadians( angle ));
    bulletDirectionY = -5*Math.sin(Math.toRadians( angle ));

    if( key == KeyEvent.VK_SPACE ) {
      fire();
    }

    if( key == KeyEvent.VK_A ) {
      updateAngle( 1 );
    }

    if( key == KeyEvent.VK_D ) {
      updateAngle( -1 );
    }

    if( key == KeyEvent.VK_W ) {
      xCoordChange = ( 5*Math.cos( Math.toRadians( angle )));
      yCoordChange = ( -5*Math.sin( Math.toRadians( angle )));
    }

    if( key == KeyEvent.VK_S ) {
      xCoordChange = ( -5*Math.cos( Math.toRadians( angle )));
      yCoordChange = ( 5*Math.sin( Math.toRadians( angle )));
    }
  }

  public void fire() throws IOException {
    int projectileX = ( int ) (( this.getTankCenterX() - ( width / 2 ) + 20 ) + Math.cos( Math.toRadians( angle )));
    int projectileY = ( int ) (( this.getTankCenterY() - 12 ) + Math.sin(Math.toRadians( angle )));
    bullets.add( new Bullet( projectileX, projectileY, bulletDirectionX, bulletDirectionY, this.getAngle() ));
  }

  public void keyReleased( KeyEvent e ) {
    int key = e.getKeyCode();
    pressed.remove( key );

    if( key == KeyEvent.VK_LEFT ) {
      xCoordChange = 0;
      yCoordChange = 0;
    }

    if( key == KeyEvent.VK_RIGHT ) {
      xCoordChange = 0;
      yCoordChange = 0;
    }

    if( key == KeyEvent.VK_UP ) {
      xCoordChange = 0;
      yCoordChange = 0;
    }

    if( key == KeyEvent.VK_DOWN ) {
      xCoordChange = 0;
      yCoordChange = 0;
    }
  }

  private void keyPressedHelperPlayerTwo( int key ) throws IOException {
    bulletDirectionX = 5*Math.cos( Math.toRadians( angle ));
    bulletDirectionY = -5*Math.sin( Math.toRadians( angle ));


    if( key == KeyEvent.VK_SPACE ) {
      fire();
    }

    if( key == KeyEvent.VK_A ) {
      updateAngle( 1 );
    }

    if( key == KeyEvent.VK_D ) {
      updateAngle( -1 );
    }

    if( key == KeyEvent.VK_W ) {
      xCoordChange = ( 5*Math.cos( Math.toRadians( angle )));
      yCoordChange = ( -5*Math.sin( Math.toRadians( angle )));
    }

    if( key == KeyEvent.VK_S ) {
      xCoordChange = ( -5*Math.cos( Math.toRadians( angle )));
      yCoordChange = ( 5*Math.sin( Math.toRadians( angle )));
    }
  }

  public void keyCallPlayerTwo( KeyEvent e ) throws IOException {
    pressed2.add( e.getKeyCode());
    for( int key : pressed2 ){
      keyPressedHelperPlayerTwo( key );
    }
  }

  public void keyReleasedPlayerTwo(KeyEvent e) {
    int key = e.getKeyCode();
    pressed2.remove(key);

    if( key == KeyEvent.VK_A ) {
      xCoordChange = 0;
      yCoordChange = 0;
    }

    if( key == KeyEvent.VK_D ) {
      xCoordChange = 0;
      yCoordChange = 0;
    }

    if( key == KeyEvent.VK_W ) {
      xCoordChange = 0;
      yCoordChange = 0;
    }

    if( key == KeyEvent.VK_S ) {
      xCoordChange = 0;
      yCoordChange = 0;
    }
  }
}
