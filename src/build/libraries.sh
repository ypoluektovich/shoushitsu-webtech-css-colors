#!/usr/bin/env bash

_collect_list_names() {
	local args=( $( find lib -name '*.txt' -printf '%f\n' ) )
	echo "${args[@]%.txt}"
}

_expand_list_names() {
	local prefix="lib/"
	local suffix=".txt"
	local params=("$@")
	params=("${params[@]/%/${suffix}}")
	params=("${params[@]/#/${prefix}}")
	echo "${params[@]}"
}

_download() {
	local destDir="$1"
	local classifier="${2:+-}$2"
	shift 2

	local gr
	local nm
	local vr
	while (( "$#" )); do
		echo "Downloading libraries from list $1..."
		while read gr nm vr; do
			local gdir="${destDir}/${gr}"
			local fnam="${nm}-${vr}${classifier}.jar"
			if [[ -e "${gdir}/${fnam}" ]]; then
				echo "${gdir}/${fnam} already exists" 1>&2
			else
				mkdir -p "${gdir}"
				wget "http://repo1.maven.org/maven2/$( echo "$gr" | tr . / )/${nm}/${vr}/${fnam}" \
					-O "${gdir}/${fnam}" -nv
			fi
		done < $1
		shift
	done
}

download_libs() {
	_download "lib" "" $( _expand_list_names "$@" )
}

download_sources() {
	_download "lib" "sources" $( _expand_list_names "$@" )
}
