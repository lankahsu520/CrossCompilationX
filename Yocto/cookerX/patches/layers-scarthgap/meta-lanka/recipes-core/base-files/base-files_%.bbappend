FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
	file://etc/profile.d/alias.sh \
"

do_install:append() {
	# /etc/profile.d
	install -d ${D}${sysconfdir}/profile.d
	install -m 0755 ${WORKDIR}/etc/profile.d/alias.sh ${D}${sysconfdir}/profile.d/alias.sh
}