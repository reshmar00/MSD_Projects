package com.example.synthesizer;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

public class VFSineWave implements AudioComponent{

    private AudioComponent input_;
    @Override
    public AudioClip getClip() {
        AudioClip original = input_.getClip();
        AudioClip vfsinewaveAudioClip = new AudioClip();
        double phase = 0;
        for(int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {
            phase += 2 * PI * original.getSample(i) / AudioClip.sampleRate;
            //System.out.println(Short.MAX_VALUE * sin( phase ));
            vfsinewaveAudioClip.setSample(i, (int)(Short.MAX_VALUE * sin( phase )));
        }
        return vfsinewaveAudioClip;
    }

    @Override
    public boolean hasInput() {
        return true;
    }

    @Override
    public void connectInput(AudioComponent input) {
        input_ = input;
    }
}
