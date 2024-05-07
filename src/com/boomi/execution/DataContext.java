package com.boomi.execution;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.stream.Stream;

public class DataContext {

    private ArrayList<Properties> _dynamicDocumentPropertiesIn;
    private ArrayList<Properties> _documentPropertiesIn;
    private ArrayList<InputStream> _streamsIn;
    private ArrayList<InputStream> _streamsOut;
    private ArrayList<Properties> _dynamicDocumentPropertiesOut;
    private ArrayList<Properties> _documentPropertiesOut;
    private static final String _ddpPrefix = "document.dynamic.userdefined.";

    public DataContext() {

        _dynamicDocumentPropertiesIn = new ArrayList<>();
        _documentPropertiesIn = new ArrayList<>();
        _dynamicDocumentPropertiesOut = new ArrayList<>();
        _documentPropertiesOut = new ArrayList<>();
        _streamsIn = new ArrayList<>();
        _streamsOut = new ArrayList<>();

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

        int numberOfProperties = _streamsIn.size();
        for (int i = 0; i < numberOfProperties; i++) {
            Properties prop = new Properties();
            _dynamicDocumentPropertiesIn.add(i, prop);
        }
    }

    private void createStream(String path) {
        try {
            InputStream is = new FileInputStream(path);
            _streamsIn.add(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getDataCount() {
        return _streamsIn.size();
    }

    public void storeStream(InputStream stream, Properties props) {
        _dynamicDocumentPropertiesOut.add(props);
        _streamsOut.add(stream);
    }

    public InputStream getStream(int index) {
        return _streamsIn.get(index);
    }

    public void addDynamicDocumentPropertyValues(int index, String key, String value) {
        Properties prop = _dynamicDocumentPropertiesIn.get(index);
        prop.put(_ddpPrefix + key, value);
    }

    public void addDocumentPropertyValues(int index, String key, String value) {
        Properties prop = _dynamicDocumentPropertiesIn.get(index);
        prop.put(_ddpPrefix + key, value);
    }

    public Properties getProperties(int index) {
        return _dynamicDocumentPropertiesIn.get(index);
    }
}