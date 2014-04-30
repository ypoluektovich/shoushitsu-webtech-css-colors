#!/usr/bin/env bash

_lscp() {
	local cpLine=""
	local gr
	local nm
	local vr
	while read gr nm vr; do
		cpLine="${cpLine}:lib/${gr}/${nm}-${vr}.jar"
	done < "lib/$1.txt"
	echo "${cpLine#:}"
}

_compile() {
	local srcset="$1"
	local addClasspath="$2"
	shift 2
	local classesDir="build/classes/${srcset}"
	echo "Cleaning ${classesDir}..."
	rm "${classesDir}" -rf || exit 1
	mkdir -p "$classesDir" || exit 1
	echo "Compiling ${srcset} into ${classesDir}..."
	if [[ -e "lib/${srcset}.txt" ]]; then
		local cpArgs=( "-cp" "$( _lscp "$srcset" )${addClasspath}" )
	else
		local cpArgs=()
	fi
	javac -d "$classesDir" -source "1.7" "${cpArgs[@]}" @<( find "src/${srcset}" -name '*.java' )
}

_assemble_jar() {
	echo "Copying generated files..."
	cp build/colortable build/classes/main/org/shoushitsu/webtech/css/color/colortable || exit 1
	echo "Creating jar..."
	jar cMf build/css-colors.jar -C build/classes/main org || exit 1
	echo "Created jar"
}
