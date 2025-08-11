SUMMARY = "Python Client for ONVIF Camera"
DESCRIPTION = "ONVIF Client Implementation in Python"
HOMEPAGE = "http://github.com/quatanium/python-onvif"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=dbb461256be2f9b062aeb92e90da9fad"

# sha256sum onvif_zeep_async-4.0.1.tar.gz
#SRC_URI="https://files.pythonhosted.org/packages/source/o/onvif-zeep-async/onvif_zeep_async-4.0.1.tar.gz"
#SRC_URI[sha256sum] = "f8255c89a575c0f0e931a1dbf8bfef7459de304ea779085f02e303aad43c90d4"

# sha256sum onvif_zeep_async-3.1.13.tar.gz
SRC_URI="https://files.pythonhosted.org/packages/source/o/onvif-zeep-async/onvif_zeep_async-3.1.13.tar.gz"
SRC_URI[sha256sum] = "6dbf4eed605819e8fa0a7bf2fe9ecb2e0e2fb1ffe8352546c02e3dd1dc4a3448"

S = "${WORKDIR}/onvif_zeep_async-3.1.13"

#PYPI_PACKAGE = "onvif-zeep-async"

inherit python_setuptools_build_meta

RDEPENDS:${PN} += " \
    python3-aiohttp \
    python3-zeep \
    python3-requests \
    python3-requests-file \
    python3-xmltodict \
    python3-isodate \
    python3-platformdirs \
    python3-requests-toolbelt \
    python3-wsdiscovery \
"