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
export PJ_TOOLCHAIN_PATH="/opt/lanka-aarch64-gcc720"
```

#### B. i386-linux

```bash
export PJ_HOST=i486-linux
export PJ_ARCH=i386
export PJ_GCC_CONFIGURE_FLAGS=--disable-libmpx
export PJ_TOOLCHAIN_PATH="/opt/lanka-i386-gcc720"
```

## 1.2. Generate step by step

>binutils-2.29.1.tar.xz
>
>gcc-7.2.0.tar.xz
>
>linux-4.14.336.tar.xz
>
>glibc-2.26.tar.xz

```bash
export PJ_TOOLCHAIN_SDK=/work/codebase/toolchainSDK/toolchain_123
export PJ_GCC_VERSION=gcc-7.2.0
export PJ_GLIBC_VERSION=glibc-2.26
export PJ_BINUTILS_VERSION=binutils-2.29.1
export PJ_LINUX_KERNEL_VERSION=linux-4.14.14

export PATH=$PJ_TOOLCHAIN_PATH/bin/:$PATH

mkdir -p $PJ_TOOLCHAIN_SDK/pkgs
mkdir -p $PJ_TOOLCHAIN_SDK/sources
mkdir -p $PJ_TOOLCHAIN_SDK/objs
mkdir -p $PJ_TOOLCHAIN_PATH

#** download *.tar.xz **
cd $PJ_TOOLCHAIN_SDK/pkgs
curl https://ftp.gnu.org/gnu/binutils/binutils-2.29.1.tar.xz -o binutils-2.29.1.tar.xz
curl https://ftp.gnu.org/gnu/gcc/gcc-7.2.0/gcc-7.2.0.tar.xz -o gcc-7.2.0.tar.xz
curl https://cdn.kernel.org/pub/linux/kernel/v4.x/linux-4.14.336.tar.xz -o linux-4.14.336.tar.xz
curl https://ftp.gnu.org/gnu/glibc/glibc-2.26.tar.xz -o glibc-2.26.tar.xz

#** unpack *.tar.xz **
for f in *.tar*; do tar xf $f -C ../sources/; done

cd $PJ_TOOLCHAIN_SDK/sources/$PJ_GCC_VERSION; ./contrib/download_prerequisites

cd $PJ_TOOLCHAIN_SDK/objs; mkdir $PJ_BINUTILS_VERSION  $PJ_GCC_VERSION  $PJ_GLIBC_VERSION

#** binutils **
cd $PJ_TOOLCHAIN_SDK/objs/$PJ_BINUTILS_VERSION
../../sources/$PJ_BINUTILS_VERSION/configure --prefix=$PJ_TOOLCHAIN_PATH --target=$PJ_HOST
colormake `-j$(nproc)`
colormake install

#** gcc (1st) **
cd $PJ_TOOLCHAIN_SDK/objs/$PJ_GCC_VERSION
../../sources/$PJ_GCC_VERSION/configure --prefix=$PJ_TOOLCHAIN_PATH --target=$PJ_HOST --enable-languages=c,c++ $PJ_GCC_CONFIGURE_FLAGS
colormake `-j$(nproc)` all-gcc
colormake install-gcc

#** linux header **
cd $PJ_TOOLCHAIN_SDK/sources/$PJ_LINUX_KERNEL_VERSION
make ARCH=$PJ_ARCH INSTALL_HDR_PATH=$PJ_TOOLCHAIN_PATH/$PJ_HOST headers_install

#** glibc (1st) **
cd $PJ_TOOLCHAIN_SDK/objs/$PJ_GLIBC_VERSION
../../sources/$PJ_GLIBC_VERSION/configure --prefix=$PJ_TOOLCHAIN_PATH/$PJ_HOST --build=$MACHTYPE --host=$PJ_HOST --target=$PJ_HOST --with-headers=$PJ_TOOLCHAIN_PATH/$PJ_HOST/include
colormake install-headers

colormake `-j$(nproc)` csu/subdir_lib
install csu/crt*.o $PJ_TOOLCHAIN_PATH/$PJ_HOST/lib/

# put NULL files
touch $PJ_TOOLCHAIN_PATH/$PJ_HOST/include/gnu/stubs.h
$PJ_HOST-gcc -nostdlib -nostartfiles -shared -x c /dev/null -o $PJ_TOOLCHAIN_PATH/$PJ_HOST/lib/libc.so

