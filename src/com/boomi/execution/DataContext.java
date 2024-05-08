package com.boomi.execution;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Stream;

public class DataContext {

    private ArrayList<Properties> _dynamicDocumentProperties;
    private ArrayList<Properties> _documentProperties;
    private ArrayList<MyStream<InputStream, String>> _streams;
    private static final String _ddpPrefix = "document.dynamic.userdefined.";
    private String _outputFilePath;


    public DataContext(String outputFilePath) {
        _outputFilePath = outputFilePath;
        _dynamicDocumentProperties = new ArrayList<>();
        _documentProperties = new ArrayList<>();
        _streams = new ArrayList<>();

    }


    //Attach files to stream objects
    public void attachFiles(String path) {
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            try {
                paths
                        .filter(Files::isRegularFile)
                        .forEach(file -> createStream(file.toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        int numberOfProperties = _streams.size();
        for (int i = 0; i < numberOfProperties; i++) {
            Properties prop = new Properties();
            _dynamicDocumentProperties.add(i, prop);
            _documentProperties.add(i, prop);
        }
    }

    private void createStream(String path) {
        try {
            InputStream is = new FileInputStream(path);
            _streams.add(new MyStream<>(is, path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getDataCount() {
        return _streams.size();
    }

    public void storeStream(InputStream is, Properties props) {
        for (MyStream<InputStream, String> s : _streams) {
            if (s.getStream().equals(is)) {
                saveFile(s);
                return;
            }
        }
        return;
    }

    public InputStream getStream(int index) {
        return _streams.get(index).getStream();
    }

    public void addDynamicDocumentPropertyValues(int index, String key, String value) {
        Properties prop = _dynamicDocumentProperties.get(index);
        prop.put(_ddpPrefix + key, value);
    }

    public void addDocumentPropertyValues(int index, String key, String value) {
        Properties prop = _documentProperties.get(index);
        prop.put(key, value);
    }

    public Properties getProperties(int index) {
        return _dynamicDocumentProperties.get(index);
    }
    public void saveFile (MyStream ms) {
        InputStream is = (InputStream) ms.getStream();
        if (!_outputFilePath.endsWith("/"))
            _outputFilePath += "/";



        try (
             OutputStream os = new FileOutputStream((String) _outputFilePath + getRandomFileName())) {

            // Buffer for reading data from InputStream
            byte[] buffer = new byte[1024];
            int bytesRead;

            // Read from InputStream and write to file
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }
    private String getRandomFileName () {
        long min = 1000000000L;
        long max = 9999999999L;

        // Create a Random object
        Random rnd = new Random();

        // Generate a random number within the specified range
        long number = min + ((long) (rnd.nextDouble() * (max - min)));
        return String.valueOf(number) + ".dat";

    }
    class MyStream<InputStream, String> {
        private final InputStream stream;
        private final String path;

        public MyStream(InputStream stream, String path) {
            this.stream = stream;
            this.path = path;
        }

        public InputStream getStream() {
            return stream;
        }

        public String getPath() {
            return path;
        }
    }


}