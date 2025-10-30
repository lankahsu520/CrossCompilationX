# [Yocto](https://www.yoctoproject.org)
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

# 1.  [Yocto Project](https://www.yoctoproject.org/software-overview/)

## 1.0. Useful ?

> 編譯很花時間，至少2小時（手邊是有intel i9-11980HK）以上 ~ 1天 or 2天都有可能。
>
> 很佔空間，至少要留個100G。如果有 Daily Build 的需求，那成本真的很重。
>
> 還有你要先決定 Poky要抓那一版本（branch 和 rev），相對應的 meta-openembedded 和硬體相依性比較高的 meta-raspberrypi 你也要決定版本（branch 和 rev）。就算所有需要的軟體都集齊了，最後也編譯完了，最後能不能開機也是問題！
>
> 如果你不是晶片供應商（如聯發科），而只是一位 embedded engineer，聯發科領多少錢，而我們領不到他們的一半，甚至連破百萬都沒有；卻要幫他們組一包可以編譯的環境，這情何以堪。
>
> BB 的語法太靈活，shellscript 的支援度不高；我們寫程式的時間都不夠了，還要學習這奇怪的語法。
>
> 在編譯時一定要上網！就算你已經編譯成功，而且已經下載過，還是會報失敗。<font color="red">這邊就有個疑慮，怎麼定版。</font>
>
> yocto 的版本變動很快，或許今年允許的 class 或語法，明年就不能用了。就連 google 大神也救不了，因為新的東西，還要等有心人把踩到的屎寫出來。
>
> 最後就是 yocto 編譯的錯誤訊息相當的難理解，這有一部分是繼承了 python的傳統。

> <font color="red">2年（2023~2025）過去了，再次試著編譯以前留下來的版本（包含系統和程式），真的是困難重重。第1個就是 python 版本問題，當初是使用python 3.8，而目前系統是 python 3.10。</font>
>
> <font color="red">再來就是編譯過程中，太多使用 git下載對應的 Revision，過程真的是`超級慢`，最慘的還有版本遺失了。</font>

> 身為軟體工師的職責不應該被這些瑣事煩心，而是要專注於程式開發。

## 1.1. [Release Activity](https://wiki.yoctoproject.org/wiki/Releases) vs Python

