FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://dm-verity.cfg"
#SRC_URI += "file://defconfig"

DELTA_KERNEL_DEFCONFIG:append = " dm-verity.cfg"

