package com.example.synthesizer;

import java.util.ArrayList;

public class Mixer implements AudioComponent{
    private ArrayList<AudioComponent> inputs_;

    public Mixer()
    {
        inputs_ = new ArrayList<>();
    }

    @Override
    public AudioClip getClip() {
        AudioClip mixerAudioClip = new AudioClip();
        for (AudioComponent a : inputs_) {
            AudioClip temp = a.getClip();
            for (int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {
                    //combines multiple audio clips to a single audio clip
                mixerAudioClip.setSample(i, (mixerAudioClip.getSample(i) + temp.getSample(i)));
            }
        }
        return mixerAudioClip;
    }
    @Override
    public boolean hasInput() {
        return true;
    }

    @Override
    public void connectInput(AudioComponent input){
        inputs_.add(input);
    }
}