|           Codename           | Yocto Project Version | Release Date  |    Current Version    |               Support Level                | Poky Version | BitBake branch | Python |                     Maintainer                      |                            Notes                             |
| :--------------------------: | :-------------------: | :-----------: | :-------------------: | :----------------------------------------: | :----------: | :------------: | ------ | :-------------------------------------------------: | :----------------------------------------------------------: |
|           Wrynose            |          6.0          |  April 2026   |                       |                   Future                   |     N/A      |      2.16      |        | Richard Purdie <richard.purdie@linuxfoundation.org> |                                                              |
|          Whinlatter          |          5.3          | October 2025  |                       |                   Future                   |     N/A      |      2.14      |        | Richard Purdie <richard.purdie@linuxfoundation.org> | [Recipe versions](https://wiki.yoctoproject.org/wiki/Recipe_Versions) |
|    Walnascar (aka Walna)     |          5.2          |   May 2025    |   5.2.1(June 2025)    | Support for 7 months (until November 2025) |     N/A      |      2.12      |        |          Steve Sakoman <steve@sakoman.com>          | [Recipe versions](https://wiki.yoctoproject.org/wiki/Recipe_Versions) |
|  Styhead (like 'try head')   |          5.1          | October 2024  |  5.1.4 (April 2025)   |                    EOL                     |     N/A      |      2.10      |        |          Steve Sakoman <steve@sakoman.com>          |                                                              |
|          Scarthgap           |          5.0          |  April 2024   |  5.0.10 (June 2025)   |    Long Term Support (until April 2028)    |     N/A      |      2.8       |        |          Steve Sakoman <steve@sakoman.com>          | [Recipe versions](https://wiki.yoctoproject.org/wiki/Recipe_Versions) |
| Nanbield (like 'man field')  |          4.3          | November 2023 |  4.3.4 (April 2024)   |                    EOL                     |     N/A      |      2.6       | 3.12   |          Steve Sakoman <steve@sakoman.com>          |                                                              |
|          Mickledore          |          4.2          |   May 2023    | 4.2.4 (December 2023) |                    EOL                     |     N/A      |      2.4       | 3.11   |          Steve Sakoman <steve@sakoman.com>          |                                                              |
|           Langdale           |          4.1          | October 2022  |   4.1.4 (May 2023)    |                    EOL                     |     N/A      |      2.2       |        |          Steve Sakoman <steve@sakoman.com>          |                                                              |
| Kirkstone (like 'kirk stun') |          4.0          |   May 2022    |  4.0.27 (June 2025)   |       Long Term Support (Apr. 2026¹)       |     N/A      |      2.0       | 3.10   |          Steve Sakoman <steve@sakoman.com>          |                                                              |
|           Honister           |          3.4          | October 2021  |   3.4.4 (May 2022)    |                    EOL                     |     N/A      |      1.52      |        |         Anuj Mittal <anuj.mittal@intel.com>         |                                                              |
|          Hardknott           |          3.3          |  April 2021   |  3.3.6 (April 2022)   |                    EOL                     |     N/A      |      1.50      |        |         Anuj Mittal <anuj.mittal@intel.com>         |                                                              |
|          Gatesgarth          |          3.2          |   Oct 2020    |   3.2.4 (May 2021)    |                    EOL                     |     N/A      |      1.48      |        |         Anuj Mittal <anuj.mittal@intel.com>         |                                                              |
|           Dunfell            |          3.1          |  April 2020   |   3.1.33 (May 2024)   |                 EOL - LTS¹                 |     23.0     |      1.46      |        |          Steve Sakoman <steve@sakoman.com>          |                                                              |
|             Zeus             |          3.0          | October 2019  |  3.0.4 (August 2020)  |                    EOL                     |    22.0.3    |      1.44      | 3.8    |                     Anuj/Armin                      |                                                              |
|           Warrior            |          2.7          |  April 2019   |   2.7.4 (June 2020)   |                    EOL                     |     21.0     |      1.42      |        |         Armin Kuster <akuster808@gmail.com>         |                                                              |
|             Thud             |          2.6          |   Nov 2018    | 2.6.4 (October 2019)  |                    EOL                     |     20.0     |      1.40      |        |         Armin Kuster <akuster808@gmail.com>         |                                                              |
|             Sumo             |          2.5          |  April 2018   |  2.5.3 (April 2019)   |                    EOL                     |     19.0     |      1.38      |        |         Armin Kuster <akuster808@gmail.com>         |                                                              |
|            Rocko             |          2.4          |   Oct 2017    | 2.4.4 (November 2018) |                    EOL                     |     18.0     |      1.36      |        |         Armin Kuster <akuster808@gmail.com>         |                                                              |
|             Pyro             |          2.3          |   May 2017    |   2.3.4 (July 2018)   |                    EOL                     |     17.0     |      1.34      |        |         Armin Kuster <akuster808@gmail.com>         |                                                              |
|            Morty             |          2.2          |   Nov 2016    |   2.2.4 (July 2018)   |                    EOL                     |     16.0     |      1.32      |        |         Armin Kuster <akuster808@gmail.com>         |                                                              |
|           Krogoth            |          2.1          |   Apr 2016    |   2.1.3 (July 2017)   |                    EOL                     |     15.0     |      1.30      |        |         Armin Kuster <akuster808@gmail.com>         |                                                              |
|            Jethro            |          2.0          |   Nov 2015    | 2.0.3 (January 2017)  |                    EOL                     |     14.0     |      1.28      |        |       Robert Yang <liezhi.yang@windriver.com>       |                                                              |
|             Fido             |          1.8          |   Apr 2015    |         1.8.2         |                    EOL                     |     13.0     |      1.26      |        |        Joshua Lock <joshua.g.lock@intel.com>        |                                                              |
|            Dizzy             |          1.7          |   Oct 2014    |         1.7.3         |                    EOL                     |     12.0     |      1.24      |        |         Armin Kuster <akuster808@gmail.com>         |                                                              |
|            Daisy             |          1.6          |   Apr 2014    |         1.6.3         |                    EOL                     |     11.0     |      1.22      |        |           Saul Wold <sgw@linux.intel.com>           |                                                              |
|             Dora             |          1.5          |   Oct 2013    |         1.5.4         |                    EOL                     |     10.0     |      1.20      |        |       Robert Yang <liezhi.yang@windriver.com>       |                                                              |
|            Dylan             |          1.4          |   Apr 2013    |         1.4.3         |                    EOL                     |     9.0      |      1.18      |        |    Paul Eggleton <paul.eggleton@linux.intel.com>    |                                                              |
|            Danny             |          1.3          |   Oct 2012    |         1.3.2         |                    EOL                     |     8.0      |      1.16      |        |         Ross Burton <ross.burton@intel.com>         |                                                              |
|            Denzil            |          1.2          |   Apr 2012    |         1.2.2         |                    EOL                     |     7.0      |      1.15      |        |                                                     |                                                              |
|            Edison            |          1.1          |   Oct 2011    |         1.1.2         |                    EOL                     |     6.0      |      1.13      |        |                                                     |                                                              |
|           Bernard            |          1.0          |   Apr 2011    |         1.0.2         |                    EOL                     |     5.0      |      1.11      |        |                                                     |                                                              |
|           Laverne            |          0.9          |   Oct 2010    |                       |                                            |     4.0      |      1.11      |        |                                                     |                                                              |
|            Green             |          N/A          | 11 June 2010  |                       |                                            |     3.3      |                |        |                                                     |                                                              |
|            Purple            |          N/A          |  15 Dec 2009  |                       |                                            |     3.2      |                |        |                                                     |                                                              |
|            Pinky             |          N/A          |  12 Nov 2009  |                       |                                            |     3.1      |                |        |                                                     |                                                              |
|            Blinky            |          N/A          |  1 Aug 2007   |                       |                                            |     3.0      |                |        |                                                     |                                                              |
|            Clyde             |          N/A          |  19 Jan 2007  |                       |                                            |     2.0      |                |        |                                                     |                                                              |
|             Inky             |          N/A          |  10 Feb 2006  |                       |                                            |     1.0      |                |        |                                                     |                                                              |

## 1.2. [Release Information](https://docs.yoctoproject.org/migration-guides/index.html)

# 2. Yocto Layers

> Yocto計畫主要由三個元件組成：
>
> BitBake：讀取組態檔與處方檔（recipes)並執行，組態與建置所指定的應用程式或者系統檔案映像檔。
>
> OpenEmbedded-Core：由基礎layers所組成，並為處方檔（recipes)，layers與classes的集合：這些要素都是在OpenEmbedded系統中共享使用的。
>
> Poky：是一個參考系統。是許多案子與工具的集合，用來讓使用者延伸出新的發行版（Distribution)

