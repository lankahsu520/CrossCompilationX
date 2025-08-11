SUMMARY = "Python Client for ONVIF Camera"
DESCRIPTION = "ONVIF Client Implementation in Python"
HOMEPAGE = "http://github.com/quatanium/python-onvif"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=319f5fc7a7a26aa4bd441bc76997938b"

# sha256sum onvif_zeep-0.2.12.tar.gz
SRC_URI="https://files.pythonhosted.org/packages/source/o/onvif-zeep/onvif_zeep-0.2.12.tar.gz"
SRC_URI[sha256sum] = "aa8bbc02a73eaa50894b0c18e39fa8d318a583a664c65bf35b3c8029d1c40b49"


S = "${WORKDIR}/onvif_zeep-0.2.12"

#PYPI_PACKAGE = "onvif-zeep"

inherit python_setuptools_build_meta

RDEPENDS:${PN} += " \
    python3-zeep \
    python3-requests \
    python3-requests-file \
    python3-six \
    python3-lxml \
    python3-isodate \
"