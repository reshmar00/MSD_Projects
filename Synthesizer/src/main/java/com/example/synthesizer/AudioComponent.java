package com.example.synthesizer;

public interface AudioComponent
{
    public AudioClip getClip();

    public boolean hasInput();

    public void connectInput(AudioComponent input);

}
