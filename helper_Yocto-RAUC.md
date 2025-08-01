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

| ITEM         | FILE                                |
| ------------ | ----------------------------------- |
| configure    | confs/imx8mm-scarthgap-emmc.conf    |
| cooker-menu: | imx8mm-evk-scarthgap-emmc-menu.json |

```bash
$ git clone https://github.com/lankahsu520/CrossCompilationX.git
$ cd CrossCompilationX/Yocto/cookerX/
$ . confs/imx8mm-scarthgap-emmc.conf
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

> 網路上的範例和討論都很陽春，如果要花大把的時間去研讀官方文件，可能你的工作已經沒了。

## 2.1. Add layer

### 2.1.1. imx8mm-evk-scarthgap-emmc-menu.json

>  DISTRO_FEATURES: rauc
>
>  IMAGE_INSTALL: rauc

```bash
$ echo $PJ_COOKER_MENU
imx8mm-evk-scarthgap-emmc-menu.json

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
    "imx8mm-evk-scarthgap-emmc": {
      "local.conf": [
        "DISTRO_FEATURES:append = ' rauc'",
        "IMAGE_INSTALL:append = ' rauc'",
      ]
    }
  }
```

## 2.2. Nothing

> 如果只是加入 meta-rauc 是沒有任何功用的。

# 3. meta-rauc-plus

## 3.1. create-layer

```bash
$ echo $PJ_YOCTO_LAYERS_DIR
/yocto/cookerX-emmc/layers-scarthgap
$ cd $PJ_YOCTO_LAYERS_DIR
$ bitbake-layers create-layer meta-rauc-plus
NOTE: Starting bitbake server...
Add your new layer with 'bitbake-layers add-layer meta-rauc-plus'

# 這邊刪除範例
$ rm -rf recipes-example

$ echo $PJ_COOKER_MENU
imx8mm-evk-scarthgap-emmc-menu.json

$ cd-root; vi ./cooker-menu/$PJ_COOKER_MENU
# add "meta-rauc-plus" into "layers"
$ cooker generate
$ bitbake-layers show-layers | grep meta-rauc-plus
$ cat $PJ_YOCTO_BUILD_DIR/conf/bblayers.conf | grep meta-rauc-plus

$ cd-root
$ make cook-clean
```

### 3.1.1. imx8mm-evk-scarthgap-emmc-menu.json

>  修改 cooker-menu/imx8mm-evk-scarthgap-emmc-menu.json

>  WKS_FILE:  使用 imx-imx-boot-bootpart-lanka520.wks.in

>  RAUC_KEY_FILE: 指定 private.pem

>  RAUC_CERT_FILE: 指定 keyring.pem

>  IMAGE_FSTYPES: wic.zst ext4
>
>  會在編譯時產出 imx-image-core-imx8mm-lpddr4-evk.rootfs.ext4；如果保留 wic.zst，將不會再看到wic.zst 產出，這或許是本身的 Bugs。

>  IMAGE_INSTALL: rauc libubootenv-bin

```bash
$ echo $PJ_COOKER_MENU
imx8mm-evk-scarthgap-emmc-menu.json

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
    "imx8mm-evk-scarthgap-emmc": {
      "target": "imx-image-core",
      "local.conf": [
        "MACHINE = 'imx8mm-lpddr4-evk'",
        "DISTRO = 'fsl-imx-wayland'",
        "PACKAGE_CLASSES = 'package_rpm'",
        "EXTRA_IMAGE_FEATURES = 'debug-tweaks ssh-server-dropbear'",
        "INIT_MANAGER = 'systemd'",
        "CONF_VERSION = '2'",
        "ACCEPT_FSL_EULA = '1'",
        "WKS_FILE = 'imx-imx-boot-bootpart-lanka520.wks.in'",
        "DISTRO_FEATURES:append = ' rauc'",
        "IMAGE_FSTYPES += ' wic.zst ext4'",
        "IMAGE_INSTALL:append = ' rauc libubootenv-bin tree helloworld123'",
      ]
    }
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
/yocto/cookerX-emmc/layers-scarthgap/meta-rauc-plus/recipes-core/rauc
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
/yocto/cookerX-emmc
$ mkdir -p ${PJ_YOCTO_ROOT}
$ cp -av openssl-ca/dev/private/development-1.key.pem ${PJ_YOCTO_ROOT}/rauc-keys
$ cp -av openssl-ca/dev/development-1.cert.pem ${PJ_YOCTO_ROOT}/rauc-keys

$ tree -L 4 ${PJ_YOCTO_ROOT}/rauc-keys
/yocto/cookerX-emmc/rauc-keys
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

[slot.data.0]
device=/dev/mmcblk2p4
type=ext4
#mountpoint=/data
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

> RAUC 實現了 `Dual Image`，於是 Disk I的配置也要改變。

