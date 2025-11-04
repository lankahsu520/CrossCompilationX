SUMMARY = "IPython-enabled pdb"
DESCRIPTION = "ipdb exports functions to access the IPython debugger, which features tab completion, syntax highlighting, better tracebacks, better introspection with the same interface as the pdb module."
HOMEPAGE = "https://github.com/gotcha/ipdb"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WHL_BP}.dist-info/COPYING.txt;md5=ce5277785b5e90142013c37baa935b20"

PYPI_PACKAGE = "ipdb"

inherit python3-dir

do_unpack[depends] += "unzip-native:do_populate_sysroot"

DEPENDS += "python3"

WHL_BPN = "ipdb"
WHL_BP = "ipdb-${PV}"
WHL_PN = "ipdb"
SRC_URI = "https://files.pythonhosted.org/packages/0c/4c/b075da0092003d9a55cf2ecc1cae9384a1ca4f650d51b00fc59875fe76f6/ipdb-0.13.13-py3-none-any.whl;downloadfilename=${BP}.zip;subdir=${BP}"
# unzip ipdb-0.13.13-py3-none-any.whl -d ipdb/
# sha256sum ipdb-0.13.13-py3-none-any.whl
SRC_URI[sha256sum] = "45529994741c4ab6d2388bfa5d7b725c2cf7fe9deffabdb8a6113aa5ed449ed4"

do_install() {
    install -d ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BPN}
    install -d ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info

    # install -m 644 ${S}/${WHL_BPN}/* ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BPN}/
    cp -r ${S}/${WHL_BPN}/* ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BPN}/
    install -m 644 ${S}/${WHL_BP}.dist-info/* ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info/
}

FILES:${PN} += "\
    ${libdir}/${PYTHON_DIR}/site-packages/${WHL_BPN} \
    ${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info \
"
