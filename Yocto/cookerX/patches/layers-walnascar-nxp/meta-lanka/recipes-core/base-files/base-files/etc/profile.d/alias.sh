#!/bin/sh

alias ll="ls -alF"
alias la="ls -A"
alias l="ls -CF"
alias ls-type="ls  | xargs -n 1 file"
alias free-mem="free -hwt"

alias PATH-ROOT="PATH=/root:/usr/local/bin:/usr/bin:/bin:/usr/local/sbin:/usr/sbin:/sbin"

alias echo-ln="echo"

alias tree-1="tree -L 1"
alias tree-2="tree -L 2"
alias tree-3="tree -L 3"

#******************************************************************************
#** 20. Shell Scripts **
#******************************************************************************
function eval-it()
{
	DO_COMMAND="$*"
	[ ! -z "${ECHO_COMMAND}" ] && echo "[${DO_COMMAND}]"
	eval "${DO_COMMAND}"
}

function eval-do()
{
	eval-it "${DO_COMMAND}"
}


#******************************************************************************
#** 1. File Handler **
#******************************************************************************

#** 1.5. Compare (diff - compare files line by line) **
function diffX()
{
	HINT="Usage: ${FUNCNAME[0]} <file1> <file2>"
	FILE1=$1
	FILE2=$2

	if [ ! -z "${FILE1}" ] && [ ! -z "${FILE2}" ]; then
		DO_COMMAND="(diff -Naur ${FILE1} ${FILE2})"
		eval-it "$DO_COMMAND"
	else
		echo $HINT
	fi
}

#** 1.6. Finder (find - search for files in a directory hierarchy) **
export FIND_DIR="."
#export FIND_PRUNE_ARG="-name lost+found -prune -o"
export FIND_PRUNE_ARG="\( -name lost+found -o -name .svn -o -name .git -o -name .venv \) -prune -o"
export FIND_PRINT_ARG="-print"

function find-environment()
{
	echo "FIND_DIR=${FIND_DIR}"
	echo "FIND_PRUNE_ARG=${FIND_PRUNE_ARG}"
	echo "FIND_PRINT_ARG=${FIND_PRINT_ARG}"
}

function find-min()
{
	HINT="Usage: ${FUNCNAME[0]} <?min>"
	MMIN1=$1

	if [ ! -z "${MMIN1}" ]; then
		DO_COMMAND="(find ${FIND_DIR} ${FIND_PRUNE_ARG} -mmin -${MMIN1} ${FIND_PRINT_ARG};)"
		eval-it "$DO_COMMAND"
	else
		echo $HINT
	fi
}

function find-day()
{
	HINT="Usage: ${FUNCNAME[0]} <?day>"
	MTIME1=$1

	if [ ! -z "${MTIME1}" ]; then
		DO_COMMAND="(find ${FIND_DIR} ${FIND_PRUNE_ARG} -mtime -${MTIME1} ${FIND_PRINT_ARG};)"
		eval-it "$DO_COMMAND"
	else
		echo $HINT
	fi
}

function find-size()
{
	HINT="Usage: ${FUNCNAME[0]} <+?M>"
	SIZE1=$1

	if [ ! -z "${SIZE1}" ]; then
		DO_COMMAND="(find ${FIND_DIR} ${FIND_PRUNE_ARG} -type f -size ${SIZE1} ${FIND_PRINT_ARG};)"
		eval-it "$DO_COMMAND"
	else
		echo $HINT
	fi
}

function find-name()
{
	HINT="Usage: ${FUNCNAME[0]} <file>"
	FILE1="$*"

	if [ ! -z "${FILE1}" ]; then
		for ITEM in ${FILE1}; do
		(
			DO_COMMAND="(find ${FIND_DIR} ${FIND_PRUNE_ARG} -name ${ITEM} ${FIND_PRINT_ARG};)"
			eval-it "$DO_COMMAND"
		)
		done
	else
		echo $HINT
	fi
}

alias find123="find-name"

function find-bak()
{
	find-name *.bak
}

function find-bash_aliases()
{
	find-name .bash_aliases
}

function find-type()
{
	DO_COMMAND="(find ${FIND_DIR} ${FIND_PRUNE_ARG} -type f ${FIND_PRINT_ARG} | xargs -n 1 file;)"
	eval-it "$DO_COMMAND"
}

function find-dup()
{
	DO_COMMAND="(find ${FIND_DIR} ${FIND_PRUNE_ARG} -type f -printf '%p -> %f\n' | sort -k2 | uniq -f1 --all-repeated=separate)"
	eval-it "$DO_COMMAND"
}

function find-path()
{
	HINT="Usage: ${FUNCNAME[0]} <path> <file>"
	PATH1=$1
	FILE2=$2

	if [ ! -z "${PATH1}" ] && [ ! -z "${FILE2}" ]; then
		DO_COMMAND="(cd ${PATH1}; find ${FIND_DIR} ${FIND_PRUNE_ARG} -name ${FILE2} ${FIND_PRINT_ARG}; cd - >/dev/null)"
		eval-it "$DO_COMMAND"
	else
		echo $HINT
	fi
}

function find-locate()
{
	HINT="Usage: ${FUNCNAME[0]} <file>"
	FILE1=$1

	if [ ! -z "${FILE1}" ]; then
		DO_COMMAND="(locate ${FILE1} | grep `pwd`)"
		eval-it "$DO_COMMAND"
	else
		echo $HINT
	fi
}

#******************************************************************************
#** 4. String Handler **
#******************************************************************************
#** String Handler **
#alias gr="grep --exclude-dir='.svn' --exclude-dir='.git' -nrs"
function grep-helper()
{
	HINT="Usage: ${FUNCNAME[0]} <string>"
	STRING1="$*"

	if [ ! -z "${STRING1}" ]; then
		for ITEM in ${STRING1}; do
		(
			DO_COMMAND="(grep --exclude-dir='.svn' --exclude-dir='.git' -nrs '${ITEM}' *)"
			eval-it "$DO_COMMAND"
		)
		done
	else
		echo $HINT
	fi
}

alias gr123="grep-helper"