## 2.1. Collects

| Layer name                                                   | Description                                   | Type | Repository                                          |
| :----------------------------------------------------------- | :-------------------------------------------- | :--- | :-------------------------------------------------- |
| [meta-openembedded](https://git.openembedded.org/meta-openembedded/) | Collection of layers for the OE-core universe | Base | https://git.openembedded.org/meta-openembedded/tree |
| [poky](https://git.yoctoproject.org/poky)                    | Poky Build Tool and Metadata                  | Base | https://git.yoctoproject.org/poky                   |

### 2.1.1. Poky

#### A. version

```bash
$ bitbake -e virtual/kernel | grep "^PN="
PN="linux-raspberrypi"

$ bitbake -e virtual/kernel | grep "^PV="
PV="5.15.56+gitAUTOINC+3b1dc2f1fc_a90998a3e5"

$ cat ./$PJ_YOCTO_LAYERS/poky/meta-poky/conf/distro/poky.conf | grep DISTRO_VERSION
#DISTRO_VERSION = "4.1+snapshot-${METADATA_REVISION}"
DISTRO_VERSION = "4.1"
SDK_VERSION = "${@d.getVar('DISTRO_VERSION').replace('snapshot-${METADATA_REVISION}', 'snapshot')}"
```

#### B. bitbake

> bitbake -c CMD

| CMD                | 說明                                                         |
| ------------------ | ------------------------------------------------------------ |
| `fetch`            | 下載原始碼（SRC_URI）                                        |
| `unpack`           | 解壓縮原始碼                                                 |
| `patch`            | 套用 patch                                                   |
| `configure`        | 執行 configure（如 autotools 或 kernel config）              |
| `compile`          | 編譯原始碼                                                   |
| `install`          | 安裝到 `WORKDIR/image`                                       |
| `package`          | 打包成 ipk/deb/rpm                                           |
| `deploy`           | 將成果放到 `DEPLOY_DIR`                                      |
| `cleansstate`      | **清除所有 build 成果與下載快取**                            |
| `clean`            | 清除所有 build 成果（保留下載）                              |
| `menuconfig`       | 針對 kernel 套件，進入圖形化 menuconfig                      |
| `savedefconfig`    | 將目前 kernel `.config` 儲存為 defconfig                     |
| `kernel_configme`  | 套用 `defconfig` 與 Yocto config fragment                    |
| `devshell`         | 開啟 shell，環境變數已設定好                                 |
| `listtasks`        | 列出此 recipe 所有支援的 task（**很有用！**）                |
| `populate_sysroot` | 安裝 headers、libs 到 sysroot                                |
| `populate_lic`     | 安裝 license 檔案                                            |
| `populate_sdk`     | 建立 cross compile SDK                                       |
| `populate_sdk_ext` | 建立 extensible SDK                                          |
| `build`            | 相當於正常 bitbake build 流程（compile+install+package+deploy） |
| `packagedata`      | 建立 recipe 的 .pkgedata                                     |
| `image`            | 產生最終映像檔（如 `.wic`、`.sdimg`）                        |
| `rootfs`           | 建立 rootfs                                                  |

## 2.2. [OpenEmbedded Layer Index](https://layers.openembedded.org/layerindex/)

| Layer name                                                   | Description                                            | Type          | Repository                                             |
| :----------------------------------------------------------- | :----------------------------------------------------- | :------------ | :----------------------------------------------------- |
| [meta-freescale](https://layers.openembedded.org/layerindex/branch/master/layer/meta-freescale/) | Freescale/NXP BSP layer                                | Machine (BSP) | https://github.com/Freescale/meta-freescale.git        |
| [meta-freescale-distro](https://layers.openembedded.org/layerindex/branch/master/layer/meta-freescale-distro/) | OpenEmbedded/Yocto layer for NXP's Distribution images | Distribution  | https://github.com/Freescale/meta-freescale-distro.git |
| [meta-imx-sdk](https://layers.openembedded.org/layerindex/branch/master/layer/meta-imx-sdk/) | Layer for NXP's i.MX Distribution images               | Distribution  | https://github.com/nxp-imx/meta-imx.git                |
| [meta-rauc](https://layers.openembedded.org/layerindex/branch/master/layer/meta-rauc/) | RAUC update handler support                            | Software      | https://github.com/rauc/meta-rauc.git                  |

# 3. Yocto Recipes

## 3.1. core-image-base


```bash
# check core-image-base exist
$ bitbake -s | grep core-image-base
core-image-base                                       :1.0-r0
$ find -name core-image-base*.bb
./layers-master/poky/meta/recipes-core/images/core-image-base.bb

$ bitbake -e core-image-base | grep ^SRC_URI=

$ bitbake -e core-image-base | grep ^S=
S="/work/codebase/RaspberryPi/Yocto/yocto-pi/builds/build-pi3-master/tmp/work/raspberrypi3-poky-linux-gnueabi/core-image-base/1.0-r0/core-image-base-1.0"

$ bitbake -c listtasks core-image-base
$ bitbake -c cleanall core-image-base
$ bitbake -c build core-image-base
```

## 3.2. Linux Kernel (linux-)

```bash
$ bitbake -s | grep linux-

# make menuconfig
$ bitbake -c menuconfig virtual/kernel
```
## 3.3. ?apache2

```json
"layers": [ "meta-openembedded/meta-webserver" ]
```

```bash
$ bitbake -s | grep apache2
```

## 3.4. avahi (mDNS)

```bash
$ bitbake -s | grep avahi
```

## 3.5. bluez5

```bash
$ bitbake -s | grep bluez5
```

## 3.6. Busybox - [yocto项目修改busybox](https://community.nxp.com/t5/i-MX-Processors/yocto项目修改busybox/m-p/1076952)

```bash
$ bitbake -s | grep busybox
```

## 3.7. dbus

```bash
$ bitbake -s | grep dbus
```

## 3.8. dropbear (SSH)

```bash
$ bitbake -s | grep dropbear
```

## 3.9. libwebsockets

```json
"layers": [ "meta-openembedded/meta-networking" ]
```

```bash
$ bitbake -s | grep libwebsockets
```

## 3.10. ?mosquitto

```json
"layers": [ "meta-openembedded/meta-networking" ]
```

```bash
$ bitbake -s | grep mosquitto
```

## 3.11. glib-2.0

```bash
$ bitbake -s | grep glib-2.0
```

## 3.12. systemd

```bash
$ bitbake -s | grep systemd
```

# 4. bb helper

## 4.1. bb.utils
> [layers/poky/bitbake/lib/bb/utils.py](layers/poky/bitbake/lib/bb/utils.py)

#### A. def contains(variable, checkvalues, truevalue, falsevalue, d):

```bb
"""Check if a variable contains all the values specified.

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'x11', '${X11DEPENDS}', '', d)}"

PACKAGECONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)}"

ENDIANNESS_GCC = "${@bb.utils.contains("LE_UBOOT_FOR_ARMBE_TARGET", "1", "-mlittle-endian", "", d)}"
ENDIANNESS_LD = "${@bb.utils.contains("LE_UBOOT_FOR_ARMBE_TARGET", "1", "-EL", "", d)}"

CFLAGS += "${@bb.utils.contains('SELECTED_OPTIMIZATION', '-Og', '-DXXH_NO_INLINE_HINTS', '', d)}"

```

#### B. def filter(variable, checkvalues, d):

```bb
"""Return all words in the variable that are present in the checkvalues.

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'x11', d)}"

```

## 4.2 Variables

>  [root](https://git.yoctoproject.org/poky/tree/)/[meta](https://git.yoctoproject.org/poky/tree/meta)/[conf](https://git.yoctoproject.org/poky/tree/meta/conf)/[bitbake.conf](https://git.yoctoproject.org/poky/tree/meta/conf/bitbake.conf)
>
> [Variables Glossary](https://docs.yoctoproject.org/ref-manual/variables.html#filesystem-layout)

### 4.2.1. Commons

> 列出常用

```bash
$ grep -rn "bindir" $PJ_YOCTO_LAYERS_DIR/poky/meta/conf
```

| 變數名稱        | 預設值            | 用途                          |
| --------------- | ----------------- | ----------------------------- |
| `bindir`        | `/usr/bin`        | 可執行程式的安裝位置          |
| `sbindir`       | `/usr/sbin`       | 系統工具                      |
| `base_bindir`   | `/bin`            | 基礎指令                      |
| `base_sbindir`  | `/sbin`           | 系統管理指令                  |
| `libdir`        | `/usr/lib`        | 程式庫                        |
| `includedir`    | `/usr/include`    | 標頭檔                        |
| `datadir`       | `/usr/share`      | 資料檔案                      |
| `sysconfdir`    | `/etc`            | 設定檔                        |
| `localstatedir` | `/var`            | 可變資料（log, spool, cache） |
| `docdir`        | `/usr/share/doc`  | 說明文件                      |
| `mandir`        | `/usr/share/man`  | man page                      |
| `infodir`       | `/usr/share/info` | info 文件                     |

### 4.2.2. Others

#### A. BB_ENV_EXTRAWHITE

```bb
ERROR: Variable BB_ENV_EXTRAWHITE has been renamed to BB_ENV_PASSTHROUGH_ADDITIONS
ERROR: Variable BB_ENV_EXTRAWHITE from the shell environment has been renamed to BB_ENV_PASSTHROUGH_ADDITIONS
ERROR: Exiting to allow enviroment variables to be corrected

```

#### B. [BB_ENV_PASSTHROUGH_ADDITIONS](https://docs.yoctoproject.org/bitbake/dev/bitbake-user-manual/bitbake-user-manual-ref-variables.html#term-BB_ENV_PASSTHROUGH_ADDITIONS)

```mermaid
flowchart LR
	ubuntu[ubuntu env]
	yocto[yocto env]

	ubuntu --> |BB_ENV_PASSTHROUGH_ADDITIONS| yocto

```

```bb
BB_ENV_PASSTHROUGH_ADDITIONS="ALL_PROXY BBPATH_EXTRA BB_LOGCONFIG BB_NO_NETWORK BB_NUMBER_THREADS BB_SETSCENE_ENFORCE BB_SRCREV_POLICY DISTRO FTPS_PROXY FTP_PROXY GIT_PROXY_COMMAND HTTPS_PROXY HTTP_PROXY MACHINE NO_PROXY PARALLEL_MAKE SCREENDIR SDKMACHINE SOCKS5_PASSWD SOCKS5_USER SSH_AGENT_PID SSH_AUTH_SOCK STAMPS_DIR TCLIBC TCMODE all_proxy ftp_proxy ftps_proxy http_proxy https_proxy no_proxy"

```

```bash
$ export BB_EXTRA_SRCREV="AUTOINC"
$ export BB_ENV_PASSTHROUGH_ADDITIONS="${BB_ENV_PASSTHROUGH_ADDITIONS} BB_EXTRA_SRCREV"

```

```bb
SRCREV = "${@bb.utils.contains('BB_EXTRA_SRCREV','AUTOINC','${AUTOREV}','${SRCREV_RELEASE}',d)}"

or

SRCREV = "${@'${AUTOREV}' if d.getVar('BB_EXTRA_SRCREV', 'AUTOINC') \
         else '${SRCREV_RELEASE}'}"
```

## 4.3. inherit

### 4.3.1. setuptools3

```bash
python3 setup.py build
python3 setup.py install
```

### 4.3.2. pypi

```bash
pip install
```



## 4.4. Documentation

>  [root](https://git.yoctoproject.org/poky/tree/)/[meta](https://git.yoctoproject.org/poky/tree/meta)/[conf](https://git.yoctoproject.org/poky/tree/meta/conf)/[documentation.conf](https://git.yoctoproject.org/poky/tree/meta/conf/documentation.conf)

# 5. Others

# Appendix

# I. Study

## I.1. [Yocto計劃](https://zh.m.wikipedia.org/zh-tw/Yocto計劃)

# II. Debug

## II.1. yocto 裏 BB_FETCH_PREMIRRORONLY  是什麼功用

> [ChatGPT]  yocto 裏 BB_FETCH_PREMIRRORONLY  是什麼功用
>
> 在 Yocto Project 中，`BB_FETCH_PREMIRRORONLY` 是一個 BitBake 變數，用於指定哪些軟體包（通常是源代碼 tarballs 或 Git 存儲庫）應該僅使用本地的預先鏡像（pre-mirrors）進行下載，而不是從遠程源進行下載。這對於加速 Yocto 構建過程並減少對外部網絡資源的依賴很有用。
>
> `BB_FETCH_PREMIRRORONLY` 的主要功能如下：
>
> 1. **本地預先鏡像：** Yocto 開發者可以設置本地預先鏡像，這是一個包含一組源代碼 tarballs 或 Git 存儲庫的本地資源。通常，這些資源是先從外部源（如官方網站或Git存儲庫）下載並儲存在本地預先鏡像中。
> 2. **提高下載效能：** 設置 `BB_FETCH_PREMIRRORONLY` 可以通知 BitBake 優先使用本地預先鏡像來下載源代碼。這有助於加速 Yocto 構建過程，因為本地下載通常比從遠程源下載更快。
> 3. **減少對外部網絡的依賴：** 通過僅使用本地預先鏡像，開發者可以減少對外部網絡資源的依賴。這在某些環境中很有用，特別是在網絡連接不穩定或有限的情況下。
>
> 使用 `BB_FETCH_PREMIRRORONLY` 變數時，您可以在 Yocto 的 `local.conf` 配置文件中設置它，如下所示：
>
> ```json
> BB_FETCH_PREMIRRORONLY = "1"
> ```
>
> 這將告訴 BitBake 優先使用本地預先鏡像，如果本地沒有所需的資源，則才會嘗試從遠程源下載。
>
> 請注意，為了正確使用 `BB_FETCH_PREMIRRORONLY`，您需要確保已設置本地預先鏡像，並在其中包含所需的源代碼 tarballs 或 Git 存儲庫。這通常需要事先下載和緩存這些資源。

## II.2. yocto 裏 BB_NO_NETWORK 是什麼功用

>[ChatGPT]  yocto 裏 BB_NO_NETWORK 是什麼功用
>
>在 Yocto Project 中，`BB_NO_NETWORK` 是一個 BitBake 變量，它用於指定是否禁用網絡訪問，特別是在構建嵌入式 Linux 映像時。這個變量的主要功能是允許您控制 BitBake 構建過程中的網絡連接，以適應不同的開發和部署場景。
>
>`BB_NO_NETWORK` 的主要功能如下：
>
>1. **禁用網絡訪問：** 將 `BB_NO_NETWORK` 設置為 "1" 可以告訴 BitBake 禁用網絡訪問。這意味著 BitBake 不會嘗試從互聯網上下載所需的源代碼、依賴項或鏡像。這對於在沒有互聯網連接的封閉環境中進行構建或在網絡不穩定的情況下進行構建非常有用。
>2. **離線構建：** 當您需要在沒有網絡連接的情況下進行 Yocto 構建時，可以設置 `BB_NO_NETWORK` 為 "1"。這樣，BitBake 將僅依賴於本地緩存、預先下載的源代碼和預先配置的依賴項。
>3. **加速構建：** 在某些情況下，開發人員可能希望通過禁用網絡訪問來加快構建過程。這是因為網絡下載操作可能會成為構建的瓶頸，禁用它們可以減少構建時間。
>4. **鏡像或本地存儲：** 在某些情況下，您可能已經準備好了所需的源代碼、鏡像或依賴項，並希望避免在每次構建時下載它們。設置 `BB_NO_NETWORK` 可以確保 BitBake 使用您提供的本地資源。
>
>要在 Yocto 構建中使用 `BB_NO_NETWORK`，您可以將以下內容添加到您的 `local.conf` 配置文件中：
>
>```json
>BB_NO_NETWORK = "1"
>```
>
>請注意，設置 `BB_NO_NETWORK` 為 "1" 可以在某些情況下加快構建，但也可能需要更多的手動管理和預先下載所需的資源。因此，根據您的特定需求和情況，謹慎使用這個選項。

## II.3. Previous bitbake instance shutting down?, waiting to retry...

```bash
$ find * -name bitbake.lock

$ rm builds/*/bitbake.lock
```

## II.4.  An error occurred during checking the C++ toolchain for '--std=gnu++20' support. Please use a g++ compiler that supports C++20 (e.g. g++ version 10 onwards).

> 發生在 ubuntu 20.04 

```bash
$ sudo apt update
$ sudo apt upgrade
$ sudo apt install g++-10

$ sudo update-alternatives --install /usr/bin/g++ g++ /usr/bin/g++-9 9
$ sudo update-alternatives --install /usr/bin/g++ g++ /usr/bin/g++-10 10
$ update-alternatives --list g++
$ sudo update-alternatives --config g++
There are 2 choices for the alternative g++ (providing /usr/bin/g++).

  Selection    Path             Priority   Status
------------------------------------------------------------
* 0            /usr/bin/g++-10   10        auto mode
  1            /usr/bin/g++-10   10        manual mode
  2            /usr/bin/g++-9    9         manual mode

Press <enter> to keep the current choice[*], or type selection number:
```

# III. Glossary

# IV. Tool Usage

# Author

> Created and designed by [Lanka Hsu](lankahsu@gmail.com).

# License

> [CrossCompilationX](https://github.com/lankahsu520/CrossCompilationX) is available under the BSD-3-Clause license. See the LICENSE file for more info.

