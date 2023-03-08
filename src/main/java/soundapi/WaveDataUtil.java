package soundapi;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class WaveDataUtil {
    public File saveToFile(String name, AudioFileFormat.Type fileType, AudioInputStream audioInputStream) {
        System.out.println("Saving...");
        if (null == name || null == fileType || audioInputStream == null) {
            return null;
        }
        File myFile = new File(name + "." + fileType.getExtension());
        try {
            audioInputStream.reset();
        } catch (Exception e) {
            return null;
        }
        int i = 0;
        while (myFile.exists()) {
            String temp = "" + i + myFile.getName();
            myFile = new File(temp);
        }
        try {
            AudioSystem.write(audioInputStream, fileType, myFile);
        } catch (Exception ex) {
            return null;
        }
        System.out.println("Saved " + myFile.getAbsolutePath());
        return myFile;
    }
    public String saveToTempFile(String name, AudioFileFormat.Type fileType, AudioInputStream audioInputStream) throws IOException {
        System.out.println("Saving...");
        if (null == name || null == fileType || audioInputStream == null) {
            return null;
        }
//        File myFile = new File(name + "." + fileType.getExtension());
        File myFile = File.createTempFile(name, "."+fileType.getExtension());

        try {
            audioInputStream.reset();
        } catch (Exception e) {
            return null;
        }
        int i = 0;
        while (myFile.exists()) {
            String temp = "" + i + myFile.getName();
            myFile = new File(temp);
        }
        try {
            AudioSystem.write(audioInputStream, fileType, myFile);
        } catch (Exception ex) {
            return null;
        }
        System.out.println("Saved " + myFile.getAbsolutePath());
        System.out.println(myFile.getName());
        return myFile.getName();
    }
}