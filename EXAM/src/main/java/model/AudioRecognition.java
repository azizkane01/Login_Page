package model;

import org.vosk.Model;
import org.vosk.Recognizer;
import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;



public  class AudioRecognition {
    private static final float SAMPLE_RATE = 16000.0f;
    private boolean micOn = false;
    private TargetDataLine microphone;
    private Thread recordingThread;
    private ByteArrayOutputStream out;
    ;

    public void startRecording() {
        micOn = true;
        try {
            AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();

            out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];

            recordingThread = new Thread(() -> {
                try {
                    while (micOn) {
                        int bytesRead = microphone.read(buffer, 0, buffer.length);
                        out.write(buffer, 0, bytesRead);
                    }
                    microphone.stop();
                    microphone.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            recordingThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] stopRecording() {
        micOn = false;
        try {
            if (recordingThread != null) {
                recordingThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public boolean isRecording() {
        return micOn;
    }


    /**
     * Transcrit les données audio fournies avec VOSK.
     *
     * @param audioData Données audio au format PCM mono 16-bit.
     * @return Texte transcrit.
     */
    public String transcribeAudio(byte[] audioData) {
        try {
            Model model = new Model("src/main/resources/vosk-model-small-fr-0.22");
            Recognizer recognizer = new Recognizer(model, SAMPLE_RATE);

            // Transcrire par morceaux
            byte[] buffer = new byte[4096];
            ByteArrayInputStream input = new ByteArrayInputStream(audioData);
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                recognizer.acceptWaveForm(buffer, bytesRead);
            }

            String result = recognizer.getResult();
            return result.split("\"text\"\\s*:\\s*\"")[1].split("\"")[0].trim();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}