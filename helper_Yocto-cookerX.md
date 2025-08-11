# [Yocto](https://www.yoctoproject.org) [cookerX](https://github.com/lankahsu520/CrossCompilationX/tree/master/Yocto/cookerX)

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

> [cookerX](https://github.com/lankahsu520/CrossCompilationX/tree/master/Yocto/cookerX) is depend on [Yocto Cooker](https://github.com/cpb-/yocto-cooker)。 
>
> Yocto Project isn't friendly to programmer. We have to collect Menu-files and create local.conf by myself.

> 為什麼特別再包裝一層，因為用慣了 make；工具的命令要好記，而不是讓`使用者`困擾。

# 2. Environment

## 2.1. [Yocto Project Quick Build](https://docs.yoctoproject.org/dev/brief-yoctoprojectqs/index.html)

> 參考官網的相關說明

```bash
$ sudo apt-get install gawk wget git-core diffstat unzip texinfo gcc-multilib build-essential chrpath socat cpio python python3 python3-pip python3-pexpect xz-utils debianutils iputils-ping python3-git python3-jinja2 libegl1-mesa libsdl1.2-dev pylint3 xterm
```

## 2.2.  [Yocto Cooker](https://github.com/cpb-/yocto-cooker)

```bash
$ python3 -m pip install --upgrade git+https://github.com/cpb-/yocto-cooker.git
$ cooker --version
# 1.4.0
```

# 3. Building

## 3.1. Quick Start

> 請特別注意 `PJ_YOCTO_DOWNLOADS_DIR` 和 `PJ_YOCTO_SSTATE_DIR`，因為編譯真的很漫長，所以特別保留 downloads 和 sstate-cache 放在另一顆硬碟存放。
>
> 如完全採用 make ，而沒有呼叫 cooker_123.sh，在編譯過程會失敗。

| meta              | branch             | rev        |
| ----------------- | ------------------ | ---------- |
| poky              | master, Zeus (3.0) | 7ec846be8b |
| meta-openembedded | master             | 7f15e7975  |
| meta-raspberrypi  | master             | 2b733d5    |

```bash
$ git clone https://github.com/lankahsu520/CrossCompilationX.git

$ python -V
Python 3.10.17

$ cd CrossCompilationX/Yocto/cookerX
# 這邊用 Raspberry Pi 3 當範本
$ . confs/pi3-master-2b733d5.conf
...

Please check dl-dir (PJ_YOCTO_DOWNLOADS_DIR=/yocto-cache/downloads) !!!
Please check sstate-dir (PJ_YOCTO_SSTATE_DIR=/yocto-cache/sstate-cache) !!!

$ make
# or
cooker  -v init cooker-menu/pi3-master-2b733d5-menu.json
cooker update
cooker generate
cooker  -v build pi3-master-2b733d5
# 漫長的等待…但就是這麼簡單！
```

## 3.2. List of Images

| IMAGE              | DESC                                                         |
| ------------------ | ------------------------------------------------------------ |
| core-image-base    | A console-only image that fully supports the target device hardware.. |
| core-image-minimal | A small image just capable of allowing a device to boot..    |
| core-image-sato    | Image with Sato, a mobile environment and visual style for mobile devices. The image supports X11 with a Sato theme, Pimlico applications, and contains terminal, editor, and file manager. |
| core-image-weston  | A very basic Wayland image with a terminal.                  |

```bash
$ bitbake -s | grep image
$ bitbake -e core-image-base | grep ^DESCRIPTION=
```

## 3.3. List of Layers

```bash
$ bitbake-layers show-layers
```

# 4. Outputs

> 本篇都會採用 cookerX 進行解說

```bash
$ make lnk-generate
# or
$ ./confs/sh/cooker_123.sh lnk
$ ./confs/sh/bb_linker.sh
```

## 4.1. rootfs

```bash
$ echo builds-lnk/$PJ_YOCTO_BUILD-rootfs
builds-lnk/pi3-master-2b733d5-rootfs

$ ll builds-lnk/$PJ_YOCTO_BUILD-rootfs/
total 72
drwxr-xr-x 17 lanka lanka 4096 Mar  9  2018 ./
drwxr-xr-x 12 lanka lanka 4096 Jul  9 11:50 ../
drwxr-xr-x  2 lanka lanka 4096 Mar  9  2018 bin/
drwxr-xr-x  2 lanka lanka 4096 Mar  9  2018 boot/
drwxr-xr-x  2 lanka lanka 4096 Mar  9  2018 dev/
drwxr-xr-x 29 lanka lanka 4096 Mar  9  2018 etc/
drwxr-xr-x  3 lanka lanka 4096 Mar  9  2018 home/
drwxr-xr-x  7 lanka lanka 4096 Mar  9  2018 lib/
-rw-r--r--  1 lanka lanka    7 Mar  9  2018 log_lock.pid
drwxr-xr-x  2 lanka lanka 4096 Mar  9  2018 media/
drwxr-xr-x  2 lanka lanka 4096 Mar  9  2018 mnt/
drwxr-xr-x  2 lanka lanka 4096 Mar  9  2018 proc/
drwxr-xr-x  2 lanka lanka 4096 Mar  9  2018 run/
drwxr-xr-x  2 lanka lanka 4096 Mar  9  2018 sbin/
drwxr-xr-x  2 lanka lanka 4096 Mar  9  2018 sys/
drwxr-xr-t  2 lanka lanka 4096 Mar  9  2018 tmp/
drwxr-xr-x 10 lanka lanka 4096 Mar  9  2018 usr/
drwxr-xr-x  8 lanka lanka 4096 Mar  9  2018 var/
```

### 4.1.1. rebuild rootfs

> 在roofs 進行刪除後，進行還原

```bash
# ln -s $PJ_YOCTO_BUILD_DIR/tmp/work/$PJ_YOCTO_LINUX/$PJ_YOCTO_IMAGE/*/rootfs $PJ_YOCTO_BUILD-rootfs

$ echo $PJ_YOCTO_IMAGE
core-image-base

# 強制執行 do_rootfs
$ bitbake -f $PJ_YOCTO_IMAGE -c rootfs
```

## 4.2. images-lnk

```bash
$ ll images-lnk/
total 4036
drwxrwxr-x  2 lanka lanka    4096 Jul  9 16:06 ./
drwxrwxr-x 10 lanka lanka    4096 Jul  9 15:30 ../
lrwxrwxrwx  1 lanka lanka     141 Jul  9 16:06 core-image-base-raspberrypi3.manifest -> /yocto/cookerX-pi3/builds/build-pi3-master-2b733d5/tmp/deploy/images/raspberrypi3/core-image-base-raspberrypi3-20250709020740.rootfs.manifest
lrwxrwxrwx  1 lanka lanka     140 Jul  9 16:06 core-image-base-raspberrypi3.wic.bz2 -> /yocto/cookerX-pi3/builds/build-pi3-master-2b733d5/tmp/deploy/images/raspberrypi3/core-image-base-raspberrypi3-20250709020740.rootfs.wic.bz2
-rw-rw-r--  1 lanka lanka 2373025 Jul  9 16:06 environment.txt
-rw-rw-r--  1 lanka lanka    4272 Jul  9 16:06 pn-buildlist
-rw-rw-r--  1 lanka lanka 1732254 Jul  9 16:06 task-depends.dot
```

### 4.2.1. Info of wic

```bash
$ ll images-lnk/$PJ_YOCTO_IMAGE_WIC
lrwxrwxrwx 1 lanka lanka 140 Jul  9 16:06 images-lnk/core-image-base-raspberrypi3.wic.bz2 -> /yocto/cookerX-pi3/builds/build-pi3-master-2b733d5/tmp/deploy/images/raspberrypi3/core-image-base-raspberrypi3-20250709020740.rootfs.wic.bz2

$ cp images-lnk/core-image-base-raspberrypi3.wic.bz2 ../
$ bzip2 -d -f core-image-base-raspberrypi3.wic.bz2
$ ll core-image-base-raspberrypi3.wic
-rw-r--r-- 1 lanka lanka 233202688 Jul 11 13:46 core-image-base-raspberrypi3.wic

$ wic ls core-image-base-raspberrypi3.wic
Num     Start        End          Size      Fstype
 1       4194304     57143295     52948992  fat16
 2      58720256    233202687    174482432  ext4
```

### 4.2.2. Build History

```bash
$ cat images-lnk/pn-buildlist
$ cat images-lnk/task-depends.dot
$ cat images-lnk/environment.txt
$ cat images-lnk/$PJ_YOCTO_IMAGE_MANIFEST
```

```bash
$ cd images-lnk
# -g, --graphviz        Save dependency tree information for the specified targets in the dot syntax.
$ bitbake -g $PJ_YOCTO_TARGET
$ cat pn-buildlist
$ cat task-depends.dot

# Show the global or per-recipe environment complete with information about where variables were set/changed.
$ bitbake -e $PJ_YOCTO_TARGET > environment.txt
$ cat environment.txt

$ cat environment.txt | grep ^IMAGE_INSTALL
```

### 4.2.3. List of Packages

```bash
$ oe-pkgdata-util list-pkgs
```

## 4.3. builds-lnk

```bash
$ ll builds-lnk
total 24
drwxrwxr-x 2 lanka lanka 4096 Jul  9 12:30 ./
drwxrwxr-x 9 lanka lanka 4096 Jul  9 12:29 ../
lrwxrwxrwx 1 lanka lanka  121 Jul  9 12:30 pi3-master-2b733d5-rootfs -> /yocto/cookerX-pi3/builds/build-pi3-master-2b733d5/tmp/work/raspberrypi3-poky-linux-gnueabi/core-image-base/1.0-r0/rootfs/
lrwxrwxrwx 1 lanka lanka   65 Jul  9 12:30 pi3-master-2b733d5-rpm -> /yocto/cookerX-pi3/builds/build-pi3-master-2b733d5/tmp/deploy/rpm/
lrwxrwxrwx 1 lanka lanka   81 Jul  9 12:30 raspberrypi3 -> /yocto/cookerX-pi3/builds/build-pi3-master-2b733d5/tmp/deploy/images/raspberrypi3/
lrwxrwxrwx 1 lanka lanka   65 Jul  9 12:30 sdk -> /yocto/cookerX-pi3/builds/build-pi3-master-2b733d5/tmp/deploy/sdk
```

## 4.4. bb-lnk

> 這邊是方便查看相關的 bb ，將它們進行連結

```bash
$ ./confs/sh/bb_linker.sh
```

```bash
$ ll bb-lnk/
total 136
drwxrwxr-x  2 lanka lanka 4096 Jul  9 11:51 ./
drwxrwxr-x 10 lanka lanka 4096 Jul  9 11:51 ../
lrwxrwxrwx  1 lanka lanka   69 Jul  9 11:51 avahi_0.8.bb -> ../layers-scarthgap/poky/meta/recipes-connectivity/avahi/avahi_0.8.bb
lrwxrwxrwx  1 lanka lanka   72 Jul  9 11:51 bluez5_5.72.bb -> ../layers-scarthgap/poky/meta/recipes-connectivity/bluez5/bluez5_5.72.bb
lrwxrwxrwx  1 lanka lanka   68 Jul  9 11:51 busybox_1.36.1.bb -> ../layers-scarthgap/poky/meta/recipes-core/busybox/busybox_1.36.1.bb
lrwxrwxrwx  1 lanka lanka   67 Jul  9 11:51 bzip2_1.0.8.bb -> ../layers-scarthgap/poky/meta/recipes-extended/bzip2/bzip2_1.0.8.bb
lrwxrwxrwx  1 lanka lanka   64 Jul  9 11:51 curl_8.7.1.bb -> ../layers-scarthgap/poky/meta/recipes-support/curl/curl_8.7.1.bb
lrwxrwxrwx  1 lanka lanka   63 Jul  9 11:51 dbus_1.14.10.bb -> ../layers-scarthgap/poky/meta/recipes-core/dbus/dbus_1.14.10.bb
lrwxrwxrwx  1 lanka lanka   71 Jul  9 11:51 dropbear_2022.83.bb -> ../layers-scarthgap/poky/meta/recipes-core/dropbear/dropbear_2022.83.bb
lrwxrwxrwx  1 lanka lanka   70 Jul  9 11:51 glib-2.0_2.78.6.bb -> ../layers-scarthgap/poky/meta/recipes-core/glib-2.0/glib-2.0_2.78.6.bb
lrwxrwxrwx  1 lanka lanka   62 Jul  9 11:51 glibc_2.39.bb -> ../layers-scarthgap/poky/meta/recipes-core/glibc/glibc_2.39.bb
lrwxrwxrwx  1 lanka lanka   84 Jul  9 11:51 glib-networking_2.78.1.bb -> ../layers-scarthgap/poky/meta/recipes-core/glib-networking/glib-networking_2.78.1.bb
lrwxrwxrwx  1 lanka lanka   90 Jul  9 11:51 hostapd_2.10.bb -> ../layers-scarthgap/meta-openembedded/meta-oe/recipes-connectivity/hostapd/hostapd_2.10.bb
lrwxrwxrwx  1 lanka lanka   86 Jul  9 11:51 jansson_2.14.bb -> ../layers-scarthgap/meta-openembedded/meta-oe/recipes-extended/jansson/jansson_2.14.bb
lrwxrwxrwx  1 lanka lanka   68 Jul  9 11:51 json-c_0.17.bb -> ../layers-scarthgap/poky/meta/recipes-devtools/json-c/json-c_0.17.bb
lrwxrwxrwx  1 lanka lanka   73 Jul  9 11:51 libevent_2.1.12.bb -> ../layers-scarthgap/poky/meta/recipes-support/libevent/libevent_2.1.12.bb
lrwxrwxrwx  1 lanka lanka   71 Jul  9 11:51 libical_3.0.17.bb -> ../layers-scarthgap/poky/meta/recipes-support/libical/libical_3.0.17.bb
lrwxrwxrwx  1 lanka lanka   69 Jul  9 11:51 libmnl_1.0.5.bb -> ../layers-scarthgap/poky/meta/recipes-extended/libmnl/libmnl_1.0.5.bb
lrwxrwxrwx  1 lanka lanka   66 Jul  9 11:51 libnl_3.9.0.bb -> ../layers-scarthgap/poky/meta/recipes-support/libnl/libnl_3.9.0.bb
lrwxrwxrwx  1 lanka lanka   80 Jul  9 11:51 libsndfile1_1.2.2.bb -> ../layers-scarthgap/poky/meta/recipes-multimedia/libsndfile/libsndfile1_1.2.2.bb
lrwxrwxrwx  1 lanka lanka   78 Jul  9 11:51 libunistring_1.2.bb -> ../layers-scarthgap/poky/meta/recipes-support/libunistring/libunistring_1.2.bb
lrwxrwxrwx  1 lanka lanka   70 Jul  9 11:51 libusb1_1.0.27.bb -> ../layers-scarthgap/poky/meta/recipes-support/libusb/libusb1_1.0.27.bb
lrwxrwxrwx  1 lanka lanka   67 Jul  9 11:51 libxml2_2.12.8.bb -> ../layers-scarthgap/poky/meta/recipes-core/libxml/libxml2_2.12.8.bb
lrwxrwxrwx  1 lanka lanka  104 Jul  9 11:51 mosquitto_2.0.18.bb -> ../layers-scarthgap/meta-openembedded/meta-networking/recipes-connectivity/mosquitto/mosquitto_2.0.18.bb
lrwxrwxrwx  1 lanka lanka   75 Jul  9 11:51 openssl_3.2.3.bb -> ../layers-scarthgap/poky/meta/recipes-connectivity/openssl/openssl_3.2.3.bb
lrwxrwxrwx  1 lanka lanka   83 Jul  9 11:51 p7zip_16.02.bb -> ../layers-scarthgap/meta-openembedded/meta-oe/recipes-extended/p7zip/p7zip_16.02.bb
lrwxrwxrwx  1 lanka lanka   67 Jul  9 11:51 rsync_3.2.7.bb -> ../layers-scarthgap/poky/meta/recipes-devtools/rsync/rsync_3.2.7.bb
lrwxrwxrwx  1 lanka lanka   61 Jul  9 11:51 sed_4.9.bb -> ../layers-scarthgap/poky/meta/recipes-extended/sed/sed_4.9.bb
lrwxrwxrwx  1 lanka lanka   62 Jul  9 11:51 tar_1.35.bb -> ../layers-scarthgap/poky/meta/recipes-extended/tar/tar_1.35.bb
lrwxrwxrwx  1 lanka lanka   65 Jul  9 11:51 unzip_6.0.bb -> ../layers-scarthgap/poky/meta/recipes-extended/unzip/unzip_6.0.bb
lrwxrwxrwx  1 lanka lanka   66 Jul  9 11:51 which_2.21.bb -> ../layers-scarthgap/poky/meta/recipes-extended/which/which_2.21.bb
lrwxrwxrwx  1 lanka lanka   68 Jul  9 11:51 xmlto_0.0.28.bb -> ../layers-scarthgap/poky/meta/recipes-devtools/xmlto/xmlto_0.0.28.bb
lrwxrwxrwx  1 lanka lanka   88 Jul  9 11:51 yaml-cpp_0.8.0.bb -> ../layers-scarthgap/meta-openembedded/meta-oe/recipes-support/yaml-cpp/yaml-cpp_0.8.0.bb
lrwxrwxrwx  1 lanka lanka   61 Jul  9 11:51 zlib_1.3.1.bb -> ../layers-scarthgap/poky/meta/recipes-core/zlib/zlib_1.3.1.bb
```

# 5. Burn Your Image

## 5.1. Image flasher

> 樹莓派是採用 SD-CARD 為儲取媒介，所以可以使用工具進行燒錄

#### A. [balenaEtcher](https://www.balena.io/etcher/)

#### B. [win32diskimager](https://sourceforge.net/projects/win32diskimager/)

#### C. [rpi-imager](https://github.com/raspberrypi/rpi-imager)

# 6. Toolchain

## 6.1. Generate the Toolchain

### 6.1.1. cross-compile SDK

> 會產生 target rootfs 中的所有頭檔與庫（**針對該 image**）

```bash
$ make toolchain
# or
bitbake core-image-base -c populate_sdk

$ make cook-lnk
$ ll builds-lnk/sdk/*.sh
-rwxr-xr-x 2 lanka lanka 302818041 Jul 11 15:00 builds-lnk/sdk/poky-glibc-x86_64-core-image-base-cortexa7t2hf-neon-vfpv4-raspberrypi3-toolchain-4.1.sh*

# install
$ builds-lnk/sdk/poky-glibc-x86_64-core-image-base-cortexa7t2hf-neon-vfpv4-raspberrypi3-toolchain-4.1.sh
Poky (Yocto Project Reference Distro) SDK installer version 4.1
===============================================================
Enter target directory for SDK (default: /opt/poky/4.1):
You are about to install the SDK to "/opt/poky/4.1". Proceed [Y/n]? y
Extracting SDK......................................................................................done
Setting it up...done
SDK has been successfully set up and is ready to be used.
Each time you wish to use the SDK in a new shell session, you need to source the environment setup script e.g.
 $ . /opt/poky/4.1/environment-setup-cortexa7t2hf-neon-vfpv4-poky-linux-gnueabi
```

### 6.1.2. generic SDK

> 很簡單的 cross-compiler 和 基本 C runtime（如 glibc 或 musl）

```bash
$ make toolchain-pure
# or
bitbake meta-toolchain
```

## 6.2. Helloworld.c

### 6.2.1. Native-Compilation

```bash
# on Ubuntu 20.04.6 LTS
$	cat > helloworld.c <<EOF
#include <stdio.h>

int main(int argc, char *argv[])
{

	printf("Hello world !!!\n");
	return 0;
}
EOF

$ gcc -o helloworld helloworld.c
$ ./helloworld
Hello world !!!
$ file helloworld
helloworld: ELF 64-bit LSB shared object, x86-64, version 1 (SYSV), dynamically linked, interpreter /lib64/ld-linux-x86-64.so.2, BuildID[sha1]=963bb5841deba5e7bed613604b0c89116249fae8, for GNU/Linux 3.2.0, not stripped
```

### 6.2.2. Cross-Compilation

```bash
#Each time you wish to use the SDK in a new shell session, you need to source the env
$ . /opt/poky/4.1/environment-setup-cortexa7t2hf-neon-vfpv4-poky-linux-gnueabi

$ echo $CC
arm-poky-linux-gnueabi-gcc -mthumb -mfpu=neon-vfpv4 -mfloat-abi=hard -mcpu=cortex-a7 -fstack-protector-strong -O2 -D_FORTIFY_SOURCE=2 -Wformat -Wformat-security -Werror=format-security --sysroot=/opt/poky/4.1/sysroots/cortexa7t2hf-neon-vfpv4-poky-linux-gnueabi

$ echo $CFLAGS
-O2 -pipe -g -feliminate-unused-debug-types

$ $CC -o helloworld helloworld.c

# 這邊直接執行當然會失敗
$ ./helloworld
-bash: ./helloworld: cannot execute binary file: Exec format error

$ file helloworld
helloworld: ELF 32-bit LSB pie executable, ARM, EABI5 version 1 (SYSV), dynamically linked, interpreter /lib/ld-linux-armhf.so.3, BuildID[sha1]=2889d82d8020d224b6a028a72a86acb72ab02f25, for GNU/Linux 3.2.0, with debug_info, not stripped
```

# 7. Customize

## 7.1. Build Configuration

> 如果要新增/修改相關 Firmware，可以先參考以下檔案

```bash
$ ls confs/*.conf
confs/imx8mm-scarthgap-core.conf  confs/pi3-master-2b733d5.conf  confs/pi3-sample.conf  confs/qemux86_64.conf

$ ls cooker-menu/*.json
cooker-menu/imx8mm-evk-scarthgap-menu.json  cooker-menu/pi3-master-2b733d5-menu.json  cooker-menu/pi3-sample-menu.json
```

## 7.2. Set root's password

```bash
$ echo $PJ_COOKER_MENU
pi3-master-2b733d5-menu.json

$ vi ./cooker-menu/$PJ_COOKER_MENU.json 
"IMAGE_CLASSES += 'extrausers'",
"EXTRA_USERS_PARAMS = 'usermod -P 1234567890 root;'",

$ rm .cook-generate
$ make .cook-generate
#or
$ cooker generate

$ cat $PJ_YOCTO_BUILD_DIR/conf/local.conf

$ cat $PJ_YOCTO_LAYERS_DIR/poky/meta/classes/extrausers.bbclass
```

#### A. [/etc/passwd](https://www.cyberciti.biz/faq/understanding-etcpasswd-file-format/)

```bash
$ cat builds-lnk/$PJ_YOCTO_BUILD-rootfs/etc/passwd
```

#### B. [/etc/shadow](https://www.cyberciti.biz/faq/understanding-etcshadow-file/)

```bash
$ cat builds-lnk/$PJ_YOCTO_BUILD-rootfs/etc/shadow
```

## 7.3. ssh server

```bash
$ vi ./cooker-menu/$PJ_COOKER_MENU
add into "local.conf"
"IMAGE_INSTALL += ' dropbear'",

$ rm .cook-generate
$ make .cook-generate
#or
$ cooker generate

$ cat $PJ_YOCTO_BUILD_DIR/conf/local.conf

$ bitbake -s | grep dropbear
$ bitbake -c build dropbear

$ cat $PJ_YOCTO_LAYERS_DIR/poky/meta/recipes-core/dropbear/dropbear/init
$ vi $PJ_YOCTO_LAYERS_DIR/poky/meta/recipes-core/dropbear/dropbear/dropbear.default
# please mark
# DROPBEAR_EXTRA_ARGS="-w"
```

## 7.4. [meta-lanka](https://github.com/lankahsu520/CrossCompilationX/tree/master/Yocto/meta-lanka)

### 7.4.1. create-layer

```bash
$ cd $PJ_YOCTO_LAYERS_DIR
$ bitbake-layers create-layer meta-lanka
NOTE: Starting bitbake server...
Add your new layer with 'bitbake-layers add-layer meta-lanka'

# we don't need to add-layer.

# check example exist
$ bitbake -s | grep example
# not found

$ echo $PJ_COOKER_MENU
pi3-master-2b733d5-menu.json

$ cd-root; vi ./cooker-menu/$PJ_COOKER_MENU
# add "meta-lanka" into "layers"
$ cooker generate
$ bitbake-layers show-layers | grep meta-lanka
$ cat $PJ_YOCTO_BUILD_DIR/conf/bblayers.conf | grep meta-lanka

# check example
$ bitbake -s | grep example
example                                               :0.1-r0

# Then update meta-lanka/recipes-example/example/example_0.1.bb and add meta-lanka/recipes-example/example/files/* 
```

```bash
# The following steps don't have to be performed.
$ cd $PJ_YOCTO_LAYERS_DIR
$ . ./poky/oe-init-build-env

$ cd $PJ_YOCTO_LAYERS_DIR
$ yocto-check-layer meta-lanka
$ rm -rf build
```

### 7.4.2. show-recipes

```bash
$ bitbake-layers show-recipes example
NOTE: Starting bitbake server...
Loading cache: 100% |#################################################################################################################################################################| Time: 0:00:00
Loaded 2929 entries from dependency cache.
=== Matching recipes: ===
example:
  meta-lanka           0.1
```

### 7.4.3. [example_0.1.bb](https://github.com/lankahsu520/CrossCompilationX/blob/master/Yocto/meta-lanka/recipes-example/example/example_0.1.bb)

#### A. Add files - [helloworld-123.c and Makefile](https://github.com/lankahsu520/HelperX/tree/master/Yocto/meta-lanka/recipes-example/example/files)

#### B. Install into Image

```bash
$ cd-root; vi ./cooker-menu/$PJ_COOKER_MENU
# add "example" into "local.conf"
# ,"IMAGE_INSTALL:append = ' example'"
```

#### C. check IMAGE_INSTALL

```bash
$ bitbake -e $PJ_YOCTO_TARGET | grep ^IMAGE_INSTALL=
```

#### D. example.bb

```bash
$ bb-info example

directfb-examples                                   :1.7.0-r0
example                                               :0.1-r0
gst-examples                                       :1.18.6-r0

./layers-master-2b733d5/meta-lanka/recipes-example/example/example_0.1.bb
./layers-master-2b733d5/poky/meta/lib/bblayers/templates/example.bb

SRC_URI="file://helloworld-123.c            file://Makefile"

S="/yocto/cookerX-pi3/builds/build-pi3-master-2b733d5/tmp/work/cortexa7t2hf-neon-vfpv4-poky-linux-gnueabi/example/0.1-r0"

WORKDIR="/yocto/cookerX-pi3/builds/build-pi3-master-2b733d5/tmp/work/cortexa7t2hf-neon-vfpv4-poky-linux-gnueabi/example/0.1-r0"

DEPENDS="virtual/arm-poky-linux-gnueabi-gcc virtual/arm-poky-linux-gnueabi-compilerlibs virtual/libc "

RDEPENDS:${KERNEL_PACKAGE_NAME}-base=""
RDEPENDS:example-staticdev="example-dev (= 0.1-r0)"
```

## 7.5. commercial

> 雖然可以在 local.conf 接受 `commercial`，但發佈 image 時，要注意商業授權套件

```bash
$ grep -r 'LICENSE_FLAGS' $PJ_YOCTO_LAYERS_DIR/meta-*/recipes-* | grep commercial
/yocto/cookerX-pi3/layers-master-2b733d5/meta-raspberrypi/recipes-multimedia/gstreamer/gstreamer1.0-plugins-bad_%.bbappend:                   ${@bb.utils.contains('LICENSE_FLAGS_ACCEPTED', 'commercial', 'gpl faad', '', d)}"
/yocto/cookerX-pi3/layers-master-2b733d5/meta-raspberrypi/recipes-multimedia/rpidistro-ffmpeg/rpidistro-ffmpeg_4.3.4.bb:LICENSE_FLAGS = "commercial"
```

```conf
"local.conf": [
        "LICENSE_FLAGS_ACCEPTED += 'commercial'"
]
```

## 7.6. devtool

> `devtool` 是 Yocto Project 提供的 **開發輔助工具**，隸屬於 `devtools`（開發者工具）套件的一部分，目的是讓開發者更方便地：
>
> - 建立新的 BitBake recipe（配方）
> - 修改、patch、測試現有套件
> - 管理 source code 與 build 的整合

### 7.6.1. [python-zeep](https://github.com/mvantellingen/python-zeep.git) (github)

> 這邊以 python-zeep 為範例，

#### A. Check exist

```bash
$ bitbake -s | grep zeep
```

#### B. add

```bash
$ devtool add python-zeep https://github.com/mvantellingen/python-zeep.git


$ ll ./builds/build-imx8mm-evk-scarthgap-home/workspace/recipes/python-zeep/python-zeep_git.bb
-rw-rw-r-- 1 lanka lanka 831 Jul 23 14:55 ./builds/build-imx8mm-evk-scarthgap-home/workspace/recipes/python-zeep/python-zeep_git.bb
```

#### C. python-zeep_git.bb

```bash
$ cat ./builds/build-imx8mm-evk-scarthgap-home/workspace/recipes/python-zeep/python-zeep_git.bb
# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
LICENSE = "Unknown"
LIC_FILES_CHKSUM = "file://LICENSE;md5=23356a26b06086844496d9e634f58ae5"

SRC_URI = "git://github.com/mvantellingen/python-zeep.git;protocol=https;branch=master"

# Modify these as desired
PV = "1.0+git"
SRCREV = "c80519ef01216f8e5bcfe8de7995d841e03b0f2a"

S = "${WORKDIR}/git"

inherit python_setuptools_build_meta

$ vi ./builds/build-imx8mm-evk-scarthgap-home/workspace/recipes/python-zeep/python-zeep_git.bb
LICENSE = "MIT"
```

#### D. build

```bash
$ devtool build python-zeep
```

#### E. finish

```bash
# 將 python-zeep*.bb 安裝到指定的目錄
$ devtool finish python-zeep ./layers-scarthgap/meta-homeassistant-plus/recipes-homeassistant-plus/homeassistant-plus/
```

#### F. reset

```bash
# 將 python-zeep*.bb 安裝到指定的目錄
$ devtool reset python-zeep
```

### 7.6.1. [commentjson](https://pypi.org/project/commentjson) (pypi)

> 這邊以 python-zeep 為範例，

#### A. Check exist

```bash
$ bitbake -s | grep commentjson
```

#### B. add

```bash
$ devtool add python3-commentjson https://files.pythonhosted.org/packages/source/c/commentjson/commentjson-0.9.0.tar.gz

$ ll ./builds/build-imx8mm-evk-scarthgap-home/workspace/recipes/python3-commentjson/python3-commentjson_0.9.0.bb
-rw-rw-r-- 1 lanka lanka 1642 Jul 23 15:45 ./builds/build-imx8mm-evk-scarthgap-home/workspace/recipes/python3-commentjson/python3-commentjson_0.9.0.bb
```

#### C. python3-commentjson_0.9.0.bb

```bash
$ cat ./builds/build-imx8mm-evk-scarthgap-home/workspace/recipes/python3-commentjson/python3-commentjson_0.9.0.bb
# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

SUMMARY = "Add Python and JavaScript style comments in your JSON files."
HOMEPAGE = "https://github.com/vaidik/commentjson"
# NOTE: License in setup.py/PKGINFO is: UNKNOWN
# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.rst;md5=722175c22a1ab5e2e0fd153da885198b"

SRC_URI[sha256sum] = "42f9f231d97d93aff3286a4dc0de39bfd91ae823d1d9eba9fa901fe0c7113dd4"

inherit pypi setuptools3

# WARNING: the following rdepends are from setuptools install_requires. These
# upstream names may not correspond exactly to bitbake package names.
RDEPENDS:${PN} += "python3-lark-parser"

# WARNING: the following rdepends are determined through basic analysis of the
# python sources, and might not be 100% accurate.
RDEPENDS:${PN} += "python3-core python3-json python3-lark python3-six python3-tests python3-unittest"

# WARNING: We were unable to map the following python package/module
# dependencies to the bitbake packages which include them:
#    json.tests.test_decode
#    json.tests.test_dump
#    json.tests.test_encode_basestring_ascii
#    json.tests.test_float
#    json.tests.test_indent
#    json.tests.test_pass1
#    json.tests.test_pass2
#    json.tests.test_pass3
#    json.tests.test_recursion
#    json.tests.test_separators
#    json.tests.test_unicode
#    simplejson

PYPI_PACKAGE = "commentjson"
```

#### D. build

> 基本上沒辦法編譯過，因為從 RDEPENDS 可以知道其它相依性

```bash
$ devtool build python3-commentjson
```

#### E. finish

```bash
# 將 python3-commentjson*.bb 安裝到指定的目錄
$ devtool finish python3-commentjson ./layers-scarthgap/meta-homeassistant-plus/recipes-homeassistant-plus/homeassistant-plus/
```

#### F. reset

```bash
# 還原 python3-commentjson
$ devtool reset python3-commentjson
```

# 8. Query recipes

## 8.1. Target

```bash
$ echo $PJ_YOCTO_TARGET
core-image-base

$ bb-info $PJ_YOCTO_TARGET

core-image-base                                       :1.0-r0

./layers-master-2b733d5/poky/meta/recipes-core/images/core-image-base.bb

SRC_URI=""

S="/yocto/cookerX-pi3/builds/build-pi3-master-2b733d5/tmp/work/raspberrypi3-poky-linux-gnueabi/core-image-base/1.0-r0/core-image-base-1.0"

WORKDIR="/yocto/cookerX-pi3/builds/build-pi3-master-2b733d5/tmp/work/raspberrypi3-poky-linux-gnueabi/core-image-base/1.0-r0"

DEPENDS="  syslinux-native bmap-tools-native cdrtools-native btrfs-tools-native squashfs-tools-native e2fsprogs-native erofs-utils-native virtual/arm-poky-linux-gnueabi-binutils   qemuwrapper-cross depmodwrapper-cross cross-localedef-native"

RDEPENDS="     packagegroup-core-boot     packagegroup-base-extended               example run-postinsts psplash-raspberrypi packagegroup-core-ssh-dropbear locale-base-en-us locale-base-en-gb "
RDEPENDS:${KERNEL_PACKAGE_NAME}-base=""
RDEPENDS:core-image-base-staticdev="core-image-base-dev (= 1.0-r0)"
```

## 8.2. *.bb

> 以下用 avahi 為範例

### 8.2.1. bb-info

| NAME     | FULL NAME          | TIME     |
| -------- | ------------------ | -------- |
| DEPENDS  | Build-time Depends | 編譯階段 |
| RDEPENDS | Runtime Depends    | 執行階段 |

```bash
$ bb-info avahi

avahi                                                 :0.8-r0

./bb-lnk/avahi_0.8.bb
./layers-master-2b733d5/poky/meta/recipes-connectivity/avahi/avahi_0.8.bb

SRC_URI="https://github.com/lathiat/avahi/releases//download/v0.8/avahi-0.8.tar.gz            file://00avahi-autoipd            file://99avahi-autoipd            file://initscript.patch            file://0001-Fix-opening-etc-resolv.conf-error.patch            file://handle-hup.patch            file://local-ping.patch            "

S="/yocto/cookerX-pi3/builds/build-pi3-master-2b733d5/tmp/work/cortexa7t2hf-neon-vfpv4-poky-linux-gnueabi/avahi/0.8-r0/avahi-0.8"

WORKDIR="/yocto/cookerX-pi3/builds/build-pi3-master-2b733d5/tmp/work/cortexa7t2hf-neon-vfpv4-poky-linux-gnueabi/avahi/0.8-r0"

DEPENDS="pkgconfig-native autoconf-native automake-native libtool-native libtool-cross  virtual/arm-poky-linux-gnueabi-gcc virtual/arm-poky-linux-gnueabi-compilerlibs virtual/libc gettext-native expat libcap libdaemon glib-2.0 python3-native  gobject-introspection gobject-introspection-native qemu-native update-rc.d initscripts base-files shadow-native shadow-sysroot shadow base-passwd dbus"

RDEPENDS:${KERNEL_PACKAGE_NAME}-base=""
RDEPENDS:avahi-dnsconfd="avahi-daemon"
RDEPENDS:avahi-staticdev="avahi-dev (= 0.8-r0)"
```

```bash
bitbake -s | grep avahi
find -name avahi*.bb
bitbake -e avahi | grep ^SRC_URI=
bitbake -e avahi | grep ^S=
bitbake -e avahi | grep ^WORKDIR=
bitbake -e avahi | grep ^DEPENDS
bitbake -e avahi | grep ^RDEPENDS
```

### 8.2.2. build

```bash
$ make build
BB_TASK=[build], BB=[]
Example:
  make listtasks BB=avahi
  make configure BB=avahi
  make clean BB=avahi
  make cleanall BB=avahi
  make fetch BB=avahi
  make compile BB=avahi
  make build BB=avahi
  make install BB=avahi
  make package_qa BB=avahi

$ make build BB=avahi
#or
$ bitbake -c build avahi
```

### 8.2.3. List the files of bb

```bash
$ oe-pkgdata-util list-pkg-files avahi
avahi:

# 如果空空的，請改下面的方式
$ oe-pkgdata-util list-pkgs | grep avahi

$ oe-pkgdata-util list-pkg-files avahi-daemon
```

# Appendix

# I. Study

## I.1. [create a layer](https://blog.csdn.net/CSDN1013/article/details/111088399)

# II. Debug

## II.1. ../qemu-4.1.0/linux-user/syscall.c:7657: undefined reference to `stime'

- [QEMU 3.1.0安装手记](https://segmentfault.com/a/1190000041094251)
- [qemu: Replace stime() API with clock_settime](https://git.openembedded.org/openembedded-core/commit/?h=dunfell&id=2cca75155baec8358939e2aae822e256bed4cfe0)

```bash
$ bitbake -s | grep qemu
$ find -name qemu*.bb
./layers/poky/meta/recipes-devtools/qemu/qemu-helper-native_1.0.bb
./layers/poky/meta/recipes-devtools/qemu/qemu-native_4.1.0.bb
./layers/poky/meta/recipes-devtools/qemu/qemuwrapper-cross_1.0.bb
./layers/poky/meta/recipes-devtools/qemu/qemu_4.1.0.bb
./layers/poky/meta/recipes-devtools/qemu/qemu-system-native_4.1.0.bb

```

```
修改
./layers/poky/meta/recipes-devtools/qemu/qemu-native.inc

新增檔案
./layers/poky/meta/recipes-devtools/qemu/qemu/0012-linux-user-remove-host-stime-syscall.patch
```

## II.2. Fetcher failure for URL: 'git://github.com/RPi-Distro/firmware-nonfree'

- [linux-firmware-rpidistro: add branch in SRC_URI](https://lore.kernel.org/all/a4a248b1-4a28-4c38-981b-76bd7013ec6f@www.fastmail.com/T/)

## II.3. it has a restricted license 'synaptics-killswitch'

> linux-firmware-rpidistro RPROVIDES linux-firmware-rpidistro-bcm43456 but was skipped: because it has a restricted license 'synaptics-killswitch'. Which is not listed in LICENSE_FLAGS_ACCEPTED

#### - local.conf

```bash
LICENSE_FLAGS_ACCEPTED = 'synaptics-killswitch'
```

## II.4. sh: no job control in this shell

> [    1.795126] Run /sbin/init as init process
> [    1.795772] hid-generic 0003:0627:0001.0002: input: USB HID v1.11 Keyboard [QEMU QEMU USB Keyboard] on usb-0000:00:1d.7-2/input0
> [    1.796985] Run /etc/init as init process
> [    1.797744] Run /bin/init as init process
> [    1.798366] Run /bin/sh as init process
> sh: cannot set terminal process group (-1): Inappropriate ioctl for device
> sh: no job control in this shell
> sh-5.2#
> [    2.108334] IPv6: ADDRCONF(NETDEV_CHANGE): eth0: link becomes ready

#### - busybox in rootfs ?

```bash
# check busybox exist
$ ls -al rootfs/bin/busybox
```

#### - local.conf

```bash
Fail:
,"IMAGE_INSTALL += ' example'"

Ok:
,"IMAGE_INSTALL:append = ' example'"
```

## II.5. /bin/sh: cert-to-efi-sig-list: command not found

```bash
$ sudo apt update
$ sudo apt install efitools
```

# III. Glossary

# IV. Tool Usage

# Author

> Created and designed by [Lanka Hsu](lankahsu@gmail.com).

# License

> [CrossCompilationX](https://github.com/lankahsu520/CrossCompilationX) is available under the BSD-3-Clause license. See the LICENSE file for more info.

