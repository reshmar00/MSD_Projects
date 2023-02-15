package com.example.synthesizer;

public class VolumeAdjuster implements AudioComponent{
    private double volume_;
    private AudioComponent input_;

    VolumeAdjuster(double scale){
        volume_ = scale;
    }
    @Override
    public AudioClip getClip() {
        AudioClip original = input_.getClip();
        AudioClip filterAudioClip = new AudioClip();
       for (int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {
           short deducedShort = original.getSample(i);
           int sample = (int)(volume_ *deducedShort); // modifies the original clip to a different volume
          filterAudioClip.setSample(i, sample);
       }
        return filterAudioClip;
    }


    @Override
    public boolean hasInput() {
        return true;
    }

    @Override
    public void connectInput(AudioComponent input){
        input_ = input;
    }
}
