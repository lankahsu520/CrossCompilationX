SUMMARY = "A library to handle automated deprecations"
DESCRIPTION = "Construct is a powerful declarative and symmetrical parser and builder for binary data."
HOMEPAGE = "http://deprecation.readthedocs.io/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WHL_BP}.dist-info/LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e"

PYPI_PACKAGE = "deprecation"

inherit python3-dir

do_unpack[depends] += "unzip-native:do_populate_sysroot"

DEPENDS += "python3"

WHL_BPN = "deprecation"
WHL_BP = "deprecation-${PV}"
WHL_PN = "deprecation"
SRC_URI = "https://files.pythonhosted.org/packages/02/c3/253a89ee03fc9b9682f1541728eb66db7db22148cd94f89ab22528cd1e1b/deprecation-2.1.0-py2.py3-none-any.whl;downloadfilename=${BP}.zip;subdir=${BP}"
# unzip deprecation-2.1.0-py2.py3-none-any.whl -d deprecation/
# sha256sum deprecation-2.1.0-py2.py3-none-any.whl
SRC_URI[sha256sum] = "a10811591210e1fb0e768a8c25517cabeabcba6f0bf96564f8ff45189f90b14a"

do_install() {
    #install -d ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BPN}
    install -d ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info

    # install -m 644 ${S}/${WHL_BPN}/* ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BPN}/
    cp -r ${S}/deprecation.py ${D}${libdir}/${PYTHON_DIR}/site-packages/deprecation.py
    install -m 644 ${S}/${WHL_BP}.dist-info/* ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info/
}

FILES:${PN} += "\
    ${libdir}/${PYTHON_DIR}/site-packages/deprecation.py \
    ${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info \
"
