[![](https://img.shields.io/badge/Powered%20by-lankahsu%20-brightgreen.svg)](https://github.com/lankahsu520/CrossCompilationX)
[![GitHub license][license-image]][license-url]
[![GitHub stars][stars-image]][stars-url]
[![GitHub forks][forks-image]][forks-url]
[![GitHub issues][issues-image]][issues-image]


[license-image]: https://img.shields.io/github/license/lankahsu520/CrossCompilationX.svg
[license-url]: https://github.com/lankahsu520/CrossCompilationX/blob/master/LICENSE
[stars-image]: https://img.shields.io/github/stars/lankahsu520/CrossCompilationX.svg
[stars-url]: https://github.com/lankahsu520/CrossCompilationX/stargazers
[forks-image]: https://img.shields.io/github/forks/lankahsu520/CrossCompilationX.svg
[forks-url]: https://github.com/lankahsu520/CrossCompilationX/network
[issues-image]: https://img.shields.io/github/issues/lankahsu520/CrossCompilationX.svg
[issues-url]: https://github.com/lankahsu520/CrossCompilationX/issues

# 1. Generate toolchain by ourselves

> 以下資料來源 [建立 gnu tool chain](http://yi-jyun.blogspot.com/2018/01/tool-chain.html)

## 1.1. which xxx-linux

#### A. aarch64-linux

```bash
export PJ_HOST=aarch64-linux
export PJ_ARCH=arm64
export PJ_GCC_CONFIGURE_FLAGS=
```

#### B. i386-linux

```bash
export PJ_HOST=i486-linux
export PJ_ARCH=i386
export PJ_GCC_CONFIGURE_FLAGS=--disable-libmpx
```

## 1.2. Generate toolchain

### 1.2.0. Download

```bash
export PJ_TOOLCHAIN_SDK=`pwd`

#** download *.tar.xz **
mkdir -p $PJ_TOOLCHAIN_SDK/pkgs; cd $PJ_TOOLCHAIN_SDK/pkgs
curl https://ftp.gnu.org/gnu/binutils/$PJ_BINUTILS_VERSION.tar.xz -o $PJ_BINUTILS_VERSION.tar.xz
curl https://ftp.gnu.org/gnu/gcc/$PJ_GCC_VERSION/$PJ_GCC_VERSION.tar.xz -o $PJ_GCC_VERSION.tar.xz
curl https://cdn.kernel.org/pub/linux/kernel/v$PJ_LINUX_KERNEL_VERSION_FOLDER/$PJ_LINUX_KERNEL_VERSION.tar.xz -o $PJ_LINUX_KERNEL_VERSION.tar.xz
curl https://ftp.gnu.org/gnu/glibc/$PJ_GLIBC_VERSION.tar.xz -o $PJ_GLIBC_VERSION.tar.xz

#** unpack *.tar.xz **
cd $PJ_TOOLCHAIN_SDK/pkgs
rm -rf $PJ_TOOLCHAIN_SDK/sources; mkdir -p $PJ_TOOLCHAIN_SDK/sources
for f in *.tar*; do tar xvf $f -C $PJ_TOOLCHAIN_SDK/sources/; done
```

### 1.2.1. Step by Step

#### A. gcc-7.2.0, glibc-2.26, linux-4.14.14

```bash
export PJ_GCC_VERSION_FULL=7.2.0
export PJ_GCC_VERSION=gcc-$PJ_GCC_VERSION_FULL
export PJ_GLIBC_VERSION_FULL=2.26
export PJ_GLIBC_VERSION=glibc-$PJ_GLIBC_VERSION_FULL
export PJ_BINUTILS_VERSION=binutils-2.29.1
export PJ_LINUX_KERNEL_VERSION_MAJOR=4.x
export PJ_LINUX_KERNEL_VERSION_FULL=4.14.14
export PJ_LINUX_KERNEL_VERSION=linux-$PJ_LINUX_KERNEL_VERSION_FULL

export PJ_TOOLCHAIN_PREFIX="$PJ_TOOLCHAIN_SDK/$PJ_HOST-$PJ_LINUX_KERNEL_VERSION_FULL-$PJ_GCC_VERSION_FULL-$PJ_GLIBC_VERSION_FULL"

```

#### B. Build

```bash
export PJ_TOOLCHAIN_SDK=`pwd`
export PJ_TOOLCHAIN_PATH=$PJ_TOOLCHAIN_PREFIX/bin
export PATH=$PJ_TOOLCHAIN_PATH:$PATH
mkdir -p $PJ_TOOLCHAIN_PREFIX

NPROC=`nproc`

#** binutils **
mkdir -p $PJ_TOOLCHAIN_SDK/build_xxx/$PJ_BINUTILS_VERSION
cd $PJ_TOOLCHAIN_SDK/build_xxx/$PJ_BINUTILS_VERSION
$PJ_TOOLCHAIN_SDK/libs/$PJ_BINUTILS_VERSION/configure --prefix=$PJ_TOOLCHAIN_PREFIX --target=$PJ_HOST
colormake -j$NPROC
colormake install

#** gcc (1st) **
cd $PJ_TOOLCHAIN_SDK/libs/$PJ_GCC_VERSION; ./contrib/download_prerequisites

mkdir -p $PJ_TOOLCHAIN_SDK/build_xxx/$PJ_GCC_VERSION
cd $PJ_TOOLCHAIN_SDK/build_xxx/$PJ_GCC_VERSION
$PJ_TOOLCHAIN_SDK/libs/$PJ_GCC_VERSION/configure --prefix=$PJ_TOOLCHAIN_PREFIX --target=$PJ_HOST --enable-languages=c,c++ $PJ_GCC_CONFIGURE_FLAGS
colormake -j$NPROC all-gcc
colormake install-gcc

#** linux header **
cd $PJ_TOOLCHAIN_SDK/libs/$PJ_LINUX_KERNEL_VERSION
make ARCH=$PJ_ARCH INSTALL_HDR_PATH=$PJ_TOOLCHAIN_PREFIX/$PJ_HOST headers_install

#** glibc (1st) **
mkdir -p $PJ_TOOLCHAIN_SDK/build_xxx/$PJ_GLIBC_VERSION
cd $PJ_TOOLCHAIN_SDK/build_xxx/$PJ_GLIBC_VERSION
$PJ_TOOLCHAIN_SDK/libs/$PJ_GLIBC_VERSION/configure --prefix=$PJ_TOOLCHAIN_PREFIX/$PJ_HOST --build=$MACHTYPE --host=$PJ_HOST --target=$PJ_HOST --with-headers=$PJ_TOOLCHAIN_PREFIX/$PJ_HOST/include
colormake install-headers

colormake -j$NPROC csu/subdir_lib
install csu/crt*.o $PJ_TOOLCHAIN_PREFIX/$PJ_HOST/lib/

# put NULL files
touch $PJ_TOOLCHAIN_PREFIX/$PJ_HOST/include/gnu/stubs.h
$PJ_HOST-gcc -nostdlib -nostartfiles -shared -x c /dev/null -o $PJ_TOOLCHAIN_PREFIX/$PJ_HOST/lib/libc.so

#** gcc (2nd) **
cd $PJ_TOOLCHAIN_SDK/build_xxx/$PJ_GCC_VERSION
colormake -j$NPROC all-target-libgcc
colormake install-target-libgcc

#** glibc (2nd) **
cd $PJ_TOOLCHAIN_SDK/build_xxx/$PJ_GLIBC_VERSION
colormake -j$NPROC
colormake install

#** gcc (3rd) **
cd $PJ_TOOLCHAIN_SDK/build_xxx/$PJ_GCC_VERSION
colormake -j$NPROC
colormake install

#** finish **
```

```bash
find
```

## 1.3. xxx-linux-xxx

#### A. aarch64-linux

```bash
$ ll $PJ_TOOLCHAIN_PATH
total 151592
drwxrwxr-x 2 lanka lanka     4096  一  16 10:58 ./
drwxrwxr-x 8 lanka lanka     4096  一  16 10:28 ../
-rwxr-xr-x 1 lanka lanka  6797144  一  16 10:09 aarch64-linux-addr2line*
-rwxr-xr-x 2 lanka lanka  7052544  一  16 10:09 aarch64-linux-ar*
-rwxr-xr-x 2 lanka lanka 10477104  一  16 10:09 aarch64-linux-as*
-rwxr-xr-x 2 lanka lanka  5488488  一  16 10:58 aarch64-linux-c++*
-rwxr-xr-x 1 lanka lanka  6742824  一  16 10:09 aarch64-linux-c++filt*
-rwxr-xr-x 1 lanka lanka  5485984  一  16 10:58 aarch64-linux-cpp*
-rwxr-xr-x 1 lanka lanka   270568  一  16 10:09 aarch64-linux-elfedit*
-rwxr-xr-x 2 lanka lanka  5488488  一  16 10:58 aarch64-linux-g++*
-rwxr-xr-x 2 lanka lanka  5482184  一  16 10:58 aarch64-linux-gcc*
-rwxr-xr-x 2 lanka lanka  5482184  一  16 10:58 aarch64-linux-gcc-7.2.0*
-rwxr-xr-x 1 lanka lanka   188400  一  16 10:58 aarch64-linux-gcc-ar*
-rwxr-xr-x 1 lanka lanka   188320  一  16 10:58 aarch64-linux-gcc-nm*
-rwxr-xr-x 1 lanka lanka   188336  一  16 10:58 aarch64-linux-gcc-ranlib*
-rwxr-xr-x 1 lanka lanka  4048624  一  16 10:58 aarch64-linux-gcov*
-rwxr-xr-x 1 lanka lanka  3393552  一  16 10:58 aarch64-linux-gcov-dump*
-rwxr-xr-x 1 lanka lanka  3641176  一  16 10:58 aarch64-linux-gcov-tool*
-rwxr-xr-x 1 lanka lanka  7565104  一  16 10:09 aarch64-linux-gprof*
-rwxr-xr-x 4 lanka lanka 10332216  一  16 10:09 aarch64-linux-ld*
-rwxr-xr-x 4 lanka lanka 10332216  一  16 10:09 aarch64-linux-ld.bfd*
-rwxr-xr-x 2 lanka lanka  6849600  一  16 10:09 aarch64-linux-nm*
-rwxr-xr-x 2 lanka lanka  8102632  一  16 10:09 aarch64-linux-objcopy*
-rwxr-xr-x 2 lanka lanka 10502224  一  16 10:09 aarch64-linux-objdump*
-rwxr-xr-x 2 lanka lanka  7052576  一  16 10:09 aarch64-linux-ranlib*
-rwxr-xr-x 2 lanka lanka  2348800  一  16 10:09 aarch64-linux-readelf*
-rwxr-xr-x 1 lanka lanka  6785040  一  16 10:09 aarch64-linux-size*
-rwxr-xr-x 1 lanka lanka  6786016  一  16 10:09 aarch64-linux-strings*
-rwxr-xr-x 2 lanka lanka  8102632  一  16 10:09 aarch64-linux-strip*
```

##### A.1. ld-linux-aarch64.so.1

```bash
$ cd $PJ_TOOLCHAIN_PREFIX
$ find-name ld-*.so*
aarch64-linux/lib/ld-linux-aarch64.so.1
aarch64-linux/lib/ld-2.26.so
$ ll aarch64-linux/lib/ld-linux-aarch64.so.1
lrwxrwxrwx 1 lanka lanka 10  一  16 10:54 aarch64-linux/lib/ld-linux-aarch64.so.1 -> ld-2.26.so*
```

#### B. i386-linux

```bash
$ ll $PJ_TOOLCHAIN_PATH
total 158724
drwxrwxr-x 2 lanka lanka     4096  一  16 14:18 ./
drwxrwxr-x 8 lanka lanka     4096  一  16 13:52 ../
-rwxr-xr-x 1 lanka lanka  7508736  一  16 13:39 i486-linux-addr2line*
-rwxr-xr-x 2 lanka lanka  7760064  一  16 13:39 i486-linux-ar*
-rwxr-xr-x 2 lanka lanka 10885000  一  16 13:39 i486-linux-as*
-rwxr-xr-x 2 lanka lanka  5406944  一  16 14:18 i486-linux-c++*
-rwxr-xr-x 1 lanka lanka  7458528  一  16 13:39 i486-linux-c++filt*
-rwxr-xr-x 1 lanka lanka  5404432  一  16 14:18 i486-linux-cpp*
-rwxr-xr-x 1 lanka lanka   270568  一  16 13:39 i486-linux-elfedit*
-rwxr-xr-x 2 lanka lanka  5406944  一  16 14:18 i486-linux-g++*
-rwxr-xr-x 2 lanka lanka  5400640  一  16 14:18 i486-linux-gcc*
-rwxr-xr-x 2 lanka lanka  5400640  一  16 14:18 i486-linux-gcc-7.2.0*
-rwxr-xr-x 1 lanka lanka   188400  一  16 14:18 i486-linux-gcc-ar*
-rwxr-xr-x 1 lanka lanka   188320  一  16 14:18 i486-linux-gcc-nm*
-rwxr-xr-x 1 lanka lanka   188336  一  16 14:18 i486-linux-gcc-ranlib*
-rwxr-xr-x 1 lanka lanka  4058792  一  16 14:18 i486-linux-gcov*
-rwxr-xr-x 1 lanka lanka  3403736  一  16 14:18 i486-linux-gcov-dump*
-rwxr-xr-x 1 lanka lanka  3659104  一  16 14:18 i486-linux-gcov-tool*
-rwxr-xr-x 1 lanka lanka  8272528  一  16 13:39 i486-linux-gprof*
-rwxr-xr-x 4 lanka lanka 10226048  一  16 13:39 i486-linux-ld*
-rwxr-xr-x 4 lanka lanka 10226048  一  16 13:39 i486-linux-ld.bfd*
-rwxr-xr-x 2 lanka lanka  7565288  一  16 13:39 i486-linux-nm*
-rwxr-xr-x 2 lanka lanka  8775280  一  16 13:39 i486-linux-objcopy*
-rwxr-xr-x 2 lanka lanka 10945736  一  16 13:39 i486-linux-objdump*
-rwxr-xr-x 2 lanka lanka  7760096  一  16 13:39 i486-linux-ranlib*
-rwxr-xr-x 2 lanka lanka  2348800  一  16 13:39 i486-linux-readelf*
-rwxr-xr-x 1 lanka lanka  7496648  一  16 13:39 i486-linux-size*
-rwxr-xr-x 1 lanka lanka  7493528  一  16 13:39 i486-linux-strings*
-rwxr-xr-x 2 lanka lanka  8775280  一  16 13:39 i486-linux-strip*
```

##### B.1. ld-linux.so.2

```bash
$ cd $PJ_TOOLCHAIN_PREFIX
$ find-name ld-*.so*
aarch64-linux/lib/ld-linux-aarch64.so.1
aarch64-linux/lib/ld-2.26.so
$ ll i486-linux/lib/ld-linux.so.2
lrwxrwxrwx 1 lanka lanka 10  一  16 14:01 i486-linux/lib/ld-linux.so.2 -> ld-2.26.so*
```

## 1.4. Hello World

```bash
$ cd /tmp
$ vi helloworld.c
#include <stdio.h>

int main(int argc, char *argv[])
{

	printf("Hello World !!!\n");
	return 0;
}

$ $PJ_HOST-gcc helloworld.c -o helloworld
```

#### A. aarch64-linux

```bash
$ file helloworld
helloworld: ELF 64-bit LSB executable, ARM aarch64, version 1 (SYSV), dynamically linked, interpreter /lib/ld-linux-aarch64.so.1, for GNU/Linux 3.7.0, with debug_info, not stripped

# run with qemu
$ sudo apt install qemu-user
$ sudo ln -sf $PJ_TOOLCHAIN_PREFIX/$PJ_HOST/lib/ld-2.26.so /lib/ld-linux-aarch64.so.1
$ qemu-aarch64 helloworld
Hello world !!!
```

```bash
$ $PJ_HOST-readelf -hl helloworld

$ $PJ_HOST-readelf -d $PJ_TOOLCHAIN_PREFIX/$PJ_HOST/lib64/libgcc_s.so
```

#### B. i386-linux

```bash
$ file helloworld
helloworld: ELF 32-bit LSB executable, Intel 80386, version 1 (SYSV), dynamically linked, interpreter /lib/ld-linux.so.2, for GNU/Linux 3.2.0, with debug_info, not stripped

$ $PJ_HOST-readelf -hl helloworld

# run
$ ./helloworld
Hello world !!!
```

# 2. crosstoolX

> 這邊簡化成使用 makefile，如有不同選擇，請參考 confs/aarch64-linux-4.14.14.sh 進行修改

#### A. aarch64-linux

>gcc-7.2.0, glibc-2.26, linux-4.14.14

```bash
$ cd crosstoolX
$ . confs/aarch64-linux-4.14.14.sh
$ make
$ ll $PJ_TOOLCHAIN_PREFIX
$ ll $PJ_TOOLCHAIN_PATH
```

#### B. i386-linux

> gcc-7.2.0, glibc-2.26, linux-4.14.14

```bash
$ cd crosstoolX
$ . confs/i486-linux-4.14.14.sh
$ make
$ ll $PJ_TOOLCHAIN_PREFIX
$ ll $PJ_TOOLCHAIN_PATH
```

# Appendix

# I. Study

## I.1. [建立 gnu tool chain](http://yi-jyun.blogspot.com/2018/01/tool-chain.html)

# II. Debug

## II.1. gcc-7.2.0/libmpx/mpxrt/mpxrt-utils.c:72:23: error: ‘PATH_MAX’ undeclared here (not in a func8_MAX’?

```bash
export PJ_GCC_CONFIGURE_FLAGS=--disable-libmpx
```

## II.2. scripts/unifdef.c:209:25: error: conflicting types for ‘getline’

```bash
$ vi scripts/unifdef.c
// change getline -> get_line
```

## II.3. glibc-2.31, *** These critical programs are missing or too old: compiler

> glibc: glibc-2.31
>
> gcc: gcc-5.5.0

# III. Glossary

# IV. Tool Usage

# Author

Created and designed by [Lanka Hsu](lankahsu@gmail.com).

# License

[CrossCompilationX](https://github.com/lankahsu520/CrossCompilationX) is available under the BSD-3-Clause license. See the LICENSE file for more info.

