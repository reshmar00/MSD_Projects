#!/bin/sh
set -e
if test "$CONFIGURATION" = "Debug"; then :
  cd /Users/reshmaraghavan/Desktop/finalProject/appletreeGame/xcode
  make -f /Users/reshmaraghavan/Desktop/finalProject/appletreeGame/xcode/CMakeScripts/ReRunCMake.make
fi
if test "$CONFIGURATION" = "Release"; then :
  cd /Users/reshmaraghavan/Desktop/finalProject/appletreeGame/xcode
  make -f /Users/reshmaraghavan/Desktop/finalProject/appletreeGame/xcode/CMakeScripts/ReRunCMake.make
fi
if test "$CONFIGURATION" = "MinSizeRel"; then :
  cd /Users/reshmaraghavan/Desktop/finalProject/appletreeGame/xcode
  make -f /Users/reshmaraghavan/Desktop/finalProject/appletreeGame/xcode/CMakeScripts/ReRunCMake.make
fi
if test "$CONFIGURATION" = "RelWithDebInfo"; then :
  cd /Users/reshmaraghavan/Desktop/finalProject/appletreeGame/xcode
  make -f /Users/reshmaraghavan/Desktop/finalProject/appletreeGame/xcode/CMakeScripts/ReRunCMake.make
fi

