SUMMARY = "Add Python and JavaScript style comments in your JSON files."
DESCRIPTION = "commentjson (Comment JSON) is a Python package that helps you create JSON files with Python and JavaScript style inline comments. Its API is very similar to the Python standard library's json module."
HOMEPAGE = "https://github.com/vaidik/commentjson"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.rst;md5=722175c22a1ab5e2e0fd153da885198b"

# sha256sum commentjson-0.9.0.tar.gz
SRC_URI[sha256sum] = "42f9f231d97d93aff3286a4dc0de39bfd91ae823d1d9eba9fa901fe0c7113dd4"

S = "${WORKDIR}/commentjson-0.9.0"

#PYPI_PACKAGE = "commentjson"

inherit pypi python_setuptools_build_meta

RDEPENDS:${PN} += " \
    python3-lark \
"
