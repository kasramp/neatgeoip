package com.madadipouya.neatgeoip.pojo;

import com.google.api.client.util.Lists;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

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
public class Country {

    private String name;

    private String isoCode;

    private Map<String, String> names;

    private List<String> errors = Lists.newArrayList();

    public Country setName(String name) {
        this.name = name;
        return this;
    }

    public Country setIsoCode(String isoCode) {
        this.isoCode = isoCode;
        return this;
    }

    public Country setNames(Map<String, String> names) {
        this.names = names;
        return this;
    }

    public Country setErrors(List<String> errors) {
        this.errors = errors;
        return this;
    }

}
