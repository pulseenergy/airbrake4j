#!/bin/bash

mvn -Pit -DskipTests=true -DskipITs=false clean integration-test
