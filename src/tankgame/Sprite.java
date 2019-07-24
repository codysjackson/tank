package tankgame;


import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite {

  protected double xCoordinate;
  protected double yCoordinate;
  protected int frame;
  protected int width;
  protected int height;
  protected boolean isVisible;
  protected boolean hasCollided;
  protected BufferedImage image;

  public Sprite( int x, int y ) {
    this.frame = 0;
    this.xCoordinate = x;
    this.yCoordinate = y;
    isVisible = true;
    hasCollided = false;
  }

  protected void loadImage( String imageName ) throws IOException {
    image = ImageIO.read( Sprite.class.getResource( imageName ));
  }

  protected void getImageDimensions() {
    width = image.getWidth( null );
    height = image.getHeight( null );
  }

  public BufferedImage getImage() {
    return image;
  }

  public int getX() {
    return ( int ) xCoordinate;
  }

  public int getY() {
    return ( int ) yCoordinate;
  }

  public boolean isVisible() {
    return isVisible;
  }

  public void setVisible( Boolean visible ) {
    this.isVisible = visible;
  }

  public Rectangle getBounds() {
    return new Rectangle(( int ) xCoordinate, ( int ) yCoordinate, width , height );
  }

  public boolean hasCollided() {
    return hasCollided;
  }

  public void setCollision( Boolean collision ) {
    this.hasCollided = collision;
  }
}