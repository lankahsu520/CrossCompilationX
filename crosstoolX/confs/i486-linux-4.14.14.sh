#!/bin/bash

export PJ_ROOT=`pwd`
export PJ_BUILDER=`whoami`
export PJ_SH_CP="cp -avrf"
export PJ_SH_MKDIR="mkdir -p"
export PJ_SH_RMDIR="rm -rf"
export PJ_SH_RM="rm -f"

export PJ_SH_SED_OPTION="-i "
if [[ "$OSTYPE" == "darwin"* ]]; then
	PJ_SH_SED_OPTION="-i ''"
fi
export PJ_SH_SED="sed $PJ_SH_SED_OPTION"

#******************************************************************************
#** i486-linux-4.14.14 **
#******************************************************************************
export PJ_TARGET=i386
export PJ_HOST=i486-linux
export PJ_CROSS=${PJ_HOST}-
export PJ_ARCH=$PJ_TARGET
export PJ_GCC_CONFIGURE_FLAGS=--disable-libmpx

export PJ_GCC_VERSION_FULL=7.2.0
export PJ_GCC_VERSION=gcc-$PJ_GCC_VERSION_FULL
export PJ_GLIBC_VERSION_FULL=2.26
export PJ_GLIBC_VERSION=glibc-$PJ_GLIBC_VERSION_FULL
export PJ_BINUTILS_VERSION=binutils-2.29.1

export PJ_LINUX_KERNEL_VERSION_FOLDER=4.x
export PJ_LINUX_KERNEL_VERSION_FULL=4.14.14
export PJ_LINUX_KERNEL_VERSION=linux-$PJ_LINUX_KERNEL_VERSION_FULL

export PJ_NAME="$PJ_HOST-$PJ_LINUX_KERNEL_VERSION_FULL"

export PJ_TOOLCHAIN_PREFIX="$PJ_ROOT/$PJ_NAME-$PJ_GCC_VERSION_FULL-$PJ_GLIBC_VERSION_FULL"
export PJ_TOOLCHAIN_PATH="$PJ_ROOT/$PJ_NAME-$PJ_GCC_VERSION_FULL-$PJ_GLIBC_VERSION_FULL/bin"
#export PJ_TOOLCHAIN_PATH="$PJ_ROOT/$PJ_HOST"

export PJ_MODE="RELEASE"

export PJ_CROSSTOOLX_SDK=$PJ_ROOT

#******************************************************************************
#** CFLAGS &  LDFLAGS**
#******************************************************************************


#******************************************************************************
#** common **
#******************************************************************************
. ${PJ_ROOT}/confs/common/common.conf