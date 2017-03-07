package me.autocep.input;

import me.autocep.models.TimeSequence;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Raef M on 2/7/2017.
 */
public class Input {

    public static List<TimeSequence> readSequences(File[] f) {
        List<TimeSequence> sequences = new ArrayList<>();

        File[] files = f;
        for (File aFile : files) {
            sequences.add(transformJsonString(aFile));
        }
        return sequences;
    }

    private static TimeSequence transformJsonString(File file) {
        ObjectMapper mapper = new ObjectMapper();

        TimeSequence sequence = null;

        try {
            sequence = mapper.readValue(file, TimeSequence.class);
        } catch (IOException e) {
        }

        return sequence;
    }

    private static String readJSON(File file) {
        String text = null;
        try {
            text = new String(Files.readAllBytes(Paths.get(file.getPath())), StandardCharsets.UTF_8);
        } catch (IOException e) {
        }
        return text;
    }

    private static List<File> getListOfFiles() {
        List<File> files = new ArrayList<>();
        files.add(new File("3217716911408dummy.json"));
        return files;
    }

    public static String lastLineInFile(File eventSource) {
        RandomAccessFile fileHandler = null;
        try {
            fileHandler = new RandomAccessFile(eventSource, "r");
            long fileLength = fileHandler.length() - 1;
            StringBuilder sb = new StringBuilder();

            for (long filePointer = fileLength; filePointer != -1; filePointer--) {
                fileHandler.seek(filePointer);
                int readByte = fileHandler.readByte();

                if (readByte == 0xA) {
                    if (filePointer == fileLength) {
                        continue;
                    }
                    break;

                } else if (readByte == 0xD) {
                    if (filePointer == fileLength - 1) {
                        continue;
                    }
                    break;
                }

                sb.append((char) readByte);
            }

            String lastLine = sb.reverse().toString();
            return lastLine;
        } catch (java.io.FileNotFoundException e) {
            Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, e);

            return null;
        } catch (java.io.IOException e) {
            Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, e);

            return null;
        } finally {
            if (fileHandler != null) {
                try {
                    fileHandler.close();
                } catch (IOException e) {
                    /* ignore */
                }
            }
        }
    }

    public static int countLines(File file) {
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(file))) {
            lnr.skip(Long.MAX_VALUE);
            return lnr.getLineNumber();
        } catch (IOException ex) {
            Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

}
