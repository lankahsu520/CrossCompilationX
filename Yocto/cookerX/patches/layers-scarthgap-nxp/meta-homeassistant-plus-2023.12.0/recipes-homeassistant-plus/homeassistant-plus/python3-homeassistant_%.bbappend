FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://automations.yaml \
            file://configuration.yaml \
            file://scenes.yaml \
            file://scripts.yaml \
            file://secrets.yaml \
"

HOMEASSISTANT_CONFIG_DIR ?= "${localstatedir}/lib/homeassistant"

RDEPENDS:${PN} += " \
    python3-pyasn1 \
    python3-pydantic \
    python3-onvif-zeep-async \
    python3-pyatv \
    python3-aiohomekit \
    python3-py-synologydsm-api \
    python3-pysensibo \
    python3-python-otbr-api \
    python3-miniaudio \
    python3-tuya-iot-py-sdk \
"

do_install:append() {
    install -d ${D}${HOMEASSISTANT_CONFIG_DIR}
    install -m 0644 ${WORKDIR}/*.yaml ${D}${HOMEASSISTANT_CONFIG_DIR}/
}