#** gcc (2nd) **
cd $PJ_TOOLCHAIN_SDK/objs/$PJ_GCC_VERSION
colormake `-j$(nproc)` all-target-libgcc
colormake install-target-libgcc

#** glibc (2nd) **
cd $PJ_TOOLCHAIN_SDK/objs/$PJ_GLIBC_VERSION
colormake `-j$(nproc)`
colormake install

#** gcc (3rd) **
cd $PJ_TOOLCHAIN_SDK/objs/$PJ_GCC_VERSION
colormake `-j$(nproc)`
colormake install

#** finish **
```

```bash
find
```

## 1.3. xxx-linux-xxx

#### A. aarch64-linux

```bash
$ ll $PJ_TOOLCHAIN_PATH/bin
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
$ cd $PJ_TOOLCHAIN_PATH
$ find-name ld-*.so*
aarch64-linux/lib/ld-linux-aarch64.so.1
aarch64-linux/lib/ld-2.26.so
$ ll aarch64-linux/lib/ld-linux-aarch64.so.1
lrwxrwxrwx 1 lanka lanka 10  一  16 10:54 aarch64-linux/lib/ld-linux-aarch64.so.1 -> ld-2.26.so*
```

#### B. i386-linux

```bash
$ ll $PJ_TOOLCHAIN_PATH/bin
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
$ cd $PJ_TOOLCHAIN_PATH
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
$ sudo ln -sf $PJ_TOOLCHAIN_PATH/$PJ_HOST/lib/ld-2.26.so /lib/ld-linux-aarch64.so.1
$ qemu-aarch64 helloworld
Hello world !!!

$ $PJ_HOST-readelf -hl helloworld

$ $PJ_HOST-readelf -d $PJ_TOOLCHAIN_PATH/$PJ_HOST/lib64/libgcc_s.so

Dynamic section at offset 0x11dc8 contains 27 entries:
  Tag        Type                         Name/Value
 0x0000000000000001 (NEEDED)             Shared library: [libc.so.6]
 0x000000000000000e (SONAME)             Library soname: [libgcc_s.so.1]
 0x000000000000000c (INIT)               0x2520
 0x000000000000000d (FINI)               0x103b0
 0x0000000000000019 (INIT_ARRAY)         0x21db8
 0x000000000000001b (INIT_ARRAYSZ)       8 (bytes)
 0x000000000000001a (FINI_ARRAY)         0x21dc0
 0x000000000000001c (FINI_ARRAYSZ)       8 (bytes)
 0x0000000000000004 (HASH)               0x190
 0x0000000000000005 (STRTAB)             0x14d0
 0x0000000000000006 (SYMTAB)             0x618
 0x000000000000000a (STRSZ)              2066 (bytes)
 0x000000000000000b (SYMENT)             24 (bytes)
 0x0000000000000003 (PLTGOT)             0x21fe8
 0x0000000000000002 (PLTRELSZ)           1080 (bytes)
 0x0000000000000014 (PLTREL)             RELA
 0x0000000000000017 (JMPREL)             0x20e8
 0x0000000000000007 (RELA)               0x2028
 0x0000000000000008 (RELASZ)             192 (bytes)
 0x0000000000000009 (RELAENT)            24 (bytes)
 0x000000006ffffffc (VERDEF)             0x1e20
 0x000000006ffffffd (VERDEFNUM)          14
 0x000000006ffffffe (VERNEED)            0x2008
 0x000000006fffffff (VERNEEDNUM)         1
 0x000000006ffffff0 (VERSYM)             0x1ce2
 0x000000006ffffff9 (RELACOUNT)          3
 0x0000000000000000 (NULL)               0x0
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

# Appendix

# I. Study

## I.1. [建立 gnu tool chain](http://yi-jyun.blogspot.com/2018/01/tool-chain.html)

# II. Debug

# III. Glossary

# IV. Tool Usage

# Author

Created and designed by [Lanka Hsu](lankahsu@gmail.com).

# License

[CrossCompilationX](https://github.com/lankahsu520/CrossCompilationX) is available under the BSD-3-Clause license. See the LICENSE file for more info.

