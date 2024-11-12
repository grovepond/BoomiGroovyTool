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

    private final ArrayList<Properties> _dynamicDocumentProperties;
    private final ArrayList<Properties> _documentProperties;
    private final ArrayList<InputStream> _streams;
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
            _streams.add(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getDataCount() {
        return _streams.size();
    }

    public void storeStream(InputStream is, Properties props) {
        saveFile(is);
    }

    public InputStream getStream(int index) {
        return _streams.get(index);
    }

    public void addDynamicDocumentPropertyValues(String key, String value) {
        addDynamicDocumentPropertyValues(0, key, value);
    }

    public void addDynamicDocumentPropertyValues(int index, String key, String value) {
        Properties prop = _dynamicDocumentProperties.get(index);
        prop.put(_ddpPrefix + key, value);
    }

    public void addDocumentPropertyValues(String key, String value) {
        addDocumentPropertyValues(0, key, value);
    }

    public void addDocumentPropertyValues(int index, String key, String value) {
        Properties prop = _documentProperties.get(index);
        prop.put(key, value);
    }

    public Properties getProperties(int index) {
        return _dynamicDocumentProperties.get(index);
    }

    public void saveFile (InputStream is) {
        if (!_outputFilePath.endsWith("/"))
            _outputFilePath += "/";

        File outDir = new File(_outputFilePath);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            FileOutputStream os = new FileOutputStream(_outputFilePath + getRandomFileName());
            is.transferTo(os);
            os.flush();

        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

    private String getRandomFileName () {
        long min = 1000000000L;
        long max = 9999999999L;
        Random rnd = new Random();
        long number = min + ((long) (rnd.nextDouble() * (max - min)));
        return number + ".dat";
    }


}