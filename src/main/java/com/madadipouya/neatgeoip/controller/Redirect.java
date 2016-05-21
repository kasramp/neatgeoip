package com.madadipouya.neatgeoip.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

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
public class Redirect {

    private static final String DOC_URL = "://kasramp.github.io/neatgeoip/";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView redirectToDocPage(HttpServletRequest request) {
        return new ModelAndView("redirect:" + request.getScheme() + DOC_URL);
    }
}
