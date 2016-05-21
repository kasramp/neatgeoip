package com.madadipouya.neatgeoip.util.config;

import com.madadipouya.neatgeoip.Application;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/*
* This file is part of NeatGeoIP.
*
* NeatGeoIP is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License version 3
* as published by the Free Software Foundation.
*
* NeatGeoIP is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.  <http://www.gnu.org/licenses/>
*
* Author(s):
*
* © 2016 Kasra Madadipouya <kasra@madadipouya.com>
*/

@Component
public class LoadDbFile {

    private static final String DEFAULT_FILE_NAME = "GeoLite2-City.mmdb";

    private static final String OPENSHIFT_REPO_DIR = System.getenv("OPENSHIFT_REPO_DIR");

    private static final String OPENSHIFT_DATA_DIR = System.getenv("OPENSHIFT_DATA_DIR");

    private static final String COMPRESSED_FILE_NAME = "GeoLite2-City.mmdb.gz";

    private static String DEFAULT_FILE_PATH_NAME;

    private static  final String URL_STR =
            "http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.mmdb.gz";

    public DatabaseReader getFile() {
        return getFile(DEFAULT_FILE_PATH_NAME);
    }

    public DatabaseReader getFile(String fileName) {
        try {
            return new DatabaseReader.Builder(new File(fileName))
                    .withCache(new CHMCache()).build(); // Load the file in memory
        } catch (Exception ex) {
            ex.printStackTrace();
            Application.log.error(ex.getMessage());
        }
        return null;
    }

    @Autowired
    private void setDefaultFilePathName() {

        String fullPath = "";

        if (!isEmpty(OPENSHIFT_DATA_DIR)) {

            fullPath = OPENSHIFT_DATA_DIR + DEFAULT_FILE_NAME;
            File file = new File(fullPath);
            if (file.exists() && !file.isDirectory()) {
                DEFAULT_FILE_PATH_NAME = fullPath;
            } else { // Try to pull from net
                if (pullDatabaseFromNet(OPENSHIFT_DATA_DIR)) {
                    DEFAULT_FILE_PATH_NAME = fullPath;
                } else {
                    // Read from local repo
                    DEFAULT_FILE_PATH_NAME = OPENSHIFT_REPO_DIR + "db/" + DEFAULT_FILE_NAME;
                }
            }

        } else { // Normal environment
            String pathWithoutFileName = System.getProperty("user.dir") + "/db/";
            fullPath =  pathWithoutFileName + DEFAULT_FILE_NAME;
            File file = new File(fullPath);
            if (file.exists() && !file.isDirectory()) {
                DEFAULT_FILE_PATH_NAME = fullPath;
            } else {
                if (pullDatabaseFromNet(pathWithoutFileName)) {
                    DEFAULT_FILE_PATH_NAME = fullPath;
                }
            }
        }
    }

    private boolean pullDatabaseFromNet(String path) {
        try {
            FileUtils.copyURLToFile(new URL(URL_STR),
                    new File(path + COMPRESSED_FILE_NAME));
            return  decompressFile(path);
        } catch (Exception ex) {
            ex.printStackTrace();
            Application.log.error(ex.getMessage());
        }
        return false;
    }

    private boolean decompressFile(String path) {

        byte[] buffer = new byte[1024];
        int len;

        try {
            GZIPInputStream gzis =
                    new GZIPInputStream(new FileInputStream(path + COMPRESSED_FILE_NAME));
            FileOutputStream out =
                    new FileOutputStream(path + DEFAULT_FILE_NAME, false);
            while ((len = gzis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            gzis.close();
            out.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            Application.log.error(ex.getMessage());
        }
        return false;
    }
}
