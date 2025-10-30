SUMMARY = "A Python SOAP client"
DESCRIPTION = "Zeep: Python SOAP client"
HOMEPAGE = "https://github.com/mvantellingen/python-zeep"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=23356a26b06086844496d9e634f58ae5"

# sha256sum zeep-4.3.1.tar.gz
SRC_URI[sha256sum] = "f45385e9e1b09d5550e0f51ab9fa7c6842713cab7194139372fd82a99c56a06e"

S = "${WORKDIR}/zeep-4.3.1"

#PYPI_PACKAGE = "zeep"

inherit pypi python_setuptools_build_meta
