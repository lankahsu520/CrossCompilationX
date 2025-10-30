# Copyright 2024 NXP
HOMEPAGE = "http://xen.org"
LICENSE = "GPL-2.0-only"
SECTION = "console/tools"
LIC_FILES_CHKSUM ?= "file://COPYING;md5=d1a1e216f80b6d8da95fec897d0dbec9"

FILESEXTRAPATHS:prepend := "${COOKER_LAYER_DIR}/meta-virtualization/recipes-extended/xen/files:"
S = "${WORKDIR}/git"

DEFAULT_PREFERENCE ??= "-1"

require xen-common.inc
require recipes-extended/xen/xen.inc
require recipes-extended/xen/xen-tools.inc

RDEPENDS:remove = "${PN}-net-conf"

FILES:${PN}:append = " \
    ${sysconfdir}/xen/*.conf \
    ${libdir}/xen/bin/xen-9pfsd \
"
