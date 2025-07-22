SUMMARY = "Chacha20Poly1305"
DESCRIPTION = "Simple pure-python chacha20-poly1305 implementation based on tlslite-ng code. Designed to be compatible with Cryptography API."
HOMEPAGE = "https://github.com/ph4r05/py-chacha20poly1305"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://README.md;md5=a9c48b4c7a722397f39a329061c4c82b"

# sha256sum chacha20poly1305-0.0.3.tar.gz
SRC_URI[sha256sum] = "f2f005c7cf4638ffa4ff06c02c78748068b642916795c6d16c7cc5e355e70edf"

S = "${WORKDIR}/chacha20poly1305-0.0.3"

#PYPI_PACKAGE = "chacha20poly1305"

inherit pypi python_setuptools_build_meta

