SUMMARY = "API to interact with an OTBR via its REST API"
DESCRIPTION = "Python package to interact with an OTBR via its REST API"
HOMEPAGE = "https://github.com/home-assistant-libs/python-otbr-api"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5a588bcbe30e4d6b149fa57244eede5e"

# sha256sum python-otbr-api-2.6.0.tar.gz
SRC_URI[sha256sum] = "9e5cf87978447dcddc842e05de39c362c75975bcbf4d49bcca9f4adb10ea1d6e"

S = "${WORKDIR}/python-otbr-api-2.6.0"

inherit pypi python_setuptools_build_meta

RDEPENDS:${PN} += " \
    python3-requests \
    python3-dbus \
    python3-typing-extensions \
    python3-bitstruct \
"