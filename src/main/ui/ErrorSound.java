package ui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

// source: http://ryisnow.net/2018/11/21/how-to-play-audio-files-sound-effect-java-game-development-extra-3/
public class ErrorSound {
    private Clip soundClip;
    private AudioInputStream audioInputStream;

    private String soundFilePath = "./data/Phase3AudioComponent.wav";

    // source: http://ryisnow.net/2018/11/21/how-to-play-audio-files-sound-effect-java-game-development-extra-3/
    // EFFECTS: takes the path of a sound file as an argument, creates an instance of the File class
    //          (while assigning the argument to the path of the File object);
    //          creates an Audio Input Stream based on
    //          the path of the sound file passed to getAudioInputStream( ) as an argument,
    //          and assigns it to the audioInputStream field;
    //          assigns to the soundClip field a clip that can be used for playing back an audio stream;
    //          opens the sound clip with the format and audio data present in audioInputStream;
    public void setFile(String soundFileName) {
        try {
            File file = new File(soundFileName);
            audioInputStream = AudioSystem.getAudioInputStream(file);
            soundClip = AudioSystem.getClip();
            soundClip.open(audioInputStream);
        } catch (Exception e) {
            System.out.println("Invalid file path");
        }
    }

    // source: http://ryisnow.net/2018/11/21/how-to-play-audio-files-sound-effect-java-game-development-extra-3/
    // EFFECTS: plays the sound clip assigned to this ErrorSound object;
    public void play() {
        soundClip.setFramePosition(0);
        soundClip.start();
    }

    public String getSoundFilePath() {
        return soundFilePath;
    }
}

