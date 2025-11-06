SUMMARY = "Python-base APIs and tools for CHIP"
HOMEPAGE = "https://github.com/home-assistant"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${WHL_BP}.dist-info/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

PYPI_PACKAGE = "home-assistant-chip-core"

inherit python3-dir

do_unpack[depends] += "unzip-native:do_populate_sysroot"

DEPENDS += "python3"

WHL_BPN = "chip"
WHL_BP = "home_assistant_chip_core-${PV}"
WHL_PN = "home_assistant_chip_core"
SRC_URI = "https://files.pythonhosted.org/packages/a4/87/2aaf26a9a9eac6f02abfc0af778a147bbfdd1e64a5f5d116ca9ef2f7cc21/home_assistant_chip_core-2024.11.4-cp37-abi3-manylinux_2_31_aarch64.whl;downloadfilename=${BP}.zip;subdir=${BP}"
# unzip home_assistant_chip_core-2024.11.4-cp37-abi3-manylinux_2_31_aarch64.whl -d home_assistant_chip_core/
# sha256sum home_assistant_chip_core-2024.11.4-cp37-abi3-manylinux_2_31_aarch64.whl
SRC_URI[sha256sum] = "a5df2196aa1b771b6bc60d61123bb9dca6e8e570a5533d2876d3905ea3bd1f14"

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