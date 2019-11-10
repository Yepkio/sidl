################################################################
# Setup the environment for compiling and running the demos on
# Unix like platforms. 
#
# Required setup
#
#	* The directory holding java and javac must be in $PATH
#	* JPL must be installed
#	* Prolog must be available as one of "swi-prolog", "swipl"
#	  or "pl" in $PATH
#
################################################################

findexe()
{ oldifs="$IFS"
  IFS=:
  for d in $PATH; do
    if [ -x $d/$1 ]; then
       IFS="$oldifs"
       return 0
    fi
  done
  IFS="$oldifs"
  return 1
}

for f in swi-prolog swipl pl; do
  if [ -z "$PL" ]; then
     if findexe $f; then
        PL="$f"
     fi
  fi
done

if findexe java; then
  true
elif [ -x "$JAVA_HOME"/bin/java ]; then
  PATH="$PATH:$JAVA_HOME/bin"
else
  echo "ERROR: Cannot find java.  Please ensure JAVA_HOME is set"
  echo "ERROR: properly or java is in $PATH"
  exit 1
fi

if findexe javac; then
  true
else
  echo "ERROR: Cannot find javac.  This demo requires the SDK to"
  echo "ERROR: be installed and accessible through JAVA_HOME"
  echo "ERROR: or PATH"
  exit 1
fi

################################################################
# Setup the environment
################################################################

eval `$PL --dump-runtime-variables`

PLLIBDIR="$PLBASE/lib/$PLARCH"
if [ -z "$JPLJAR" ]; then
  if [ -f "$PLBASE/lib/jpl.jar" ]; then
    JPLJAR="$PLBASE/lib/jpl.jar"
  elif [ -f "$PLBASE/../packages/jpl/src/java/jpl.jar" ]; then
    JPLJAR="$PLBASE/../packages/jpl/src/java/jpl.jar"
  else
    echo "ERROR: Cannot find jpl.jar (PLBASE=$PLBASE)"
    exit 1
  fi
fi

if [ -z "$SIDLJAR" ]; then
  if [ ! -d "build" ]; then
    mkdir build
  fi
  SIDLJAR="build"
fi

JPL_LIBRARY_PATH="$PLLIBDIR"
if [ ! -f "$PLLIBDIR/libjpl.$PLSOEXT" ]; then
  if [ -f "$PLBASE/../packages/jpl/libjpl.$PLSOEXT" ]; then
    JPL_LIBRARY_PATH="$PLLIBDIR:$PLBASE/../packages/jpl"
  else
    echo "ERROR: Cannot find libjpl.$PLSOEXT"
    exit 1
  fi
fi

if [ -z "$LD_LIBRARY_PATH" ]; then
   LD_LIBRARY_PATH="$JPL_LIBRARY_PATH";
else
   LD_LIBRARY_PATH="$LD_LIBRARY_PATH:$JPL_LIBRARY_PATH"
fi

if [ -z "$CLASSPATH" ]; then
   CLASSPATH=".:$JPLJAR:$SIDLJAR";
else
   CLASSPATH=".:$JPLJAR:$SIDLJAR:$CLASSPATH"
fi

echo CLASSPATH=$CLASSPATH
echo LD_LIBRARY_PATH=$LD_LIBRARY_PATH
export LD_LIBRARY_PATH CLASSPATH

################################################################
# compile library
#
#
################################################################

compile()
{ 
  echo "Compiling $1"
  rm -rf build
  mkdir build
  javac -d build -classpath $JPLJAR -sourcepath javasrc/ javasrc/*/*/*.java
  cp javasrc/sidl/core/sidl.pl build/sidl/core/sidl.pl
  cp javasrc/sidl/utils/helpful.pl build/sidl/utils/helpful.pl
}

################################################################
# run Class
#
# Compiles Class if the .class file does not exsist and runs it
# Note that some systems (Linux, ...) find the libjpl.xxx from
# LD_LIBRARY_PATH.  MacOS finds this only when named libjpl.jnilib
# and using -Djava.library.path=<Path>.  We pass both, hoping to
# satisfy most systems ...
################################################################

run()
{ compile $1
  if [ "$JPL_COMPILE_ONLY" != "yes" ]; then
    echo ""
    echo "SIDL demo: $1"
    echo ""

    java -Djava.library.path=$JPL_LIBRARY_PATH $*
  fi
}


