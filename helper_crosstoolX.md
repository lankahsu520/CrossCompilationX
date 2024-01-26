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

# 1. Overview

> 本篇存在的目的是當無良的主管拿台“未知的電腦”給開發人員，沒有附上任何開發工具和文件，而要求在上面加工，雖然有提供帳號和密碼登入其裝置。

> 這時可憐的開發人員也只能登入系統，查看這水有多深。

## 1.1. Check the system

#### A. Release

```bash
$ uname -a

$ cat /proc/version

$ cat /etc/os-release

$ cat /etc/issue

$ cat /etc/debian_version

$ ls -al /bin/sh
```

#### B. Libraries

```bash
$ ldd --version

$ ls -al /lib
$ ls -al /usr/lib

$ ll /lib/ld-*

$ file /bin/bash

$ cd /; find * -name *.h
```

#### C. CPU

```bash
$ cat /proc/cpuinfo
```

#### D. Memory

```bash
$ grep MemTotal /proc/meminfo
```

#### E. Disk

```bash
$ df -h

$ mount
```

#### F. Compiler

```bash
$ gcc --version

$ make --version

$ cmake --version

# check binutils version
$ ld -v

$ meson --version
$ ninja --version

$ python --version
```

# 2. Generate toolchain by ourselves

> 以下資料來源 [建立 gnu tool chain](http://yi-jyun.blogspot.com/2018/01/tool-chain.html)

> 建議可以先使用 crosstool-ng，除非無法解決問題，才執行下面的程序。

## 2.1. ARCH

#### A. aarch64-linux-XXX

```bash
export PJ_HOST=aarch64-linux
export PJ_ARCH=arm64
export PJ_GCC_CONFIGURE_FLAGS=
```

#### B. i486-linux-4.14.14-XXX

```bash
export PJ_HOST=i486-linux
export PJ_ARCH=i386
export PJ_GCC_CONFIGURE_FLAGS=--disable-libmpx
```

## 2.2. Compiling Packages

#### A. linux-4.14.14, gcc-7.2.0, glibc-2.26

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

#### B. Download packages

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

#### B. Building a toolchain step by step

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

## 2.3. Check xxx-linux-xxx

#### A. aarch64-linux-4.14.14-7.2.0-2.26

##### A.1. aarch64-linux-4.14.14-7.2.0-2.26/bin

```bash
$ ll $PJ_TOOLCHAIN_PATH
$ ll aarch64-linux-4.14.14-7.2.0-2.26/bin
total 151852
drwxrwxr-x 2 lanka lanka     4096  一  17 12:52 ./
drwxrwxr-x 8 lanka lanka     4096  一  17 11:30 ../
-rwxr-xr-x 1 lanka lanka  6810152  一  17 12:43 aarch64-linux-addr2line*
-rwxr-xr-x 2 lanka lanka  7062048  一  17 12:43 aarch64-linux-ar*
-rwxr-xr-x 2 lanka lanka 10493584  一  17 12:43 aarch64-linux-as*
-rwxr-xr-x 2 lanka lanka  5497472  一  17 12:52 aarch64-linux-c++*
-rwxr-xr-x 1 lanka lanka  6755664  一  17 12:43 aarch64-linux-c++filt*
-rwxr-xr-x 1 lanka lanka  5494968  一  17 12:52 aarch64-linux-cpp*
-rwxr-xr-x 1 lanka lanka   271544  一  17 12:43 aarch64-linux-elfedit*
-rwxr-xr-x 2 lanka lanka  5497472  一  17 12:52 aarch64-linux-g++*
-rwxr-xr-x 2 lanka lanka  5491168  一  17 12:52 aarch64-linux-gcc*
-rwxr-xr-x 2 lanka lanka  5491168  一  17 12:52 aarch64-linux-gcc-7.2.0*
-rwxr-xr-x 1 lanka lanka   189912  一  17 12:52 aarch64-linux-gcc-ar*
-rwxr-xr-x 1 lanka lanka   189800  一  17 12:52 aarch64-linux-gcc-nm*
-rwxr-xr-x 1 lanka lanka   189816  一  17 12:52 aarch64-linux-gcc-ranlib*
-rwxr-xr-x 1 lanka lanka  4055144  一  17 12:52 aarch64-linux-gcov*
-rwxr-xr-x 1 lanka lanka  3399680  一  17 12:52 aarch64-linux-gcov-dump*
-rwxr-xr-x 1 lanka lanka  3652112  一  17 12:52 aarch64-linux-gcov-tool*
-rwxr-xr-x 1 lanka lanka  7575792  一  17 12:43 aarch64-linux-gprof*
-rwxr-xr-x 4 lanka lanka 10352648  一  17 12:43 aarch64-linux-ld*
-rwxr-xr-x 4 lanka lanka 10352648  一  17 12:43 aarch64-linux-ld.bfd*
-rwxr-xr-x 2 lanka lanka  6862632  一  17 12:43 aarch64-linux-nm*
-rwxr-xr-x 2 lanka lanka  8112896  一  17 12:43 aarch64-linux-objcopy*
-rwxr-xr-x 2 lanka lanka 10517816  一  17 12:43 aarch64-linux-objdump*
-rwxr-xr-x 2 lanka lanka  7062080  一  17 12:43 aarch64-linux-ranlib*
-rwxr-xr-x 2 lanka lanka  2350480  一  17 12:43 aarch64-linux-readelf*
-rwxr-xr-x 1 lanka lanka  6797976  一  17 12:43 aarch64-linux-size*
-rwxr-xr-x 1 lanka lanka  6794856  一  17 12:43 aarch64-linux-strings*
-rwxr-xr-x 2 lanka lanka  8112888  一  17 12:43 aarch64-linux-strip*
```

##### A.2. ld-linux-aarch64.so.1

```bash
$ find-name ld-*.so*
aarch64-linux-4.14.14-7.2.0-2.26/aarch64-linux/lib/ld-linux-aarch64.so.1
aarch64-linux-4.14.14-7.2.0-2.26/aarch64-linux/lib/ld-2.26.so

$ ll aarch64-linux-4.14.14-7.2.0-2.26/aarch64-linux/lib/ld-linux-aarch64.so.1
lrwxrwxrwx 1 lanka lanka 10  一  17 12:51 aarch64-linux-4.14.14-7.2.0-2.26/aarch64-linux/lib/ld-linux-aarch64.so.1 -> ld-2.26.so*
```

#### B. i486-linux-4.14.14-7.2.0-2.26

##### B.1. i486-linux-4.14.14-7.2.0-2.26/bin

```bash
$ ll $PJ_TOOLCHAIN_PATH
$ ll i486-linux-4.14.14-7.2.0-2.26/bin
total 158992
drwxrwxr-x 2 lanka lanka     4096  一  17 13:23 ./
drwxrwxr-x 8 lanka lanka     4096  一  17 13:20 ../
-rwxr-xr-x 1 lanka lanka  7518760  一  17 13:17 i486-linux-addr2line*
-rwxr-xr-x 2 lanka lanka  7774784  一  17 13:17 i486-linux-ar*
-rwxr-xr-x 2 lanka lanka 10906336  一  17 13:17 i486-linux-as*
-rwxr-xr-x 2 lanka lanka  5415848  一  17 13:23 i486-linux-c++*
-rwxr-xr-x 1 lanka lanka  7468392  一  17 13:17 i486-linux-c++filt*
-rwxr-xr-x 1 lanka lanka  5413344  一  17 13:23 i486-linux-cpp*
-rwxr-xr-x 1 lanka lanka   271544  一  17 13:17 i486-linux-elfedit*
-rwxr-xr-x 2 lanka lanka  5415848  一  17 13:23 i486-linux-g++*
-rwxr-xr-x 2 lanka lanka  5409552  一  17 13:23 i486-linux-gcc*
-rwxr-xr-x 2 lanka lanka  5409552  一  17 13:23 i486-linux-gcc-7.2.0*
-rwxr-xr-x 1 lanka lanka   189912  一  17 13:23 i486-linux-gcc-ar*
-rwxr-xr-x 1 lanka lanka   189800  一  17 13:23 i486-linux-gcc-nm*
-rwxr-xr-x 1 lanka lanka   189816  一  17 13:23 i486-linux-gcc-ranlib*
-rwxr-xr-x 1 lanka lanka  4065304  一  17 13:23 i486-linux-gcov*
-rwxr-xr-x 1 lanka lanka  3409856  一  17 13:23 i486-linux-gcov-dump*
-rwxr-xr-x 1 lanka lanka  3670040  一  17 13:23 i486-linux-gcov-tool*
-rwxr-xr-x 1 lanka lanka  8284336  一  17 13:17 i486-linux-gprof*
-rwxr-xr-x 4 lanka lanka 10246880  一  17 13:17 i486-linux-ld*
-rwxr-xr-x 4 lanka lanka 10246880  一  17 13:17 i486-linux-ld.bfd*
-rwxr-xr-x 2 lanka lanka  7575352  一  17 13:17 i486-linux-nm*
-rwxr-xr-x 2 lanka lanka  8786528  一  17 13:17 i486-linux-objcopy*
-rwxr-xr-x 2 lanka lanka 10957424  一  17 13:17 i486-linux-objdump*
-rwxr-xr-x 2 lanka lanka  7774816  一  17 13:17 i486-linux-ranlib*
-rwxr-xr-x 2 lanka lanka  2350480  一  17 13:17 i486-linux-readelf*
-rwxr-xr-x 1 lanka lanka  7506608  一  17 13:17 i486-linux-size*
-rwxr-xr-x 1 lanka lanka  7503496  一  17 13:17 i486-linux-strings*
-rwxr-xr-x 2 lanka lanka  8786528  一  17 13:17 i486-linux-strip*
```

##### B.2. ld-linux.so.2

```bash
$ find-name ld-*.so*
i486-linux-4.14.14-7.2.0-2.26/i486-linux/lib/ld-2.26.so
i486-linux-4.14.14-7.2.0-2.26/i486-linux/lib/ld-linux.so.2

$ ll i486-linux-4.14.14-7.2.0-2.26/i486-linux/lib/ld-2.26.so
-rwxr-xr-x 1 lanka lanka 1192912  一  17 13:22 i486-linux-4.14.14-7.2.0-2.26/i486-linux/lib/ld-2.26.so*
```

## 2.4. Hello World

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

#### A. aarch64-linux-4.14.14-7.2.0-2.26

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

#### B. i486-linux-4.14.14-7.2.0-2.26

```bash
$ file helloworld
helloworld: ELF 32-bit LSB executable, Intel 80386, version 1 (SYSV), dynamically linked, interpreter /lib/ld-linux.so.2, for GNU/Linux 3.2.0, with debug_info, not stripped

$ $PJ_HOST-readelf -hl helloworld

# run
$ ./helloworld
Hello world !!!
```

# 3. crosstoolX

> 這邊簡化成使用 makefile。如有不同設定，請參考 confs/aarch64-linux-4.14.14.sh 進行修改

#### A. aarch64-linux-4.14.14-7.2.0-2.26

>linux-4.14.14, gcc-7.2.0, glibc-2.26

```bash
$ cd crosstoolX
$ . confs/aarch64-linux-4.14.14.sh
$ make
$ ll $PJ_TOOLCHAIN_PREFIX
$ ll $PJ_TOOLCHAIN_PATH
```

#### B. i486-linux-4.14.14-7.2.0-2.26

> linux-4.14.14, gcc-7.2.0, glibc-2.26

```bash
$ cd crosstoolX
$ . confs/i486-linux-4.14.14.sh
$ make
$ ll $PJ_TOOLCHAIN_PREFIX
$ ll $PJ_TOOLCHAIN_PATH
```

#### C. i486-linux-2.6.38.8-7.2.0-2.26

>linux-2.6.38.8, gcc-7.2.0, glibc-2.26
>
>glibc-2.26 最低要求 linux-3.2，這邊需要打 patch 才能編譯完成。沒有實際在專案中使用，所以效果未知。

```bash
$ cd crosstoolX
$ . confs/i486-linux-2.6.38.8.sh
$ make
$ ll $PJ_TOOLCHAIN_PREFIX
$ ll $PJ_TOOLCHAIN_PATH
$ tree -L 1 /opt/i486-linux-gnu-2.6.38.8-7.2.0-2.26/
/opt/i486-linux-gnu-2.6.38.8-7.2.0-2.26/
├── bin
├── i486-linux-gnu
├── include
├── lib
├── libexec
└── share

6 directories, 0 files
```

#### ~~D. i486-linux-2.6.24-7.2.0-2.26~~

> linux-2.6.24, gcc-7.2.0, glibc-2.26
>
> 此組合是失敗的。請先試試 2.6.30 以上的版本。

# Appendix

# I. Study

## I.1. [建立 gnu tool chain](http://yi-jyun.blogspot.com/2018/01/tool-chain.html)

# II. Debug

## II.1. gcc-7.2.0/libmpx/mpxrt/mpxrt-utils.c:72:23: error: ‘PATH_MAX’ undeclared here (not in a func8_MAX’?

> linux-4.14.14, gcc-7.2.0, glibc-2.26

```bash
export PJ_GCC_CONFIGURE_FLAGS=--disable-libmpx
```

## II.2. glibc-2.26, checking installed Linux kernel header files... missing or too old!

>linux-2.6.38.8, gcc-7.2.0, glibc-2.26

```bash
$ vi glibc-2.26/sysdeps/unix/sysv/linux/configure
...
#if !defined LINUX_VERSION_CODE || LINUX_VERSION_CODE <  (2 *65536+ 6 *256+ 0) /* 3.2.0 */
...
test -n "$arch_minimum_kernel" || arch_minimum_kernel=2.6.0
```

## II.3. linux-2.6.24, scripts/unifdef.c:209:25: error: conflicting types for ‘getline’

> linux-2.6.24, gcc-7.2.0, glibc-2.26

```bash
$ vi linux-2.6.24/scripts/unifdef.c
// change getline -> get_line
```

## II.4. glibc-2.26, error: ‘__NR_sendmmsg’ undeclared (first use in this function); did you mean ‘__sendmmsg’?

> linux-2.6.38.8, gcc-7.2.0, glibc-2.26

```bash
$ vi glibc-2.26/sysdeps/unix/sysv/linux/kernel-features.h
//#define __ASSUME_SENDMMSG 1

# or

$ vi linux-2.6.38.8/arch/x86/include/asm/unistd_32.h
#add
#define __NR_sendmmsg 345

#define NR_syscalls 346
```

## II.5. error: #error "Assumed value of MB_LEN_MAX wrong"

> 當編譯其它 opensource 時

```bash
$ mv /work/codebase/toolchainSDK/crosstoolX_123/i486-linux-gnu-2.6.38.8-7.2.0-2.26/lib/gcc/i486-linux-gnu/7.2.0/include-fixed/limits.h /work/codebase/toolchainSDK/crosstoolX_123/i486-linux-gnu-2.6.38.8-7.2.0-2.26/lib/gcc/i486-linux-gnu/7.2.0/include-fixed/limits-bak.h

/work/codebase/toolchainSDK/crosstoolX_123/libs/gcc-7.2.0$ cat gcc/limitx.h gcc/glimits.h gcc/limity.h > /opt/i486-linux-gnu-2.6.38.8-7.2.0-2.26/lib/gcc/i486-linux-gnu/7.2.0/include-fixed/limits.h
```

# III. Glossary

# IV. Tool Usage

# Author

Created and designed by [Lanka Hsu](lankahsu@gmail.com).

# License

[CrossCompilationX](https://github.com/lankahsu520/CrossCompilationX) is available under the BSD-3-Clause license. See the LICENSE file for more info.

