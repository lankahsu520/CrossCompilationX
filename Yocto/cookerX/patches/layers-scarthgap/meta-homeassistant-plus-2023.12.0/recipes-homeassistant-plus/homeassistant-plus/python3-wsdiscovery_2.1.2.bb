SUMMARY = "WS-Discovery implementation for python"
DESCRIPTION = "This is WS-Discovery implementation for Python 3. It allows to both discover services and publish discoverable services. For Python 2 support, use the latest 1.x version of this package."
HOMEPAGE = "https://github.com/andreikop/python-ws-discovery.git"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b52f2d57d10c4f7ee67a7eb9615d5d24"

# sha256sum WSDiscovery-2.1.2.tar.gz
SRC_URI[sha256sum] = "ab816deff2db101be59830317e19b78a95e9146b2b0a747953345ae68d1d8b58"

S = "${WORKDIR}/WSDiscovery-2.1.2"

PYPI_PACKAGE = "WSDiscovery"

inherit pypi python_setuptools_build_meta

RDEPENDS:${PN} += " \
    python3-xml \
"