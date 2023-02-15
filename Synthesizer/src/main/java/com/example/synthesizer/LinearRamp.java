package com.example.synthesizer;

public class LinearRamp implements AudioComponent{
    private float start_;
    private float stop_;

    public LinearRamp(){
        start_ = 50;
        stop_ = 10000;
    }

    public LinearRamp(float start, float stop){
        start_ = start;
        stop_ = stop;
    }
    @Override
    public AudioClip getClip() {
        AudioClip rampAudioClip = new AudioClip();
        for(int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {
            rampAudioClip.setSample(i, (int)(start_ * (AudioClip.TOTAL_SAMPLES - i) + stop_ * i) / AudioClip.TOTAL_SAMPLES);
        }
        return rampAudioClip;
    }

    @Override
    public boolean hasInput() {
        return false;
    }

    @Override
    public void connectInput(AudioComponent input) {
        // nothing here because we do not use connectInput with SineWave
    }
}
