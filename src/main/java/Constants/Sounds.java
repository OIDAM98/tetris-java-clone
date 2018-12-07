/*

    Sounds used for this game were found at
    https://www.sounds-resource.com/pc_computer/tetriszone/sound/586/
    from the game Tetris Zone

 */


package Constants;

import javax.sound.sampled.*;
import java.io.IOException;

public class Sounds {

    private static final String START = "Sounds/GameStart.wav";
    private static final String OVER = "Sounds/GameOver.wav";
    private static final String CLEARLINE = "Sounds/LineClearSingle.wav";
    private static final String CLEARTWO = "Sounds/LineClearDouble.wav";
    private static final String CLEARTHREE = "Sounds/LineClearTriple.wav";
    private static final String CLEARTETRIS = "Sounds/Tetris.wav";
    private static final String ROTATE = "Sounds/Rotate.wav";

    private static final ClassLoader loader = ClassLoader.getSystemClassLoader();

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

    /**
     * Plays a specific sound
     * @param toPlay Sound to be Played
     */

    private static synchronized void playSound(String toPlay) {
        try {
            /*
                Gets audio file
                Every audio file for the game is under the folder /src/main/resources
             */
            AudioInputStream audio = AudioSystem.getAudioInputStream(ClassLoader.getSystemClassLoader().getResource(toPlay));
            clip = AudioSystem.getClip(); //Initializes clip to play audio file

            //Adds a LineListener that is in charge of indicating when the clip has stopped playing a sound
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

            //Unloads clip and closes it
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
