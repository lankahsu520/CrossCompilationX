SUMMARY = "Python-base APIs and tools for CHIP"
HOMEPAGE = "https://github.com/home-assistant"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WHL_BP}.dist-info/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

PYPI_PACKAGE = "home-assistant-chip-core"

# unzip home_assistant_chip_core-2025.4.0-cp37-abi3-manylinux_2_31_aarch64.whl -d extracted_whl/

inherit python3-dir

do_unpack[depends] += "unzip-native:do_populate_sysroot"

DEPENDS += "python3"

WHL_BPN = "chip"
WHL_BP = "home_assistant_chip_core-${PV}"
WHL_PN = "home_assistant_chip_core"
SRC_URI = "https://files.pythonhosted.org/packages/05/81/f6c6008d7ac1bb2eff5ae27c76c26410547d894f1ca445b9d4a6a2319301/home_assistant_chip_core-2025.4.0-cp37-abi3-manylinux_2_31_aarch64.whl;downloadfilename=${BP}.zip;subdir=${BP}"
# sha256sum home_assistant_chip_core-2025.4.0-cp37-abi3-manylinux_2_31_aarch64.whl
SRC_URI[sha256sum] = "4ccbb06d439c2fbaf553492266226ea7168bd9c8abb8fe810ed6994c7d992906"

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

DEPENDS += " \
    glib-2.0 \
    libnl \
"

RDEPENDS:${PN} += " \
    python3-core \
    python3-logging \
    python3-asyncio \
    python3-typing-extensions \
    python3-cryptography \
    python3-requests \
    python3-construct \
    python3-deprecation \
    python3-ipdb \
    python3-rich \
    python3-ipython \
    python3-markdown-it-py \
    python3-jedi \
    python3-mdurl \
    python3-prompt-toolkit \
    python3-traitlets \
    python3-ipython-pygments-lexers \
    python3-matplotlib-inline \
    python3-stack-data \
    python3-asttokens \
    python3-executing \
    python3-parso \
    python3-pure-eval \
    python3-wcwidth \
"

#INSANE_SKIP:${PN} += "file-rdeps"