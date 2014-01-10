#!/bin/bash

# this is a sample script to deploy current local git repo to heroku.
# i use ClearDB (MySQL) and SendGrid (smtp service) add-ons
# This script assume that theses add-ons are already up for your app.

# db username value
heroku config:set PPRB_DB_USER=<db_username_value>

# db password value
heroku config:set PPRB_DB_PWD=<db_password_value>

# email recipient for technicals emails sent by the app (onStart and onError Play events)
heroku config:set PPRB_TECH_EMAIL=<email_address>

git push heroku master