SUMMARY = "Safely evaluate AST nodes without side effects"
DESCRIPTION = "This is a Python package that lets you safely evaluate certain AST nodes without triggering arbitrary code that may have unwanted side effects."
HOMEPAGE = "http://github.com/alexmojaki/pure_eval"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WHL_BP}.dist-info/LICENSE.txt;md5=a3d6c15f7859ae235a78f2758e5a48cf"

PYPI_PACKAGE = "pure-eval"

inherit python3-dir

do_unpack[depends] += "unzip-native:do_populate_sysroot"

DEPENDS += "python3"

WHL_BPN = "pure_eval"
WHL_BP = "pure_eval-${PV}"
WHL_PN = "pure_eval"
SRC_URI = "https://files.pythonhosted.org/packages/8e/37/efad0257dc6e593a18957422533ff0f87ede7c9c6ea010a2177d738fb82f/pure_eval-0.2.3-py3-none-any.whl;downloadfilename=${BP}.zip;subdir=${BP}"
# unzip pure_eval-0.2.3-py3-none-any.whl -d pure-eval/
# sha256sum pure_eval-0.2.3-py3-none-any.whl
SRC_URI[sha256sum] = "1db8e35b67b3d218d818ae653e27f06c3aa420901fa7b081ca98cbedc874e0d0"

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
