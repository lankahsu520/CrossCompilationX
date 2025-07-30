FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://fw_env.config"

do_install:append() {
	# /etc/fw_env.config
	install -d ${D}${sysconfdir}
	install -m 0644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/fw_env.config
}