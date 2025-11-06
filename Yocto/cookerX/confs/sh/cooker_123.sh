#!/bin/sh

HINT="$0 {all|cook|build|bundle|dry-run|generate|shell|clean|distclean|update|init|ls|pull|lnk}"
HINT+="\nExample:"
HINT+="\n\t init + update + generate + cook, check: $0 all"
HINT+="\n\t init + update + generate + cook: $0 cook"
HINT+="\n\t build: $0 build"
PWD=`pwd`

ACTION=$1

RUN_SH=`basename $0`

[ "$PJ_YOCTO_MACHINE" != "" ] || export PJ_YOCTO_MACHINE="raspberrypi3"
[ "$PJ_YOCTO_BUILD" != "" ] || export PJ_YOCTO_BUILD="pi3-master-2b733d5"

BUILD_START_STRING=`date +%Y%m%d%H%M%S`
BUILD_END_STRING=`date +%Y%m%d%H%M%S`

[ -d builds ] || (mkdir -p builds;)
LOG="$PWD/builds/build_log_$BUILD_START_STRING.txt"
LOG_ARG=">> $LOG"
TEE_ARG=""
[ "$LOG" != "" ] && TEE_ARG="| tee -a $LOG"

INTERACTIVE=""

COOKER_MENU="$PJ_COOKER_MENU_FOLDER/$PJ_COOKER_MENU"
COOKER_VERBOSE="-v"
#COOKER_DRY="-n"

BUILD_SCRIPT="build-script.sh"

datetime_fn()
{
	PROMPT=$1
	#NOW_t=`date +"%Y%m%d %T"`
	NOW_t=`date +"%Y%m%d %H%M%S"`
	#DO_COMMAND="echo \"$NOW_t - $PROMPT\" $TEE_ARG"
	sh -c "echo \"$NOW_t - $PROMPT\" $TEE_ARG"

	return 0
}

die_fn()
{
	#echo $@
	[ "$@" = "$HINT" ] && echo -e "$@" && exit 1
	datetime_fn "$@"
	exit 1
}

do_command_fn()
{
	DO_COMMAND=$1
	datetime_fn "[$DO_COMMAND]"
	sh -c "$DO_COMMAND"
}

ls_fn()
{
	datetime_fn "${FUNCNAME[0]} ... "
	do_command_fn "ls -al $PJ_YOCTO_BUILD_DIR/tmp/deploy/images/$PJ_YOCTO_MACHINE"
}

pull_fn()
{
	datetime_fn "${FUNCNAME[0]} ... "
	do_command_fn "(cd $PJ_COOKER_MENU_DIR; git pull;)"
}

