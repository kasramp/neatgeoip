package com.madadipouya.neatgeoip.util.loader;

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
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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
public class DatabaseFileLoader {

    private enum Environment {
        OPENSHIFT, NON_OPENSHIFT
    }

    private static final String DATABASE_FILE_NAME = "GeoLite2-City.mmdb";

    private static final String DATABASE_FILE_NAME_COMPRESSED = "GeoLite2-City.mmdb.gz";

    private static final String OPENSHIFT_REPO_DIR = System.getenv("OPENSHIFT_REPO_DIR");

    private static final String OPENSHIFT_DATA_DIR = System.getenv("OPENSHIFT_DATA_DIR");

    private static  final String MAXMIND_DATABASE_URL =
            "http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.mmdb.gz";

    private String defaultDatabaseFilePath;

    public DatabaseReader loadDatabaseFileFromDisk() {
        return loadDatabaseFileFromDisk(defaultDatabaseFilePath);
    }

    public DatabaseReader loadDatabaseFileFromDisk(String fileName) {
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
    private void locateDatabaseFilePath() {

        if(Environment.OPENSHIFT.equals(getRunningEnvironmnet())) {
            setDatabaseFilePathForOpenShift();
        } else {
            setDatabaseFilePathForNonOpenShift();
        }
    }

    private Enum getRunningEnvironmnet() {
        if(isNotEmpty(OPENSHIFT_DATA_DIR)) {
            return Environment.OPENSHIFT;
        } else {
            return Environment.NON_OPENSHIFT;
        }
    }

    private void setDatabaseFilePathForOpenShift() {
        String fullPath = OPENSHIFT_DATA_DIR + DATABASE_FILE_NAME;
        File file = new File(fullPath);
        if (file.exists() && !file.isDirectory()) {
            defaultDatabaseFilePath = fullPath;
        } else { // Try to pull from the internet
            if (downloadAndExtractDatabaseFromInternetTo(OPENSHIFT_DATA_DIR)) {
                defaultDatabaseFilePath = fullPath;
            } else {
                // Read from local repo
                defaultDatabaseFilePath = OPENSHIFT_REPO_DIR + "db/" + DATABASE_FILE_NAME;
            }
        }
    }

    private void setDatabaseFilePathForNonOpenShift() {
        String userDirPath = System.getProperty("user.dir") + "/db/";
        String fullPath = userDirPath + DATABASE_FILE_NAME;
        File file = new File(fullPath);
        if (file.exists() && !file.isDirectory()) {
            defaultDatabaseFilePath = fullPath;
        } else {
            if (downloadAndExtractDatabaseFromInternetTo(userDirPath)) {
                defaultDatabaseFilePath = fullPath;
            }
        }
    }

    private boolean downloadAndExtractDatabaseFromInternetTo(String userDirPath) {
        try {
            new Downloader().download(MAXMIND_DATABASE_URL, userDirPath, DATABASE_FILE_NAME_COMPRESSED);
            new Archiver().decompress(userDirPath + DATABASE_FILE_NAME_COMPRESSED
                    , userDirPath + DATABASE_FILE_NAME);
            return true;
        } catch(IOException ex) {
            ex.printStackTrace();
            Application.log.error(ex.getMessage());
        }
        return false;
    }

    private static class Downloader {

        private boolean download(String uri, String saveTo, String fileName) throws IOException {
            try {
                FileUtils.copyURLToFile(new URL(uri),
                        new File(saveTo + fileName));
                return true;
            } catch (IOException ex) {
                ex.printStackTrace();
                Application.log.error(ex.getMessage());
                throw ex;
            }
        }
    }

    private static class Archiver {

        private boolean decompress(String source, String destination) throws IOException {
            byte[] buffer = new byte[1024];
            int len;
            try {
                GZIPInputStream gzis =
                        new GZIPInputStream(new FileInputStream(source));
                FileOutputStream out =
                        new FileOutputStream(destination, false);
                while ((len = gzis.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                gzis.close();
                out.close();
                return true;
            } catch (IOException ex) {
                ex.printStackTrace();
                Application.log.error(ex.getMessage());
                throw ex;
            }
        }
    }
}
