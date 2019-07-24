package tankgame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class SoundEffects {
  public  void playSound() {
    new Thread( new Runnable() {
      @Override
      public void run() {
        try {
          Clip clip = AudioSystem.getClip();
          AudioInputStream inputStream = AudioSystem.getAudioInputStream( SoundEffects.class.getResourceAsStream( "resources/turret.wav" ));
          clip.open( inputStream );
          clip.start(); 
        } catch ( Exception e ) {
          System.err.println( e.getMessage());
        }
      }
    }).start();
  }
}
