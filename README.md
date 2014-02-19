
poc-play-rest-backend
=====================


Current [Play!] (http://www.playframework.com/) version is 2.2.1 (nov 2013)

What is this ?
--------------
A Play! app to serve JSon REST services without serving any web page.
The app is configured for a MySQL db using JPA via Hibernate provider.
We assume that a MySQL db is running on localhost in dev mode.

For what purpose ?
------------------
The main goal is to provide a full dev environment, a skeleton for a REST server,
with some good practices for the most common problems encountered in that case.

I try to follow theses principles :
* [The 12 factors] (http://12factor.net/)
* [Heroku architecture advice] (https://devcenter.heroku.com/articles/architecting-apps)
* Service Oriented Architecture

What is done and you can read or pick ?
---------------------------------------
* [Play config for MySQL with JPA via Hibernate provider] (http://www.playframework.com/documentation/2.2.x/JavaJPA)
* [Play database evolution] (http://www.playframework.com/documentation/2.2.x/Evolutions)
* [Rest services crud demo] (http://www.restapitutorial.com/lessons/restfulresourcenaming.html)
* [Pojo validation via Play] (http://www.playframework.com/documentation/2.2.x/JavaForms)
* [Play testing] (http://www.playframework.com/documentation/2.2.x/JavaTest)
* [Play l18n] (http://www.playframework.com/documentation/2.2.x/JavaI18N)
* [Play dependencies management] (http://www.playframework.com/documentation/2.2.x/SBTDependencies)
* A home-made login system using [NIST recommendations] (http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf)
* [Play action composition example for controllers credentials] (http://www.playframework.com/documentation/2.2.1/JavaActionsComposition)
* [Dependency Injection] (http://en.wikipedia.org/wiki/Dependency_injection) with [Google Guice] (http://code.google.com/p/google-guice/)
* [CORS] (https://developer.mozilla.org/en-US/docs/HTTP/Access_control_CORS) support

Installation
------------
* You need a MySQL working installation. (Just create a new user for that app, check config, then run the app or the `db-reset` ant task.)
* You need to install Play! version 2.2.1+. (That config will not work with an inferior version due to framework last changes.)

Running the application locally and play with it
------------------------------------------------
1. Enter in your project root directory in the console.
2. Run the app with `play run` command. (`play "run 8080"` to run it on the 8080 port)
3. Go to http://localhost:9000/version with your favourite browser.

To manually run the app without a front-end, i use the [RESTClient plugin] (https://addons.mozilla.org/fr/firefox/addon/restclient/) for Firefox.
If you want install it, you can import some Rest requests from `rest-client/poc-play-rest-backend-requests.json` file.
In RESTClient menu, "Favorite requests" -> "Import favorite requests".

Configurations files
--------------------
* Check database configuration in `conf/application.conf`.
* Check `conf/configure.sh` to set up shell before launching Play! console.

IDE setup
---------
This project is set for IntelliJ by [mpeltonen sbt plugin] (https://github.com/mpeltonen/sbt-idea)
Open a play console then type `gen-idea`.
A valid IntelliJ project will be set up, at this point you can open project in IntelliJ.
Notices that each time you add a dependency in `build.sbt` file you need to execute `gen-idea` (no need to close IDEA).

Running tests
-------------
Unit tests uses [JUnit4] (http://junit.org/).
Integration tests uses [RestAssured] (http://code.google.com/p/rest-assured/).

* Tests can be run with the regular way through play console by typing `play test`.
* Tests can be run manually with IntelliJ.
* Tests can be run manually with IntelliJ via the `run-tests-fullstack` ant task
to generate a html report in `junit/reports/index.html`.

Ant tasks
---------
Tasks config are done on the top of `build.xml` file.
You will need to update properties value for db access and play directory.
See description of each tasks.

Version number management
-------------------------
We use [sbt-buildinfo plugin] (https://github.com/sbt/sbt-buildinfo).
Version number is set in `build.sbt` in the Play! regular way.
Open a play console then type `reload` to see changes.
We follow [SemVer] (http://semver.org/) convention.

Heroku Deployment
-----------------
* [heroku.com] (https://www.heroku.com/)
* We assume that you have installed the [Heroku Toolbelt] (https://toolbelt.heroku.com/).
* We assume that you have read the [related documentation] (https://devcenter.heroku.com/articles/play-support#play-2-2) on Heroku website.
* We assume that you know [how to run a Play application in production mode] (http://www.playframework.com/documentation/2.2.x/Production).
* I use [ClearDB] (https://addons.heroku.com/cleardb#ignite) and [SendGrid] (https://addons.heroku.com/sendgrid#starter) add-ons.

Related project files :
* `scripts/heroku-deploy-sample.sh` (to set up the env)
* `Procfile` (to configure the process (the dyno in heroku world) running the app)
* `system.properties` (to run the app under 1.7 JRE)

Some useful tips :
* Once ClearDB is up, examine the `CLEARDB_DATABASE_URL` environment variable to get the database name, username and password.
* Don't forget to examine the [Heroku dashboard] (https://dashboard.heroku.com/apps), each add-on have his own dashboard.
* [How to] (http://stackoverflow.com/questions/9822313/remote-connect-to-cleardb-heroku-database) connect to your MySQL db on Heroku with your SQL tools (like Workbench)

Some useful heroku shell commands (need to be in your source root directory) :
* `$ heroku create` to create a new app. Name is auto-generated and can be changed in the dashboard settings panel.
* `$ heroku ps` to list the running dynos.
* `$ heroku config` to display your app config.
* `$ git push heroku master` push your current git code to heroku. Heroku app are built from source code.
* `$ heroku ps:scale web=1` to run the app with only one dyno (free)
* `$ heroku logs > logs/heroku.log` to get logs from heroku in your play logs dir locally.

Areas of improvements
---------------------
* I would like to get comments on this, of any kind.
* I don't know how to run only unit tests without integration tests with Play! via Ant in a simple way.
* I don't know how to rollback the database after each integration test without deleting each record used manually.
* Transactions are functional but i have to work on [concurrency] (https://blogs.oracle.com/enterprisetechtips/entry/locking_and_concurrency_in_java).
* I need also to work on continuous integration.

