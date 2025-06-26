SUMMARY = "OpenGL / OpenGL ES Reference Compiler"
DESCRIPTION = "Glslang is the official reference compiler front end for the \
OpenGL ES and OpenGL shading languages. It implements a strict interpretation \
of the specifications for these languages. It is open and free for anyone to use, \
either from a command line or programmatically."
SECTION = "graphics"
HOMEPAGE = "https://www.khronos.org/opengles/sdk/tools/Reference-Compiler"
LICENSE = "BSD-3-Clause & BSD-2-Clause & MIT & Apache-2.0 & GPL-3-with-bison-exception"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=c5ce49c0456e9b413b98a4368c378229"

SRC_URI = "https://github.com/KhronosGroup/glslang/archive/refs/tags/11.3.0.tar.gz"
SRC_URI[sha256sum] = "2221ff544a301269f0762facd2d62f1399f57ff49f5b57683421891665546425"

S = "${WORKDIR}/glslang-11.3.0"

DEPENDS = "python3-native"

inherit cmake pkgconfig

EXTRA_OECMAKE += "-DCMAKE_SKIP_RPATH=ON"

BBCLASSEXTEND = "native nativesdk"