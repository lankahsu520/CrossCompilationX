SUMMARY = "HomeKit Accessory Protocol implementation in python."
DESCRIPTION = "HomeKit Accessory Protocol implementation in python 3. With this project, you can integrate your own smart devices and add them to your iOS Home app. Since Siri is integrated with the Home app, you can start voice-control your accessories right away."
HOMEPAGE = "https://github.com/ikalchev/HAP-python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9a7f7961e2e1d54a12aca53beeb18701"

# sha256sum HAP-python-4.9.1.tar.gz
SRC_URI[sha256sum] = "625b4e6688d6593d1fab35f404838dc5cf24498bad1dbef2bd955f17cb9688bb"

S = "${WORKDIR}/HAP-python-4.9.1"

PYPI_PACKAGE = "HAP-python"

inherit pypi python_setuptools_build_meta

#DEPENDS += "python3-setuptools-scm-native"

RDEPENDS:${PN} += " \
    python3-zeroconf \
    python3-cryptography \
"