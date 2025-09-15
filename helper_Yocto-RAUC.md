# [Yocto](https://www.yoctoproject.org) [RAUC (Robust Auto-Update Controller)](https://rauc.readthedocs.io/en/latest/integration.html)

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

> 一套 Firmware 升級系統。有支援 `Dual Image`。

> 網路上的範例和討論都很陽春，不只介紹殘缺，更是錯誤百出。
>
> 如果要花大把的時間去研讀官方文件，可能你的工作已經沒了。

> [ChatGPT] meta-rauc 的功用
>
> `meta-rauc` 是一個專門為 **RAUC（Robust Auto-Update Controller）** 提供 Yocto 整合的 **Layer**，用途是讓你能輕鬆地在 Yocto 系統裡啟用、設定並建構 RAUC OTA 更新功能。
>
> | 功能                        | 說明                                                         |
> | --------------------------- | ------------------------------------------------------------ |
> | 提供 RAUC 的 bitbake recipe | 安裝 `rauc` 主程式、相關工具、DBus 服務等                    |
> | 整合 systemd 支援           | 提供 `rauc.service` 啟用 systemd 開機啟動                    |
> | 支援 keyring 安全驗證       | 預設支援製作與部署公私鑰 (`keyring.pem`, `private.pem`) 驗證 bundle |
> | 提供 `system.conf` 範本     | 給你參考如何定義 A/B slot 等更新邏輯                         |
> | 整合與 WIC image 工具       | 搭配 A/B 分割 wks 建立更新支援的 rootfs image                |

# 2. Build and Target

