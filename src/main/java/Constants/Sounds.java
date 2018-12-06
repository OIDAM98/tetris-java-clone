/*

    Sounds used for this game were found at
    https://www.sounds-resource.com/pc_computer/tetriszone/sound/586/
    from the game Tetris Zone

 */


package Constants;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sounds {

    private static final String START = "GameStart.wav";
    private static final String OVER = "GameOver.wav";
    private static final String CLEARLINE = "LineClearSingle.wav";
    private static final String CLEARTWO = "LineClearDouble.wav";
    private static final String CLEARTHREE = "LineClearTriple.wav";
    private static final String CLEARTETRIS = "Tetris.wav";
    private static final String ROTATE = "Rotate.wav";

    private static Clip clip;

    public static void playStart(){
        playSound(START);
    }

    public static void playOver(){
        playSound(OVER);
    }

    public static void playClearLine(){
        playSound(CLEARLINE);
    }

    public static void playClearTwo(){
        playSound(CLEARTWO);
    }

    public static void playClearThree(){
        playSound(CLEARTHREE);
    }

    public static void playClearTetris(){
        playSound(CLEARTETRIS);
    }

    public static void playRotate(){
        playSound(ROTATE);
    }


    private static synchronized void playSound(String toPlay) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File("src/main/resources/" + toPlay).getAbsoluteFile());
            clip = AudioSystem.getClip();

            clip.addLineListener(e -> {
                if(e.getType() == LineEvent.Type.STOP){ //When it stops running
                    synchronized (clip) {
                        clip.notifyAll(); //Notifies that it has stopped running
                    }
                }
            });

            clip.open(audio); //Load audio to clip
            clip.start(); //Start playing audio

            //Waits of audioclip to stop running, if running
            while (true) {
                synchronized (clip) {
                    clip.wait();
                }
                if (!clip.isRunning()) { //If it has stopped running
                    break;
                }
            }

            clip.drain();
            clip.stop();
            clip.close();
        }
        catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
