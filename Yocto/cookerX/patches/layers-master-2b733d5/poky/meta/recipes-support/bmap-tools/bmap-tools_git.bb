SUMMARY = "Tools to generate block map (AKA bmap) and flash images using bmap"
DESCRIPTION = "Bmap-tools - tools to generate block map (AKA bmap) and flash images using \
bmap. Bmaptool is a generic tool for creating the block map (bmap) for a file, \
and copying files using the block map. The idea is that large file containing \
unused blocks, like raw system image files, can be copied or flashed a lot \
faster with bmaptool than with traditional tools like "dd" or "cp"."
HOMEPAGE = "https://github.com/01org/bmap-tools"
SECTION = "console/utils"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

#SRC_URI = "git://github.com/intel/${BPN};branch=master;protocol=https"

#SRCREV = "c0673962a8ec1624b5189dc1d24f33fe4f06785a"
#S = "${WORKDIR}/git"
#BASEVER = "3.6"
#PV = "${BASEVER}+git${SRCPV}"
SRC_URI = "https://github.com/intel/bmap-tools/archive/refs/tags/v3.6.tar.gz"
SRC_URI[sha256sum] = "0658afb972e7221aa16fece3f84e29e5102e901c929253d7fb6040bde28243f7"

S = "${WORKDIR}/bmap-tools-3.6"

#UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\d+(\.\d+)+)"

# Need df from coreutils
RDEPENDS:${PN} = "python3-core python3-compression python3-mmap python3-setuptools python3-fcntl python3-six coreutils"

inherit setuptools3

BBCLASSEXTEND = "native nativesdk"
