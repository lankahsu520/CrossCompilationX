#!/bin/sh

mkdir -p ${PJ_YOCTO_ROOT}/bb-lnk
cd ${PJ_YOCTO_ROOT}/bb-lnk

datetime_fn()
{
	PROMPT=$1
	#NOW_t=`date +"%Y%m%d %T"`
	NOW_t=`date +"%Y%m%d %H%M%S"`
	#DO_COMMAND="echo \"$NOW_t - $PROMPT\" $TEE_ARG"
	sh -c "echo \"$NOW_t - $PROMPT\" $TEE_ARG"

	return 0
}

do_command_fn()
{
	DO_COMMAND=$1
	datetime_fn "[$DO_COMMAND]"
	sh -c "$DO_COMMAND"
}

create_lnk_fn()
{
	YOCTO_BB_NAME=$1
	YOCTO_BB_LINK=`cd ..; find * -name $YOCTO_BB_NAME*.bb`
	for link in ${YOCTO_BB_LINK}; do
		do_command_fn "ln -s ../${link}"
	done

}

exit_fn()
{
	datetime_fn "exit_fn ... "

	return 0
}

main_fn()
{
	#create_lnk_fn "apache2"
	rm -f *.bb
	create_lnk_fn "avahi_"

	#create_lnk_fn "bluez5"
	#create_lnk_fn "busybox_"
	#create_lnk_fn "bzip2"
	#
	#create_lnk_fn "curl_"
	#
	#create_lnk_fn "dbus_"
	#create_lnk_fn "dropbear_"
	#
	#create_lnk_fn "glib-"
	#create_lnk_fn "glibc_"
	#
	#create_lnk_fn "hostapd_"
	#
	#create_lnk_fn "jansson_"
	#create_lnk_fn "json-c_"
	#
	#create_lnk_fn "libevent_"
	#create_lnk_fn "libical_"
	#create_lnk_fn "libmnl_"
	#create_lnk_fn "libnl_"
	#create_lnk_fn "libsndfile1_"
	#create_lnk_fn "libunistring_"
	#create_lnk_fn "libusb1_"
	#create_lnk_fn "libyokis"
	#create_lnk_fn "libxml2_"
	#
	#create_lnk_fn "mosquitto_"
	#
	#create_lnk_fn "openssl_"
	#
	#create_lnk_fn "p7zip_"
	#
	#create_lnk_fn "rsync_"
	#
	#create_lnk_fn "sed_"
	#
	#create_lnk_fn "tar_"
	#
	#create_lnk_fn "unzip_"
	#
	#create_lnk_fn "which_"
	#
	#create_lnk_fn "xmlto_"
	#
	#create_lnk_fn "yaml-cpp_"
	#
	#create_lnk_fn "zlib_"
	#
	#create_lnk_fn "python3-homeassistant"
	#create_lnk_fn "python3-ha-av"
	#create_lnk_fn "zigbee_coordinator"
}

main_fn

exit_fn
exit 0
