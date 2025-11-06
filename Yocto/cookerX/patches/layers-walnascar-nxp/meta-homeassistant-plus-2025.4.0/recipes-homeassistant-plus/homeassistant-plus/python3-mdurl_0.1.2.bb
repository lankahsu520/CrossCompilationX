SUMMARY = "Markdown URL utilities"
DESCRIPTION = "This is a Python port of the JavaScript mdurl package. See the upstream README.md file for API documentation."
HOMEPAGE = "https://github.com/executablebooks/mdurl"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WHL_BP}.dist-info/LICENSE;md5=aca1dc6b9088f1dda81c89cad2c77ad1"

PYPI_PACKAGE = "markdown-it-py"

inherit python3-dir

do_unpack[depends] += "unzip-native:do_populate_sysroot"

DEPENDS += "python3"

WHL_BPN = "mdurl"
WHL_BP = "mdurl-${PV}"
WHL_PN = "mdurl"
SRC_URI = "https://files.pythonhosted.org/packages/b3/38/89ba8ad64ae25be8de66a6d463314cf1eb366222074cfda9ee839c56a4b4/mdurl-0.1.2-py3-none-any.whl;downloadfilename=${BP}.zip;subdir=${BP}"
# unzip mdurl-0.1.2-py3-none-any.whl -d mdurl/
# sha256sum mdurl-0.1.2-py3-none-any.whl
SRC_URI[sha256sum] = "84008a41e51615a49fc9966191ff91509e3c40b939176e643fd50a5c2196b8f8"

do_install() {
    install -d ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BPN}
    install -d ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info

    # install -m 644 ${S}/${WHL_BPN}/* ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BPN}/
    cp -r ${S}/${WHL_BPN}/* ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BPN}/
    install -m 644 ${S}/${WHL_BP}.dist-info/* ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info/
    #cp -r ${S}/${WHL_BP}.dist-info/* ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info/
}

FILES:${PN} += "\
    ${libdir}/${PYTHON_DIR}/site-packages/${WHL_BPN} \
    ${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info \
"