# make cook-lnk
lnk_fn()
{
	datetime_fn "${FUNCNAME[0]} ... "

	mkdir -p $PJ_YOCTO_ROOT/builds-lnk
	if [ -d $PJ_YOCTO_ROOT/builds-lnk ]; then
		do_command_fn "(cd $PJ_YOCTO_ROOT/builds-lnk; rm -f *;)"
		do_command_fn "(cd $PJ_YOCTO_ROOT/builds-lnk; ln -s $PJ_YOCTO_BUILD_DIR/tmp/work/$PJ_YOCTO_LINUX/$PJ_YOCTO_IMAGE/*/rootfs $PJ_YOCTO_BUILD-rootfs;)"
		[ -d $PJ_YOCTO_BUILD_DIR/tmp/deploy/rpm $PJ_YOCTO_BUILD-rpm ] && do_command_fn "(cd $PJ_YOCTO_ROOT/builds-lnk; ln -s $PJ_YOCTO_BUILD_DIR/tmp/deploy/rpm $PJ_YOCTO_BUILD-rpm;)"
		[ -d $PJ_YOCTO_BUILD_DIR/tmp/deploy/rpm $PJ_YOCTO_BUILD-deb ] && do_command_fn "(cd $PJ_YOCTO_ROOT/builds-lnk; ln -s $PJ_YOCTO_BUILD_DIR/tmp/deploy/deb $PJ_YOCTO_BUILD-deb;)"
		do_command_fn "(cd $PJ_YOCTO_ROOT/builds-lnk; ln -s $PJ_YOCTO_BUILD_DIR/tmp/deploy/images/$PJ_YOCTO_MACHINE;)"
		do_command_fn "(cd $PJ_YOCTO_ROOT/builds-lnk; ln -s $PJ_YOCTO_BUILD_DIR/tmp/deploy/sdk;)"
	else
		echo "Please mkdir $PJ_YOCTO_ROOT/builds-lnk first !!!"
	fi

	mkdir -p $PJ_YOCTO_ROOT/images-lnk
	if [ -d $PJ_YOCTO_ROOT/images-lnk ]; then
		do_command_fn "(cd $PJ_YOCTO_ROOT/images-lnk; rm -f *;)"
		do_command_fn "(cd $PJ_YOCTO_ROOT/images-lnk; ln -s $(readlink -f $PJ_YOCTO_BUILD_DIR/tmp/deploy/images/$PJ_YOCTO_MACHINE/$PJ_YOCTO_IMAGE_WIC) $PJ_YOCTO_IMAGE_WIC;)"
		do_command_fn "(cd $PJ_YOCTO_ROOT/images-lnk; ln -s $(readlink -f $PJ_YOCTO_BUILD_DIR/tmp/deploy/images/$PJ_YOCTO_MACHINE/$PJ_YOCTO_IMAGE_MANIFEST) $PJ_YOCTO_IMAGE_MANIFEST;)"
		do_command_fn "(cd $PJ_YOCTO_ROOT/images-lnk; ln -s $(readlink -f $PJ_YOCTO_BUILD_DIR/tmp/deploy/images/$PJ_YOCTO_MACHINE/$PJ_YOCTO_IMAGE_EXT4) $PJ_YOCTO_IMAGE_EXT4;)"
		do_command_fn "(cd $PJ_YOCTO_ROOT/images-lnk; ln -s $(readlink -f $PJ_YOCTO_BUILD_DIR/tmp/deploy/images/$PJ_YOCTO_MACHINE/$PJ_YOCTO_BUNDLE_RAUCB) $PJ_YOCTO_BUNDLE_RAUCB;)"
		do_command_fn "(cd $PJ_YOCTO_ROOT/images-lnk; ln -s $(readlink -f $PJ_YOCTO_BUILD_DIR/tmp/deploy/images/$PJ_YOCTO_MACHINE/$PJ_YOCTO_BOOT_BIN) $PJ_YOCTO_BOOT_BIN;)"
		do_command_fn "(cd $PJ_YOCTO_ROOT/images-lnk; ln -s $(readlink -f $PJ_YOCTO_BUILD_DIR/tmp/deploy/images/$PJ_YOCTO_MACHINE/$PJ_YOCTO_BOOT_ENV) $PJ_YOCTO_BOOT_ENV;)"
		do_command_fn "(cd $PJ_YOCTO_ROOT/images-lnk; bitbake -g $PJ_YOCTO_TARGET;)"
		do_command_fn "(cd $PJ_YOCTO_ROOT/images-lnk; bitbake -e $PJ_YOCTO_TARGET > environment.txt;)"
	else
		echo "Please mkdir $PJ_YOCTO_ROOT/images-lnk first !!!"
	fi
}

distclean_fn()
{
	datetime_fn "${FUNCNAME[0]} ... "
	do_command_fn "rm -rf builds/*"
}

