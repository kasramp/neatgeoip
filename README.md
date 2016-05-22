# Neat Geo IP
Neat Geo IP is a simple & free IP to country API

##Description
Neat Geo IP is a simple Spring Boot Restful webservice which allows you to get the country of any given IP address. For IP address lookip [MaxMind](http://www.maxmind.com) database is used and it is automatically getting updated monthly via a bash script.
The project is hosted on Openshift free tier and is available for anyone to use.
Bear in mind that the service has 5-15 minutes down time every month due to automated database updating process.

##Dependencies
All the project dependencies are added in pom.xml file and once your run the project, all dependencies will be downloaded.

## How to use  
To run and deploy the project on your local or any desired server, first clone the project and the follow the below instruction

	$ maven clean install
	$ cd target  
	$ java -jar neat-geo-ip-1.0-SNAPSHOT.jar

##Project & API documentation
To know more about the project structure and API documentation please refer to our Github page [documentation](http://kasramp.github.io/neatgeoip/documentation.html) at this link.

## Contact
* kasra@madadipouya.com  
* kasra_mp@live.com  
	
## Disclaimer
The source of this project is published under GPLv3 excluding the database file which belongs to [MaxMind](http://www.maxmind.com) Copyright© 2012-2016 MaxMind, Inc. All Rights Reserved. For more information about terms and conditons refer to our Github page section "[License and terms & conditions](http://kasramp.github.io/neatgeoip/#license-and-terms--conditions)".

This product includes GeoLite2 data created by MaxMind, available from
<a href="http://www.maxmind.com">http://www.maxmind.com</a>.

##License
Neat Geo IP is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3
as published by the Free Software Foundation.

Neat Geo IP is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.  <http://www.gnu.org/licenses/>

Author(s):

© 2016 Kasra Madadipouya <kasra@madadipouya.com>
