//
//import com.example.synthesizer.AudioComponent;
//
//import javax.sound.sampled.AudioFormat;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.Clip;
//import javax.sound.sampled.LineUnavailableException;
//
//
//// Will put non-GUI testing code here
//public class Main
//{
//    public static void main (String[] args) throws LineUnavailableException {
//        // Get properties from the system about samples rates, etc.
//        // AudioSystem is a class from the Java standard library.
//        Clip c = AudioSystem.getClip(); // Note, this is different from our AudioClip class.
//
//        // This is the format that we're following, 44.1 KHz mono audio, 16 bits per sample.
//        AudioFormat format16 = new AudioFormat( 44100, 16, 1, true, false );
//
//        AudioComponent mixer = new Mixer(); // My code
//        AudioComponent filter = new Filter(0.25); // My code
//        AudioComponent filter2 = new Filter(0.25); // My code
//        AudioComponent gen = new SineWave(440); // My code
//        AudioComponent gen2 = new SineWave(320); // My code
//
//        filter.connectInput(gen);
//        filter2.connectInput(gen2);
//
//        mixer.connectInput(filter);
//        mixer.connectInput(filter2);
//
//        AudioClip mixClip = mixer.getClip();
//
//        AudioComponent linearRamp = new LinearRamp(50, 15000);
//        AudioComponent vfSineWave = new VFSineWave();
//        vfSineWave.connectInput(linearRamp);
//
//        AudioClip linearVFSClip = vfSineWave.getClip();
//
//        c.open( format16, linearVFSClip.getData(), 0, linearVFSClip.getData().length ); // Reads data from our byte array to play it.
//
//        System.out.println( "About to play..." );
//        c.start(); // Plays it.
//        c.loop( 2 ); // Plays it 2 more times if desired, so 6 seconds total
//
//        // Makes sure the program doesn't quit before the sound plays.
//        while( c.getFramePosition() < AudioClip.TOTAL_SAMPLES || c.isActive() || c.isRunning() ){
//            // Do nothing while we wait for the note to play.
//        }
//
//        System.out.println( "Done." );
//        c.close();
//    }
//}
