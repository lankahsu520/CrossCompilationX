FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
     file://homeassistant-root.service \
"

#SRC_URI += "file://automations.yaml \
#            file://configuration.yaml \
#            file://scenes.yaml \
#            file://scripts.yaml \
#            file://secrets.yaml \
#"

#HOMEASSISTANT_CONFIG_DIR ?= "${localstatedir}/lib/homeassistant"
#HOMEASSISTANT_CONFIG_DIR= "/root/.homeassistant"

RDEPENDS:${PN} += " \
    python3-onvif-zeep-async \
    python3-pyatv \
    python3-aiohomekit \
    python3-py-synologydsm-api \
    python3-pysensibo \
    python3-python-otbr-api \
    python3-miniaudio \
    python3-tuya-device-sharing-sdk \
    python3-home-assistant-chip-core \
"

#do_install:append() {
#    #install -d ${D}${HOMEASSISTANT_CONFIG_DIR}
#    #install -m 0644 ${UNPACKDIR}/*.yaml ${D}${HOMEASSISTANT_CONFIG_DIR}/
#    rm -rf ${D}/root
#}

