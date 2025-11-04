SUMMARY = "Python port of markdown-it. Markdown parsing, done right!"
DESCRIPTION = "Markdown parser done right."
HOMEPAGE = "https://github.com/executablebooks/markdown-it-py"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WHL_BP}.dist-info/licenses/LICENSE;md5=a38a1697260a7ad7bf29f44b362db1fc"

PYPI_PACKAGE = "markdown-it-py"

inherit python3-dir

do_unpack[depends] += "unzip-native:do_populate_sysroot"

DEPENDS += "python3"

WHL_BPN = "markdown_it"
WHL_BP = "markdown_it_py-${PV}"
WHL_PN = "markdown_it_py"
SRC_URI = "https://files.pythonhosted.org/packages/94/54/e7d793b573f298e1c9013b8c4dade17d481164aa517d1d7148619c2cedbf/markdown_it_py-4.0.0-py3-none-any.whl;downloadfilename=${BP}.zip;subdir=${BP}"
# unzip markdown_it_py-4.0.0-py3-none-any.whl -d markdown-it-py/
# sha256sum markdown_it_py-4.0.0-py3-none-any.whl
SRC_URI[sha256sum] = "87327c59b172c5011896038353a81343b6754500a08cd7a4973bb48c6d578147"

do_install() {
    install -d ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BPN}
    install -d ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info

    # install -m 644 ${S}/${WHL_BPN}/* ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BPN}/
    cp -r ${S}/${WHL_BPN}/* ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BPN}/
    #install -m 644 ${S}/${WHL_BP}.dist-info/* ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info/
    cp -r ${S}/${WHL_BP}.dist-info/* ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info/
}

FILES:${PN} += "\
    ${libdir}/${PYTHON_DIR}/site-packages/${WHL_BPN} \
    ${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info \
"
