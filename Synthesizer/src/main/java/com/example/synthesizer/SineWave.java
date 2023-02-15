package com.example.synthesizer;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

public class SineWave implements AudioComponent
{
    private int desiredFrequency;

    SineWave(int frequency)
    {
        desiredFrequency = frequency;
    }
    public int getDesiredFrequency()
    {
        return desiredFrequency;
    }

    @Override
    public AudioClip getClip() {
        AudioClip sinewaveAudioClip = new AudioClip();
        //short MaxValue = (short)(Short.MAX_VALUE);
        for(int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {
            short someMath = (short)(Short.MAX_VALUE * sin(2 * PI * desiredFrequency * i / AudioClip.sampleRate));
            //System.out.println(someMath);
            sinewaveAudioClip.setSample(i, someMath);
        }
        return sinewaveAudioClip;
    }

    @Override
    public boolean hasInput() {
        return false;
    }

    @Override
    public void connectInput(AudioComponent input){
        // nothing here because we do not use connectInput with SineWave
    }

    public int getFrequency(){
        return desiredFrequency;
    }

    public void setFrequency(int value){
        desiredFrequency = value;
    }
}
