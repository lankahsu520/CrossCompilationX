SUMMARY = "A powerful declarative symmetric parser/builder for binary data"
DESCRIPTION = "Construct is a powerful declarative and symmetrical parser and builder for binary data."
HOMEPAGE = "https://construct.readthedocs.io/en/latest"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WHL_BP}.dist-info/LICENSE;md5=202b39559c1c79fe4715ce81e9e0ac02"

PYPI_PACKAGE = "home-assistant-chip-core"

inherit python3-dir

do_unpack[depends] += "unzip-native:do_populate_sysroot"

DEPENDS += "python3"

WHL_BPN = "construct"
WHL_BP = "construct-${PV}"
WHL_PN = "construct"
SRC_URI = "https://files.pythonhosted.org/packages/b2/fb/08b3f4bf05da99aba8ffea52a558758def16e8516bc75ca94ff73587e7d3/construct-2.10.70-py3-none-any.whl;downloadfilename=${BP}.zip;subdir=${BP}"
# unzip construct-2.10.70-py3-none-any.whl -d construct/
# sha256sum construct-2.10.70-py3-none-any.whl
SRC_URI[sha256sum] = "c80be81ef595a1a821ec69dc16099550ed22197615f4320b57cc9ce2a672cb30"

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
