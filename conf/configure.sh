#!/bin/bash

# add the section below to your .bashrc to run manually your app in Play
# or execute this script with ". configure.sh" shell command (and NOT "./configure.sh").
# before launching Play console.

export PPRB_DB_URL="jdbc:mysql://localhost/myweb"
export PPRB_DB_USER="myweb_db_user"
export PPRB_DB_PWD="password"

export PPRB_CORS_ACCESS_CONTROL_ALLOW_ORIGIN="http://127.0.0.1:9000"

export PPRB_TECH_EMAIL="junk@marc-loupias.fr"
export PPRB_NOREPLY_EMAIL="noreply@poc-play-rest-backed.org";
export PPRB_MAILER_SMTP_URL="uselessInDevMode";
export PPRB_MAILER_SMTP_USERNAME="uselessInDevMode";
export PPRB_MAILER_SMTP_PWD="uselessInDevMode";

# this var is for dev purpose
# this is to see loader directive in action on frontend ...
#export DEV_QUERY_PROCESSING_DELAY=4000
