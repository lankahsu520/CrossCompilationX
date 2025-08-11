SUMMARY = "asyncio-friendly python API for Sensibo"
DESCRIPTION = "asyncio-friendly python API for Sensibo (https://sensibo.com). Supported on Python 3.11+"
HOMEPAGE = "https://github.com/andrey-git/pysensibo"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1158e526cc5762c9bb47c9beee43935a"

# sha256sum pysensibo-1.2.1.tar.gz
SRC_URI[sha256sum] = "3ad9395b75536ce01e64e5675ef5bc552c54d671daf3352f9af76e453765c15b"

S = "${WORKDIR}/pysensibo-1.2.1"

inherit pypi python_setuptools_build_meta

DEPENDS += " \
    python3-poetry-core-native \
"

RDEPENDS:${PN} += " \
    python3-requests \
"