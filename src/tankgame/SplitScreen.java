package tankgame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Image;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.SwingConstants;

public class SplitScreen extends JPanel implements ActionListener {
  protected GameWorld GW;
  private Timer timer;
  private final int DELAY = 10;
  protected int x;
  protected int y;
  protected Tank focusTank;
  protected Explosion explosion;

  private Image scaledTankImage;
  private Image scaledTank2Image;
  private Image scaledBackground;
  private Image scaledSolidWall;
  private Image scaledDestructibleWall;
  private int playerNumber;
  private JLabel label;

  public SplitScreen( GameWorld gameWorld, Tank tank, int player ) {
    this.setBackground( Color.black );

    setLayout( new BorderLayout());
    label = new JLabel("");
    add( label, BorderLayout.CENTER );

    this.focusTank = tank;
    this.GW = gameWorld;
    this.playerNumber = player;

    scaledTankImage = scaleImage( focusTank.getImage());
    scaledTank2Image = scaleImage( focusTank.getImage());
    scaledBackground = scaleImage( GW.getBack());

    SolidWall[][] solidWall = GW.getSolidWall();
    DestructibleWall[][] destructibleWall = GW.getDestructibleWall();
    for( int i = 0; i < solidWall.length; i++ ) {
      for( int j = 0; j < solidWall[ i ].length; j++ ) {
        if( solidWall[ i ][ j ] != null ) {
          Wall wall = solidWall[ i ][ j ];
          scaledSolidWall = scaleImage( wall.getImage());
          break;
        }
      }
    }
    for( int i = 0; i < destructibleWall.length; i++ ) {
      for( int j = 0; j < destructibleWall[i].length; j++ ) {
        if( destructibleWall[ i ][ j ] != null ) {
          Wall wall = destructibleWall[ i ][ j ];
          scaledDestructibleWall = scaleImage( wall.getImage());
        }
      }
    }

    addKeyListener(( KeyListener ) new TAdapter());
    setFocusable( true );

    x = 0;
    y = 0;

    timer = new Timer( DELAY, this );
    timer.start();
  }

  private int transform( int num, char a ) {
    int newNum;
    if( a == 'x' ) {
      newNum = num - x;
    }
    else if( a == 'y' ) {
      newNum = num - y;
    }
    else {
      newNum= num;
    }
    return newNum;
  }

  @Override
  public void actionPerformed( ActionEvent e ) {
    if( GW.isOver()) {
      try {
        timer.wait( 10 );
      } catch ( Exception ex ) {}
      timer.stop();
      displayGameOver();
    }
    repaint();
    updateFrameOfReference();
  }
  private void updateFrameOfReference() {
    if( focusTank.getX() < x + 600 ){
      x = focusTank.getX() - 300;
    }
    else if( focusTank.getX() > x + 600 ) {
      x = focusTank.getX() + 300;
    }

    x = Math.max( 10,x );
    x = Math.min( 611,x );

    if( focusTank.getY() < y + 720 ) {
      y = focusTank.getY() - 280;
    }
    else if( focusTank.getY() > y + 720 ) {
      y = focusTank.getY() + 280;
    }
    y = Math.max( 0,y );
    y = Math.min( 589,y );
  }

  private class TAdapter extends KeyAdapter {
    //use the getter methods to create tank and tank2 variables
    Tank tank = GW.getTank();
    Tank tank2 = GW.getTank2();

    @Override
    public void keyReleased( KeyEvent e ) {
      tank.keyReleased( e );
      tank2.keyReleasedPlayerTwo( e );
    }

    @Override
    public void keyPressed( KeyEvent e ) {
      try {
        tank.keyCall( e );
        tank2.keyCallPlayerTwo( e );
      } catch ( IOException ex ) {
        Logger.getLogger( SplitScreen.class.getName()).log( Level.SEVERE, null, ex );
      }
    }
  }

  @Override
  public void paintComponent( Graphics g ) {
    super.paintComponent( g );
    paintBackground( g );
    doDrawing( g );
    
    if( this.playerNumber == 1 ) {
      drawMiniMapLeft( g );
    } else if( this.playerNumber == 2 ) {
      drawMiniMapRight( g );
    }
    Toolkit.getDefaultToolkit().sync();
  }

