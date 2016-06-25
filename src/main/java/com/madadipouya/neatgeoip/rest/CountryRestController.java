package com.madadipouya.neatgeoip.rest;

import com.madadipouya.neatgeoip.Application;
import com.madadipouya.neatgeoip.pojo.Country;
import com.madadipouya.neatgeoip.service.MaxMindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Lists;
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

@RestController
public class CountryRestController {

    @Autowired
    private MaxMindService maxMindService;

    @RequestMapping("/getCountry")
    public Country getCountry(@RequestParam(value="ip") String ip) {
        try {
            if(isEmpty(ip)) {
                return Country.class.newInstance().setErrors(Lists.newArrayList("No IP Provided!"));
            }
            return maxMindService.findCountryFor(ip);
        } catch(Exception ex) {
            Application.log.error(ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
}
