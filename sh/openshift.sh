# This file is part of NeatGeoIP.
#
# NeatGeoIP is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License version 3
# as published by the Free Software Foundation.
#
# NeatGeoIP is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.  <http://www.gnu.org/licenses/>
#
# Author(s):
#
# © 2016 Kasra Madadipouya <kasra@madadipouya.com>

#!/bin/bash
cd $OPENSHIFT_DATA_DIR
rm -rvf GeoLite2-City.mmdb GeoLite2-City.mmdb.gz
gear restart --cart diy-0.1