```bash
$ tree -L 4 ${PJ_YOCTO_LAYERS_DIR}/meta-freescale/wic
/yocto/cookerX-emmc/layers-scarthgap/meta-freescale/wic
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
/yocto/cookerX-emmc/layers-scarthgap/meta-rauc-plus/recipes-core/base-files
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
> data: 保留使用者區塊

| Default            | Default Size                  | New Size                       | New                                           |
| ------------------ | ----------------------------- | ------------------------------ | --------------------------------------------- |
| mtdblock0          | 33,554,432<br/>(~32 MB)       | 33,554,432<br/>(~32 MB)        | mtdblock0                                     |
| mmcblk2            | 31,268,536,320<br>(~29.12 GB) | 31,268,536,320<br/>(~29.12 GB) | mmcblk2                                       |
| mmcblk2p1 (boot)   | 348,965,888<br>(~332.8 MB)    | 348,965,888<br/>(~332.8 MB)    | mmcblk2p1 (boot)<br>/run/media/boot-mmcblk2p1 |
| mmcblk2p2 (rootfs) | 1,898,146,816<br>(~1.8 GB)    | 5,583,457,280<br>(~5.2 GB)     | mmcblk2p2 (rootfs A)                          |
|                    |                               | 5,583,457,280<br/>(~5.2 GB)    | mmcblk2p3 (rootfs B)                          |
|                    |                               | 4,294,967,296<br>(~4 GB)       | mmcblk2p4 (data)<br/>/data                    |
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
part /data --ondisk mmcblk --fstype=ext4 --label data --align 8192 --size 4096M

bootloader --ptable msdos
```

### 3.3.2. fstab

> 開機自動掛載 /dev/mmcblk2p4 -> /data

```bash
# stock fstab - you probably want to override this with a machine specific one

/dev/root            /                    auto       defaults              1  1
proc                 /proc                proc       defaults              0  0
devpts               /dev/pts             devpts     mode=0620,ptmxmode=0666,gid=5      0  0
tmpfs                /run                 tmpfs      mode=0755,nodev,nosuid,strictatime 0  0
tmpfs                /var/volatile        tmpfs      defaults              0  0

# uncomment this if your device has a SD/MMC/Transflash slot
#/dev/mmcblk0p1       /media/card          auto       defaults,sync,noauto  0  0

/dev/mmcblk2p4 /data auto defaults 0 2
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


SRC_URI="git://github.com/nxp-imx/uboot-imx.git;protocol=https;branch=lf_v2024.04"

S="/yocto/cookerX-emmc/builds/build-imx8mm-evk-scarthgap-emmc/tmp/work/imx8mm_lpddr4_evk-poky-linux/u-boot-imx/2024.04/git"

WORKDIR="/yocto/cookerX-emmc/builds/build-imx8mm-evk-scarthgap-emmc/tmp/work/imx8mm_lpddr4_evk-poky-linux/u-boot-imx/2024.04"

DEPENDS="virtual/aarch64-poky-linux-gcc virtual/aarch64-poky-linux-compilerlibs virtual/libc   swig-native kern-tools-native      bc-native     bison-native     dtc-native     flex-native     gnutls-native     xxd-native  python3-native  openssl-native"

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

./poky/meta/recipes-bsp/u-boot/libubootenv_0.3.5.bb

SRC_URI="git://github.com/sbabic/libubootenv;protocol=https;branch=master"

S="/yocto/cookerX-emmc/builds/build-imx8mm-evk-scarthgap-emmc/tmp/work/armv8a-poky-linux/libubootenv/0.3.5/git"

WORKDIR="/yocto/cookerX-emmc/builds/build-imx8mm-evk-scarthgap-emmc/tmp/work/armv8a-poky-linux/libubootenv/0.3.5"

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
/yocto/cookerX-emmc/layers-scarthgap/meta-rauc-plus/recipes-bsp/u-boot
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
/yocto/cookerX-emmc/layers-scarthgap/meta-rauc-plus/recipes-core/images
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
/yocto/cookerX-emmc/layers-scarthgap/meta-rauc-plus/recipes-bsp/u-boot
├── files
│   ├── boot.cmd
│   └── fw_env.config
├── libubootenv_%.bbappend
└── u-boot-imx_%.bbappend

1 directory, 4 files

$ tree -L 4 ${PJ_YOCTO_LAYERS_DIR}/meta-rauc-plus/recipes-core/images
/yocto/cookerX-emmc/layers-scarthgap/meta-rauc-plus/recipes-core/images
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
    install -d ${DEPLOYDIR}
    install -m 0644 ${WORKDIR}/${UBOOT_ENV_BINARY} ${DEPLOYDIR}
}

```

#### C. imx-image-core.bbappend

