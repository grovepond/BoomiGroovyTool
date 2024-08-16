package com.boomi.execution;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class ExecutionUtil {

    String _containerId;
    String _direcory;
    Map<String, String> _executionProperties;
    Properties _processProrties;
    Properties _dynamicProcessProperties;

    public ExecutionUtil () {
        _processProrties = new Properties();
        _dynamicProcessProperties = new Properties();
        _executionProperties = new HashMap<>();

    }
    public  String getContainerId() { return _containerId; }

    public  String getDirectory() { return _direcory; }

    public  String getExecutionProperty(String key) { return _executionProperties.get(key); }

    public  void setExecutionProperty(String key, String value) {
        _executionProperties.put(key, value);
    }
    public  void setProcessProperty(String ComponentID, String PropertyKey, String PropertyValue) {
        _processProrties.put(PropertyKey, PropertyValue);
    }

    public  String getProcessProperty(String componentId, String propertyKey) {
        Object value = _processProrties.get(propertyKey);
        return Objects.requireNonNullElse(value != null ? value.toString() : null, "");
    }

    public  String getDynamicProcessProperty(String propertyName) {
        Object value = _dynamicProcessProperties.get(propertyName);
        return Objects.requireNonNullElse(value != null ? value.toString() : null, "");
    }

    public  void setDynamicProcessProperty(String propertyName, String propertyValue, Boolean persist) {
        _dynamicProcessProperties.put(propertyName, propertyValue);
    }

    public  Logger getBaseLogger() {
        return new Logger();
    }
}