  protected void drawMiniMapLeft( Graphics g ) {
    Tank tank = GW.getTank();
    Tank tank2 = GW.getTank2();
    Image background = scaledBackground;
    int x1 = 527;
    int x2 = 757;

    Graphics2D g2d = ( Graphics2D ) g;

    for( int x = 0; x < 1000; x += background.getWidth( null )) {
      for( int y = 0; y < 1225; y += background.getHeight( null )) {
        g.drawImage( background, x1, 400, x2, 610, x, y, background.getWidth( this ), background.getHeight( this ), null );
      }
    }

    SolidWall[][] solidWall = GW.getSolidWall();
    DestructibleWall[][] destructibleWall = GW.getDestructibleWall();
    for( int i = 0; i < solidWall.length; i++ ) {
      for( int j = 0; j < solidWall[ i ].length; j++ ) {
        if( solidWall[ i ][ j ] != null ) {
          Wall wall = solidWall[ i ][ j ];
          g2d.drawImage( wall.getImage(), x1 + ( wall.getX() / 6 ), 400 + ( wall.getY() / 6 ),
                  x2 + ( wall.getX() / 6 ), 600 + ( wall.getY() / 6 ), 0, 0, 1280, 720, this );
        }
        if( destructibleWall[ i ][ j ] != null ) {
          Wall wall = destructibleWall[ i ][ j ];
          g2d.drawImage( wall.getImage(), x1 + ( wall.getX() / 6 ), 400 + ( wall.getY() / 6 ),
                  x2 + ( wall.getX() / 6 ), 600 + ( wall.getY() / 6 ), 0, 0, 1280, 720, this );
        }
      }
    }
    g2d.drawImage( tank.getImage(), x1 + ( tank.getX() / 6 ), 400 + ( tank.getY() / 6 ),
            x2 + ( tank.getX() / 6 ), 600 + ( tank.getY() / 6 ), 0, 0, 1280, 720, this );
    g2d.drawImage( tank2.getImage(), x1 + ( tank2.getX() / 6 ), 400 + ( tank2.getY()/6 ), 
            x2 + ( tank2.getX() / 6 ), 600 + ( tank2.getY() / 6 ), 0, 0, 1280, 720, this );
  }

  protected void drawMiniMapRight( Graphics g ) {
    Tank tank = GW.getTank();
    Tank tank2 = GW.getTank2();
    Image background = scaledBackground;
    int x1 = -110;
    int x2 = 140;

    Graphics2D g2d = ( Graphics2D ) g;

    for( int x = 0; x < 1000; x += background.getWidth( null )) {
      for( int y = 0; y < 1225; y += background.getHeight( null )) {
        g.drawImage( background, x1, 400, x2 - 45, 610, x, y, background.getWidth( this ), background.getHeight( this ), null );
      }
    }

    SolidWall[][] solidWall = GW.getSolidWall();
    DestructibleWall[][] destructibleWall = GW.getDestructibleWall();
    for( int i = 0; i < solidWall.length; i++ ) {
      for( int j = 0; j < solidWall[ i ].length; j++ ) {
        if( solidWall[ i ][ j ] != null ) {
          Wall wall = solidWall[ i ][ j ];
          g2d.drawImage( wall.getImage(), x1 + ( wall.getX() / 6 ), 400 + ( wall.getY() / 6 ), x2 + ( wall.getX() / 6 ), 600 + ( wall.getY() / 6 ), 0, 0, 1280, 720, this );
        }
        if( destructibleWall[ i ][ j ] != null ) {
          Wall wall = destructibleWall[ i ][ j ];
          g2d.drawImage( wall.getImage(), x1 + ( wall.getX() / 6 ), 400 + ( wall.getY() / 6 ), x2 + ( wall.getX() / 6 ), 600 + ( wall.getY() / 6 ), 0, 0, 1280, 720, this );
        }
      }
    }

    g2d.drawImage( tank.getImage(), x1 + ( tank.getX() / 6 ), 400 + ( tank.getY() / 6 ), x2 + ( tank.getX() / 6 ), 600 + ( tank.getY() / 6 ), 0, 0, 1280, 720, this );
    g2d.drawImage( tank2.getImage(), x1 + ( tank2.getX() / 6 ), 400 + ( tank2.getY() / 6 ), x2 + ( tank2.getX() / 6 ), 600 + ( tank2.getY() / 6 ), 0, 0, 1280, 720, this );
  }

