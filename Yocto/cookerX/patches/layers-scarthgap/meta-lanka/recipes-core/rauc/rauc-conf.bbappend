FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

RAUC_KEYRING_FILE = "keyring.pem"

SRC_URI:append = " \
    file://system.conf \
    file://keyring.pem \
"

do_install:append() {
    install -d ${D}${sysconfdir}/rauc
    install -m 0644 ${WORKDIR}/system.conf ${D}${sysconfdir}/rauc/system.conf
    install -m 0644 ${WORKDIR}/keyring.pem ${D}${sysconfdir}/rauc/keyring.pem
}
