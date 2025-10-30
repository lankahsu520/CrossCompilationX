SUMMARY = "ChaCha20Poly1305 that is reuseable for asyncio"
DESCRIPTION = "ChaCha20Poly1305 that is reuseable for asyncio"
HOMEPAGE = "https://github.com/bdraco/chacha20poly1305-reuseable"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89361b011ca9139ce569202482825464"

# sha256sum chacha20poly1305_reuseable-0.13.2.tar.gz
SRC_URI[sha256sum] = "dd8be876e25dfc51909eb35602b77a76e0d01a364584756ab3fa848e2407e4ec"

S = "${WORKDIR}/chacha20poly1305_reuseable-0.13.2"

PYPI_PACKAGE = "chacha20poly1305_reuseable"

inherit pypi python_setuptools_build_meta

DEPENDS += " \
    python3-poetry-core-native \
"
