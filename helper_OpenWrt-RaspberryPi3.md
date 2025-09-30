# [OpenWrt](https://openwrt.org) Raspberry Pi 3
[![](https://img.shields.io/badge/Powered%20by-lankahsu%20-brightgreen.svg)](https://github.com/lankahsu520/HelperX)
[![GitHub license][license-image]][license-url]
[![GitHub stars][stars-image]][stars-url]
[![GitHub forks][forks-image]][forks-url]
[![GitHub issues][issues-image]][issues-image]
[![GitHub watchers][watchers-image]][watchers-image]

[license-image]: https://img.shields.io/github/license/lankahsu520/HelperX.svg
[license-url]: https://github.com/lankahsu520/HelperX/blob/master/LICENSE
[stars-image]: https://img.shields.io/github/stars/lankahsu520/HelperX.svg
[stars-url]: https://github.com/lankahsu520/HelperX/stargazers
[forks-image]: https://img.shields.io/github/forks/lankahsu520/HelperX.svg
[forks-url]: https://github.com/lankahsu520/HelperX/network
[issues-image]: https://img.shields.io/github/issues/lankahsu520/HelperX.svg
[issues-url]: https://github.com/lankahsu520/HelperX/issues
[watchers-image]: https://img.shields.io/github/watchers/lankahsu520/HelperX.svg
[watchers-url]: https://github.com/lankahsu520/HelperX/watchers

# 1. Overview

> The OpenWrt Project is a Linux operating system targeting embedded devices. Instead of trying to create a single, static firmware, OpenWrt provides a fully writable filesystem with package management. 

# 2. Environment

> Host: Ubuntu 20.04 x86_64

```bash
$ sudo apt install -y sed make binutils gcc g++ bash patch gzip bzip2 perl tar cpio python unzip rsync wget libncurses-dev

```

# 3. Build with OpenWrt

> Host: Ubuntu 20.04 x86_64
>
> Target: Raspberry Pi3

## 3.1. To build Image

#### A. Git clone [openwrt](https://github.com/openwrt/openwrt.git)

```bash
$ git clone https://github.com/openwrt/openwrt.git openwrtX_pi3

$ cd openwrtX_pi3

$ ./scripts/feeds update -a
$ ./scripts/feeds install -a
```

#### B. menuconfig

> 這邊選擇輕量化，省略 drivers

```bash
$ cd openwrtX_pi3
$ make menuconfig
```
- [x] Target System (Broadcom BCM27xx)  ---> 

- [x] Subtarget (BCM2710 boards (64 bit))  --->

- [x] Target Profile (Raspberry Pi 3B/3B+/CM3 (64bit))  --->

- [x] Target Images  --->

    ​	*** Root filesystem archives ***

  - [x] [*] tar.gz
  
    ​    *** Image Options ***

  - [x] (64) Kernel partition size (in MiB)

  - [x] (1024) Root filesystem partition size (in MiB)
  
- [x] Global build settings  --->
  
  - [x] [*] Select all target specific packages by default
  - [x] -*- Select all kernel module packages by default
  
- [x] [*] Build the OpenWrt Image Builder

- [x] [*]   Include package repositories (NEW)

- [x] [*] Build the OpenWrt SDK

- [x] Package the OpenWrt-based Toolchain

- [x] [*] Image configuration  --->

- [x] Base system  --->

- [x] Kernel modules  --->

- [x] Libraries  --->

- [x] Network  --->

  - [x] SSH  --->  
    - [x] -*- openssh-client............................................ OpenSSH client

    - [x] <*> openssh-client-utils............................ OpenSSH client utilities

    - [x] -*- openssh-keygen............................................ OpenSSH keygen

    - [x] <*> openssh-server............................................ OpenSSH server

    - [x] [*]   Include libfido2 support in openssh-server (NEW) 

#### C. Build

```bash
$ make
```

## 3.2. To Generate Toolchain

> make 後，可在 staging_dir/ 得到 Toolchain

```bash
$ cd openwrtX_pi3

$ tree -L 1 staging_dir/
staging_dir/
├── host
├── hostpkg
├── packages
├── target-aarch64_cortex-a53_musl
└── toolchain-aarch64_cortex-a53_gcc-12.3.0_musl

5 directories, 0 files

$ ll staging_dir/toolchain-aarch64_cortex-a53_gcc-12.3.0_musl/bin/*-gcc
lrwxrwxrwx 1 lanka lanka      30  一  29 23:01 aarch64-openwrt-linux-gcc -> aarch64-openwrt-linux-musl-gcc*
-rwxr-xr-x 2 lanka lanka 1523080  一  29 23:01 aarch64-openwrt-linux-musl-gcc*

```

# 4. Burn Your Image into SD CARD

>注意！網路上的教學中，都沒有提到要燒那個檔案；這些文章可能都是抄來的，沒有實際燒過！
>

```mermaid
flowchart LR
	SD[SD CARD]
	Image --> |dd|SD
	Image --> |balenaEtcher|SD
	
```

#### A. use dd

#### B. use [balenaEtcher-Portable-1.5.49](https://www.balena.io/etcher/)

> 個人比較喜歡用 Etcher，執行時請使用系統管理者啟動
>
> 選擇 openwrt-bcm27xx-bcm2710-rpi-3-ext4-sysupgrade.img.gz

```bash
$ cd bin/targets/bcm27xx/bcm2710
$ tree -L 1./
.
├── config.buildinfo
├── feeds.buildinfo
├── openwrt-bcm27xx-bcm2710-rpi-3-ext4-factory.img.gz
├── openwrt-bcm27xx-bcm2710-rpi-3-ext4-sysupgrade.img.gz
├── openwrt-bcm27xx-bcm2710-rpi-3.manifest
├── openwrt-bcm27xx-bcm2710-rpi-3-rootfs.tar.gz
├── openwrt-bcm27xx-bcm2710-rpi-3-squashfs-factory.img.gz
├── openwrt-bcm27xx-bcm2710-rpi-3-squashfs-sysupgrade.img.gz
├── openwrt-imagebuilder-bcm27xx-bcm2710.Linux-x86_64.tar.xz
├── openwrt-sdk-bcm27xx-bcm2710_gcc-12.3.0_musl.Linux-x86_64.tar.xz
├── openwrt-toolchain-bcm27xx-bcm2710_gcc-12.3.0_musl.Linux-x86_64.tar.xz
├── packages
├── profiles.json
├── sha256sums
└── version.buildinfo

1 directory, 14 files

$ ls *-sysupgrade.img.gz
openwrt-bcm27xx-bcm2710-rpi-3-ext4-sysupgrade.img.gz  openwrt-bcm27xx-bcm2710-rpi-3-squashfs-sysupgrade.img.gz

```

# 5. Boot from SD Card

```bash
$ udhcpc -i br-lan

$ uname -a
Linux OpenWrt 6.1.74 #0 SMP Mon Jan 29 09:28:41 2024 aarch64 GNU/Linux

$ cat /proc/version
Linux version 6.1.74 (lanka@msi-vbx) (aarch64-openwrt-linux-musl-gcc (OpenWrt GCC 12.3.0 r24937-920414ca88) 12.3.0, GNU ld (GNU Binutils) 2.40.0) #0 SMP Mon Jan 29 09:28:41 2024

$ cat /etc/os-release
NAME=Buildroot
VERSION=-svn8
ID=buildroot
VERSION_ID=2022.02
PRETTY_NAME="Buildroot 2022.02"

$ cat /etc/os-release
NAME="OpenWrt"
VERSION="SNAPSHOT"
ID="openwrt"
ID_LIKE="lede openwrt"
PRETTY_NAME="OpenWrt SNAPSHOT"
VERSION_ID="snapshot"
HOME_URL="https://openwrt.org/"
BUG_URL="https://bugs.openwrt.org/"
SUPPORT_URL="https://forum.openwrt.org/"
BUILD_ID="r24937-920414ca88"
OPENWRT_BOARD="bcm27xx/bcm2710"
OPENWRT_ARCH="aarch64_cortex-a53"
OPENWRT_TAINTS=""
OPENWRT_DEVICE_MANUFACTURER="OpenWrt"
OPENWRT_DEVICE_MANUFACTURER_URL="https://openwrt.org/"
OPENWRT_DEVICE_PRODUCT="Generic"
OPENWRT_DEVICE_REVISION="v0"
OPENWRT_RELEASE="OpenWrt SNAPSHOT r24937-920414ca88"

$ ls -al /bin/sh
lrwxrwxrwx    1 root     root             7 Jan 29 09:28 /bin/sh -> busybox

```

# 6. Build helloworld on Host

```bash
$ cd openwrtX_pi3
$ cd staging_dir/toolchain-aarch64_cortex-a53_gcc-12.3.0_musl
$ export STAGING_DIR=`pwd`
$ export PATH=$STAGING_DIR/bin:$PATH

$ aarch64-openwrt-linux-gcc helloworld.c -o helloworld
# 避免動態連結 --static
$ aarch64-openwrt-linux-gcc --static helloworld.c -o helloworld

$ file helloworld
helloworld: ELF 64-bit LSB executable, ARM aarch64, version 1 (SYSV), dynamically linked, interpreter /lib/ld-musl-aarch64.so.1, with debug_info, not stripped

```

#### A. Qemu user mode

```bash
# run with qemu
$ sudo apt install qemu-user qemu-user-static

$ qemu-aarch64 helloworld
#or
$ LD_LIBRARY_PATH=$STAGING_DIR/lib qemu-aarch64 $STAGING_DIR/lib/ld-musl-aarch64.so.1 /tmp/helloworld
Hello world !!!
```

# 7. Run helloworld on Pi

```bash
# Please scp helloworld from Ubuntu -> Pi 
$ helloworld
Hello world !!!
```

# Appendix

# I. Study

## I.1. [Build & Customise OpenWrt for Raspberry Pi](https://www.cnx-software.com/2020/01/12/build-customize-openwrt-for-raspberry-pi/)

## I.2. [OpenWrt DIY — 多设备固件云编译](https://github.com/IvanSolis1989/OpenWrt-DIY)

## I.3. [openwrt-pi4](https://github.com/mestadler/openwrt-pi4)

# II. Debug

# III. Glossary

# IV. Tool Usage

# Author

> Created and designed by [Lanka Hsu](lankahsu@gmail.com).

# License

> [CrossCompilationX](https://github.com/lankahsu520/CrossCompilationX) is available under the BSD-3-Clause license. See the LICENSE file for more info.
