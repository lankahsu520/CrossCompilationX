SUMMARY = "A Python sdk for Tuya Open API, which provides IoT capabilities, maintained by Tuya official"
DESCRIPTION = "A Python sdk for Tuya Open API, which provides basic IoT capabilities like device management, asset management and industry capabilities, helping you create IoT solutions. With diversified devices and industries, Tuya Cloud Development Platform opens basic IoT capabilities like device management, AI scenarios, and data analytics services, as well as industry capabilities, helping you create IoT solutions."
HOMEPAGE = "https://github.com/tuya/tuya-iot-app-sdk-python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0be66ebcb0ad47bd797b88f11e2744c6"

# sha256sum tuya-iot-py-sdk-0.6.6.tar.gz
SRC_URI[sha256sum] = "4764be929c967bcc1e24daad1dd4d852d2a9665df5410c0117c150853e44e057"

S = "${WORKDIR}/tuya-iot-py-sdk-0.6.6"

#PYPI_PACKAGE = "tuya-iot-py-sdk"

inherit pypi python_setuptools_build_meta

DEPENDS += " \
    python3-requests-native \
    python3-pycryptodome-native \
    python3-paho-mqtt-native \
"

#BBCLASSEXTEND = "native nativesdk"

RDEPENDS:${PN} += " \
    python3-requests \
    python3-pytz \
    python3-urllib3 \
    python3-paho-mqtt \
"