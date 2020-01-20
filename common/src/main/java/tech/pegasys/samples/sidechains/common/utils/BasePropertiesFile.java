/*
 * Copyright 2019 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package tech.pegasys.samples.sidechains.common.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Base class for all properties file classes.
 *
 * Converts between a Properties dictionary and its representation on disk.
 * Subclasses publish the keys as fields.
 */
public class BasePropertiesFile {
    // Name of properties file which holds information for this sample code.
    private static final Logger LOG = LogManager.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

    private final String samplePropertiesFileName;

    public Properties properties = new Properties();


    protected BasePropertiesFile(String name) {
        if ( !name.isEmpty()){
            name = "." + name;
        }
        this.samplePropertiesFileName = "sample" + name + ".properties";
    }


    public boolean propertiesFileExists() {
        return Files.exists(getSamplePropertiesPath());
    }


    public void deletePropertiesFile() throws IOException {
        Path path = getSamplePropertiesPath();
        Files.deleteIfExists(path);
    }


    public void loadProperties() {
        Path path = getSamplePropertiesPath();
        try {
            FileInputStream fis = new FileInputStream(path.toFile());
            properties.load(fis);
            LOG.info("Loaded properties from file {}", path.toString());
        } catch (IOException ioEx) {
            // By the time we have reached the loadProperties method, we should be sure the file
            // exists. As such, just throw an exception to stop.
            throw new RuntimeException(ioEx);
        }
    }

    public void storeProperties() {
        Path path = getSamplePropertiesPath();
        try {
            final FileOutputStream fos = new FileOutputStream(path.toFile());
            properties.store(fos, "Sample code properties file");
            fos.close();
            LOG.info("Stored properties to file {}", path.toString());
        } catch (IOException ioEx) {
            // By the time we have reached the loadProperties method, we should be sure the file
            // exists. As such, just throw an exception to stop.
            throw new RuntimeException(ioEx);
        }
    }

    private Path getSamplePropertiesPath() {
        return Paths.get(System.getProperty("user.dir"), this.samplePropertiesFileName);
    }
}
