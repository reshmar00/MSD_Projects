package com.example.synthesizer;

import com.example.synthesizer.AudioClip;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.Random;

class AudioClipTest {

    @org.junit.jupiter.api.Test
    void runAllTests() {
        getSample();
        setSample();
        getData();
    }

    @Test
    void getSample() {
        AudioClip au = new AudioClip();

        int index = 88199; // last index possible
        Short value = Short.MAX_VALUE;
        au.setSample(index, value.byteValue());
        short valueAtGivenIndex = au.getSample(index);
        Assertions.assertEquals(valueAtGivenIndex, value.byteValue());

        index = 0;
        Short value2 = Short.MIN_VALUE;
        au.setSample(index, value2.byteValue());
        valueAtGivenIndex = au.getSample(index);
        Assertions.assertEquals(valueAtGivenIndex, value2.byteValue());
        Assertions.assertTrue(valueAtGivenIndex == value2.byteValue());
        Assertions.assertFalse(valueAtGivenIndex == value.byteValue());

        int initializeSize = 44100*(int)2.0*2;
        ArrayList<Byte> audioClipArrayList = new ArrayList<>(initializeSize);
        for (Short i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++){
            audioClipArrayList.add(i.byteValue());
        }

        for (int i = 0; i < audioClipArrayList.size(); i++) {
            au.setSample(i, audioClipArrayList.get(i)); //test is trivial, will always pass
            Assertions.assertEquals((byte)audioClipArrayList.get(i), au.getSample(i));
        }
    }

    @org.junit.jupiter.api.Test
    void setSample() {

        AudioClip a = new AudioClip();

        int index = 5;
        Short value = Short.MAX_VALUE;
        a.setSample(index, value.byteValue());
        Assertions.assertEquals(a.getSample(index), value.byteValue());

        Short value2 = Short.MIN_VALUE;
        a.setSample(index, value2.byteValue());
        Assertions.assertEquals(a.getSample(index), value2.byteValue());
        Assertions.assertFalse(a.getSample(index) == value.byteValue());
        Assertions.assertTrue(a.getSample(index) == value2.byteValue());
    }

    @org.junit.jupiter.api.Test
    void getData() {
        AudioClip aud = new AudioClip();
        byte [] bytes = new byte[aud.getDataSize()];
        Integer byte_i = 0;
        Arrays.fill(bytes, byte_i.byteValue());
        Assertions.assertArrayEquals(aud.getData(), bytes);
    }
}