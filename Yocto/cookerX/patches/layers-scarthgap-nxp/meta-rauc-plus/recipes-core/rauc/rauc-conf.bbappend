FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

RAUC_KEYRING_FILE = "ca.cert.pem"

SRC_URI:append = " \
    file://system.conf \
    file://ca.cert.pem\
"

do_install:append() {
    install -d ${D}${sysconfdir}/rauc
    install -m 0644 ${WORKDIR}/system.conf ${D}${sysconfdir}/rauc/system.conf
    install -m 0644 ${WORKDIR}/ca.cert.pem ${D}${sysconfdir}/rauc/ca.cert.pem
}
