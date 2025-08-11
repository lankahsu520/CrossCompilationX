SUMMARY = "a modern parsing library"
DESCRIPTION = "Lark is a modern general-purpose parsing library for Python. With Lark, you can parse any context-free grammar, efficiently, with very little code."
HOMEPAGE = "https://github.com/lark-parser/lark"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ba5edf8d60cf416b5588b1e6f152fcef"

# sha256sum lark-1.2.2.tar.gz
SRC_URI[sha256sum] = "ca807d0162cd16cef15a8feecb862d7319e7a09bdb13aef927968e45040fed80"

S = "${WORKDIR}/lark-1.2.2"

#PYPI_PACKAGE = "lark"

inherit pypi python_setuptools_build_meta
