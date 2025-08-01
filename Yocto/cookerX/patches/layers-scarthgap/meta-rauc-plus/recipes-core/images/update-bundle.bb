DESCRIPTION = "RAUC Bundle"
LICENSE = "MIT"
inherit bundle

RAUC_BUNDLE_COMPATIBLE = "Lanka520"
#RAUC_BUNDLE_FORMAT = "plain"
RAUC_BUNDLE_FORMAT = "verity"
#RAUC_BUNDLE_FORMAT = "crypt"
RAUC_BUNDLE_SLOTS ?= "rootfs"

RAUC_SLOT_rootfs ?= "imx-image-core"
RAUC_SLOT_rootfs[type] ?= "image"
RAUC_SLOT_rootfs[fstype] ?= "ext4"
RAUC_SLOT_rootfs[rename] ?= "rootfs.ext4"

RAUC_KEY_FILE = "${COOKER_LAYER_DIR}/../rauc-keys/development-1.key.pem"
RAUC_CERT_FILE = "${COOKER_LAYER_DIR}/../rauc-keys/development-1.cert.pem"
#RAUC_KEYRING_FILE = "${COOKER_LAYER_DIR}/../rauc-keys/ca.cert.pem"

#RAUC_CASYNC_BUNDLE = "1"