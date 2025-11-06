FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
     file://matter-server.service \
"

inherit systemd

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE:${PN} = "matter_server.service"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/matter_server.service ${D}${systemd_unitdir}/system
}

