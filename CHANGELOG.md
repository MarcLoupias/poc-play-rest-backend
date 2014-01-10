
poc-play-rest-backend changelog
===============================

1.2-SNAPSHOT
------------
Added 2 models class, County and Cinema. County is the French counties list. Cinema is the French cinemas list.
Counties list is [from here] (http://sql.sh/1879-base-donnees-departements-francais)
Cinemas list is [from there] (http://www.data.gouv.fr/fr/dataset/liste-des-etablissements-cinematographiques-actifs)
Theses data have been refined with a spreadsheet manually and eventually a perl script to match my usage.

So the app provide now basic crud web-services for theses models.

Data are integrated into db with evolutions scripts.

1.1-SNAPSHOT
------------
* http admin creation service return value fixed
* services credentials changed from /public url convention to Play! action composition system
* rest-client favorite requests file indented
* Some cleaning in build.xml
* Each incoming request is now logged in the application.log
* Removing junit report files

1.0-SNAPSHOT
------------
First working version