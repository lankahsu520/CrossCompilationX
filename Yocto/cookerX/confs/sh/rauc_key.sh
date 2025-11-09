#!/bin/sh

[ "$PJ_YOCTO_ROOT" = "" ] && echo "PJ_YOCTO_ROOT is NULL !!!" && exit -1

if [ ! -f $PJ_YOCTO_ROOT/rauc-keys/ca.cert.pem ]; then
	cd ${PJ_YOCTO_LAYERS_DIR}/meta-rauc/scripts
	./openssl-ca.sh
	tree -L 4 openssl-ca

	mkdir -p ${PJ_YOCTO_ROOT}/rauc-keys
	cp -avr openssl-ca ${PJ_YOCTO_ROOT}/rauc-keys/

	cp -av openssl-ca/dev/ca.cert.pem ${PJ_YOCTO_ROOT}/rauc-keys
	cp -av openssl-ca/dev/private/development-1.key.pem ${PJ_YOCTO_ROOT}/rauc-keys
	cp -av openssl-ca/dev/development-1.cert.pem ${PJ_YOCTO_ROOT}/rauc-keys

	rm -rf openssl-ca
fi

if [ -d $PJ_YOCTO_ROOT/rauc-keys  ]; then
	echo "Please check ${PJ_YOCTO_ROOT}/rauc-keys !!!"
	tree -L 4 ${PJ_YOCTO_ROOT}/rauc-keys
else
	echo "Couldn't found any ${PJ_YOCTO_ROOT}/rauc-keys !!!"
fi

exit 0
