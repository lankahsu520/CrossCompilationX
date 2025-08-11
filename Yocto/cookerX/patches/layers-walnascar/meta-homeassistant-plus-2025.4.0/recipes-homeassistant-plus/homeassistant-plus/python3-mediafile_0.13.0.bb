SUMMARY = "Handles low-level interfacing for files' tags. Wraps Mutagen to"
DESCRIPTION = "MediaFile is a simple interface to the metadata tags for many audio file formats. It wraps Mutagen, a high-quality library for low-level tag manipulation, with a high-level, format-independent interface for a common set of tags."
HOMEPAGE = "https://github.com/beetbox/mediafile"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=95e6d6f89ab860bdcc3b1d677492e2e3"

# sha256sum pyatv-0.14.5.tar.gz
SRC_URI[sha256sum] = "de71063e1bffe9733d6ccad526ea7dac8a9ce760105827f81ab0cb034c729a6d"

S = "${WORKDIR}/mediafile-0.13.0"

#PYPI_PACKAGE = "mediafile"

inherit pypi python_setuptools_build_meta

#DEPENDS += "python3-build-native python3-setuptools-native python3-pytest-runner-native"

#BBCLASSEXTEND = "native nativesdk"

RDEPENDS:${PN} += " \
    python3-filetype \
"

#RDEPENDS:${PN} += " \
#    python3-musicbrainzngs \
#    python3-mutatag \
#"
