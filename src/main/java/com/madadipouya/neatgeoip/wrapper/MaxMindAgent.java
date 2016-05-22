package com.madadipouya.neatgeoip.wrapper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.madadipouya.neatgeoip.Application;
import com.madadipouya.neatgeoip.pojo.Country;
import com.madadipouya.neatgeoip.util.config.LoadDbFile;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
@Data
public class MaxMindAgent {


    @Autowired
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private LoadDbFile loadDbFile;

    @Autowired
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private Country country;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private DatabaseReader reader;

    @Autowired
    private void loadDatabaseFile() {
        setReader(loadDbFile.getFile());
    }

    public Country getResult(String ip) {
        return resultMapper(lookup(ip));
    }

    private CityResponse lookup(String ip) {
        try {
            return getReader().city(InetAddress.getByName(ip));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeoIp2Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Country resultMapper(CityResponse response) {
        try {
            if(StringUtils.isEmpty(response.getCountry().getName())) {
                return noMatchingCountry();
            }
            country.setName(response.getCountry().getName())
                    .setIsoCode(response.getCountry().getIsoCode())
                    .setNames(response.getCountry().getNames());
        } catch(Exception ex) {
            noMatchingCountry();
        }
        return country;
    }

    private Country noMatchingCountry() {
        return country.setName("").setIsoCode("")
                .setNames(ImmutableMap.<String, String>builder().build())
                .setErrors(ImmutableList.<String>builder().add("Country not found!").build());
    }
}
