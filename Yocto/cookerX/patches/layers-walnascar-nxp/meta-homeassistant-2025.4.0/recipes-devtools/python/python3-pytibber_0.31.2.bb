SUMMARY = "A python3 library to communicate with Tibber"
HOMEPAGE = "https://github.com/Danielhiversen/pyTibber"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"
RECIPE_MAINTAINER = "Tom Geelen <t.f.g.geelen@gmail.com>"

SRC_URI[sha256sum] = "b1d21bb4ea45e2b92713652213fa655c96f04367e381a4b083d3751362d09f55"

inherit pypi python_setuptools_build_meta ptest-python-pytest

RDEPENDS:${PN} = "\
    python3-aiohttp (>=3.0.6) \
    python3-gql (>=3.0.0) \
    python3-websockets (>=10.0) \
"

PTEST_PYTEST_DIR = "test"

PYPI_PACKAGE = "pytibber"

do_configure:prepend() {
    FILE_PYPROJECT="${S}/pyproject.toml"
    #bbplain "==>>(WORKDIR: ${WORKDIR})"
    #bbplain "==>>(S: ${S})"
    bbplain "==>>(FILE_PYPROJECT: ${FILE_PYPROJECT})"

    if grep -q "^requires *= *\[" "$FILE_PYPROJECT"; then
        sed -i 's/^requires *= *\[.*\]/requires = \["setuptools>=76.0"\]/' "$FILE_PYPROJECT"
    fi

    if grep -q "^license *= *" "$FILE_PYPROJECT"; then
        sed -i 's/^license *= *.*/license = { file = "LICENSE" }/' "$FILE_PYPROJECT"
    fi
}