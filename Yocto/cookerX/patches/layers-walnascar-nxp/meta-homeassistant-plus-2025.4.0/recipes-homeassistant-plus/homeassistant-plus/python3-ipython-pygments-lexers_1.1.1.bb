SUMMARY = "Defines a variety of Pygments lexers for highlighting IPython code."
DESCRIPTION = "A Pygments plugin for IPython code & console sessions"
HOMEPAGE = "https://github.com/ipython/ipython-pygments-lexers"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WHL_BP}.dist-info/LICENSE;md5=59028f809aa24c7b88bff27927343d58"

PYPI_PACKAGE = "ipython-pygments-lexers"

inherit python3-dir

do_unpack[depends] += "unzip-native:do_populate_sysroot"

DEPENDS += "python3"

WHL_BPN = "ipython_pygments_lexers"
WHL_BP = "ipython_pygments_lexers-${PV}"
WHL_PN = "ipython_pygments_lexers"
SRC_URI = "https://files.pythonhosted.org/packages/d9/33/1f075bf72b0b747cb3288d011319aaf64083cf2efef8354174e3ed4540e2/ipython_pygments_lexers-1.1.1-py3-none-any.whl;downloadfilename=${BP}.zip;subdir=${BP}"
# unzip ipython_pygments_lexers-1.1.1-py3-none-any.whl -d ipython-pygments-lexers/
# sha256sum ipython_pygments_lexers-1.1.1-py3-none-any.whl
SRC_URI[sha256sum] = "a9462224a505ade19a605f71f8fa63c2048833ce50abc86768a0d81d876dc81c"

do_install() {
    #install -d ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BPN}
    install -d ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info

    # install -m 644 ${S}/${WHL_BPN}/* ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BPN}/
    cp -r ${S}/ipython_pygments_lexers.py ${D}${libdir}/${PYTHON_DIR}/site-packages/ipython_pygments_lexers.py
    install -m 644 ${S}/${WHL_BP}.dist-info/* ${D}${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info/
}

FILES:${PN} += "\
    ${libdir}/${PYTHON_DIR}/site-packages/ipython_pygments_lexers.py \
    ${libdir}/${PYTHON_DIR}/site-packages/${WHL_BP}.dist-info \
"
