#!/bin/bash

# this is a sample script to deploy current local git repo to heroku.
# i use ClearDB (MySQL) and SendGrid (smtp service) add-ons
# This script assume that theses add-ons are already up for your app.

# db config
# ~~~~~~~~~
heroku config:set PPRB_DB_USER=<db_username_value>
heroku config:set PPRB_DB_PWD=<db_password_value>

# CORS config
# ~~~~~~~~~~~
heroku config:set PPRB_CORS_ACCESS_CONTROL_ALLOW_ORIGIN=<YOUR_FRONTEND_URL>

# mailer config
# ~~~~~~~~~~~~~
USERNAME=heroku config:get <heroku_mailer_provider_username_env_var>
PWD=heroku config:get <heroku_mailer_provider_pwd_env_var>

heroku config:set PPRB_MAILER_SMTP_URL=<mailer_provider_url>
heroku config:set PPRB_MAILER_SMTP_USERNAME=$USERNAME
heroku config:set PPRB_MAILER_SMTP_PWD=$PWD

# noreply email
# ~~~~~~~~~~~~~
heroku config:set PPRB_NOREPLY_EMAIL=<noreply_email_address>

# email recipient for technicals emails sent by the app (onStart and onError Play events)
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
heroku config:set PPRB_TECH_EMAIL=<email_address>


# deploy
# ~~~~~~
git push heroku master