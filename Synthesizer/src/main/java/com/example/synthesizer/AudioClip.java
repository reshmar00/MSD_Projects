package com.example.synthesizer;

import java.util.Arrays;

// This class represents an audioclip that will get passed throughout our system
public class AudioClip
{
    public static final double duration = 2.0; // in seconds
    public static final int sampleRate = 44100;
    public static final int TOTAL_SAMPLES = (int)duration* sampleRate;

    int dataSize = sampleRate*(int)duration*2;
    byte[] data;

    public AudioClip()
    {
        data = new byte[dataSize];
    }

    public short getSample(int index)
    {
        // Convert to bytes using bitwise operations
        int littleEnd = ((data[2 * index] & 0xFF) >>> 4); // to populate with zeros and get the bits we want
        int bigEnd = data[(2 * index) + 1];
        bigEnd <<= 8;
        return (short)(bigEnd | littleEnd);
    }

    public void setSample(int index, int value)
    {
        // Sets the data to respective indices in Little Endian
        if (value <= Short.MAX_VALUE && value >= Short.MIN_VALUE) {
            data[index * 2] = (byte) (value & 0xff);
            data[index * 2 + 1] = (byte) (value >>> 8);
        }
        else {
            System.err.println("Invalid.");
            System.exit(1);
        }
    }

    public byte[] getData()
    {
        // returns our array
        return Arrays.copyOf(data, data.length);
    }

    public int getDataSize()
    {
        return data.length;
    }

}
