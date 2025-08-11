SUMMARY = "bitbake-layers recipe"
DESCRIPTION = "Recipe created by bitbake-layers"
LICENSE = "MIT"

python do_display_banner() {
    bb.plain("***********************************************");
    bb.plain("*                                             *");
    bb.plain("*  Example recipe created by bitbake-layers   *");
    bb.plain("*                                             *");
    bb.plain("***********************************************");
}

addtask display_banner before do_build

LIC_FILES_CHKSUM = "file://Makefile;md5=7740947326d14f13a56b127b79e3cda2"

SRC_URI = " \
    file://helloworld123.c \
    file://Makefile \
"

S = "${WORKDIR}"

do_compile() {
	#${CC} ${LDFLAGS} helloworld123.c -o helloworld123
	oe_runmake -f Makefile
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 helloworld123 ${D}${bindir}
}
