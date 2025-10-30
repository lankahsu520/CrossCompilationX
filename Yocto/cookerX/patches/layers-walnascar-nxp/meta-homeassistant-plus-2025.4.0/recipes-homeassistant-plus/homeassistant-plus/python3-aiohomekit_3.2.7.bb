SUMMARY = "This library implements the HomeKit protocol for controlling Homekit accessories using asyncio."
DESCRIPTION = "It's primary use is for with Home Assistant. We target the same versions of python as them and try to follow their code standards."
HOMEPAGE = "https://github.com/Jc2k/aiohomekit"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=cc46ca29f3052de422844abf6c8e9935"
#LIC_FILES_CHKSUM = "file://LICENSE.md;md5=fed67e79e32ce41adeff80ff7498f10f"

# sha256sum aiohomekit-3.2.7.tar.gz
SRC_URI[sha256sum] = "d5c783b36eedead697c7ca9d7019551837e4ad611b5eb9cc24a04e473925498e"
# sha256sum aiohomekit-3.2.15.tar.gz
#SRC_URI[sha256sum] = "b458e06ee29f2757e471a4cb686a236ffcfee631299c7e7f865bf33d82a737d9"

S = "${WORKDIR}/aiohomekit-3.2.7"

#PYPI_PACKAGE = "aiohomekit"

inherit pypi python_setuptools_build_meta

DEPENDS += "python3-hap-python python3-poetry-core-native"

RDEPENDS:${PN} += " \
    python3-zeroconf \
    python3-cryptography \
    python3-poetry-core \
    python3-commentjson \
    python3-chacha20poly1305 \
    python3-chacha20poly1305-reuseable \
"