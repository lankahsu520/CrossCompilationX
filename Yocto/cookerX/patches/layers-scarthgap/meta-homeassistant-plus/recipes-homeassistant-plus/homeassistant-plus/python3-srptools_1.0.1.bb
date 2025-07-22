SUMMARY = "Tools to implement Secure Remote Password (SRP) authentication"
DESCRIPTION = "SRP is a secure password-based authentication and key-exchange protocol - a password-authenticated key agreement protocol (PAKE)."
HOMEPAGE = "https://github.com/idlesign/srptools"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ab61e61d6fda178bef6ff39ab2f571c2"

# sha256sum srptools-1.0.1.tar.gz
SRC_URI[sha256sum] = "7fa4337256a1542e8f5bb4bed19e1d9aea98fe5ff9baf76693342a1dd6ac7c96"

S = "${WORKDIR}/srptools-1.0.1"

inherit pypi python_setuptools_build_meta