```bbappend

IMAGE_BOOT_FILES += " boot.scr"

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

S="/yocto/cookerX-emmc/builds/build-imx8mm-evk-scarthgap-emmc/tmp/work/imx8mm_lpddr4_evk-poky-linux/linux-imx/6.6.52+git/git"

WORKDIR="/yocto/cookerX-emmc/builds/build-imx8mm-evk-scarthgap-emmc/tmp/work/imx8mm_lpddr4_evk-poky-linux/linux-imx/6.6.52+git"

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
/yocto/cookerX-emmc/layers-scarthgap/meta-rauc-plus/recipes-kernel/linux
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
    
$ sudo mkdir -p /tmp/wic
$ sudo mount -o loop,offset=$((16384 * 512)) imx-image-core-imx8mm-lpddr4-evk.rootfs.wic /tmp/wic

# 看看有沒有 boot.scr
$ ll /tmp/wic
total 36520
drwxr-xr-x  3 root root    16384 Jan  1  1970 ./
drwxrwxrwt 21 root root    36864 Jul 31 10:28 ../
-rwxr-xr-x  1 root root     1404 Apr  6  2011 boot.scr*
-rwxr-xr-x  1 root root 35631616 Apr  6  2011 Image*
-rwxr-xr-x  1 root root    50816 Apr  6  2011 imx8mm-evk-8mic-revE.dtb*
-rwxr-xr-x  1 root root    51184 Apr  6  2011 imx8mm-evk-8mic-swpdm.dtb*
-rwxr-xr-x  1 root root    48591 Apr  6  2011 imx8mm-evk-ak4497.dtb*
-rwxr-xr-x  1 root root    48283 Apr  6  2011 imx8mm-evk-ak5558.dtb*
-rwxr-xr-x  1 root root    48355 Apr  6  2011 imx8mm-evk-audio-tdm.dtb*
-rwxr-xr-x  1 root root    48169 Apr  6  2011 imx8mm-evk-dpdk.dtb*
-rwxr-xr-x  1 root root    48263 Apr  6  2011 imx8mm-evk.dtb*
-rwxr-xr-x  1 root root    48176 Apr  6  2011 imx8mm-evk-ecspi-slave.dtb*
-rwxr-xr-x  1 root root     3171 Apr  6  2011 imx8mm-evk-inmate.dtb*
-rwxr-xr-x  1 root root    48976 Apr  6  2011 imx8mm-evk-lk.dtb*
-rwxr-xr-x  1 root root    48371 Apr  6  2011 imx8mm-evk-pcie-ep.dtb*
-rwxr-xr-x  1 root root    48406 Apr  6  2011 imx8mm-evk-qca-wifi.dtb*
-rwxr-xr-x  1 root root    48430 Apr  6  2011 imx8mm-evk-revb-qca-wifi.dtb*
-rwxr-xr-x  1 root root    48713 Apr  6  2011 imx8mm-evk-rm67191-cmd-ram.dtb*
-rwxr-xr-x  1 root root    48713 Apr  6  2011 imx8mm-evk-rm67191.dtb*
-rwxr-xr-x  1 root root    48787 Apr  6  2011 imx8mm-evk-rm67199-cmd-ram.dtb*
-rwxr-xr-x  1 root root    48787 Apr  6  2011 imx8mm-evk-rm67199.dtb*
-rwxr-xr-x  1 root root    48848 Apr  6  2011 imx8mm-evk-root.dtb*
-rwxr-xr-x  1 root root    49552 Apr  6  2011 imx8mm-evk-rpmsg.dtb*
-rwxr-xr-x  1 root root    49955 Apr  6  2011 imx8mm-evk-rpmsg-wm8524.dtb*
-rwxr-xr-x  1 root root    49943 Apr  6  2011 imx8mm-evk-rpmsg-wm8524-lpv.dtb*
-rwxr-xr-x  1 root root    48199 Apr  6  2011 imx8mm-evk-usd-wifi.dtb*
drwxr-xr-x  2 root root     8192 Apr  6  2011 mcore-demos/
-rwxr-xr-x  1 root root   599952 Apr  6  2011 tee.bin*

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
rauc-Message: 13:49:46.778: Reading bundle: /yocto/cookerX-emmc/images-lnk/update-bundle-imx8mm-lpddr4-evk.raucb
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

## II.2. LastError: Failed mounting bundle: Failed to load dm table: Invalid argument, check DM_VERITY, DM_CRYPT or CRYPTO_AES kernel options.

> 這邊主要是少了 dm-verity.ko；其它還要確認 dm-crypt.ko

```bash
root@imx8mm-lpddr4-evk:~# rauc install /tmp/update-bundle-imx8mm-lpddr4-evk.raucb
[ 2668.276180] loop0: detected capacity change from 0 to 677395
installing
  0% Installing
  0% Determining slot states
 10% Determining slot states done.
 10% Checking bundle
 10% Verifying signature
 20% Verifying signature done.
 20% Checking bundle done.
[ 2668.304396] loop0: detected capacity change from 677395 to 677392
 20% Checking manifest contents
 30% Checking manifest contents done.
[ 2668.339072] device-mapper: table: 253:0: verity: unknown target type
[ 2668.346803] device-mapper: ioctl: error adding target to table
100% Installing failed.
LastError: Failed mounting bundle: Failed to load dm table: Invalid argument, check DM_VERITY, DM_CRYPT or CRYPTO_AES kernel options.
Installing `/tmp/update-bundle-imx8mm-lpddr4-evk.raucb` failed
```

# III. Glossary

# IV. Tool Usage

# Author

> Created and designed by [Lanka Hsu](lankahsu@gmail.com).

# License

> [CrossCompilationX](https://github.com/lankahsu520/CrossCompilationX) is available under the BSD-3-Clause license. See the LICENSE file for more info.

