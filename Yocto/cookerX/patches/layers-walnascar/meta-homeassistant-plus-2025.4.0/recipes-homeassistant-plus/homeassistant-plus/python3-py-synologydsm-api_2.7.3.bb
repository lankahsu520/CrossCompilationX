SUMMARY = "Python API for communication with Synology DSM"
DESCRIPTION = "Asynchronous Python API for Synology DSM"
HOMEPAGE = "https://github.com/mib1185/py-synologydsm-api#readme"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=84e60ab84e8fcf378e060181c4f16878"

#SRC_URI = "file://py-synologydsm-api-2.7.3.tar.gz"
#SRC_URI = "https://codeload.github.com/mib1185/py-synologydsm-api/tar.gz/refs/tags/v2.7.3;downloadfilename=py-synologydsm-api-2.7.3.tar.gz"
#SRC_URI = "https://github.com/mib1185/py-synologydsm-api/archive/refs/tags/v2.7.3.tar.gz;downloadfilename=py-synologydsm-api-2.7.3.tar.gz"
SRC_URI = "git://github.com/mib1185/py-synologydsm-api.git;protocol=https;branch=master"
SRCREV = "v2.7.3"
# sha256sum py-synologydsm-api-2.7.3.tar.gz
SRC_URI[sha256sum] = "56bfa1d61aac4a1ecca46e03933b8509e8803bf45598eb2bfb9e91af635f3d31"

S = "${WORKDIR}/py-synologydsm-api-2.7.3"

#PYPI_PACKAGE = "py-synologydsm-api"

inherit python_pep517 python_setuptools_build_meta

S = "${WORKDIR}/sources-unpack/git"

DEPENDS += " \
    python3-setuptools-native \
"

RDEPENDS:${PN} += " \
    python3-requests \
    python3-aiohttp \
"

FILES:${PN} += " \
    ${libdir} \
    ${bindir} \
"