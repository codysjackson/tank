package tankgame;


import java.awt.Color;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.net.URISyntaxException;

public class GameFrame extends JFrame {

  public GameFrame() throws IOException, URISyntaxException {
    initUI();
  }

  private void initUI() throws IOException, URISyntaxException {
    JPanel container = new JPanel();
    container.setLayout( new BoxLayout( container, BoxLayout.X_AXIS ));
    container.setBackground( Color.BLACK );

    JPanel center = new JPanel();
    center.setLayout( new BoxLayout( center, BoxLayout.Y_AXIS ));
    center.setBackground( Color.BLACK );

    GameWorld gameWorld = new GameWorld();

    SplitScreen left = new SplitScreen( gameWorld,gameWorld.getTank(), 1 );
    left.setPreferredSize( new Dimension( 500, 720 ));
    container.add( left );

    SplitScreen right = new SplitScreen( gameWorld,gameWorld.getTank2(), 2 );
    right.setPreferredSize( new Dimension( 500,720 ));
    container.add( right );


    add( container );

    setSize( 1280, 720 );
    setResizable( false );

    setTitle( "Tank Wars" );
    setLocationRelativeTo( null );
    setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
  }

  public static void main( String[] args ) {
    EventQueue.invokeLater( new Runnable() {
        @Override
        public void run() {
          GameFrame ex = null;
          try {
            ex = new GameFrame();
          } catch( IOException ex1 ) {
            Logger.getLogger( GameFrame.class.getName()).log( Level.SEVERE, null, ex1 );
          } catch( URISyntaxException ex1 ) {
            Logger.getLogger( GameFrame.class.getName()).log( Level.SEVERE, null, ex1 );
          }
          ex.setVisible( true );
        }
    });
  }
}