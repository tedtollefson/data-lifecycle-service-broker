#!/bin/sh
APPNAME=${APPNAME:-`grep name manifest.yml | awk '{print $3}'`}
cf set-env $APPNAME AWS_ACCESS_KEY_ID $AWS_ACCESS_KEY_ID
cf set-env $APPNAME AWS_SECRET_ACCESS_KEY $AWS_SECRET_ACCESS_KEY
cf set-env $APPNAME PROD_DB_PASSWORD $PROD_DB_PASSWORD
cf set-env $APPNAME PROD_DB_USER $PROD_DB_USER
cf set-env $APPNAME PROD_DB_URI $PROD_DB_URI
cf set-env $APPNAME SECURITY_USER_NAME $SECURITY_USER_NAME
cf set-env $APPNAME SECURITY_USER_PASSWORD $SECURITY_USER_PASSWORD
cf set-env $APPNAME SOURCE_INSTANCE_ID $SOURCE_INSTANCE_ID
cf set-env $APPNAME SUBNET_ID $SUBNET_ID
