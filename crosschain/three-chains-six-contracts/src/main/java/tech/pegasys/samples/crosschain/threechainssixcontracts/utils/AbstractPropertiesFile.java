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
package tech.pegasys.samples.crosschain.threechainssixcontracts.utils;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Base class for all properties file classes.
 */
public class AbstractPropertiesFile {
    // Name of properties file which holds information for this sample code.
    private final String samplePropertiesFileName;

    protected Properties properties = new Properties();


    protected AbstractPropertiesFile(String name) {
        this.samplePropertiesFileName = "sample." + name + ".properties";
    }


    public boolean propertiesFileExists() {
        return Files.exists(getSamplePropertiesPath());
    }


    public void deletePropertiesFile() throws IOException {
        Path path = getSamplePropertiesPath();
        Files.deleteIfExists(path);
    }


    protected void loadProperties() {
        Path path = getSamplePropertiesPath();
        try {
            FileInputStream fis = new FileInputStream(path.toFile());
            this.properties.load(fis);
        } catch (IOException ioEx) {
            // By the time we have reached the loadProperties method, we should be sure the file
            // exists. As such, just throw an exception to stop.
            throw new RuntimeException(ioEx);
        }
    }

    protected void storeProperties() {
        Path path = getSamplePropertiesPath();
        try {
            final FileOutputStream fos = new FileOutputStream(path.toFile());
            properties.store(fos, "Sample code properties file");
            fos.close();
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
