#!/bin/bash
# Start BaseX
java -cp '/Users/thomasstuckey/Applications/myApps/baseX772/BaseX.jar' org.basex.BaseXServer &

# Run the loader application
#change to the directory from which this script is invoked before invoking the jar
cd "`dirname "$0"`"
java -jar FreeplaneToBaseX_app.jar initDB.xml 

# Stop BaseX
java -cp '/Users/thomasstuckey/Applications/myApps/baseX772/BaseX.jar' org.basex.BaseXServer stop
java -cp '/Users/thomasstuckey/Applications/myApps/baseX772/BaseX.jar' org.basex.BaseXGUI FreePlaneDB_tom &
