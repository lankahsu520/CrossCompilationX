SUMMARY = "Infer file type and MIME type of any file/buffer. No external dependencies."
DESCRIPTION = "Small and dependency free Python package to infer file type and MIME type checking the magic numbers signature of a file or buffer."
HOMEPAGE = "https://github.com/h2non/filetype.py"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=cc0e8af5f14a75ce53b7b9db0f4bd91e"

# sha256sum filetype-1.2.0.tar.gz
SRC_URI[sha256sum] = "66b56cd6474bf41d8c54660347d37afcc3f7d1970648de365c102ef77548aadb"

#SRC_URI += "file://ProtocolMessage_pb2.py"

S = "${WORKDIR}/filetype-1.2.0"

#PYPI_PACKAGE = "filetype"

inherit pypi python_setuptools_build_meta

#DEPENDS += "python3-build-native python3-setuptools-native python3-pytest-runner-native"

#BBCLASSEXTEND = "native nativesdk"