> 請善用 [cookerX](https://github.com/lankahsu520/CrossCompilationX/tree/master/Yocto/cookerX)，這是本作者包裝後的整合平台。以下內容都將採用此開發。

## 2.1. Environment

| ITEM        | VERSION         |
| ----------- | --------------- |
| Hardware    | 8MMINILPD4‑EVKB |
| Yocto       | 5.0 Scarthgap   |
| Building OS | Ubuntu 20.04    |
| Python      | 3.10.18         |

## 2.2. Build

| ITEM         | FILE                                              |
| ------------ | ------------------------------------------------- |
| configure    | imx8mm-scarthgap-rauc-home2023.12.0.conf          |
| cooker-menu: | imx8mm-evk-scarthgap-rauc-home2023.12.0-menu.json |

```bash
$ git clone https://github.com/lankahsu520/CrossCompilationX.git
$ cd CrossCompilationX/Yocto/cookerX/
$ . confs/imx8mm-scarthgap-rauc.conf
$ make
```

## 2.3. Target

| ITEM        | FILE                                            |
| ----------- | ----------------------------------------------- |
| u-boot      | imx-boot-imx8mm-lpddr4-evk-sd.bin-flash_evk     |
| u-boot-env  | u-boot-imx-initial-env-sd                       |
| Image       | imx-image-core-imx8mm-lpddr4-evk.rootfs.wic.zst |
| RAUC Bundle | update-bundle-imx8mm-lpddr4-evk.raucb           |

## 2.4. Burn

### 2.4.1. uuu

```bash
$ cd /drives/d/WINAPPS/Worker/uuu
$ uuu -lsusb
$ uuu -b emmc_all \
 ./evkb/imx-boot-imx8mm-lpddr4-evk-sd.bin-flash_evk \
 ./evkb/imx-image-core-imx8mm-lpddr4-evk.rootfs.wic.zst
```

### 2.4.2. rauc install

```bash
root@imx8mm-lpddr4-evk:~# rauc install /tmp/update-bundle-imx8mm-lpddr4-evk.raucb
```

# 2. [meta-rauc](https://github.com/rauc/meta-rauc.git)

## 2.1. Add layer

### 2.1.1. update $PJ_COOKER_MENU

>  DISTRO_FEATURES: rauc
>
>  IMAGE_FSTYPES: wic.zst ext4
>
>  會在編譯時產出 imx-image-core-imx8mm-lpddr4-evk.rootfs.ext4；如果保留 wic.zst，將不會再看到wic.zst 產出，這或許是本身的 Bugs。
>
>  IMAGE_INSTALL: rauc  libubootenv-bin
>
>  WKS_FILE:  使用 imx-imx-boot-bootpart-lanka520.wks.in

```bash
$ echo $PJ_COOKER_MENU
imx8mm-evk-scarthgap-rauc-home2023.12.0-menu.json

# 更新 $PJ_COOKER_MENU
$ vi cooker-menu/$PJ_COOKER_MENU
  ...
  "sources": [
    {
      "url": "https://github.com/rauc/meta-rauc.git",
      "branch": "scarthgap",
      "dir": "meta-rauc",
      "rev": "a0f4a8b9986954239850b9d4256c003c91e6b931"
    },
  ],
  "layers": [
    "meta-rauc",
  ],
  "builds": {
    "imx8mm-evk-scarthgap-rauc": {
      "local.conf": [
        "DISTRO_FEATURES:append = ' rauc'",
        "IMAGE_FSTYPES += ' wic.zst ext4'",
        "IMAGE_INSTALL:append = ' rauc libubootenv-bin'",
        "WKS_FILE = 'imx-imx-boot-bootpart-lanka520.wks.in'",
      ]
    }
  }
```

> 這邊要確定是否符合 Yocto 的版本
>
> <font color="red">LAYERSERIES_COMPAT_rauc = "nanbield scarthgap"</font>

```bash
$ cat $PJ_YOCTO_LAYERS_DIR/meta-rauc/conf/layer.conf
# We have a conf and classes directory, add to BBPATH
BBPATH .= ":${LAYERDIR}"

# We have recipes-* directories, add to BBFILES
BBFILES += "${LAYERDIR}/recipes-*/*/*.bb \
        ${LAYERDIR}/recipes-*/*/*.bbappend" 

BBFILE_COLLECTIONS += "rauc"
BBFILE_PATTERN_rauc = "^${LAYERDIR}/"
BBFILE_PRIORITY_rauc = "6"

LAYERDEPENDS_rauc = "core"

# meta-python is needed to build/run hawkbit-client
LAYERRECOMMENDS_rauc = "meta-python"

# meta-filesystems is needed to build cascync with fuse support (the default)
LAYERRECOMMENDS_rauc += "meta-filesystems"

LAYERSERIES_COMPAT_rauc = "nanbield scarthgap"

# Sanity check for meta-rauc layer.
# Setting SKIP_META_RAUC_FEATURE_CHECK to "1" would skip the bbappend files check.
INHERIT += "sanity-meta-rauc"
```

## 2.2. Nothing

> 如果只是加入 meta-rauc 是沒有任何功用的。

# 3. meta-rauc-plus

## 3.1. create-layer

```bash
$ echo $PJ_YOCTO_LAYERS_DIR
/yocto/cookerX-scarthgap/layers-scarthgap
$ cd $PJ_YOCTO_LAYERS_DIR
$ bitbake-layers create-layer meta-rauc-plus
NOTE: Starting bitbake server...
Add your new layer with 'bitbake-layers add-layer meta-rauc-plus'

# 這邊刪除範例
$ rm -rf recipes-example

$ echo $PJ_COOKER_MENU
imx8mm-evk-scarthgap-rauc-home2023.12.0-menu.json

$ cd-root; vi ./cooker-menu/$PJ_COOKER_MENU
# add "meta-rauc-plus" into "layers"
$ cooker generate
$ bitbake-layers show-layers | grep meta-rauc-plus
$ cat $PJ_YOCTO_BUILD_DIR/conf/bblayers.conf | grep meta-rauc-plus

$ cd-root
$ make cook-clean
```

### 3.1.1. update $PJ_COOKER_MENU

```bash
$ echo $PJ_COOKER_MENU
imx8mm-evk-scarthgap-rauc-home2023.12.0-menu.json

# 更新 $PJ_COOKER_MENU
$ vi cooker-menu/$PJ_COOKER_MENU
  ...
  "sources": [
    {
      "url": "https://github.com/rauc/meta-rauc.git",
      "branch": "scarthgap",
      "dir": "meta-rauc",
      "rev": "a0f4a8b9986954239850b9d4256c003c91e6b931"
    },
  ],
  "layers": [
    "meta-rauc",
    "meta-rauc-plus",
  ],
  "builds": {
    "imx8mm-evk-scarthgap-rauc": {
      "target": "imx-image-core",
      "local.conf": [
        "MACHINE = 'imx8mm-lpddr4-evk'",
        "DISTRO = 'fsl-imx-wayland'",
        "CONF_VERSION = '2'",
        "ACCEPT_FSL_EULA = '1'",
        "DISTRO_FEATURES:append = ' rauc'",
        "EXTRA_IMAGE_FEATURES = 'debug-tweaks package-management'",
        "PATCHRESOLVE = 'noop'",
        "PACKAGECONFIG:append:pn-qemu-system-native = 'sdl'",
        "IMAGE_FSTYPES += ' wic.zst ext4'",
        "IMAGE_INSTALL:append = ' rauc libubootenv-bin tree helloworld123'",
        "PACKAGE_CLASSES = 'package_rpm'",
        "USER_CLASSES = 'buildstats'",
        "WKS_FILE = 'imx-imx-boot-bootpart-lanka520.wks.in'",
      ]
    },
  }
```

## 3.2. RAUC system configuration & verification keyring

> [Bundle Formats](https://rauc.readthedocs.io/en/latest/reference.html#id9): plain, verity and crypt
>
> The `verity` format was added to support new use cases like network streaming, for better parallelization of installation with hash verification and to detect modification of the bundle during installation.
>
> The `crypt` format is an extension to the `verity` format that allows full encryption of the bundle.

> 細節不研究。
>
> rootfs 會加入兩個檔案 ca.cert.pem 和 system.conf

```bash
$ tree -L 4 ${PJ_YOCTO_LAYERS_DIR}/meta-rauc-plus/recipes-core/rauc
/yocto/cookerX-scarthgap/layers-scarthgap/meta-rauc-plus/recipes-core/rauc
├── files
│   ├── ca.cert.pem
│   └── system.conf
└── rauc-conf.bbappend

1 directory, 3 files
```

### 3.2.1. Generate Testing Certificate

> 建議使用 meta-rauc/scripts/openssl-ca.sh，加解密的處理，有時候只是差個空格都會失敗

```bash
$ cd ${PJ_YOCTO_LAYERS_DIR}/meta-rauc/scripts
$ ./openssl-ca.sh

$ tree -L 4 openssl-ca
openssl-ca
├── dev
│   ├── ca.cert.pem
│   ├── ca.csr.pem
│   ├── certs
│   │   ├── 01.pem
│   │   └── 02.pem
│   ├── development-1.cert.pem
│   ├── development-1.csr.pem
│   ├── index.txt
│   ├── index.txt.attr
│   ├── index.txt.attr.old
│   ├── index.txt.old
│   ├── private
│   │   ├── ca.key.pem
│   │   └── development-1.key.pem
│   ├── serial
│   └── serial.old
└── openssl.cnf

3 directories, 15 files

$ cp -av openssl-ca/dev/ca.cert.pem ${PJ_YOCTO_LAYERS_DIR}/meta-rauc-plus/recipes-core/rauc/files

$ echo ${PJ_YOCTO_ROOT}
/yocto/cookerX-scarthgap
$ mkdir -p ${PJ_YOCTO_ROOT}/rauc-keys
$ cp -av openssl-ca/dev/private/development-1.key.pem ${PJ_YOCTO_ROOT}/rauc-keys
$ cp -av openssl-ca/dev/development-1.cert.pem ${PJ_YOCTO_ROOT}/rauc-keys

$ tree -L 4 ${PJ_YOCTO_ROOT}/rauc-keys
/yocto/cookerX-scarthgap/rauc-keys
├── ca.cert.pem
├── development-1.cert.pem
└── development-1.key.pem

0 directories, 4 files
```

### 3.2.2. system.cnf

> 使用  RAUC 進行系統更新時，會透過 D-Bus 與 rauc.service 溝通。而 service 將會參照  system.conf。

```conf
[system]
compatible=Lanka520
bootloader=uboot
#bundle-formats=plain
bundle-formats=verity
#bundle-formats=crypt

[keyring]
path=/etc/rauc/ca.cert.pem

[slot.rootfs.A]
device=/dev/mmcblk2p2
type=ext4
bootname=A
#mountpoint=/

[slot.rootfs.B]
device=/dev/mmcblk2p3
type=ext4
bootname=B
#mountpoint=/

[slot.root.0]
device=/dev/mmcblk2p4
type=ext4
#mountpoint=/root
```

### 3.2.3. rauc-conf.bbappend

```bbappend
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

RAUC_KEYRING_FILE = "ca.cert.pem"

SRC_URI:append = " \
    file://system.conf \
    file://ca.cert.pem\
"

do_install:append() {
    install -d ${D}${sysconfdir}/rauc
    install -m 0644 ${WORKDIR}/system.conf ${D}${sysconfdir}/rauc/system.conf
    install -m 0644 ${WORKDIR}/ca.cert.pem ${D}${sysconfdir}/rauc/ca.cert.pem
}
```

## 3.3. Disk Partition

> RAUC 實現了 `Dual Image`，於是 Disk 的配置也要改變。

```bash
$ tree -L 4 ${PJ_YOCTO_LAYERS_DIR}/meta-freescale/wic
/yocto/cookerX-scarthgap/layers-scarthgap/meta-freescale/wic
├── imx-imx-boot-bootpart-lanka520.wks.in
├── imx-imx-boot-bootpart.wks.in
├── imx-imx-boot.wks.in
├── imx-uboot-bootpart.wks.in
├── imx-uboot-mxs-bootpart.wks.in
├── imx-uboot-mxs.wks.in
├── imx-uboot-spl-bootpart.wks.in
├── imx-uboot-spl.wks.in
├── imx-uboot.wks
└── ls104x-uboot-bootpart.wks.in

0 directories, 10 files

$ tree -L 4 ${PJ_YOCTO_LAYERS_DIR}/meta-rauc-plus/recipes-core/base-files
/yocto/cookerX-scarthgap/layers-scarthgap/meta-rauc-plus/recipes-core/base-files
├── base-files
│   └── etc
│       └── fstab
└── base-files_%.bbappend

2 directories, 2 files
```

### 3.3.1. OpenEmbedded Kickstart (wks)

> 這邊有幾個重點，官網提供的設定只能測試用，而且錯誤很多
>
> ~~u-boot-env: 要指定 u-boot-imx-initial-env-sd~~
>
> /: 分割兩塊 rootfs
>
> ~~data: 保留使用者區塊~~
>
> root: 保留使用者區塊，直接對應到 home

| Default            | Default Size                  | New Size                       | New                                           |
| ------------------ | ----------------------------- | ------------------------------ | --------------------------------------------- |
| mtdblock0          | 33,554,432<br/>(~32 MB)       | 33,554,432<br/>(~32 MB)        | mtdblock0                                     |
| mmcblk2            | 31,268,536,320<br>(~29.12 GB) | 31,268,536,320<br/>(~29.12 GB) | mmcblk2                                       |
| mmcblk2p1 (boot)   | 348,965,888<br>(~332.8 MB)    | 348,965,888<br/>(~332.8 MB)    | mmcblk2p1 (boot)<br>/run/media/boot-mmcblk2p1 |
| mmcblk2p2 (rootfs) | 1,898,146,816<br>(~1.8 GB)    | 5,583,457,280<br>(~5.2 GB)     | mmcblk2p2 (rootfs A)                          |
|                    |                               | 5,583,457,280<br/>(~5.2 GB)    | mmcblk2p3 (rootfs B)                          |
|                    |                               | 4,294,967,296<br>(~4 GB)       | mmcblk2p4 (root)<br/>/root                    |
| mmcblk2boot0       | 4,194,304                     |                                |                                               |
| mmcblk2boot1       | 4,194,304                     |                                |                                               |

```bash
root@imx8mm-lpddr4-evk:~# fdisk -l /dev/mmcblk2
Disk /dev/mmcblk2: 29.12 GiB, 31268536320 bytes, 61071360 sectors
Units: sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disklabel type: dos
Disk identifier: 0x076c4a2a

Device         Boot    Start      End  Sectors   Size Id Type
/dev/mmcblk2p1 *       16384   697957   681574 332.8M  c W95 FAT32 (LBA)
/dev/mmcblk2p2        704512 11609701 10905190   5.2G 83 Linux
/dev/mmcblk2p3      11616256 22521445 10905190   5.2G 83 Linux
/dev/mmcblk2p4      22528000 30916607  8388608     4G 83 Linux
```

#### A. imx-imx-boot-bootpart-lanka520.wks.in

```bash
part u-boot --source rawcopy --sourceparams="file=imx-boot.tagged" --ondisk mmcblk --no-table --align 33

part /boot --source bootimg-partition --ondisk mmcblk --fstype=vfat --label boot --active --align 8192 --size 256

#part u-boot-env --source rawcopy --sourceparams="file=u-boot-imx-initial-env-sd" --ondisk mmcblk --no-table --offset 7M

part / --source rootfs --ondisk mmcblk --fstype=ext4 --label rootfs.a --align 8192 --size 4096M

part / --source rootfs --ondisk mmcblk --fstype=ext4 --label rootfs.b --align 8192 --size 4096M

#part /data --ondisk mmcblk --fstype=ext4 --label data --align 8192 --source empty --size 0
part /root --ondisk mmcblk --fstype=ext4 --label root --align 8192 --size 4096M

bootloader --ptable msdos
```

### 3.3.2. fstab

> 開機自動掛載 /dev/mmcblk2p4 -> /root

```bash
# stock fstab - you probably want to override this with a machine specific one

/dev/root            /                    auto       defaults              1  1
proc                 /proc                proc       defaults              0  0
devpts               /dev/pts             devpts     mode=0620,ptmxmode=0666,gid=5      0  0
tmpfs                /run                 tmpfs      mode=0755,nodev,nosuid,strictatime 0  0
tmpfs                /var/volatile        tmpfs      defaults              0  0

# uncomment this if your device has a SD/MMC/Transflash slot
#/dev/mmcblk0p1       /media/card          auto       defaults,sync,noauto  0  0

/dev/mmcblk2p4 /root auto defaults 0 2
```

### 3.3.3. base-files_%.bbappend

```bbappend
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
	file://etc/fstab \
"

do_install:append() {
	install -m 0755 ${WORKDIR}/etc/fstab ${D}${sysconfdir}/fstab
}
```

## 3.4. u-boot

> 這邊主要因為要使用 fw_printenv

### 3.4.1. u-boot-imx

> 網路上沒有一篇是對的，大多是複製貼上，就連官網也是。這邊讓大家知道如何查詢
>
> CONFIG_ENV_SIZE=0x4000
> CONFIG_ENV_OFFSET=0x700000

```bash
$ bitbake -e u-boot-imx | grep ^UBOOT_CONFIG=
UBOOT_CONFIG="sd"
$ bitbake -e u-boot-imx | grep ^UBOOT_MACHINE=
UBOOT_MACHINE=" imx8mm_evk_defconfig"

$ cat $PJ_YOCTO_BUILD_DIR/tmp/work/imx8mm_lpddr4_evk-poky-linux/u-boot-imx/2024.04/git/configs/imx8mm_evk_defconfig | grep CONFIG_ENV_
CONFIG_ENV_SIZE=0x4000
CONFIG_ENV_OFFSET=0x700000
CONFIG_ENV_OVERWRITE=y
CONFIG_ENV_IS_IN_MMC=y
CONFIG_ENV_VARS_UBOOT_RUNTIME_CONFIG=y
```

```bash
$ bb-info u-boot-imx

nativesdk-u-boot-imx-tools                        :2024.04-r0
u-boot-imx                                        :2024.04-r0
u-boot-imx-tools                                  :2024.04-r0
u-boot-imx-tools-native                           :2024.04-r0

./layers-scarthgap/meta-freescale/recipes-bsp/u-boot/u-boot-imx_2024.04.bb
./layers-scarthgap/meta-imx/meta-imx-bsp/recipes-bsp/u-boot/u-boot-imx_2024.04.bb
./layers-scarthgap/meta-imx/meta-imx-bsp/recipes-bsp/u-boot/u-boot-imx-tools_2024.04.bb

SRC_URI="git://github.com/nxp-imx/uboot-imx.git;protocol=https;branch=lf_v2024.04      file://boot.cmd "

S="/yocto/cookerX-scarthgap/builds/build-imx8mm-evk-scarthgap-rauc-home/tmp/work/imx8mm_lpddr4_evk-poky-linux/u-boot-imx/2024.04/git"

WORKDIR="/yocto/cookerX-scarthgap/builds/build-imx8mm-evk-scarthgap-rauc-home/tmp/work/imx8mm_lpddr4_evk-poky-linux/u-boot-imx/2024.04"

DEPENDS="virtual/aarch64-poky-linux-gcc virtual/aarch64-poky-linux-compilerlibs virtual/libc  u-boot-mkimage-native swig-native kern-tools-native      bc-native     bison-native     dtc-native     flex-native     gnutls-native     xxd-native  u-boot-mkimage-native bc-native dtc-native swig-native python3-native flex-native bison-native python3-native  openssl-native"

RDEPENDS:${KERNEL_PACKAGE_NAME}-image:imx8dx-mek=""
RDEPENDS:${KERNEL_PACKAGE_NAME}-image:imx8dxl-a1-ddr3l-evk=""
RDEPENDS:${KERNEL_PACKAGE_NAME}-image:imx8dxl-a1-lpddr4-evk=""
RDEPENDS:${KERNEL_PACKAGE_NAME}-image:imx8dxl-b0-ddr3l-evk=""
RDEPENDS:${KERNEL_PACKAGE_NAME}-image:imx8dxl-b0-lpddr4-evk=""
RDEPENDS:${KERNEL_PACKAGE_NAME}-image:imx8qm-mek=""
RDEPENDS:${KERNEL_PACKAGE_NAME}-image:imx8qxp-mek=""
RDEPENDS:u-boot-imx="  u-boot-imx-env"
RDEPENDS:u-boot-imx-staticdev="u-boot-imx-dev (= 2024.04-r0)"
```

```bash
$ bitbake -c cleansstate u-boot-imx
$ bitbake u-boot-imx
```

### 3.4.2. libubootenv

> In Yocto Project 3.1, `u-boot-fw-utils`: functionally replaced by `libubootenv`
>
> 首先要新增相關的 u-boot 工具，用於安裝 fw_printenv

```bash
$ bitbake -s | grep libubootenv
# yocto 已經內建 libubootenv
$ bb-info libubootenv

libubootenv                                         :0.3.5-r0
libubootenv-native                                  :0.3.5-r0

./layers-scarthgap/poky/meta/recipes-bsp/u-boot/libubootenv_0.3.5.bb

SRC_URI="git://github.com/sbabic/libubootenv;protocol=https;branch=master file://fw_env.config"

S="/yocto/cookerX-scarthgap/builds/build-imx8mm-evk-scarthgap-rauc-home/tmp/work/armv8a-poky-linux/libubootenv/0.3.5/git"

WORKDIR="/yocto/cookerX-scarthgap/builds/build-imx8mm-evk-scarthgap-rauc-home/tmp/work/armv8a-poky-linux/libubootenv/0.3.5"

DEPENDS="cmake-native virtual/aarch64-poky-linux-gcc virtual/aarch64-poky-linux-compilerlibs virtual/libc zlib libyaml ninja-native"

RDEPENDS:${KERNEL_PACKAGE_NAME}-image:imx8dx-mek=""
RDEPENDS:${KERNEL_PACKAGE_NAME}-image:imx8dxl-a1-ddr3l-evk=""
RDEPENDS:${KERNEL_PACKAGE_NAME}-image:imx8dxl-a1-lpddr4-evk=""
RDEPENDS:${KERNEL_PACKAGE_NAME}-image:imx8dxl-b0-ddr3l-evk=""
RDEPENDS:${KERNEL_PACKAGE_NAME}-image:imx8dxl-b0-lpddr4-evk=""
RDEPENDS:${KERNEL_PACKAGE_NAME}-image:imx8qm-mek=""
RDEPENDS:${KERNEL_PACKAGE_NAME}-image:imx8qxp-mek=""
RDEPENDS:libubootenv-staticdev="libubootenv-dev (= 0.3.5-r0)"
```

```bash
$ tree -L 4 ${PJ_YOCTO_LAYERS_DIR}/meta-rauc-plus/recipes-bsp/u-boot
/yocto/cookerX-scarthgap/layers-scarthgap/meta-rauc-plus/recipes-bsp/u-boot
├── files
│   ├── boot.cmd
│   └── fw_env.config
├── libubootenv_%.bbappend
└── u-boot-imx_%.bbappend

1 directory, 4 files
```

#### A. fw_env.config

> fw_printenv 操作時，需要使用 /etc/fw_env.config
>
> 透過 libubootenv_%.bbappend 將 fw_env.config 檔案放入 rootfs
>
> 為什麼是 0x700000 0x400，請參照 u-boot-imx 裏的 imx8mm_evk_defconfig

```bash
/dev/mmcblk2 0x700000 0x4000
```

> 如果發生 Cannot read environment, using default。請重新開機進入 u-boot，saveenv 後，再重新進到Linux 就好了。

```bash
root@imx8mm-lpddr4-evk:~# cat /etc/fw_env.config
/dev/mmcblk2 0x700000 0x4000

root@imx8mm-lpddr4-evk:~# fw_printenv
Cannot read environment, using default
Cannot read default environment from file
```

#### B. libubootenv_%.bbappend

```bbappend
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://fw_env.config"

do_install:append() {
	# /etc/fw_env.config
	install -d ${D}${sysconfdir}
	install -m 0644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/fw_env.config
}
```

### 3.4.3. RAUC Bundle

> 這邊就是要產生 *.raucb

```bash
$ tree -L 4 ${PJ_YOCTO_LAYERS_DIR}/meta-rauc-plus/recipes-core/images
/yocto/cookerX-scarthgap/layers-scarthgap/meta-rauc-plus/recipes-core/images
├── imx-image-core.bbappend
└── update-bundle.bb

0 directories, 2 files
```

#### A. update-bundle.bb

> RAUC bundle generator

```bb
DESCRIPTION = "RAUC Bundle"
LICENSE = "MIT"
inherit bundle

RAUC_BUNDLE_COMPATIBLE = "Lanka520"
#RAUC_BUNDLE_FORMAT = "plain"
RAUC_BUNDLE_FORMAT = "verity"
#RAUC_BUNDLE_FORMAT = "crypt"
RAUC_BUNDLE_SLOTS ?= "rootfs"

RAUC_SLOT_rootfs ?= "imx-image-core"
RAUC_SLOT_rootfs[type] ?= "image"
RAUC_SLOT_rootfs[fstype] ?= "ext4"
RAUC_SLOT_rootfs[rename] ?= "rootfs.ext4"

RAUC_KEY_FILE = "${COOKER_LAYER_DIR}/../rauc-keys/development-1.key.pem"
RAUC_CERT_FILE = "${COOKER_LAYER_DIR}/../rauc-keys/development-1.cert.pem"
#RAUC_KEYRING_FILE = "${COOKER_LAYER_DIR}/../rauc-keys/ca.cert.pem"

#RAUC_CASYNC_BUNDLE = "1"
```

### 3.4.4. U-boot boot script

> u-boot-imx_%.bbappend : boot.cmd -> boot.scr
> imx-image-core.bbappend : boot.scr -> IMAGE_BOOT_FILES

```bash
# 確定有沒有 boot.scr
$ bitbake -e imx-image-core | grep ^IMAGE_BOOT_FILES=
```

```bash
$ tree -L 4 ${PJ_YOCTO_LAYERS_DIR}/meta-rauc-plus/recipes-bsp/u-boot
/yocto/cookerX-scarthgap/layers-scarthgap/meta-rauc-plus/recipes-bsp/u-boot
├── files
│   ├── boot.cmd
│   └── fw_env.config
├── libubootenv_%.bbappend
└── u-boot-imx_%.bbappend

1 directory, 4 files

$ tree -L 4 ${PJ_YOCTO_LAYERS_DIR}/meta-rauc-plus/recipes-core/images
/yocto/cookerX-scarthgap/layers-scarthgap/meta-rauc-plus/recipes-core/images
├── imx-image-core.bbappend
└── update-bundle.bb

0 directories, 2 files
```

#### A. boot.cmd

```cmd
test -n "${BOOT_ORDER}" || setenv BOOT_ORDER "A B"
test -n "${BOOT_A_LEFT}" || setenv BOOT_A_LEFT 3
test -n "${BOOT_B_LEFT}" || setenv BOOT_B_LEFT 3
test -n "${BOOT_DEV}" || setenv BOOT_DEV "mmc 2:1"

env set rootfs_part
env set rauc_slot

for BOOT_SLOT in "${BOOT_ORDER}"; do
    if test "x${rootfs_part}" != "x"; then
        # skip remaining slots

    elif test "x${BOOT_SLOT}" = "xA"; then
        if test ${BOOT_A_LEFT} -gt 0; then
            setexpr BOOT_A_LEFT ${BOOT_A_LEFT} - 1
            echo "Booting RAUC slot A"

            setenv rootfs_part "/dev/mmcblk2p2"
            setenv rauc_slot "A"
        fi

    elif test "x${BOOT_SLOT}" = "xB"; then
        if test ${BOOT_B_LEFT} -gt 0; then
            setexpr BOOT_B_LEFT ${BOOT_B_LEFT} - 1
            echo "Booting RAUC slot B"

            setenv rootfs_part "/dev/mmcblk2p3"
            setenv rauc_slot "B"
        fi
    fi
done

if test -n "${rootfs_part}"; then
    saveenv
else
    echo "No valid RAUC slot found. Resetting tries to 3"
    setenv BOOT_A_LEFT 3
    setenv BOOT_B_LEFT 3
    saveenv
    reset
fi

# set bootargs
setenv bootargs "console=${console} root=${rootfs_part} rootwait rw rauc.slot=${rauc_slot}"

# load dtb + kernel
fatload mmc 2:1 ${loadaddr} Image;
fatload mmc 2:1 ${fdt_addr_r} imx8mm-evk.dtb;
booti ${loadaddr} - ${fdt_addr_r}

```

#### B. u-boot-imx_%.bbappend

```bbappend
FILESEXTRAPATHS:append := "${THISDIR}/files:"

SRC_URI += " \
    file://boot.cmd \
"

DEPENDS += "u-boot-mkimage-native"
PROVIDES += "u-boot-default-script"

DEPENDS += "bc-native dtc-native swig-native python3-native flex-native bison-native"
EXTRA_OEMAKE += 'HOSTLDSHARED="${BUILD_CC} -shared ${BUILD_LDFLAGS} ${BUILD_CFLAGS}"'


# The UBOOT_ENV_SUFFIX and UBOOT_ENV are mandatory in order to run the
# uboot-mkimage command from poky/meta/recipes-bsp/u-boot/u-boot.inc
UBOOT_ENV_SUFFIX = "scr"
UBOOT_ENV = "boot"

do_deploy:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES','rauc','true','false',d)}; then
        install -d ${DEPLOYDIR}
        install -m 0644 ${WORKDIR}/${UBOOT_ENV_BINARY} ${DEPLOYDIR}
    fi
}

```

#### C. imx-image-core.bbappend

```bbappend

IMAGE_BOOT_FILES += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'rauc', 'boot.scr', '', d)} \
"

```

## 3.5. kernel

### 3.5.1. linux-imx

```bash
$ bb-info linux-imx

linux-imx                                      :6.6.52+git-r0
linux-imx-headers                                     :6.6-r0

./layers-scarthgap/meta-freescale/recipes-kernel/linux/linux-imx_6.6.bb
./layers-scarthgap/meta-freescale/recipes-kernel/linux/linux-imx-headers_6.6.bb
./layers-scarthgap/meta-imx/meta-imx-bsp/recipes-kernel/linux/linux-imx_6.6.bb
./layers-scarthgap/meta-imx/meta-imx-bsp/recipes-kernel/linux/linux-imx-headers_6.6.bb

SRC_URI="git://github.com/nxp-imx/linux-imx.git;protocol=https;branch=lf-6.6.y file://dm-verity.cfg"

S="/yocto/cookerX-scarthgap/builds/build-imx8mm-evk-scarthgap-rauc-home/tmp/work/imx8mm_lpddr4_evk-poky-linux/linux-imx/6.6.52+git/git"

WORKDIR="/yocto/cookerX-scarthgap/builds/build-imx8mm-evk-scarthgap-rauc-home/tmp/work/imx8mm_lpddr4_evk-poky-linux/linux-imx/6.6.52+git"

DEPENDS="pkgconfig-native   virtual/aarch64-poky-linux-binutils virtual/aarch64-poky-linux-gcc kmod-native bc-native bison-native   "

RDEPENDS:kernel="kernel-base (= 6.6.52+git-r0)"
RDEPENDS:kernel-image="  kernel-image-image (= 6.6.52+git-r0)"
RDEPENDS:kernel-image:imx8dx-mek=""
RDEPENDS:kernel-image:imx8dxl-a1-ddr3l-evk=""
RDEPENDS:kernel-image:imx8dxl-a1-lpddr4-evk=""
RDEPENDS:kernel-image:imx8dxl-b0-ddr3l-evk=""
RDEPENDS:kernel-image:imx8dxl-b0-lpddr4-evk=""
RDEPENDS:kernel-image:imx8qm-mek=""
RDEPENDS:kernel-image:imx8qxp-mek=""
RDEPENDS:linux-imx-staticdev="linux-imx-dev (= 6.6.52+git-r0)"
```

### 3.5.2. dm-verity.ko

> 解決 LastError: Failed mounting bundle: Failed to load dm table: Invalid argument, check DM_VERITY, DM_CRYPT or CRYPTO_AES kernel options.

> 這邊要特別注意，要重複確認好幾次

```bash
$ tree -L 4 ${PJ_YOCTO_LAYERS_DIR}/meta-rauc-plus/recipes-kernel/linux
/yocto/cookerX-scarthgap/layers-scarthgap/meta-rauc-plus/recipes-kernel/linux
├── files
│   └── dm-verity.cfg
└── linux-imx_%.bbappend

1 directory, 2 files
```

#### A. dm-verity.cfg

```bash
CONFIG_DM_VERITY=m
CONFIG_DM_VERITY_VERIFY_ROOTHASH_SIG=y
CONFIG_DM_VERITY_FEC=y
CONFIG_BLK_DEV_DM=y
CONFIG_DM_CRYPT=y
CONFIG_CRYPTO_SHA256=y
CONFIG_CRYPTO_SHA512=y
CONFIG_CRYPTO_AES=y
CONFIG_CRYPTO_XTS=y
CONFIG_CRYPTO_USER_API_HASH=y
CONFIG_CRYPTO_USER_API_SKCIPHER=y

```

#### B. linux-imx_%.bbappend

> 有幾種可以設定 Kernel 的方式
>
> [2.3.3. Changing the Configuration](https://docs.yoctoproject.org/kernel-dev/common.html#changing-the-configuration)
>
> [2.6 Configuring the Kernel](https://docs.yoctoproject.org/kernel-dev/common.html#configuring-the-kernel)
>
> DELTA_KERNEL_DEFCONFIG:  官網都沒有提到，這是最後測試完的結果。這邊就要抱怨一下，官網真的有用嗎？

```bash
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://dm-verity.cfg"
#SRC_URI += "file://defconfig"

DELTA_KERNEL_DEFCONFIG:append = " dm-verity.cfg"

```

#### C. menuconfig

```bash
# Device Drivers  ---> Multiple devices driver support (RAID and LVM)  ---> Verity target support
# Verity data device root hash signature verification support (NEW)
# Verity forward error correction support (NEW) 
$ bitbake -c menuconfig virtual/kernel
```

#### D. .config

```bash
$ vi $PJ_YOCTO_BUILD_DIR/tmp/work/imx8mm_lpddr4_evk-poky-linux/linux-imx/6.6.52+git/build/.config
# find CONFIG_DM_VERITY

$ cd $PJ_YOCTO_BUILD_DIR/tmp/work/imx8mm_lpddr4_evk-poky-linux/linux-imx/6.6.52+git/build
```

### 3.5.3. Build & Check

> 燒錄很花時間，所以請先驗證。
>
> 這邊保留常用命令以供測試。

```bash
#bitbake -c cleansstate linux-imx
bitbake -c clean linux-imx

bitbake -c kernel_configme -f linux-imx
bitbake -c kernel_configcheck -f linux-imx

bitbake -c configure linux-imx

# 這個會產出 defconfig
bitbake -c savedefconfig virtual/kernel
 
bitbake linux-imx

$ cd $PJ_YOCTO_BUILD_DIR
$ find * -name dm-verity.ko 
$ find * -name dm-crypt.ko
```

# 4. Check

## 4.1. Build

### 4.1.1. Image

```bash
$ bitbake -e $(PJ_YOCTO_IMAGE) | grep ^IMAGE_FSTYPES=
IMAGE_FSTYPES="  wic.zst ext4"

# 編譯
$ make image
# or
# bitbake $(PJ_YOCTO_IMAGE)
$ bitbake imx-image-core
```

### 4.1.2. bundle

```bash
# 編譯
$ make bundle
# or
# bitbake $(PJ_YOCTO_BUNDLE)
$ bitbake imx-bundle
```

## 4.2. Check Image

### 4.2.1. rootfs

```bash
$ make lnk-generate

$ cd-rootfs
$ cat ./usr/lib/systemd/system/rauc.service
$ cat ./etc/fstab
$ cat ./etc/rauc/system.conf 
```

### 4.2.2. ext4 and wic

```bash
$ cd-root
# check *.ext4
$ ll images-lnk/*.ext4
# check *.zst
$ ll images-lnk/*.zst
 
# check *.wic.zst
$ cp images-lnk/imx-image-core-imx8mm-lpddr4-evk.rootfs.wic.zst ./
$ unzstd imx-image-core-imx8mm-lpddr4-evk.rootfs.wic.zst
$ ll imx-image-core-imx8mm-lpddr4-evk.rootfs.wic
$ wic ls imx-image-core-imx8mm-lpddr4-evk.rootfs.wic
Num     Start        End          Size      Fstype
 1       8388608    357354495    348965888  fat16
 2     360710144   5944167423   5583457280  ext4
 3    5947523072  11530980351   5583457280  ext4
 4   11534336000  15829303295   4294967296  ext4
```

#### A. mount wic

```bash
$ sudo mkdir -p /tmp/wic
# 8388608=16384*512
$ sudo mount -o loop,offset=$((16384 * 512)) imx-image-core-imx8mm-lpddr4-evk.rootfs.wic /tmp/wic

# 看看有沒有 boot.scr
$ tree -L 1 /tmp/wic/
/tmp/wic/
├── boot.scr
├── Image
├── imx8mm-evk-8mic-revE.dtb
├── imx8mm-evk-8mic-swpdm.dtb
├── imx8mm-evk-ak4497.dtb
├── imx8mm-evk-ak5558.dtb
├── imx8mm-evk-audio-tdm.dtb
├── imx8mm-evk-dpdk.dtb
├── imx8mm-evk.dtb
├── imx8mm-evk-ecspi-slave.dtb
├── imx8mm-evk-inmate.dtb
├── imx8mm-evk-lk.dtb
├── imx8mm-evk-pcie-ep.dtb
├── imx8mm-evk-qca-wifi.dtb
├── imx8mm-evk-revb-qca-wifi.dtb
├── imx8mm-evk-rm67191-cmd-ram.dtb
├── imx8mm-evk-rm67191.dtb
├── imx8mm-evk-rm67199-cmd-ram.dtb
├── imx8mm-evk-rm67199.dtb
├── imx8mm-evk-root.dtb
├── imx8mm-evk-rpmsg.dtb
├── imx8mm-evk-rpmsg-wm8524.dtb
├── imx8mm-evk-rpmsg-wm8524-lpv.dtb
├── imx8mm-evk-usd-wifi.dtb
├── mcore-demos
└── tee.bin

1 directory, 25 files

$ sudo umount /tmp/wic
```

### 4.2.3. raucb

#### A. plain

```bash
$ cd-root
# check *.raucb
$ ll images-lnk/*.raucb

$ rauc info --no-verify images-lnk/update-bundle-imx8mm-lpddr4-evk.raucb
 
$ rauc info \
  --keyring=$PJ_YOCTO_LAYERS_DIR/meta-rauc-plus/recipes-core/rauc/files/ca.cert.pem \
  images-lnk/update-bundle-imx8mm-lpddr4-evk.raucb
```

#### B. verity

> 當設定 RAUC_BUNDLE_FORMAT = "verity"

```bash
$ bitbake rauc-native -c addto_recipe_sysroot

$ oe-run-native rauc-native rauc info \
  --keyring=$PJ_YOCTO_LAYERS_DIR/meta-rauc-plus/recipes-core/rauc/files/ca.cert.pem \
  images-lnk/update-bundle-imx8mm-lpddr4-evk.raucb
```

```bash
$ cd-rootfs
$ find123 dm-verity.ko
```

#### C. crypt

> 為了能正常使用 verity，就有一堆要修正，且研究和測試時間過長，不值得！
>
> 暫不花時間研究 crypt。

## 4.3.  Check on Board

### 4.3.1. Partitions

```bash
# 在板子查看
# /proc/partitions 的誤差很大
root@imx8mm-lpddr4-evk:~# cat /proc/partitions
major minor  #blocks  name

  31        0      32768 mtdblock0
 179        0   30535680 mmcblk2
 179        1     340787 mmcblk2p1
 179        2    5452595 mmcblk2p2
 179        3    5452595 mmcblk2p3
 179        4    4194304 mmcblk2p4
 179       32       4096 mmcblk2boot0
 179       64       4096 mmcblk2boot1

root@imx8mm-lpddr4-evk:~# fdisk -l /dev/mmcblk2
Disk /dev/mmcblk2: 29.12 GiB, 31268536320 bytes, 61071360 sectors
Units: sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disklabel type: dos
Disk identifier: 0x076c4a2a

Device         Boot    Start      End  Sectors   Size Id Type
/dev/mmcblk2p1 *       16384   697957   681574 332.8M  c W95 FAT32 (LBA)
/dev/mmcblk2p2        704512 11609701 10905190   5.2G 83 Linux
/dev/mmcblk2p3      11616256 22521445 10905190   5.2G 83 Linux
/dev/mmcblk2p4      22528000 30916607  8388608     4G 83 Linux
```

### 4.3.2. rauc.service

```bash
root@imx8mm-lpddr4-evk:~# cat /usr/lib/systemd/system/rauc.service

root@imx8mm-lpddr4-evk:~# cat /etc/fstab

root@imx8mm-lpddr4-evk:~# cat /etc/rauc/system.conf 

root@imx8mm-lpddr4-evk:~# systemctl status rauc
```

### 4.3.3. /boot

```bash
root@imx8mm-lpddr4-evk:~# ls -l /boot
total 33292
lrwxrwxrwx 1 root root       35 Mar  9  2018 Image -> Image-6.6.52-lts-next-ge0f9e2afd4cf
-rw-r--r-- 1 root root 35631616 Mar  9  2018 Image-6.6.52-lts-next-ge0f9e2afd4cf
```

### 4.3.4. boot.scr

```bash
root@imx8mm-lpddr4-evk:~# mount /dev/mmcblk2p1 /mnt
root@imx8mm-lpddr4-evk:~# ls -l /mnt
total 36464
-rwxrwx--- 1 root disk 35631616 Apr  5  2011 Image
-rwxrwx--- 1 root disk     1406 Apr  5  2011 boot.scr
-rwxrwx--- 1 root disk    50816 Apr  5  2011 imx8mm-evk-8mic-revE.dtb
-rwxrwx--- 1 root disk    51184 Apr  5  2011 imx8mm-evk-8mic-swpdm.dtb
-rwxrwx--- 1 root disk    48591 Apr  5  2011 imx8mm-evk-ak4497.dtb
-rwxrwx--- 1 root disk    48283 Apr  5  2011 imx8mm-evk-ak5558.dtb
-rwxrwx--- 1 root disk    48355 Apr  5  2011 imx8mm-evk-audio-tdm.dtb
-rwxrwx--- 1 root disk    48169 Apr  5  2011 imx8mm-evk-dpdk.dtb
-rwxrwx--- 1 root disk    48176 Apr  5  2011 imx8mm-evk-ecspi-slave.dtb
-rwxrwx--- 1 root disk     3171 Apr  5  2011 imx8mm-evk-inmate.dtb
-rwxrwx--- 1 root disk    48976 Apr  5  2011 imx8mm-evk-lk.dtb
-rwxrwx--- 1 root disk    48371 Apr  5  2011 imx8mm-evk-pcie-ep.dtb
-rwxrwx--- 1 root disk    48406 Apr  5  2011 imx8mm-evk-qca-wifi.dtb
-rwxrwx--- 1 root disk    48430 Apr  5  2011 imx8mm-evk-revb-qca-wifi.dtb
-rwxrwx--- 1 root disk    48713 Apr  5  2011 imx8mm-evk-rm67191-cmd-ram.dtb
-rwxrwx--- 1 root disk    48713 Apr  5  2011 imx8mm-evk-rm67191.dtb
-rwxrwx--- 1 root disk    48787 Apr  5  2011 imx8mm-evk-rm67199-cmd-ram.dtb
-rwxrwx--- 1 root disk    48787 Apr  5  2011 imx8mm-evk-rm67199.dtb
-rwxrwx--- 1 root disk    48848 Apr  5  2011 imx8mm-evk-root.dtb
-rwxrwx--- 1 root disk    49943 Apr  5  2011 imx8mm-evk-rpmsg-wm8524-lpv.dtb
-rwxrwx--- 1 root disk    49955 Apr  5  2011 imx8mm-evk-rpmsg-wm8524.dtb
-rwxrwx--- 1 root disk    49552 Apr  5  2011 imx8mm-evk-rpmsg.dtb
-rwxrwx--- 1 root disk    48199 Apr  5  2011 imx8mm-evk-usd-wifi.dtb
-rwxrwx--- 1 root disk    48263 Apr  5  2011 imx8mm-evk.dtb
drwxrwx--- 2 root disk     8192 Apr  5  2011 mcore-demos
-rwxrwx--- 1 root disk   599952 Apr  5  2011 tee.bin

root@imx8mm-lpddr4-evk:/mnt# cat /mnt/boot.scr

```

# 5. Update raucb and Testing

```bash
$ cp -avr images-lnk/*.raucb /tmp/

# copy update-bundle-imx8mm-lpddr4-evk.raucb
root@imx8mm-lpddr4-evk:~# scp lanka@192.168.50.72:/tmp/update-bundle-imx8mm-lpddr4-evk.raucb /tmp
```

## 5.1. Step by Step

#### A. A->B

```bash
root@imx8mm-lpddr4-evk:~# rauc status
=== System Info ===
Compatible:  Lanka520
Variant:
Booted from: rootfs.A (A)

=== Bootloader ===
Activated: rootfs.A (A)

=== Slot States ===
  [data.0] (/dev/mmcblk2p4, ext4, inactive)
      mounted: /data

x [rootfs.A] (/dev/mmcblk2p2, ext4, booted)
      bootname: A
      mounted: /
      boot status: good

o [rootfs.B] (/dev/mmcblk2p3, ext4, inactive)
      bootname: B
      mounted: /run/media/rootfs.b-mmcblk2p3
      boot status: good

root@imx8mm-lpddr4-evk:~# fw_printenv | grep BOOT
BOOT_A_LEFT=3
BOOT_B_LEFT=3
BOOT_DEV=mmc 2:1
BOOT_ORDER=A B

root@imx8mm-lpddr4-evk:~# fw_printenv rootfs_part
rootfs_part=/dev/mmcblk2p2

root@imx8mm-lpddr4-evk:/# BOOT_NEXT=`rauc status | awk '/\[rootfs\./ { slot=$2 }/mounted:/ && $2 != "/" { if (slot ~ /\[.*\]/ && $2 ~ /^\//) print $2 }'`
root@imx8mm-lpddr4-evk:/# echo $BOOT_NEXT
/run/media/rootfs.b-mmcblk2p3

root@imx8mm-lpddr4-evk:~# umount $BOOT_NEXT
root@imx8mm-lpddr4-evk:~# rauc install /tmp/update-bundle-imx8mm-lpddr4-evk.raucb
root@imx8mm-lpddr4-evk:~# reboot
```

#### B. B->A

```bash
root@imx8mm-lpddr4-evk:~# rauc status
=== System Info ===
Compatible:  Lanka520
Variant:
Booted from: rootfs.B (B)

=== Bootloader ===
Activated: rootfs.B (B)

=== Slot States ===
  [data.0] (/dev/mmcblk2p4, ext4, inactive)
      mounted: /data

o [rootfs.A] (/dev/mmcblk2p2, ext4, inactive)
      bootname: A
      mounted: /run/media/rootfs.a-mmcblk2p2
      boot status: good

x [rootfs.B] (/dev/mmcblk2p3, ext4, booted)
      bootname: B
      mounted: /
      boot status: good

root@imx8mm-lpddr4-evk:~# fw_printenv | grep BOOT
BOOT_A_LEFT=3
BOOT_B_LEFT=3
BOOT_DEV=mmc 2:1
BOOT_ORDER=B A

root@imx8mm-lpddr4-evk:~# fw_printenv rootfs_part
rootfs_part=/dev/mmcblk2p3

root@imx8mm-lpddr4-evk:/# BOOT_NEXT=`rauc status | awk '/\[rootfs\./ { slot=$2 }/mounted:/ && $2 != "/" { if (slot ~ /\[.*\]/ && $2 ~ /^\//) print $2 }'`
root@imx8mm-lpddr4-evk:/# echo $BOOT_NEXT
/run/media/rootfs.a-mmcblk2p2

root@imx8mm-lpddr4-evk:~# umount $BOOT_NEXT
root@imx8mm-lpddr4-evk:~# rauc install /tmp/update-bundle-imx8mm-lpddr4-evk.raucb
root@imx8mm-lpddr4-evk:~# reboot
```

#### C. Abnormal boot-up occurred 4 times

> 這邊操作不正常開機 4次後再次進到 Linux 查看是否切換到 rootfs.B (/dev/mmcblk2p3)

```bash
root@imx8mm-lpddr4-evk:~# fw_printenv | grep BOOT
BOOT_A_LEFT=0
BOOT_B_LEFT=3
BOOT_DEV=mmc 2:1
BOOT_ORDER=A B

root@imx8mm-lpddr4-evk:~# rauc status
=== System Info ===
Compatible:  Lanka520
Variant:
Booted from: rootfs.B (B)

=== Bootloader ===
Activated: rootfs.B (B)

=== Slot States ===
  [data.0] (/dev/mmcblk2p4, ext4, inactive)
      mounted: /data

o [rootfs.A] (/dev/mmcblk2p2, ext4, inactive)
      bootname: A
      mounted: /run/media/mmcblk2p2
      boot status: bad

x [rootfs.B] (/dev/mmcblk2p3, ext4, booted)
      bootname: B
      mounted: /
      boot status: good
```

## 5.2. ShellScript

#### A. update.sh

> 一個簡易的更新 sh

```bash
$ cd /data
$ vi update.sh
#!/bin/sh

rauc status

echo
fw_printenv | grep BOOT

echo
fw_printenv rootfs_part

echo
BOOT_NEXT=`rauc status | awk '/\[rootfs\./ { slot=$2 }/mounted:/ && $2 != "/" { if (slot ~ /\[.*\]/ && $2 ~ /^\//) print $2 }'`

echo "(BOOT_NEXT=$BOOT_NEXT)"

echo
if [ -f "/tmp/update-bundle-imx8mm-lpddr4-evk.raucb" ]; then
  umount $BOOT_NEXT

  rauc install /tmp/update-bundle-imx8mm-lpddr4-evk.raucb

  echo
  echo "Please wait for 3 seconds for reboot ..."
  sleep 3

  reboot
else
  echo "Couldn't find /tmp/update-bundle-imx8mm-lpddr4-evk.raucb !!!"
fi

echo

$ chmod 777 update.sh
```

# Appendix

# I. Study

## I.1. [Rauc-Yocto-Integration-Tips.md](https://gist.github.com/Scott31393/497e37790446c59b240f7a788e6329de)

## I.2. [Tutorial: Start With RAUC Bundle Encryption Using meta-rauc](https://pengutronix.de/en/blog/2022-03-31-tutorial-rauc-bundle-encryption-using-meta-rauc.html)

# II. Debug

## II.1. CMS_verify:content verify error

> 當設定 RAUC_BUNDLE_FORMAT = "verity"

```bash
$ rauc info \
  --keyring=$PJ_YOCTO_LAYERS_DIR/meta-rauc-plus/recipes-core/rauc/files/ca.cert.pem \
  images-lnk/update-bundle-imx8mm-lpddr4-evk.raucb
rauc-Message: 13:49:46.778: Reading bundle: /yocto/cookerX-scarthgap/images-lnk/update-bundle-imx8mm-lpddr4-evk.raucb
rauc-Message: 13:49:46.780: Verifying bundle...
140293232773760:error:2E09D06D:CMS routines:CMS_verify:content verify error:../crypto/cms/cms_smime.c:393:
signature verification failed: error:2E09A09E:CMS routines:CMS_SignerInfo_verify_content:verification failure
```

> 這邊請改成

```bash
$ bitbake rauc-native -c addto_recipe_sysroot

$ oe-run-native rauc-native rauc info \
  --keyring=$PJ_YOCTO_LAYERS_DIR/meta-rauc-plus/recipes-core/rauc/files/ca.cert.pem \
  images-lnk/update-bundle-imx8mm-lpddr4-evk.raucb
```

## II.2. linux

#### A. CPU

```bash
root@imx8mm-lpddr4-evk:~# cat /proc/cpuinfo
processor       : 0
BogoMIPS        : 16.00
Features        : fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid
CPU implementer : 0x41
CPU architecture: 8
CPU variant     : 0x0
CPU part        : 0xd03
CPU revision    : 4

processor       : 1
BogoMIPS        : 16.00
Features        : fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid
CPU implementer : 0x41
CPU architecture: 8
CPU variant     : 0x0
CPU part        : 0xd03
CPU revision    : 4

processor       : 2
BogoMIPS        : 16.00
Features        : fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid
CPU implementer : 0x41
CPU architecture: 8
CPU variant     : 0x0
CPU part        : 0xd03
CPU revision    : 4

processor       : 3
BogoMIPS        : 16.00
Features        : fp asimd evtstrm aes pmull sha1 sha2 crc32 cpuid
CPU implementer : 0x41
CPU architecture: 8
CPU variant     : 0x0
CPU part        : 0xd03
CPU revision    : 4
```

#### B. RAM

```bash
root@imx8mm-lpddr4-evk:~# free -h
               total        used        free      shared  buff/cache   available
Mem:           1.8Gi       334Mi       1.4Gi       9.0Mi       197Mi       1.5Gi
Swap:             0B          0B          0B

root@imx8mm-lpddr4-evk:~# cat /proc/meminfo
MemTotal:        1925928 kB
MemFree:         1480952 kB
MemAvailable:    1583232 kB
Buffers:           34800 kB
Cached:           151576 kB
SwapCached:            0 kB
Active:            58676 kB
Inactive:         164536 kB
Active(anon):        668 kB
Inactive(anon):    45372 kB
Active(file):      58008 kB
Inactive(file):   119164 kB
Unevictable:           0 kB
Mlocked:               0 kB
SwapTotal:             0 kB
SwapFree:              0 kB
Dirty:                 0 kB
Writeback:             0 kB
AnonPages:         36844 kB
Mapped:            55188 kB
Shmem:              9204 kB
KReclaimable:      15468 kB
Slab:              44352 kB
SReclaimable:      15468 kB
SUnreclaim:        28884 kB
KernelStack:        2400 kB
PageTables:         1736 kB
SecPageTables:         0 kB
NFS_Unstable:          0 kB
Bounce:                0 kB
WritebackTmp:          0 kB
CommitLimit:      962964 kB
Committed_AS:     222536 kB
VmallocTotal:   133141626880 kB
VmallocUsed:        9180 kB
VmallocChunk:          0 kB
Percpu:             1168 kB
HardwareCorrupted:     0 kB
AnonHugePages:         0 kB
ShmemHugePages:        0 kB
ShmemPmdMapped:        0 kB
FileHugePages:         0 kB
FilePmdMapped:         0 kB
CmaTotal:         655360 kB
CmaFree:          519536 kB
HugePages_Total:       0
HugePages_Free:        0
HugePages_Rsvd:        0
HugePages_Surp:        0
Hugepagesize:       2048 kB
Hugetlb:               0 kB
```

#### C. DISK

| RAUC                                           | Size                           |
| ---------------------------------------------- | ------------------------------ |
| mtdblock0                                      | 33,554,432<br/>(~32 MB)        |
| mmcblk2                                        | 31,268,536,320<br/>(~29.12 GB) |
| mmcblk2p1 (boot)<br/>/run/media/boot-mmcblk2p1 | 348,965,888<br/>(~332.8 MB)    |
| mmcblk2p2 (rootfs A)                           | 5,583,457,280<br>(~5.2 GB)     |
| mmcblk2p3 (rootfs B)                           | 5,583,457,280<br/>(~5.2 GB)    |
| mmcblk2p4 (data)<br/>/data                     | 4,294,967,296<br>(~4 GB)       |
| mmcblk2boot0                                   |                                |
| mmcblk2boot1                                   |                                |

```bash
root@imx8mm-lpddr4-evk:~# df -h
Filesystem      Size  Used Avail Use% Mounted on
/dev/root       1.4G 1003M  283M  79% /
devtmpfs        619M  4.0K  619M   1% /dev
tmpfs           941M     0  941M   0% /dev/shm
tmpfs           377M  9.0M  368M   3% /run
tmpfs           941M  8.0K  941M   1% /tmp
tmpfs           941M   12K  941M   1% /var/volatile
/dev/mmcblk2p4  3.8G  1.1M  3.6G   1% /data
/dev/mmcblk2p1  333M   37M  297M  11% /run/media/boot-mmcblk2p1
/dev/mmcblk2p3  1.4G 1003M  283M  79% /run/media/mmcblk2p3
tmpfs           189M  4.0K  189M   1% /run/user/0

root@imx8mm-lpddr4-evk:~# cat /proc/partitions
major minor  #blocks  name

  31        0      32768 mtdblock0
 179        0   30535680 mmcblk2
 179        1     340787 mmcblk2p1
 179        2    5452595 mmcblk2p2
 179        3    5452595 mmcblk2p3
 179        4    4194304 mmcblk2p4
 179       32       4096 mmcblk2boot0
 179       64       4096 mmcblk2boot1

root@imx8mm-lpddr4-evk:~# fdisk -l /dev/mmcblk2
Disk /dev/mmcblk2: 29.12 GiB, 31268536320 bytes, 61071360 sectors
Units: sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disklabel type: dos
Disk identifier: 0x076c4a2a

Device         Boot    Start      End  Sectors   Size Id Type
/dev/mmcblk2p1 *       16384   697957   681574 332.8M  c W95 FAT32 (LBA)
/dev/mmcblk2p2        704512 11609701 10905190   5.2G 83 Linux
/dev/mmcblk2p3      11616256 22521445 10905190   5.2G 83 Linux
/dev/mmcblk2p4      22528000 30916607  8388608     4G 83 Linux

# 61071360 × 512 / 1024 / 1024 / 1024 ≒ 29 GB
root@imx8mm-lpddr4-evk:~# cat /sys/block/mmcblk2/size
61071360

root@imx8mm-lpddr4-evk:~# mount | grep '^/dev'
/dev/mmcblk2p2 on / type ext4 (rw,relatime)
/dev/mmcblk2p4 on /data type ext4 (rw,relatime)
/dev/mmcblk2p1 on /run/media/boot-mmcblk2p1 type vfat (rw,relatime,gid=6,fmask=0007,dmask=0007,allow_utime=0020,codepage=437,iocharset=iso8859-1,shortname=mixed,errors=remount-ro)
/dev/mmcblk2p3 on /run/media/mmcblk2p3 type ext4 (rw,relatime)

root@imx8mm-lpddr4-evk:~# cat /etc/fstab
# stock fstab - you probably want to override this with a machine specific one

/dev/root            /                    auto       defaults              1  1
proc                 /proc                proc       defaults              0  0
devpts               /dev/pts             devpts     mode=0620,ptmxmode=0666,gid=5      0  0
tmpfs                /run                 tmpfs      mode=0755,nodev,nosuid,strictatime 0  0
tmpfs                /var/volatile        tmpfs      defaults              0  0

# uncomment this if your device has a SD/MMC/Transflash slot
#/dev/mmcblk0p1       /media/card          auto       defaults,sync,noauto  0  0

/dev/mmcblk2p4 /data auto defaults 0 2

root@imx8mm-lpddr4-evk:~# mount
/dev/mmcblk2p2 on / type ext4 (rw,relatime)
devtmpfs on /dev type devtmpfs (rw,relatime,size=633268k,nr_inodes=158317,mode=755)
proc on /proc type proc (rw,relatime)
sysfs on /sys type sysfs (rw,nosuid,nodev,noexec,relatime)
securityfs on /sys/kernel/security type securityfs (rw,nosuid,nodev,noexec,relatime)
tmpfs on /dev/shm type tmpfs (rw,nosuid,nodev)
devpts on /dev/pts type devpts (rw,relatime,gid=5,mode=620,ptmxmode=666)
tmpfs on /run type tmpfs (rw,nosuid,nodev,size=385188k,nr_inodes=819200,mode=755)
cgroup2 on /sys/fs/cgroup type cgroup2 (rw,nosuid,nodev,noexec,relatime,nsdelegate,memory_recursiveprot)
bpf on /sys/fs/bpf type bpf (rw,nosuid,nodev,noexec,relatime,mode=700)
hugetlbfs on /dev/hugepages type hugetlbfs (rw,nosuid,nodev,relatime,pagesize=2M)
mqueue on /dev/mqueue type mqueue (rw,nosuid,nodev,noexec,relatime)
debugfs on /sys/kernel/debug type debugfs (rw,nosuid,nodev,noexec,relatime)
tmpfs on /tmp type tmpfs (rw,nosuid,nodev,nr_inodes=1048576)
fusectl on /sys/fs/fuse/connections type fusectl (rw,nosuid,nodev,noexec,relatime)
configfs on /sys/kernel/config type configfs (rw,nosuid,nodev,noexec,relatime)
tmpfs on /var/volatile type tmpfs (rw,relatime)
/dev/mmcblk2p4 on /data type ext4 (rw,relatime)
/dev/mmcblk2p1 on /run/media/boot-mmcblk2p1 type vfat (rw,relatime,gid=6,fmask=0007,dmask=0007,allow_utime=0020,codepage=437,iocharset=iso8859-1,shortname=mixed,errors=remount-ro)
/dev/mmcblk2p3 on /run/media/mmcblk2p3 type ext4 (rw,relatime)
tmpfs on /run/user/0 type tmpfs (rw,nosuid,nodev,relatime,size=192592k,nr_inodes=48148,mode=700)
```

#### D. dmesg

```bash
root@imx8mm-lpddr4-evk:~# strings /proc/device-tree/model
FSL i.MX8MM EVK board

root@imx8mm-lpddr4-evk:~# dmesg
[    0.000000] Booting Linux on physical CPU 0x0000000000 [0x410fd034]
[    0.000000] Linux version 6.6.52-lts-next-ge0f9e2afd4cf (oe-user@oe-host) (aarch64-poky-linux-gcc (GCC) 13.3.0, GNU ld (GNU Binutils) 2.42.0.20240723) #1 SMP PREEMPT Tue Nov 19 23:01:49 UTC 2024
[    0.000000] KASLR enabled
[    0.000000] Machine model: FSL i.MX8MM EVK board
[    0.000000] efi: UEFI not found.
[    0.000000] Reserved memory: created CMA memory pool at 0x0000000096000000, size 640 MiB
[    0.000000] OF: reserved mem: initialized node linux,cma, compatible id shared-dma-pool
[    0.000000] OF: reserved mem: 0x0000000096000000..0x00000000bdffffff (655360 KiB) map reusable linux,cma
[    0.000000] OF: reserved mem: 0x00000000be000000..0x00000000bfdfffff (30720 KiB) nomap non-reusable optee_core@be000000
[    0.000000] OF: reserved mem: 0x00000000bfe00000..0x00000000bfffffff (2048 KiB) nomap non-reusable optee_shm@bfe00000
[    0.000000] NUMA: No NUMA configuration found
[    0.000000] NUMA: Faking a node at [mem 0x0000000040000000-0x00000000bdffffff]
[    0.000000] NUMA: NODE_DATA [mem 0x95bce6c0-0x95bd0fff]
[    0.000000] Zone ranges:
[    0.000000]   DMA      [mem 0x0000000040000000-0x00000000bdffffff]
[    0.000000]   DMA32    empty
[    0.000000]   Normal   empty
[    0.000000] Movable zone start for each node
[    0.000000] Early memory node ranges
[    0.000000]   node   0: [mem 0x0000000040000000-0x00000000bdffffff]
[    0.000000] Initmem setup node 0 [mem 0x0000000040000000-0x00000000bdffffff]
[    0.000000] On node 0, zone DMA: 8192 pages in unavailable ranges
[    0.000000] psci: probing for conduit method from DT.
[    0.000000] psci: PSCIv1.1 detected in firmware.
[    0.000000] psci: Using standard PSCI v0.2 function IDs
[    0.000000] psci: Trusted OS migration not required
[    0.000000] psci: SMC Calling Convention v1.4
[    0.000000] percpu: Embedded 22 pages/cpu s50920 r8192 d31000 u90112
[    0.000000] pcpu-alloc: s50920 r8192 d31000 u90112 alloc=22*4096
[    0.000000] pcpu-alloc: [0] 0 [0] 1 [0] 2 [0] 3
[    0.000000] Detected VIPT I-cache on CPU0
[    0.000000] CPU features: detected: GIC system register CPU interface
[    0.000000] CPU features: kernel page table isolation forced ON by KASLR
[    0.000000] CPU features: detected: Kernel page table isolation (KPTI)
[    0.000000] CPU features: detected: ARM erratum 845719
[    0.000000] alternatives: applying boot alternatives
[    0.000000] Kernel command line: console=ttymxc1,115200 root=/dev/mmcblk2p2 rootwait rw rauc.slot=A
[    0.000000] Dentry cache hash table entries: 262144 (order: 9, 2097152 bytes, linear)
[    0.000000] Inode-cache hash table entries: 131072 (order: 8, 1048576 bytes, linear)
[    0.000000] Fallback order for Node 0: 0
[    0.000000] Built 1 zonelists, mobility grouping on.  Total pages: 508032
[    0.000000] Policy zone: DMA
[    0.000000] mem auto-init: stack:all(zero), heap alloc:off, heap free:off
[    0.000000] software IO TLB: area num 4.
[    0.000000] software IO TLB: mapped [mem 0x000000008f800000-0x0000000093800000] (64MB)
[    0.000000] Memory: 1266536K/2064384K available (21248K kernel code, 1646K rwdata, 7844K rodata, 4032K init, 643K bss, 142488K reserved, 655360K cma-reserved)
[    0.000000] SLUB: HWalign=64, Order=0-3, MinObjects=0, CPUs=4, Nodes=1
[    0.000000] rcu: Preemptible hierarchical RCU implementation.
[    0.000000] rcu:     RCU event tracing is enabled.
[    0.000000] rcu:     RCU restricting CPUs from NR_CPUS=256 to nr_cpu_ids=4.
[    0.000000]  Trampoline variant of Tasks RCU enabled.
[    0.000000]  Tracing variant of Tasks RCU enabled.
[    0.000000] rcu: RCU calculated value of scheduler-enlistment delay is 25 jiffies.
[    0.000000] rcu: Adjusting geometry for rcu_fanout_leaf=16, nr_cpu_ids=4
[    0.000000] NR_IRQS: 64, nr_irqs: 64, preallocated irqs: 0
[    0.000000] GICv3: GIC: Using split EOI/Deactivate mode
[    0.000000] GICv3: 128 SPIs implemented
[    0.000000] GICv3: 0 Extended SPIs implemented
[    0.000000] Root IRQ handler: gic_handle_irq
[    0.000000] GICv3: GICv3 features: 16 PPIs
[    0.000000] GICv3: CPU0: found redistributor 0 region 0:0x0000000038880000
[    0.000000] ITS: No ITS available, not enabling LPIs
[    0.000000] rcu: srcu_init: Setting srcu_struct sizes based on contention.
[    0.000000] arch_timer: cp15 timer(s) running at 8.00MHz (phys).
[    0.000000] clocksource: arch_sys_counter: mask: 0xffffffffffffff max_cycles: 0x1d854df40, max_idle_ns: 440795202120 ns
[    0.000001] sched_clock: 56 bits at 8MHz, resolution 125ns, wraps every 2199023255500ns
[    0.000440] Console: colour dummy device 80x25
[    0.000506] Calibrating delay loop (skipped), value calculated using timer frequency.. 16.00 BogoMIPS (lpj=32000)
[    0.000517] pid_max: default: 32768 minimum: 301
[    0.000582] LSM: initializing lsm=capability,integrity
[    0.000678] Mount-cache hash table entries: 4096 (order: 3, 32768 bytes, linear)
[    0.000690] Mountpoint-cache hash table entries: 4096 (order: 3, 32768 bytes, linear)
[    0.002208] RCU Tasks: Setting shift to 2 and lim to 1 rcu_task_cb_adjust=1.
[    0.002288] RCU Tasks Trace: Setting shift to 2 and lim to 1 rcu_task_cb_adjust=1.
[    0.002460] rcu: Hierarchical SRCU implementation.
[    0.002463] rcu:     Max phase no-delay instances is 1000.
[    0.003650] EFI services will not be available.
[    0.003850] smp: Bringing up secondary CPUs ...
[    0.004381] Detected VIPT I-cache on CPU1
[    0.004447] GICv3: CPU1: found redistributor 1 region 0:0x00000000388a0000
[    0.004491] CPU1: Booted secondary processor 0x0000000001 [0x410fd034]
[    0.005030] Detected VIPT I-cache on CPU2
[    0.005074] GICv3: CPU2: found redistributor 2 region 0:0x00000000388c0000
[    0.005098] CPU2: Booted secondary processor 0x0000000002 [0x410fd034]
[    0.005571] Detected VIPT I-cache on CPU3
[    0.005611] GICv3: CPU3: found redistributor 3 region 0:0x00000000388e0000
[    0.005633] CPU3: Booted secondary processor 0x0000000003 [0x410fd034]
[    0.005697] smp: Brought up 1 node, 4 CPUs
[    0.005703] SMP: Total of 4 processors activated.
[    0.005707] CPU features: detected: 32-bit EL0 Support
[    0.005710] CPU features: detected: 32-bit EL1 Support
[    0.005714] CPU features: detected: CRC32 instructions
[    0.005779] CPU: All CPU(s) started at EL2
[    0.005800] alternatives: applying system-wide alternatives
[    0.007663] devtmpfs: initialized
[    0.014999] clocksource: jiffies: mask: 0xffffffff max_cycles: 0xffffffff, max_idle_ns: 7645041785100000 ns
[    0.015023] futex hash table entries: 1024 (order: 4, 65536 bytes, linear)
[    0.033671] pinctrl core: initialized pinctrl subsystem
[    0.035741] DMI not present or invalid.
[    0.036391] NET: Registered PF_NETLINK/PF_ROUTE protocol family
[    0.037339] DMA: preallocated 256 KiB GFP_KERNEL pool for atomic allocations
[    0.037440] DMA: preallocated 256 KiB GFP_KERNEL|GFP_DMA pool for atomic allocations
[    0.037556] DMA: preallocated 256 KiB GFP_KERNEL|GFP_DMA32 pool for atomic allocations
[    0.037618] audit: initializing netlink subsys (disabled)
[    0.037780] audit: type=2000 audit(0.036:1): state=initialized audit_enabled=0 res=1
[    0.038300] thermal_sys: Registered thermal governor 'step_wise'
[    0.038305] thermal_sys: Registered thermal governor 'power_allocator'
[    0.038340] cpuidle: using governor menu
[    0.038552] hw-breakpoint: found 6 breakpoint and 4 watchpoint registers.
[    0.038634] ASID allocator initialised with 32768 entries
[    0.039566] Serial: AMBA PL011 UART driver
[    0.039635] imx mu driver is registered.
[    0.039656] imx rpmsg driver is registered.
[    0.046240] platform soc@0: Fixed dependency cycle(s) with /soc@0/bus@30000000/efuse@30350000/unique-id@4
[    0.049867] imx8mm-pinctrl 30330000.pinctrl: initialized IMX pinctrl driver
[    0.050542] platform 30350000.efuse: Fixed dependency cycle(s) with /soc@0/bus@30000000/clock-controller@30380000
[    0.051711] platform 30350000.efuse: Fixed dependency cycle(s) with /soc@0/bus@30000000/clock-controller@30380000
[    0.058319] platform 32e00000.lcdif: Fixed dependency cycle(s) with /soc@0/bus@32c00000/mipi_dsi@32e10000
[    0.058543] platform 32e00000.lcdif: Fixed dependency cycle(s) with /soc@0/bus@32c00000/mipi_dsi@32e10000
[    0.058657] platform 32e10000.mipi_dsi: Fixed dependency cycle(s) with /soc@0/bus@30800000/i2c@30a30000/adv7535@3d
[    0.058688] platform 32e10000.mipi_dsi: Fixed dependency cycle(s) with /soc@0/bus@32c00000/lcdif@32e00000
[    0.058954] platform 32e20000.csi1_bridge: Fixed dependency cycle(s) with /soc@0/bus@32c00000/mipi_csi@32e30000
[    0.059187] platform 32e20000.csi1_bridge: Fixed dependency cycle(s) with /soc@0/bus@32c00000/mipi_csi@32e30000
[    0.059298] platform 32e30000.mipi_csi: Fixed dependency cycle(s) with /soc@0/bus@32c00000/csi1_bridge@32e20000
[    0.059362] platform 32e30000.mipi_csi: Fixed dependency cycle(s) with /soc@0/bus@30800000/i2c@30a40000/ov5640_mipi@3c
[    0.060029] platform 32e40000.usb: Fixed dependency cycle(s) with /soc@0/bus@30800000/i2c@30a30000/tcpc@50
[    0.065748] Modules: 2G module region forced by RANDOMIZE_MODULE_REGION_FULL
[    0.065775] Modules: 0 pages in range for non-PLT usage
[    0.065778] Modules: 515376 pages in range for PLT usage
[    0.066564] HugeTLB: registered 1.00 GiB page size, pre-allocated 0 pages
[    0.066571] HugeTLB: 0 KiB vmemmap can be freed for a 1.00 GiB page
[    0.066576] HugeTLB: registered 32.0 MiB page size, pre-allocated 0 pages
[    0.066579] HugeTLB: 0 KiB vmemmap can be freed for a 32.0 MiB page
[    0.066583] HugeTLB: registered 2.00 MiB page size, pre-allocated 0 pages
[    0.066589] HugeTLB: 0 KiB vmemmap can be freed for a 2.00 MiB page
[    0.066593] HugeTLB: registered 64.0 KiB page size, pre-allocated 0 pages
[    0.066598] HugeTLB: 0 KiB vmemmap can be freed for a 64.0 KiB page
[    0.068529] ACPI: Interpreter disabled.
[    0.069549] iommu: Default domain type: Translated
[    0.069558] iommu: DMA domain TLB invalidation policy: strict mode
[    0.069855] SCSI subsystem initialized
[    0.069967] libata version 3.00 loaded.
[    0.070168] usbcore: registered new interface driver usbfs
[    0.070199] usbcore: registered new interface driver hub
[    0.070235] usbcore: registered new device driver usb
[    0.071357] mc: Linux media interface: v0.10
[    0.071398] videodev: Linux video capture interface: v2.00
[    0.071458] pps_core: LinuxPPS API ver. 1 registered
[    0.071461] pps_core: Software ver. 5.3.6 - Copyright 2005-2007 Rodolfo Giometti <giometti@linux.it>
[    0.071473] PTP clock support registered
[    0.071688] EDAC MC: Ver: 3.0.0
[    0.072121] scmi_core: SCMI protocol bus registered
[    0.072435] FPGA manager framework
[    0.072512] Advanced Linux Sound Architecture Driver Initialized.
[    0.073217] Bluetooth: Core ver 2.22
[    0.073240] NET: Registered PF_BLUETOOTH protocol family
[    0.073244] Bluetooth: HCI device and connection manager initialized
[    0.073251] Bluetooth: HCI socket layer initialized
[    0.073256] Bluetooth: L2CAP socket layer initialized
[    0.073267] Bluetooth: SCO socket layer initialized
[    0.073640] vgaarb: loaded
[    0.074175] clocksource: Switched to clocksource arch_sys_counter
[    0.074396] VFS: Disk quotas dquot_6.6.0
[    0.074426] VFS: Dquot-cache hash table entries: 512 (order 0, 4096 bytes)
[    0.074588] pnp: PnP ACPI: disabled
[    0.081501] NET: Registered PF_INET protocol family
[    0.081652] IP idents hash table entries: 32768 (order: 6, 262144 bytes, linear)
[    0.083203] tcp_listen_portaddr_hash hash table entries: 1024 (order: 2, 16384 bytes, linear)
[    0.083230] Table-perturb hash table entries: 65536 (order: 6, 262144 bytes, linear)
[    0.083244] TCP established hash table entries: 16384 (order: 5, 131072 bytes, linear)
[    0.083391] TCP bind hash table entries: 16384 (order: 7, 524288 bytes, linear)
[    0.083852] TCP: Hash tables configured (established 16384 bind 16384)
[    0.083949] UDP hash table entries: 1024 (order: 3, 32768 bytes, linear)
[    0.084003] UDP-Lite hash table entries: 1024 (order: 3, 32768 bytes, linear)
[    0.084159] NET: Registered PF_UNIX/PF_LOCAL protocol family
[    0.084573] RPC: Registered named UNIX socket transport module.
[    0.084578] RPC: Registered udp transport module.
[    0.084581] RPC: Registered tcp transport module.
[    0.084583] RPC: Registered tcp-with-tls transport module.
[    0.084586] RPC: Registered tcp NFSv4.1 backchannel transport module.
[    0.085630] PCI: CLS 0 bytes, default 64
[    0.086015] kvm [1]: IPA Size Limit: 40 bits
[    0.088067] kvm [1]: GICv3: no GICV resource entry
[    0.088073] kvm [1]: disabling GICv2 emulation
[    0.088093] kvm [1]: GIC system register CPU interface enabled
[    0.088117] kvm [1]: vgic interrupt IRQ9
[    0.088139] kvm [1]: Hyp mode initialized successfully
[    0.089315] Initialise system trusted keyrings
[    0.089507] workingset: timestamp_bits=42 max_order=19 bucket_order=0
[    0.089785] squashfs: version 4.0 (2009/01/31) Phillip Lougher
[    0.089999] NFS: Registering the id_resolver key type
[    0.090020] Key type id_resolver registered
[    0.090024] Key type id_legacy registered
[    0.090041] nfs4filelayout_init: NFSv4 File Layout Driver Registering...
[    0.090051] nfs4flexfilelayout_init: NFSv4 Flexfile Layout Driver Registering...
[    0.090068] jffs2: version 2.2. (NAND) \xc2\xa9 2001-2006 Red Hat, Inc.
[    0.090288] 9p: Installing v9fs 9p2000 file system support
[    0.123824] NET: Registered PF_ALG protocol family
[    0.123835] Key type asymmetric registered
[    0.123840] Asymmetric key parser 'x509' registered
[    0.123878] Block layer SCSI generic (bsg) driver version 0.4 loaded (major 243)
[    0.123884] io scheduler mq-deadline registered
[    0.123888] io scheduler kyber registered
[    0.123918] io scheduler bfq registered
[    0.131087] EINJ: ACPI disabled.
[    0.142043] imx-sdma 302c0000.dma-controller: Direct firmware load for imx/sdma/sdma-imx7d.bin failed with error -2
[    0.142059] imx-sdma 302c0000.dma-controller: Falling back to sysfs fallback for: imx/sdma/sdma-imx7d.bin
[    0.148012] mxs-dma 33000000.dma-controller: initialized
[    0.149293] SoC: i.MX8MM revision 1.0
[    0.149727] Bus freq driver module loaded
[    0.162720] Serial: 8250/16550 driver, 4 ports, IRQ sharing enabled
[    0.165780] 30860000.serial: ttymxc0 at MMIO 0x30860000 (irq = 18, base_baud = 5000000) is a IMX
[    0.165920] serial serial0: tty port ttymxc0 registered
[    0.166470] 30880000.serial: ttymxc2 at MMIO 0x30880000 (irq = 19, base_baud = 5000000) is a IMX
[    0.167172] 30890000.serial: ttymxc1 at MMIO 0x30890000 (irq = 20, base_baud = 1500000) is a IMX
[    0.167210] printk: console [ttymxc1] enabled
[    1.470884] imx-drm display-subsystem: bound imx-lcdif-crtc.0 (ops lcdif_crtc_ops)
[    1.478634] imx_sec_dsim_drv 32e10000.mipi_dsi: version number is 0x1060200
[    1.485677] [drm:drm_bridge_attach] *ERROR* failed to attach bridge /soc@0/bus@32c00000/mipi_dsi@32e10000 to encoder DSI-34: -517
[    1.497375] imx_sec_dsim_drv 32e10000.mipi_dsi: Failed to attach bridge: 32e10000.mipi_dsi
[    1.505649] imx_sec_dsim_drv 32e10000.mipi_dsi: failed to bind sec dsim bridge: -517
[    1.519962] loop: module loaded
[    1.524824] megasas: 07.725.01.00-rc1
[    1.533796] spi-nor spi0.0: n25q256ax1 (32768 Kbytes)
[    1.543217] tun: Universal TUN/TAP device driver, 1.6
[    1.549232] thunder_xcv, ver 1.0
[    1.552504] thunder_bgx, ver 1.0
[    1.555773] nicpf, ver 1.0
[    1.560769] hns3: Hisilicon Ethernet Network Driver for Hip08 Family - version
[    1.568004] hns3: Copyright (c) 2017 Huawei Corporation.
[    1.573358] hclge is initializing
[    1.576718] e1000: Intel(R) PRO/1000 Network Driver
[    1.581605] e1000: Copyright (c) 1999-2006 Intel Corporation.
[    1.587386] e1000e: Intel(R) PRO/1000 Network Driver
[    1.592356] e1000e: Copyright(c) 1999 - 2015 Intel Corporation.
[    1.598304] igb: Intel(R) Gigabit Ethernet Network Driver
[    1.603751] igb: Copyright (c) 2007-2014 Intel Corporation.
[    1.609358] igbvf: Intel(R) Gigabit Virtual Function Network Driver
[    1.615633] igbvf: Copyright (c) 2009 - 2012 Intel Corporation.
[    1.621738] sky2: driver version 1.30
[    1.626001] usbcore: registered new device driver r8152-cfgselector
[    1.632307] usbcore: registered new interface driver r8152
[    1.638234] VFIO - User Level meta-driver version: 0.3
[    1.646287] usbcore: registered new interface driver uas
[    1.651643] usbcore: registered new interface driver usb-storage
[    1.657727] usbcore: registered new interface driver usbserial_generic
[    1.664282] usbserial: USB Serial support registered for generic
[    1.670317] usbcore: registered new interface driver ftdi_sio
[    1.676090] usbserial: USB Serial support registered for FTDI USB Serial Device
[    1.683430] usbcore: registered new interface driver usb_serial_simple
[    1.689983] usbserial: USB Serial support registered for carelink
[    1.696105] usbserial: USB Serial support registered for flashloader
[    1.702505] usbserial: USB Serial support registered for funsoft
[    1.708541] usbserial: USB Serial support registered for google
[    1.714486] usbserial: USB Serial support registered for hp4x
[    1.720259] usbserial: USB Serial support registered for kaufmann
[    1.726382] usbserial: USB Serial support registered for libtransistor
[    1.732939] usbserial: USB Serial support registered for moto_modem
[    1.739231] usbserial: USB Serial support registered for motorola_tetra
[    1.745874] usbserial: USB Serial support registered for nokia
[    1.751737] usbserial: USB Serial support registered for novatel_gps
[    1.758121] usbserial: USB Serial support registered for siemens_mpi
[    1.764504] usbserial: USB Serial support registered for suunto
[    1.770456] usbserial: USB Serial support registered for vivopay
[    1.776492] usbserial: USB Serial support registered for zio
[    1.782193] usbcore: registered new interface driver usb_ehset_test
[    1.791774] input: 30370000.snvs:snvs-powerkey as /devices/platform/soc@0/30000000.bus/30370000.snvs/30370000.snvs:snvs-powerkey/input/input0
[    1.806558] snvs_rtc 30370000.snvs:snvs-rtc-lp: registered as rtc0
[    1.812787] snvs_rtc 30370000.snvs:snvs-rtc-lp: setting system clock to 2025-08-04T00:01:05 UTC (1754265665)
[    1.822773] i2c_dev: i2c /dev entries driver
[    1.828997] mx6s-csi 32e20000.csi1_bridge: initialising
[    1.835199] mxc_mipi-csi 32e30000.mipi_csi: supply mipi-phy not found, using dummy regulator
[    1.843963] mxc_mipi-csi 32e30000.mipi_csi: mipi csi v4l2 device registered
[    1.850942] CSI: Registered sensor subdevice: mxc_mipi-csi.0
[    1.856628] mxc_mipi-csi 32e30000.mipi_csi: lanes: 2, hs_settle: 13, clk_settle: 2, wclk: 1, freq: 333000000
[    1.870777] device-mapper: ioctl: 4.48.0-ioctl (2023-03-01) initialised: dm-devel@redhat.com
[    1.879342] Bluetooth: HCI UART driver ver 2.3
[    1.883806] Bluetooth: HCI UART protocol H4 registered
[    1.888963] Bluetooth: HCI UART protocol BCSP registered
[    1.894318] Bluetooth: HCI UART protocol LL registered
[    1.899466] Bluetooth: HCI UART protocol ATH3K registered
[    1.904893] Bluetooth: HCI UART protocol Three-wire (H5) registered
[    1.911295] Bluetooth: HCI UART protocol Broadcom registered
[    1.916995] Bluetooth: HCI UART protocol QCA registered
[    1.923846] sdhci: Secure Digital Host Controller Interface driver
[    1.930057] sdhci: Copyright(c) Pierre Ossman
[    1.935044] Synopsys Designware Multimedia Card Interface Driver
[    1.941753] sdhci-pltfm: SDHCI platform and OF driver helper
[    1.949888] ledtrig-cpu: registered to indicate activity on CPUs
[    1.957448] SMCCC: SOC_ID: ARCH_SOC_ID not implemented, skipping ....
[    1.964363] usbcore: registered new interface driver usbhid
[    1.969949] usbhid: USB HID core driver
[    1.979731] hw perfevents: enabled with armv8_cortex_a53 PMU driver, 7 counters available
[    1.982203] mmc2: SDHCI controller on 30b60000.mmc [30b60000.mmc] using ADMA
[    1.997884]  cs_system_cfg: CoreSight Configuration manager initialised
[    2.005630] platform soc@0: Fixed dependency cycle(s) with /soc@0/bus@30000000/efuse@30350000
[    2.015330] optee: probing for conduit method.
[    2.019815] optee: revision 4.4 (60beb308810f9561)
[    2.020135] optee: dynamic shared memory is enabled
[    2.030216] optee: initialized driver
[    2.036260] hantrodec 0 : module inserted. Major = 509
[    2.041982] hantrodec 1 : module inserted. Major = 509
[    2.048085] hx280enc: module inserted. Major <508>
[    2.057735] NET: Registered PF_LLC protocol family
[    2.062367] mmc2: new HS400 Enhanced strobe MMC card at address 0001
[    2.062651] u32 classifier
[    2.069823] mmcblk2: mmc2:0001 DG4032 29.1 GiB
[    2.071661]     input device check on
[    2.077386]  mmcblk2: p1 p2 p3 p4
[    2.079753]     Actions configured
[    2.083882] mmcblk2boot0: mmc2:0001 DG4032 4.00 MiB
[    2.087481] NET: Registered PF_INET6 protocol family
[    2.092636] mmcblk2boot1: mmc2:0001 DG4032 4.00 MiB
[    2.099003] Segment Routing with IPv6
[    2.103054] mmcblk2rpmb: mmc2:0001 DG4032 4.00 MiB, chardev (234:0)
[    2.104963] In-situ OAM (IOAM) with IPv6
[    2.115170] NET: Registered PF_PACKET protocol family
[    2.120268] bridge: filtering via arp/ip/ip6tables is no longer available by default. Update your scripts to load br_netfilter if you need this.
[    2.134340] Bluetooth: RFCOMM TTY layer initialized
[    2.139239] Bluetooth: RFCOMM socket layer initialized
[    2.144399] Bluetooth: RFCOMM ver 1.11
[    2.148162] Bluetooth: BNEP (Ethernet Emulation) ver 1.3
[    2.153480] Bluetooth: BNEP filters: protocol multicast
[    2.158717] Bluetooth: BNEP socket layer initialized
[    2.163691] Bluetooth: HIDP (Human Interface Emulation) ver 1.2
[    2.169619] Bluetooth: HIDP socket layer initialized
[    2.175685] 8021q: 802.1Q VLAN Support v1.8
[    2.179905] lib80211: common routines for IEEE802.11 drivers
[    2.185575] lib80211_crypt: registered algorithm 'NULL'
[    2.185582] lib80211_crypt: registered algorithm 'WEP'
[    2.185589] lib80211_crypt: registered algorithm 'CCMP'
[    2.185593] lib80211_crypt: registered algorithm 'TKIP'
[    2.185626] 9pnet: Installing 9P2000 support
[    2.190063] Key type dns_resolver registered
[    2.195109] NET: Registered PF_VSOCK protocol family
[    2.223004] registered taskstats version 1
[    2.227270] Loading compiled-in X.509 certificates
[    2.254597] gpio gpiochip0: Static allocation of GPIO base is deprecated, use dynamic allocation.
[    2.265047] gpio gpiochip1: Static allocation of GPIO base is deprecated, use dynamic allocation.
[    2.275573] gpio gpiochip2: Static allocation of GPIO base is deprecated, use dynamic allocation.
[    2.286147] gpio gpiochip3: Static allocation of GPIO base is deprecated, use dynamic allocation.
[    2.296373] gpio gpiochip4: Static allocation of GPIO base is deprecated, use dynamic allocation.
[    2.309717] usb_phy_generic usbphynop1: dummy supplies not allowed for exclusive requests
[    2.318258] usb_phy_generic usbphynop2: dummy supplies not allowed for exclusive requests
[    2.327962] i2c i2c-0: IMX I2C adapter registered
[    2.333933] adv7511 1-003d: supply avdd not found, using dummy regulator
[    2.340789] adv7511 1-003d: supply dvdd not found, using dummy regulator
[    2.347537] adv7511 1-003d: supply pvdd not found, using dummy regulator
[    2.354289] adv7511 1-003d: supply a2vdd not found, using dummy regulator
[    2.361143] adv7511 1-003d: supply v3p3 not found, using dummy regulator
[    2.367889] adv7511 1-003d: supply v1p2 not found, using dummy regulator
[    2.375413] adv7511 1-003d: Probe failed. Remote port 'mipi_dsi@32e10000' disabled
[    2.383235] platform 32e40000.usb: Fixed dependency cycle(s) with /soc@0/bus@30800000/i2c@30a30000/tcpc@50
[    2.393032] i2c 1-0050: Fixed dependency cycle(s) with /soc@0/bus@32c00000/usb@32e40000
[    2.403054] i2c i2c-1: IMX I2C adapter registered
[    2.410990] ov5640_mipi 2-003c: No sensor reset pin available
[    2.412551] nxp-pca9450 0-0025: pca9450a probed.
[    2.416800] ov5640_mipi 2-003c: supply DOVDD not found, using dummy regulator
[    2.428638] ov5640_mipi 2-003c: supply DVDD not found, using dummy regulator
[    2.435736] ov5640_mipi 2-003c: supply AVDD not found, using dummy regulator
[    2.459410] ov5640_mipi 2-003c: Read reg error: reg=300a
[    2.464742] ov5640_mipi 2-003c: Camera is not found
[    2.469968] i2c i2c-2: IMX I2C adapter registered
[    2.477734] imx6q-pcie 33800000.pcie: host bridge /soc@0/pcie@33800000 ranges:
[    2.480484] imx-drm display-subsystem: bound imx-lcdif-crtc.0 (ops lcdif_crtc_ops)
[    2.485033] imx6q-pcie 33800000.pcie:       IO 0x001ff80000..0x001ff8ffff -> 0x0000000000
[    2.492721] imx_sec_dsim_drv 32e10000.mipi_dsi: version number is 0x1060200
[    2.500779] imx6q-pcie 33800000.pcie:      MEM 0x0018000000..0x001fefffff -> 0x0018000000
[    2.507763] [drm:drm_bridge_attach] *ERROR* failed to attach bridge /soc@0/bus@32c00000/mipi_dsi@32e10000 to encoder DSI-34: -19
[    2.527507] imx_sec_dsim_drv 32e10000.mipi_dsi: Failed to attach bridge: 32e10000.mipi_dsi
[    2.535784] imx_sec_dsim_drv 32e10000.mipi_dsi: failed to bind sec dsim bridge: -19
[    2.543450] imx-drm display-subsystem: bound 32e10000.mipi_dsi (ops imx_sec_dsim_ops)
[    2.551908] [drm] Initialized imx-drm 1.0.0 20120507 for display-subsystem on minor 0
[    2.559796] imx-drm display-subsystem: [drm] Cannot find any crtc or sizes
[    2.570743] pps pps0: new PPS source ptp0
[    2.682599] mdio_bus 30be0000.ethernet-1:00: Fixed dependency cycle(s) with /soc@0/bus@30800000/ethernet@30be0000/mdio/ethernet-phy@0/vddio-regulator
[    2.730680] imx6q-pcie 33800000.pcie: iATU: unroll T, 4 ob, 4 ib, align 64K, limit 4G
[    2.782593] vddio: Bringing 1500000uV into 1800000-1800000uV
[    2.790256] fec 30be0000.ethernet eth0: registered PHC device 0
[    2.802904] imx-cpufreq-dt imx-cpufreq-dt: cpu speed grade 3 mkt segment 0 supported-hw 0x8 0x1
[    2.817182] galcore: clk_get vg clock failed, disable vg!
[    2.817390] sdhci-esdhc-imx 30b50000.mmc: Got CD GPIO
[    2.823314] Galcore version 6.4.11.p2.745085
[    2.847071] mmc0: SDHCI controller on 30b40000.mmc [30b40000.mmc] using ADMA
[    2.858739] mmc1: SDHCI controller on 30b50000.mmc [30b50000.mmc] using ADMA
[    2.876587] [drm] Initialized vivante 1.0.0 20170808 for 38000000.gpu on minor 1
[    2.889477] OF: graph: no port node found in /soc@0/bus@30800000/i2c@30a30000/tcpc@50/connector
[    2.898268] OF: graph: no port node found in /soc@0/bus@30800000/i2c@30a30000/tcpc@50/connector
[    2.907001] OF: graph: no port node found in /soc@0/bus@30800000/i2c@30a30000/tcpc@50/connector
[    2.924133] pca953x 2-0020: using no AI
[    2.937447] cfg80211: Loading compiled-in X.509 certificates for regulatory database
[    2.946890] Loaded X.509 cert 'sforshee: 00b28ddf47aef9cea7'
[    2.953156] Loaded X.509 cert 'wens: 61c038651aabdcf94bd0ac7ff06c7248db18c600'
[    2.955124] mmc0: new ultra high speed SDR104 SDIO card at address 0001
[    2.960450] clk: Disabling unused clocks
[    2.970983] platform regulatory.0: Direct firmware load for regulatory.db failed with error -2
[    2.976471] ALSA device list:
[    2.979615] platform regulatory.0: Falling back to sysfs fallback for: regulatory.db
[    2.982589]   No soundcards found.
[    3.744854] imx6q-pcie 33800000.pcie: Phy link never came up
[    4.035551] ddrc freq set to low bus mode
[    4.756653] imx6q-pcie 33800000.pcie: Phy link never came up
[    4.765650] imx6q-pcie 33800000.pcie: PCI host bridge to bus 0000:00
[    4.772665] pci_bus 0000:00: root bus resource [bus 00-ff]
[    4.778250] pci_bus 0000:00: root bus resource [io  0x0000-0xffff]
[    4.784474] pci_bus 0000:00: root bus resource [mem 0x18000000-0x1fefffff]
[    4.791586] pci 0000:00:00.0: [16c3:abcd] type 01 class 0x060400
[    4.797732] pci 0000:00:00.0: reg 0x10: [mem 0x00000000-0x000fffff]
[    4.804343] pci 0000:00:00.0: reg 0x38: [mem 0x00000000-0x0000ffff pref]
[    4.811292] pci 0000:00:00.0: supports D1
[    4.815324] pci 0000:00:00.0: PME# supported from D0 D1 D3hot D3cold
[    4.825952] pci 0000:00:00.0: BAR 0: assigned [mem 0x18000000-0x180fffff]
[    4.832889] pci 0000:00:00.0: BAR 6: assigned [mem 0x18100000-0x1810ffff pref]
[    4.840150] pci 0000:00:00.0: PCI bridge to [bus 01-ff]
[    4.848639] pcieport 0000:00:00.0: PME: Signaling with IRQ 221
[    4.861731] ddrc freq set to high bus mode
[    4.876876] EXT4-fs (mmcblk2p2): mounted filesystem 98ed423a-dee0-4be7-b332-3da98b714ea7 r/w with ordered data mode. Quota mode: none.
[    4.889063] VFS: Mounted root (ext4 filesystem) on device 179:2.
[    4.895491] devtmpfs: mounted
[    4.899352] Freeing unused kernel memory: 4032K
[    4.903961] Run /sbin/init as init process
[    4.908063]   with arguments:
[    4.908065]     /sbin/init
[    4.908067]   with environment:
[    4.908069]     HOME=/
[    4.908071]     TERM=linux
[    5.027784] systemd[1]: systemd 255.4^ running in system mode (+PAM -AUDIT -SELINUX -APPARMOR +IMA -SMACK +SECCOMP -GCRYPT -GNUTLS -OPENSSL +ACL +BLKID -CURL -ELFUTILS -FIDO2 -IDN2 -IDN -IPTC +KMOD -LIBCRYPTSETUP +LIBFDISK -PCRE2 -PWQUALITY -P11KIT -QRENCODE -TPM2 -BZIP2 -LZ4 -XZ -ZLIB +ZSTD -BPF_FRAMEWORK -XKBCOMMON +UTMP +SYSVINIT default-hierarchy=unified)
[    5.059675] systemd[1]: Detected architecture arm64.
[    5.082765] systemd[1]: Hostname set to <imx8mm-lpddr4-evk>.
[    5.166385] systemd-sysv-generator[130]: SysV service '/etc/init.d/rc.local' lacks a native systemd unit file. ~ Automatically generating a unit file for compatibility. Please update package to include a native systemd unit file, in order to make it safe, robust and future-proof. ! This compatibility logic is deprecated, expect removal soon. !
[    5.476871] systemd[1]: Queued start job for default target Graphical Interface.
[    5.505635] systemd[1]: Created slice Slice /system/getty.
[    5.528954] systemd[1]: Created slice Slice /system/modprobe.
[    5.552585] systemd[1]: Created slice Slice /system/serial-getty.
[    5.576589] systemd[1]: Created slice Slice /system/systemd-fsck.
[    5.600051] systemd[1]: Created slice User and Session Slice.
[    5.622661] systemd[1]: Started Dispatch Password Requests to Console Directory Watch.
[    5.650590] systemd[1]: Started Forward Password Requests to Wall Directory Watch.
[    5.674366] systemd[1]: Expecting device /dev/mmcblk2p4...
[    5.694580] systemd[1]: Reached target Path Units.
[    5.714325] systemd[1]: Reached target Remote File Systems.
[    5.734334] systemd[1]: Reached target Slice Units.
[    5.754317] systemd[1]: Reached target Swaps.
[    5.803866] systemd[1]: Listening on RPCbind Server Activation Socket.
[    5.826441] systemd[1]: Reached target RPC Port Mapper.
[    5.848741] systemd[1]: Listening on Syslog Socket.
[    5.871036] systemd[1]: Listening on initctl Compatibility Named Pipe.
[    5.896387] systemd[1]: Listening on Journal Audit Socket.
[    5.919003] systemd[1]: Listening on Journal Socket (/dev/log).
[    5.943414] systemd[1]: Listening on Journal Socket.
[    5.963058] systemd[1]: Listening on Network Service Netlink Socket.
[    5.990267] systemd[1]: Listening on udev Control Socket.
[    6.010865] systemd[1]: Listening on udev Kernel Socket.
[    6.030985] systemd[1]: Listening on User Database Manager Socket.
[    6.086501] systemd[1]: Mounting Huge Pages File System...
[    6.109928] systemd[1]: Mounting POSIX Message Queue File System...
[    6.134066] systemd[1]: Mounting Kernel Debug File System...
[    6.154729] systemd[1]: Kernel Trace File System was skipped because of an unmet condition check (ConditionPathExists=/sys/kernel/tracing).
[    6.172707] systemd[1]: Mounting Temporary Directory /tmp...
[    6.195274] systemd[1]: Starting Create List of Static Device Nodes...
[    6.221747] systemd[1]: Starting Load Kernel Module configfs...
[    6.245853] systemd[1]: Starting Load Kernel Module drm...
[    6.270438] systemd[1]: Starting Load Kernel Module fuse...
[    6.296339] systemd[1]: Starting RPC Bind...
[    6.307877] fuse: init (API version 7.39)
[    6.314531] systemd[1]: File System Check on Root Device was skipped because of an unmet condition check (ConditionPathIsReadWrite=!/).
[    6.333367] systemd[1]: Starting Journal Service...
[    6.351664] systemd[1]: Load Kernel Modules was skipped because no trigger condition checks were met.
[    6.364751] systemd[1]: Starting Generate network units from Kernel command line...
[    6.405737] systemd-journald[147]: Collecting audit messages is enabled.
[    6.408645] systemd[1]: Starting Remount Root and Kernel File Systems...
[    6.438306] systemd[1]: Starting Apply Kernel Variables...
[    6.465405] systemd[1]: Starting Coldplug All udev Devices...
[    6.482634] EXT4-fs (mmcblk2p2): re-mounted 98ed423a-dee0-4be7-b332-3da98b714ea7 r/w. Quota mode: none.
[    6.498912] systemd[1]: Starting Virtual Console Setup...
[    6.527483] systemd[1]: Started RPC Bind.
[    6.542842] systemd[1]: Started Journal Service.
[    6.711054] systemd-journald[147]: Received client request to flush runtime journal.
[    6.952853] audit: type=1334 audit(1754265670.636:2): prog-id=6 op=LOAD
[    6.959613] audit: type=1334 audit(1754265670.644:3): prog-id=7 op=LOAD
[    7.089793] audit: type=1334 audit(1754265670.772:4): prog-id=8 op=LOAD
[    7.096850] audit: type=1334 audit(1754265670.780:5): prog-id=9 op=LOAD
[    7.103871] audit: type=1334 audit(1754265670.788:6): prog-id=10 op=LOAD
[    8.354713] Registered IR keymap rc-empty
[    8.360656] rc rc0: gpio_ir_recv as /devices/platform/ir-receiver/rc/rc0
[    8.369833] input: gpio_ir_recv as /devices/platform/ir-receiver/rc/rc0/input1
[    8.427531] EXT4-fs (mmcblk2p4): mounted filesystem 130d59ae-6f2f-45bf-b247-8efb2a1726a6 r/w with ordered data mode. Quota mode: none.
[    8.512597] debugfs: File 'Playback' in directory 'dapm' already present!
[    8.519578] debugfs: File 'Capture' in directory 'dapm' already present!
[    8.531421] caam 30900000.crypto: device ID = 0x0a16040100000000 (Era 9)
[    8.533736] caam-snvs 30370000.caam-snvs: ipid matched - 0x3e
[    8.546733] caam-snvs 30370000.caam-snvs: violation handlers armed - non-secure state
[    8.547253] caam 30900000.crypto: job rings = 1, qi = 0
[    8.791824] EXT4-fs (mmcblk2p3): mounted filesystem 98ed423a-dee0-4be7-b332-3da98b714ea7 r/w with ordered data mode. Quota mode: none.
[    8.955811] caam algorithms registered in /proc/crypto
[    8.963795] caam 30900000.crypto: caam pkc algorithms registered in /proc/crypto
[    8.972545] caam 30900000.crypto: rng crypto API alg registered prng-caam
[    8.979446] caam 30900000.crypto: registering rng-caam
[    8.984951] random: crng init done
[    8.990263] Device caam-keygen registered
[    9.180969] audit: type=1334 audit(1754265672.864:7): prog-id=11 op=LOAD
[    9.234530] audit: type=1334 audit(1754265672.920:8): prog-id=12 op=LOAD
[   10.064505] imx-sdma 30bd0000.dma-controller: firmware found.
[   10.064709] imx-sdma 302b0000.dma-controller: firmware found.
[   10.070623] imx-sdma 302c0000.dma-controller: firmware found.
[   10.076440] imx-sdma 302b0000.dma-controller: loaded firmware 4.6
[   10.181443] audit: type=1334 audit(1754265673.864:9): prog-id=13 op=LOAD
[   10.188558] audit: type=1334 audit(1754265673.872:10): prog-id=14 op=LOAD
[   10.195608] audit: type=1334 audit(1754265673.872:11): prog-id=15 op=LOAD
[   10.738903] Qualcomm Atheros AR8031/AR8033 30be0000.ethernet-1:00: attached PHY driver (mii_bus:phy_addr=30be0000.ethernet-1:00, irq=POLL)
[   14.851198] fec 30be0000.ethernet eth0: Link is Up - 1Gbps/Full - flow control off
[   14.928729] kauditd_printk_skb: 19 callbacks suppressed
[   14.928740] audit: type=1334 audit(1754265678.612:19): prog-id=19 op=LOAD
[   14.941044] audit: type=1334 audit(1754265678.620:20): prog-id=20 op=LOAD
[   14.948015] audit: type=1334 audit(1754265678.628:21): prog-id=21 op=LOAD
[   19.172568] platform backlight: deferred probe pending
[   19.177748] platform sound-ak4458: deferred probe pending
```

## II.3. u-boot

#### A. help

```bash
u-boot=> help
?         - alias for 'help'
base      - print or set address offset
bdinfo    - print Board Info structure
bind      - Bind a device to a driver
blkcache  - block cache diagnostics and control
bmp       - manipulate BMP image data
boot      - boot default, i.e., run 'bootcmd'
bootaux   - Start auxiliary core
bootd     - boot default, i.e., run 'bootcmd'
bootefi   - Boots an EFI payload from memory
bootelf   - Boot from an ELF image in memory
bootflow  - Boot flows
booti     - boot Linux kernel 'Image' format from memory
bootm     - boot application image from memory
bootp     - boot image via network using BOOTP/TFTP protocol
bootvx    - Boot vxWorks from an ELF image
clk       - CLK sub-system
clocks    - display clocks
cls       - clear screen
cmp       - memory compare
coninfo   - print console devices and information
cp        - memory copy
crc32     - checksum calculation
date      - get/set/reset date & time
dcache    - enable or disable data cache
dfu       - Device Firmware Upgrade
dhcp      - boot image via network using DHCP/TFTP protocol
dm        - Driver model low level access
echo      - echo args to console
editenv   - edit environment variable
eficonfig - provide menu-driven UEFI variable maintenance interface
efidebug  - Configure UEFI environment
env       - environment handling commands
exit      - exit script
ext2load  - load binary file from a Ext2 filesystem
ext2ls    - list files in a directory (default /)
ext4load  - load binary file from a Ext4 filesystem
ext4ls    - list files in a directory (default /)
ext4size  - determine a file's size
ext4write - create a file in the root directory
false     - do nothing, unsuccessfully
fastboot  - run as a fastboot usb or udp device
fatinfo   - print information about filesystem
fatload   - load binary file from a dos filesystem
fatls     - list files in a directory (default /)
fatmkdir  - create a directory
fatrm     - delete a file
fatsize   - determine a file's size
fatwrite  - write file into a dos filesystem
fdt       - flattened device tree utility commands
fspinand  - FSPI NAND Boot Control Blocks(BCB) sub-system
fstype    - Look up a filesystem type
fstypes   - List supported filesystem types
fuse      - Fuse sub-system
gettime   - get timer val elapsed
go        - start application at address 'addr'
gpio      - query and control gpio pins
gpt       - GUID Partition Table
gzwrite   - unzip and write memory to block device
hash      - compute hash message digest
help      - print command description/usage
i2c       - I2C sub-system
icache    - enable or disable instruction cache
iminfo    - print header information for application image
imxtract  - extract a part of a multi-image
itest     - return true/false on integer compare
lcdputs   - print string on video framebuffer
ln        - Create a symbolic link
load      - load binary file from a filesystem
loadb     - load binary file over serial line (kermit mode)
loads     - load S-Record file over serial line
loadx     - load binary file over serial line (xmodem mode)
loady     - load binary file over serial line (ymodem mode)
loop      - infinite loop on address range
ls        - list files in a directory (default /)
lzmadec   - lzma uncompress a memory region
md        - memory display
mdio      - MDIO utility commands
mii       - MII utility commands
mm        - memory modify (auto-incrementing address)
mmc       - MMC sub system
mmcinfo   - display MMC info
mtest     - simple RAM read/write test
mw        - memory write (fill)
net       - NET sub-system
nm        - memory modify (constant address)
optee_rpmb- Provides commands for testing secure storage on RPMB on OPTEE
panic     - Panic with optional message
part      - disk partition related commands
ping      - send ICMP ECHO_REQUEST to network host
pinmux    - show pin-controller muxing
poweroff  - Perform POWEROFF of the device
printenv  - print environment variables
pxe       - get and boot from pxe files
qspihdr   - Q(F)SPI Boot Config sub-system
random    - fill memory with random pattern
read      - Load binary data from a partition
regulator - uclass operations
reset     - Perform RESET of the CPU
rtc       - RTC subsystem
run       - run commands in an environment variable
save      - save file to a filesystem
saveenv   - save environment variables to persistent storage
sdp       - Serial Downloader Protocol
setcurs   - set cursor position within screen
setenv    - set environment variables
setexpr   - set environment variable as the result of eval expression
sf        - SPI flash sub-system
showvar   - print local hushshell variables
size      - determine a file's size
sleep     - delay execution for some time
sntp      - synchronize RTC via network
source    - run script from memory
sysboot   - command to get and boot from syslinux files
test      - minimal test like /bin/sh
tftpboot  - load file via network using TFTP protocol
time      - run commands and summarize execution time
timer     - access the system timer
true      - do nothing, successfully
ums       - Use the UMS [USB Mass Storage]
unbind    - Unbind a device from a driver
unlz4     - lz4 uncompress a memory region
unzip     - unzip a memory region
usb       - USB sub-system
usbboot   - boot from USB device
version   - print monitor, compiler and linker version
videolink - list and select video link
u-boot=>

```

#### B. printenv

```bash
u-boot=> printenv
BOOT_A_LEFT=3
BOOT_B_LEFT=3
BOOT_DEV=mmc 2:1
BOOT_ORDER=A B
arch=arm
baudrate=115200
board=imx8mm_evk
board_name=EVK
board_rev=iMX8MM
boot_a_script=load ${devtype} ${devnum}:${distro_bootpart} ${scriptaddr} ${prefix}${script}; source ${scriptaddr}
boot_efi_binary=load ${devtype} ${devnum}:${distro_bootpart} ${kernel_addr_r} efi/boot/bootaa64.efi; if fdt addr -q ${fdt_addr_r}; then bootefi ${kernel_addr_r} ${fdt_addr_r};else bootefi ${kernel_addr_r} ${fdtcontroladdr};fi
boot_efi_bootmgr=if fdt addr -q ${fdt_addr_r}; then bootefi bootmgr ${fdt_addr_r};else bootefi bootmgr;fi
boot_extlinux=sysboot ${devtype} ${devnum}:${distro_bootpart} any ${scriptaddr} ${prefix}${boot_syslinux_conf}
boot_fit=no
boot_net_usb_start=usb start
boot_prefixes=/ /boot/
boot_script_dhcp=boot.scr.uimg
boot_scripts=boot.scr.uimg boot.scr
boot_syslinux_conf=extlinux/extlinux.conf
boot_targets=usb0 mmc1 mmc2
bootcmd=run sr_ir_v2_cmd;run distro_bootcmd;run bsp_bootcmd
bootcmd_mfg=run mfgtool_args;if iminfo ${initrd_addr}; then if test ${tee} = yes; then bootm ${tee_addr} ${initrd_addr} ${fdt_addr}; else booti ${loadaddr} ${initrd_addr} ${fdt_addr}; fi; else echo "Run fastboot ..."; fastboot 0; fi;
bootcmd_mmc1=devnum=1; run mmc_boot
bootcmd_mmc2=devnum=2; run mmc_boot
bootcmd_usb0=devnum=0; run usb_boot
bootdelay=2
bootfstype=fat
bootm_size=0x10000000
bootscript=echo Running bootscript from mmc ...; source
bsp_bootcmd=echo Running BSP bootcmd ...; mmc dev ${mmcdev}; if mmc rescan; then if run loadbootscript; then run bootscript; else if run loadimage; then run mmcboot; else run netboot; fi; fi; fi;
bsp_script=boot.scr
console=ttymxc1,115200
cpu=armv8
devplist=1
distro_bootcmd=for target in ${boot_targets}; do run bootcmd_${target}; done
distro_bootpart_uuid=076c4a2a-01
efi_dtb_prefixes=/ /dtb/ /dtb/current/
emmc_dev=2
ethaddr=00:04:9f:07:2a:01
ethprime=FEC
fastboot_dev=mmc2
fdt_addr=0x43000000
fdt_addr_r=0x43000000
fdt_high=0xffffffffffffffff
fdtaddr=43000000
fdtcontroladdr=bace93f0
fdtfile=imx8mm-evk.dtb
fileaddr=43500000
filesize=57c
image=Image
initrd_addr=0x43800000
initrd_high=0xffffffffffffffff
jh_clk=
jh_mmcboot=mw 0x303d0518 0xff; setenv fdtfile ${jh_root_dtb};setenv jh_clk kvm.enable_virt_at_load=false clk_ignore_unused mem=1212MB; if run loadimage; then run mmcboot; else run jh_netboot; fi;
jh_netboot=mw 0x303d0518 0xff; setenv fdtfile ${jh_root_dtb}; setenv jh_clk kvm.enable_virt_at_load=false clk_ignore_unused mem=1212MB; run netboot;
jh_root_dtb=imx8mm-evk-root.dtb
kboot=booti
kernel_addr_r=0x40400000
load_efi_dtb=load ${devtype} ${devnum}:${distro_bootpart} ${fdt_addr_r} ${prefix}${efi_fdtfile}
loadaddr=0x40400000
loadbootscript=fatload mmc ${mmcdev}:${mmcpart} ${loadaddr} ${bsp_script};
loadfdt=fatload mmc ${mmcdev}:${mmcpart} ${fdt_addr_r} ${fdtfile}
loadimage=fatload mmc ${mmcdev}:${mmcpart} ${loadaddr} ${image}
mfgtool_args=setenv bootargs console=${console},${baudrate} rdinit=/linuxrc clk_ignore_unused
mmc_boot=if mmc dev ${devnum}; then devtype=mmc; run scan_dev_for_boot_part; fi
mmcargs=setenv bootargs ${jh_clk} ${mcore_clk} console=${console} root=${mmcroot}
mmcautodetect=yes
mmcboot=echo Booting from mmc ...; run mmcargs; if test ${boot_fit} = yes || test ${boot_fit} = try; then bootm ${loadaddr}; else if run loadfdt; then booti ${loadaddr} - ${fdt_addr_r}; else echo WARN: Cannot load the DT; fi; fi;
mmcdev=2
mmcpart=1
mmcroot=/dev/mmcblk2p2 rootwait rw
nandfit_part=yes
netargs=setenv bootargs ${jh_clk} ${mcore_clk} console=${console} root=/dev/nfs ip=dhcp nfsroot=${serverip}:${nfsroot},v3,tcp
netboot=echo Booting from net ...; run netargs;  if test ${ip_dyn} = yes; then setenv get_cmd dhcp; else setenv get_cmd tftp; fi; ${get_cmd} ${loadaddr} ${image}; if test ${boot_fit} = yes || test ${boot_fit} = try; then bootm ${loadaddr}; else if ${get_cmd} ${fdt_addr_r} ${fdtfile}; then booti ${loadaddr} - ${fdt_addr_r}; else echo WARN: Cannot load the DT; fi; fi;
nodes=/usbg1 /usbg2 /wdt-reboot /soc@0/caam-sm@100000 /soc@0/bus@30000000/caam_secvio /soc@0/bus@30000000/caam-snvs@30370000 /soc@0/bus@32c00000/lcdif@32e00000 /soc@0/bus@32c00000/csi1_bridge@32e20000 /soc@0/bus@32c00000/mipi_dsi@32e10000 /soc@0/bus@32c00000/mipi_csi@32e30000 /soc@0/bus@32c00000/display-subsystem /audio-codec-bt-sco /sound-bt-sco /audio-codec /sound-wm8524 /dsi-host /rm67199_panel /soc@0/bus@30800000/i2c@30a20000/pca9450@25 /soc@0/bus@30800000/i2c@30a30000/adv7535@3d /soc@0/bus@30800000/i2c@30a30000/tcpc@50 /soc@0/memory-controller@3d400000 /soc@0/bus@30800000/spi@30830000/spi@0 /binman /vpu_h1@38320000 /vpu_g1@38300000 /vpu_g2@38310000 /vpu_v4l2 /gpu@38000000
prepare_mcore=setenv mcore_clk clk-imx8mm.mcore_booted;
rauc_slot=A
rootfs_part=/dev/mmcblk2p2
scan_dev_for_boot=echo Scanning ${devtype} ${devnum}:${distro_bootpart}...; for prefix in ${boot_prefixes}; do run scan_dev_for_extlinux; run scan_dev_for_scripts; done;run scan_dev_for_efi;
scan_dev_for_boot_part=part list ${devtype} ${devnum} -bootable devplist; env exists devplist || setenv devplist 1; for distro_bootpart in ${devplist}; do if fstype ${devtype} ${devnum}:${distro_bootpart} bootfstype; then part uuid ${devtype} ${devnum}:${distro_bootpart} distro_bootpart_uuid ; run scan_dev_for_boot; fi; done; setenv devplist
scan_dev_for_efi=setenv efi_fdtfile ${fdtfile}; for prefix in ${efi_dtb_prefixes}; do if test -e ${devtype} ${devnum}:${distro_bootpart} ${prefix}${efi_fdtfile}; then run load_efi_dtb; fi;done;run boot_efi_bootmgr;if test -e ${devtype} ${devnum}:${distro_bootpart} efi/boot/bootaa64.efi; then echo Found EFI removable media binary efi/boot/bootaa64.efi; run boot_efi_binary; echo EFI LOAD FAILED: continuing...; fi; setenv efi_fdtfile
scan_dev_for_extlinux=if test -e ${devtype} ${devnum}:${distro_bootpart} ${prefix}${boot_syslinux_conf}; then echo Found ${prefix}${boot_syslinux_conf}; run boot_extlinux; echo EXTLINUX FAILED: continuing...; fi
scan_dev_for_scripts=for script in ${boot_scripts}; do if test -e ${devtype} ${devnum}:${distro_bootpart} ${prefix}${script}; then echo Found U-Boot script ${prefix}${script}; run boot_a_script; echo SCRIPT FAILED: continuing...; fi; done
scriptaddr=0x43500000
sd_dev=1
serial#=0a1d3209dab5b3c9
soc=imx8m
soc_type=imx8mm
splashimage=0x50000000
sr_ir_v2_cmd=cp.b ${fdtcontroladdr} ${fdt_addr_r} 0x10000;fdt addr ${fdt_addr_r};for i in ${nodes}; do fdt rm ${i}; done
usb_boot=usb start; if usb dev ${devnum}; then devtype=usb; run scan_dev_for_boot_part; fi
vendor=freescale

Environment size: 6465/16380 bytes
```

#### C. part

```bash
u-boot=> part list mmc 2

Partition Map for MMC device 2  --   Partition Type: DOS

Part    Start Sector    Num Sectors     UUID            Type
  1     16384           681574          076c4a2a-01     0c Boot
  2     704512          10905190        076c4a2a-02     83
  3     11616256        10905190        076c4a2a-03     83
  4     22528000        8388608         076c4a2a-04     83
```

#### D. ls

> Image: 表示要從 FAT 分割區載入的檔名（通常是 Linux kernel image）

> imx8mm-evk.dtb: 要載入的 Device Tree Blob (DTB) 檔案，描述 SoC 和硬體架構
>
> Device Tree (`*.dtb`) 是 Linux 開機時用來**告訴 kernel：板子上有哪些裝置、memory mapping、串口、I2C、SPI、GPIO** 等資訊。

```bash
u-boot=> ls mmc 2:1
     1404   boot.scr
 35764736   Image
    50816   imx8mm-evk-8mic-revE.dtb
    51184   imx8mm-evk-8mic-swpdm.dtb
    48591   imx8mm-evk-ak4497.dtb
    48283   imx8mm-evk-ak5558.dtb
    48355   imx8mm-evk-audio-tdm.dtb
    48169   imx8mm-evk-dpdk.dtb
    48263   imx8mm-evk.dtb
    48176   imx8mm-evk-ecspi-slave.dtb
     3171   imx8mm-evk-inmate.dtb
    48976   imx8mm-evk-lk.dtb
    48371   imx8mm-evk-pcie-ep.dtb
    48406   imx8mm-evk-qca-wifi.dtb
    48430   imx8mm-evk-revb-qca-wifi.dtb
    48713   imx8mm-evk-rm67191-cmd-ram.dtb
    48713   imx8mm-evk-rm67191.dtb
    48787   imx8mm-evk-rm67199-cmd-ram.dtb
    48787   imx8mm-evk-rm67199.dtb
    48848   imx8mm-evk-root.dtb
    49552   imx8mm-evk-rpmsg.dtb
    49955   imx8mm-evk-rpmsg-wm8524.dtb
    49943   imx8mm-evk-rpmsg-wm8524-lpv.dtb
    48199   imx8mm-evk-usd-wifi.dtb
            mcore-demos/
   599952   tee.bin

25 file(s), 1 dir(s)
```

# III. Glossary

# IV. Tool Usage

# Author

> Created and designed by [Lanka Hsu](lankahsu@gmail.com).

# License

> [CrossCompilationX](https://github.com/lankahsu520/CrossCompilationX) is available under the BSD-3-Clause license. See the LICENSE file for more info.

