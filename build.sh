#!/usr/bin/env bash

_print_usage() {
	cat 1>&2 <<EOF
Usage: $0 <command> <arguments>...
Where <command> is one of:
    libc: remove all library files
    libd: download libraries
    libs: download library sources
      (specify library list names for lib[ds] commands;
       if unspecified, will operate on all lists)
    gen: (re)generate the color table file
EOF
}

cd "$( dirname "$0" )"

if [[ "$#" == "0" ]]; then
	_print_usage
	exit 1
fi

cmd="$1"
shift
case "$cmd" in
	"libc")
		find lib -mindepth 1 -maxdepth 1 -type d | xargs rm -rfv
		;;
	"libd")
		source src/build/libraries.sh
		if [[ "$#" == "0" ]]; then
			libraryLists=( $( _collect_list_names ) )
		else
			libraryLists=( "$@" )
		fi
		download_libs "${libraryLists[@]}"
		;;
	"libs")
		source src/build/libraries.sh
		if [[ "$#" == "0" ]]; then
			libraryLists=( $( _collect_list_names ) )
		else
			libraryLists=( "$@" )
		fi
		download_sources "${libraryLists[@]}"
		;;
	"gen")
		./build.sh libd generator || exit 1
		source src/build/build.sh
		_compile generator || exit 1
		echo "Running generator..."
		java -cp "$( _lscp generator ):build/classes/generator" org.shoushitsu.webtech.css.color.generator.Generator \
			src/latest-spec.html build/colortable
		;;
	"jar")
		./build.sh gen || exit 1
		source src/build/build.sh
		_compile main || exit 1
		_assemble_jar
		;;
	"test")
		./build.sh jar || exit 1
		./build.sh libd test || exit 1
		source src/build/build.sh
		_compile test ":build/css-colors.jar" || exit 1
		echo "Running tests..."
		java -cp "$( _lscp test ):build/css-colors.jar:build/classes/test" org.testng.TestNG -d build/test-output src/testng.xml
		;;
	*)
		echo "Unknown command: $cmd" 1>&2
		_print_usage
		exit 1
		;;
esac
