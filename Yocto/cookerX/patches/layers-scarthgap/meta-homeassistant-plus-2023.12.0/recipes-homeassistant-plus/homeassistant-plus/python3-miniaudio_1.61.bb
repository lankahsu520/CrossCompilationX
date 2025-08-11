SUMMARY = "python bindings for the miniaudio library and its decoders (mp3, flac, ogg vorbis, wav)"
DESCRIPTION = "Multiplatform audio playback, recording, decoding and sample format conversion for Linux (including Raspberri Pi), Windows, Mac and others."
HOMEPAGE = "https://github.com/irmen/pyminiaudio"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1546904731ab7752de3bc2c7cb005936"

# sha256sum miniaudio-1.61.tar.gz
SRC_URI[sha256sum] = "e88e97837d031f0fb6982394218b6487de02eaa382ad273b8fca37791a2b4b15"

S = "${WORKDIR}/miniaudio-1.61"

#PYPI_PACKAGE = "miniaudio"

inherit pypi python_setuptools_build_meta

DEPENDS += " \
    python3-cffi-native \
"

RDEPENDS:${PN} += " \
    python3-cffi \
"