cfg_fn()
{
	#datetime_fn "${FUNCNAME[0]} ... "

	if [ ! -f ~/.local/bin/cooker ]; then
		echo "Please install cooker first !!!"
		echo " python3 -m pip install --upgrade git+https://github.com/cpb-/yocto-cooker.git"
	fi

	[ -f .cookerconfig_bak ] || (cp .cookerconfig .cookerconfig_bak;)

	export PJ_YOCTO_DOWNLOADS_DIR=`realpath $PJ_YOCTO_DOWNLOADS_DIR`
	export PJ_YOCTO_SSTATE_DIR=`realpath $PJ_YOCTO_SSTATE_DIR`
	jq . .cookerconfig_bak | jq ".menu=\"`pwd`/$PJ_COOKER_MENU_FOLDER/$PJ_COOKER_MENU\"" | jq ".[\"layer-dir\"]=\"$PJ_YOCTO_LAYERS_FOLDER\"" | jq ".[\"build-dir\"]=\"$PJ_YOCTO_BUILDS_FOLDER\"" | jq ".[\"sstate-dir\"]=\"$PJ_YOCTO_SSTATE_DIR\"" | jq ".[\"dl-dir\"]=\"$PJ_YOCTO_DOWNLOADS_DIR\"" | jq -c . > .cookerconfig

	return 0
}

# make cook-clean
clean_fn()
{
	datetime_fn "${FUNCNAME[0]} ... "

	do_command_fn "cooker clean $PJ_YOCTO_TARGET"

	return 0
}

# make .cook-update
update_fn()
{
	datetime_fn "${FUNCNAME[0]} ... "

	do_command_fn "cooker update"

	return 0
}

# make .cook-init
init_fn()
{
	datetime_fn "${FUNCNAME[0]} ... "

	do_command_fn "cooker $COOKER_DRY $COOKER_VERBOSE init $COOKER_MENU"

	return 0
}

# make cook
cook_fn()
{
	datetime_fn "${FUNCNAME[0]} ... "

	do_command_fn "cooker $COOKER_DRY $COOKER_VERBOSE cook $COOKER_MENU $PJ_YOCTO_BUILD"

	return 0
}

bundle_fn()
{
	datetime_fn "${FUNCNAME[0]} ... "

	if [ ! -z "$PJ_YOCTO_BUNDLE" ]; then
		do_command_fn "bitbake $PJ_YOCTO_BUNDLE"
	else
		echo "Please check (PJ_YOCTO_BUNDLE=$PJ_YOCTO_BUNDLE) !!!"
	fi

	return 0
}

# make cook-build
build_fn()
{
	datetime_fn "${FUNCNAME[0]} ... "

	do_command_fn "cooker $COOKER_DRY $COOKER_VERBOSE build $PJ_YOCTO_BUILD"

	return 0
}

# make cook-dry-run
dry_run_fn()
{
	datetime_fn "${FUNCNAME[0]} ... "

	do_command_fn "cooker --dry-run cook $COOKER_MENU $PJ_YOCTO_BUILD > $BUILD_SCRIPT; chmod  +x  $BUILD_SCRIPT"
	sed -i --follow-symlinks "s|env bash -c source|source|g" $BUILD_SCRIPT

	return 0
}

# make .cook-generate
generate_fn()
{
	datetime_fn "${FUNCNAME[0]} ... "

	do_command_fn "cooker generate"

	return 0
}

shell_fn()
{
	datetime_fn "${FUNCNAME[0]} ... "

	do_command_fn "cooker shell $PJ_YOCTO_BUILD"

	return 0
}

all_fn()
{
	datetime_fn "${FUNCNAME[0]} ... "

	do_command_fn "cooker cook $COOKER_MENU"

	return 0
}

showusage_fn()
{
	die_fn "$HINT"

	return 0
}

exit_fn()
{
	datetime_fn "${FUNCNAME[0]} ... "

	return 0
}

[ "$ACTION" != "" ] || showusage_fn

main_fn()
{
	cfg_fn

	case $ACTION in
		all)
			all_fn
		;;
		shell)
			shell_fn
		;;
		cook)
			cook_fn
		;;
		bundle)
			bundle_fn
		;;
		build)
			build_fn
		;;
		dry-run)
			dry_run_fn
		;;
		generate)
			generate_fn
		;;
		update)
			update_fn
		;;
		init)
			init_fn
		;;
		clean)
			clean_fn
		;;
		distclean)
			distclean_fn
		;;
		ls)
			ls_fn
		;;
		pull)
			pull_fn
		;;
		lnk)
			lnk_fn
		;;
		*)
			showusage_fn
		;;
	esac
}

#trap "trap_ctrlc" 2
main_fn

exit_fn
exit 0
