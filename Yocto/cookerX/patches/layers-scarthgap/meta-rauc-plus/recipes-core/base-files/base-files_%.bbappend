FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
	file://etc/fstab \
"

do_install:append() {
	install -m 0755 ${WORKDIR}/etc/fstab ${D}${sysconfdir}/fstab
}