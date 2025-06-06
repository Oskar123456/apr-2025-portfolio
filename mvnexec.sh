#!/bin/bash

#
# Execute clases with main method inside maven project.
#

# debug {{{
eval SCRIPT_DEBUG="\$$(basename $0 | tr - _)_DEBUG"
SCRIPT_DEBUG=${SCRIPT_DEBUG:--1}

if [ "$SCRIPT_DEBUG" -ge 1 ]
then
   set -x
fi
if [ "$SCRIPT_DEBUG" -ge 10 ]
then
   set -v
fi
#}}}

# init {{{
VERSION=1.1
SET_PARAMS="yes"
BUILD="yes"
CLEAN="yes"
COPY_DEP="yes"
SEARCH_MAIN="yes"
SEARCH_TEST="yes"
FILTER_NAME_PROG="cat -"
AUTO="no"
LIST="no"
SEPARATOR=":"
CONFIG=""
# }}}

# functions {{{
function help {
   local scriptname="$(basename $0)"
   cat >&2 <<EOF

$scriptname [options] [args]

version: $VERSION

Execute clases with main method inside maven project.

Options:
   -P, --no-parameters   Don't ask for parameters.
   -C, --no-clean        Don't clean compilation
   -q, --quiet           Be quiet. Don't show [INFO] messages.
   -B, --no-build        Avoid building. 
   -D, --no-copy-deps    Avoid copy dependencies
   -M, --no-main         Don't search on src/main/java.
   -T, --no-test         Don't search on src/test/java.
   -f, --filter-name     Name filter for classes.
   -a, --auto            If there is only one match launch it.
   -l, --list            Just output found classes with main method.
       --win-separator   Use ; as classpath separator
       --config          Additional maven attributes

Examples:

 # lauch doing all common task, but quietly
 $ $scriptname -q
 # launch without prompt for parameters
 $ $scriptname -P
 # launch without any compilation task
 $ $scriptname -CBDq

EOF
}

function errorJavaExecutable() {
   echo cannot locate java. Set JDK_HOME or JAVA_HOME or add java to path.
   exit 1
}

function testJavaExec() {
   test -x "$1/bin/java"
}

function setJavaExecutable() {
   if [ -z "$1" ] || ! testJavaExec "$1"
   then
      errorJavaExecutable
   else
      JAVA="$1/bin/java"
   fi
}

function findMainClasses() {
   if [ -d $1 ]
   then
      grep " main *(" -R "$1" --exclude-dir=.git --exclude-dir=.svn --exclude-dir=.cvs --include=*.java | \
         cut -d ':' -f 1 | $FILTER_NAME_PROG | sed 's+'$1'/++;s/.java$//' | tr / . | sort
   fi
}

function dependencyClasspath() {
   IFS=. read major minor extra < <($JAVA -version 2>&1 | awk -F '"' '/version/ {print $2}')
   if [ "$major" == 1 ] && [[ "$minor" > 5 ]]
   then
      echo 'target/dependency/*'
   else
      for i in target/dependency/*.jar
      do 
         echo -n $i$SEPARATOR
      done | sed 's/'${SEPARATOR}'$//'
   fi
}

function launch() {
   if [ "$CLEAN" == "yes" ]
   then
      echo cleaning...
      mvn $QUIET clean
   fi
   if [ "$BUILD" == "yes" ]
   then
      echo compiling...
      mvn $CONFIG $QUIET install
   fi
   if [ "$COPY_DEP" == "yes" ]
   then
      echo getting dependencies...
      mvn $QUIET dependency:copy-dependencies
   fi
   echo executing $CLASS
   mvn $QUIET exec:exec -Dexec.executable="$JAVA" -Dexec.workingdir="." \
      -Dexec.args="-cp target/classes${SEPARATOR}target/test-classes${SEPARATOR}$(dependencyClasspath) ${CLASS}${ARGS} $*"
}
# }}}

# parse args {{{
TEMP=$(getopt -o "PqBMTf:DaClh" -l no-parameters,quiet,no-build,no-main,no-test,filter-name:,auto,no-copy-deps,no-clean,win-separator,list,config:,help -n $(basename $0) -- "$@")

EXIT=$?
if [ $EXIT != 0 ]
then
   help
   exit $EXIT
fi

# process script arguments
eval set -- "$TEMP"

while true
do
   case "$1" in
      -P|--no-parameters)
         SET_PARAMS=
         ;;
      -B|--no-build) 
         BUILD="no" 
         ;;
      -C|--no-clean) 
         CLEAN="no" 
         ;;
      -D|--no-copy-deps) 
         COPY_DEP="no" 
         ;;
      -q|--quiet) 
         QUIET="-q" 
         ;;
      -M|--no-main)
         SEARCH_MAIN="no"
         ;;
      -T|--no-test)
         SEARCH_TEST="no"
         ;;
      -f|--filter-name)
         ARG=$2
         shift
         FILTER=$ARG
         FILTER_NAME_PROG="grep -i $FILTER"
         ;;
      -a|--auto)
         AUTO="yes"
         ;;
      -l|--list)
         LIST="yes"
         ;;
      --win-separator)
         SEPARATOR=";"
         ;;
      --config)
         CONFIG="$CONFIG $2"
         shift
         ;;
      -h|--help)
         help
         exit
         ;;
      --)
         shift
         break ;;
      *)
         cat <&2 <<EOF

Error, unknow arguments $1
EOF
         help
         exit 1
         ;;
   esac
   shift
done
# }}}

# exec {{{
if [ -n "$JDK_HOME" ]
then
   setJavaExecutable "$JDK_HOME"
elif [ -n "$JAVA_HOME" ]
then
   setJavaExecutable "$JAVA_HOME"
elif command -v java >&-
then
   JAVA=$(command -v java)
else
   errorJavaExecutable
fi


if [ $SEARCH_MAIN == "yes" ]
then
   FOUNDCLASSES=$(findMainClasses src/main/java)
fi

if [ $SEARCH_TEST == "yes" ]
then
   TESTCLASSES=$(findMainClasses src/test/java)
   if [ -n "$TESTCLASSES" ]
   then
      if [ -n "$FOUNDCLASSES" ]
      then
         FOUNDCLASSES="$FOUNDCLASSES
$TESTCLASSES"
      else
         FOUNDCLASSES=$TESTCLASSES
      fi
   fi
fi

if [ -z "$FOUNDCLASSES" ]
then
   echo
   echo no classes with main method in $PWD
   if [ -n "$FILTER" ]
   then
      echo using filter: $FILTER
   fi
   echo
   exit 1
fi

if [ $LIST == "yes" ]
then
   echo $FOUNDCLASSES
   exit 0
fi

if [ $AUTO == "yes" ] && ! [[ "$FOUNDCLASSES" == *$'\n'* ]]
then
   CLASS=$FOUNDCLASSES
   launch $*
   exit
fi

select CLASS in ${FOUNDCLASSES}
do
   if [ -z "$CLASS" ]
   then
      if [ "$REPLY" == "salir" ]
      then
         break
      else
         echo unknow option
         continue
      fi
   fi
   if [ -n "$SET_PARAMS" ]
   then
      echo set parameters:
      read -i "$*" -e ARGS
      if [ -n "$ARGS" ]
      then 
         ARGS=" $ARGS"
      fi
   fi
   launch
   break
done
# }}}
# vim: fdm=marker

