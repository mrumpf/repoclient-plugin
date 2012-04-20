#!/bin/bash

mvn clean install
cp target/repoclient.hpi  ../../../jenkins-test-server/work/plugins/
