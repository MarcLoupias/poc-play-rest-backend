
poc-play-rest-backend changelog
===============================

0.0.1
-----
* Versioning convention changes. Following now [SemVer] (http://semver.org/) so started from scratch at 0.0.1.
* Licence moves from MIT to the better suited [WTFPL] (http://www.wtfpl.net/faq/).
* [CORS] (https://developer.mozilla.org/en-US/docs/HTTP/Access_control_CORS) support added. Mandatory due to frontend served on a different Heroku app.
* `Product` and `Category` models removed.
* Filter and pagination support added on query/get WS.
* Logout WS added.
* User infos WS added.
* A demo-user is now created on application start up.
* Error general management refactored.
* Log general management refactored.
* WS results management refactored.
* Some environment variable configuration changes. See `conf/application.conf`, `conf/configure.sh` and `scripts/heroku-deploy-sample.sh`.
* `ExceptionUtilsService` replaced with `ExceptionUtils` from `commons-lang3` lib from `org.apache.commons`.

1.3-SNAPSHOT
------------
* Added [Google Guice DI lib] (http://code.google.com/p/google-guice/).
* The whole application have been refactored accordingly.
* Heroku deployment.

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