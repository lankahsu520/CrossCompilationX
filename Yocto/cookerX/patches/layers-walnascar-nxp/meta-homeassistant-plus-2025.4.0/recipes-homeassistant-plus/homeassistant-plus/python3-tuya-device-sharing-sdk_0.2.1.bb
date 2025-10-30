SUMMARY = "A Python sdk for Tuya Open API, which provides IoT capabilities, maintained by Tuya official"
DESCRIPTION = "A Python sdk for Tuya Open API, which provides basic IoT capabilities like device management capabilities, helping you create IoT solutions. With diversified devices and industries, Tuya IoT Development Platform opens basic IoT capabilities like device management, AI scenarios, and data analytics services, as well as industry capabilities, helping you create IoT solutions."
HOMEPAGE = "https://github.com/tuya/tuya-iot-app-sdk-python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f178fab8744e66c739377b0615c7c9d8"

# "PACKAGECONFIG:append:pn-openssl = ' legacy'"
# pip download tuya-device-sharing-sdk==0.2.1 --no-deps
# sha256sum tuya-device-sharing-sdk-0.2.1.tar.gz
SRC_URI[sha256sum] = "72e2c97c22109a7c9208dd727b3c72aad1669d351d843f6d63db0160d87d8942"

S = "${WORKDIR}/tuya-device-sharing-sdk-0.2.1"

#PYPI_PACKAGE = "tuya-device-sharing-sdk"

inherit pypi python_setuptools_build_meta

do_configure:prepend() {
    export CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1
    if [ ! -f ${S}/requirements.txt ]; then
        echo "cryptography" > ${S}/requirements.txt
        echo "pycryptodome" >> ${S}/requirements.txt
        echo "requests" >> ${S}/requirements.txt
    fi
}

do_compile:prepend() {
    export CRYPTOGRAPHY_OPENSSL_NO_LEGACY=1
}

DEPENDS += " \
    python3-cryptography-native \
    python3-requests-native \
    python3-pycryptodome-native \
    python3-paho-mqtt-native \
"

RDEPENDS:${PN} += " \
    python3-cryptography \
    python3-requests \
    python3-paho-mqtt \
"