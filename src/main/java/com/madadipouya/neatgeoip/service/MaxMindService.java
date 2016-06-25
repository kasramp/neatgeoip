package com.madadipouya.neatgeoip.service;

import com.madadipouya.neatgeoip.pojo.Country;

/**
 * Created by Kasra Madadipouya (madadipouya.com) on 6/25/16.
 */
public interface MaxMindService {
    Country findCountryFor(String ip);
}
