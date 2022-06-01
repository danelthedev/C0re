package Utilitarians;

import Interface.Buttons.SoundButtons.SoundBar;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Sound{
    protected Clip sound;
    protected int timer;

    Sound(Clip c){
        sound = c;
        timer = 120;
    }
}

public class AudioPlayer {

    private List<Sound> sounds = new ArrayList<>();

    public void play(String filepath){

        Clip sound=null;
        AudioInputStream audioInputStream=null;

        //create Audio Input Stream
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile());
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        //create sound clip
        try {
            sound = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        try {
            sound.open(audioInputStream);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }

        FloatControl gainControl = (FloatControl) sound.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(SoundBar.volume));
        sound.start();

        Sound s = new Sound(sound);
        sounds.add(s);
    }

    public void update(){
        for(int i = 0; i < sounds.size(); ++i){
            if(sounds.get(i).timer > 0)
                sounds.get(i).timer--;
            else{
                sounds.get(i).sound.close();
                sounds.remove(sounds.get(i));
            }

        }

    }


}