  protected void doDrawing( Graphics g ) {

    Tank tank = GW.getTank();
    Tank tank2 = GW.getTank2();
    Explosion explosion = GW.getExplosion();

    Graphics2D g2d = ( Graphics2D ) g;

    drawHealth( g2d,tank );
    drawHealth( g2d,tank2 );
    drawLives( g2d,tank );
    drawLives( g2d,tank2 );

    if( explosion != null ) {
      g2d.drawImage( explosion.getImage(), transform(explosion.getX(),'x' ), transform( explosion.getY(), 'y' ), this );
    }
    ArrayList bul = tank.getBullets();
    ArrayList bul2 = tank2.getBullets();

    for( Object b1 : bul ) {
      Bullet b = ( Bullet ) b1;
      g2d.drawImage( b.getImage(), transform( b.getX(), 'x' ), transform( b.getY(), 'y' ), this );
    }
    for( Object b2 : bul2 ) {
      Bullet b = ( Bullet ) b2;
      g2d.drawImage( b.getImage(), transform( b.getX(), 'x' ), transform( b.getY(), 'y' ), this );
    }

    g2d.drawImage( tank.getImage(), transform( tank.getX(), 'x' ), transform( tank.getY(), 'y' ), this );
    g2d.drawImage( tank2.getImage(), transform( tank2.getX(), 'x' ), transform( tank2.getY(), 'y' ), this );
  }

  public void paintBackground( Graphics g ) {
    Graphics2D g2d = ( Graphics2D ) g;

    //initialize a BufferedImage background using getBack()
    BufferedImage background = GW.getBack();
    for( int x = 0; x < 1300; x += background.getWidth( null )) {
      for( int y = 0; y < 1225; y += background.getHeight( null )) {
        g.drawImage( background,transform( x,'x' ), transform( y,'y' ), null );
      }
    }

    //initialize solidWall and destructibleWall
    SolidWall[][] solidWall = GW.getSolidWall();
    DestructibleWall[][] destructibleWall = GW.getDestructibleWall();
    for( int i = 0; i < solidWall.length; i++ ) {
      for( int j = 0; j < solidWall[i].length; j++ ) {
        //g2d.drawImage(, xform, this)
        if( solidWall[ i ][ j ] != null ) {
          Wall wall = solidWall[ i ][ j ];
          g2d.drawImage( wall.getImage(), transform( wall.getX(),'x' ), transform( wall.getY(),'y' ), this );
        }
        if( destructibleWall[ i ][ j ] != null ) {
          Wall wall = destructibleWall[ i ][ j ];
          g2d.drawImage( wall.getImage(), transform( wall.getX(),'x' ),transform( wall.getY(),'y' ), this );
        }
      }
    }
  }
  
  private void drawHealth( Graphics g, Tank tank ){
    int x = transform( tank.getX(),'x' );
    int y = transform( tank.getY(),'y' ) - 10;
    int width = tank.getImage().getWidth() * tank.getHealth() / 130;
    int height = 5;

    if( tank.getHealth() >= 100 ) {
      g.setColor( Color.green );
    } else if ( tank.getHealth() >= 30 ) {
      g.setColor( Color.yellow );
    } else {
      g.setColor( Color.red );
    }
    g.fillRect( x,y,width,height );
  }
  
  private void drawLives( Graphics g, Tank tank ) {
    int interval = tank.getImage().getWidth() / 6;
    int x = transform( tank.getX(), 'x' );
    int y = transform( tank.getY(), 'y' ) + 10 + tank.getImage().getHeight();
    int lives = tank.getLives();

    int buffer = tank.getImage().getWidth() / 3;

    for( int i = 1; i <= lives; i++ ) {
      g.setColor( Color.blue );
      g.fillOval( x + ( buffer - 10 )+ i * interval, y, 10, 10 );
    }
  }

  private int scaledImageHeight( BufferedImage image ) {
    int height = image.getHeight() / 5;
    return height;
  }

  private int scaledImageWidth( BufferedImage image ) {
    int width = image.getWidth() / 5;
    return width;
  }

  private Image scaleImage( BufferedImage image ) {
    return image.getScaledInstance( scaledImageWidth( image ), scaledImageHeight( image ), Image.SCALE_DEFAULT );
  }

  private void displayGameOver(){
    label.setFont( new Font( "Impact", Font.BOLD, 100 ));
    label.setHorizontalAlignment( SwingConstants.CENTER );
    label.setForeground( Color.WHITE );
    label.setText( "GAME OVER" );
  }
}