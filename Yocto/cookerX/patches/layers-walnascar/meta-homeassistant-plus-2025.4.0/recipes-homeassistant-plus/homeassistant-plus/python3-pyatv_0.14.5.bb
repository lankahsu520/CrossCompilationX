SUMMARY = "A client library for Apple TV and AirPlay devices"
DESCRIPTION = "This is an asyncio python library for interacting with Apple TV and AirPlay devices. It mainly targets Apple TVs (all generations, including tvOS 15 and later), but also supports audio streaming via AirPlay to receivers like the HomePod, AirPort Express and third-party speakers. It can act as remote control to the Music app/iTunes in macOS."
HOMEPAGE = "https://github.com/postlund/pyatv"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=7f8955c41f09c20ef7074b6b2b1390ba"

# sha256sum pyatv-0.14.5.tar.gz
SRC_URI[sha256sum] = "b48216aa7fd9c205d2935196d7f4bd97645e5d4863600eafd13a964f99cdd876"
# sha256sum pyatv-0.16.1.tar.gz
#SRC_URI[sha256sum] = "4a25944fd82edcc353f2b2b08aecd6b5fc053d18c956b4d58addc51d598209e6"

S = "${WORKDIR}/pyatv-0.14.5"

#PYPI_PACKAGE = "pyatv"

inherit pypi python_setuptools_build_meta

#DEPENDS += "python3-build-native python3-setuptools-native python3-pytest-runner-native"
DEPENDS += " \
    python3-pytest-runner-native \
"

#BBCLASSEXTEND = "native nativesdk"

RDEPENDS:${PN} += " \
    python3-aiohttp \
    python3-aiohttp-cors \
    python3-zeroconf \
    python3-protobuf \
    python3-typing-extensions \
    python3-cryptography \
    python3-setuptools \
    python3-srptools \
    python3-chacha20poly1305 \
    python3-chacha20poly1305-reuseable \
    python3-mediafile \
"

FILES:${PN} += " \
    ${libdir}/python${PYTHON_BASEVERSION}/site-packages/pyatv \
    ${libdir}/python${PYTHON_BASEVERSION}/site-packages/pyatv-*.dist-info \
    ${bindir}/atvproxy \
    ${bindir}/atvlog \
    ${bindir}/atvremote \
    ${bindir}/atvscript \
"