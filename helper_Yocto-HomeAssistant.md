# [Yocto](https://www.yoctoproject.org) [Home Assistant](https://www.home-assistant.io)

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

> Home Assistant 是一款開源的家庭自動化平台，強調本地控制與隱私保護。由全球熱衷創客與 DIY 愛好者所推動與維護，非常適合在 Raspberry Pi 或本地伺服器上執行。
>
> 它是一個輔助工具，是讓現有的環境下，整合成一個方便的 UI 環境，讓您能夠輕鬆地管理和控制各種智慧設備，讓它們可以協同作業。

> [ChatGPT] Home Assistant 是什麼
>
> Home Assistant 是一個開源的**家庭自動化平台**，用來整合並控制你家中所有的智慧裝置。它可以安裝在像是 Raspberry Pi 或其他電腦上，讓你把燈光、溫度、感測器、相機、安全系統等智慧設備統一管理，甚至可以設定自動化規則。
>
> ###  Home Assistant 的核心特色：
>
> 1. **完全本地執行（Local Control）**：
>    - 資料存在你自己的裝置，不必依賴雲端，保障隱私與安全。
> 2. **支援上千種設備與平台**：
>    - 包括 Google Nest、Philips Hue、Xiaomi、Zigbee/Z-Wave 裝置、智慧插座、感測器等。
>    - 透過整合（Integration）方式與設備連接。
> 3. **自動化（Automation）**：
>    - 你可以寫條件，例如「當日落時自動開燈」、「當門打開時發送通知」、「溫度高於30度時啟動冷氣」等。
> 4. **Web 界面控制**：
>    - 用瀏覽器或手機 App（iOS / Android）就可以控制家中所有裝置。
>    - 可以自訂儀表板（Dashboard）介面。
> 5. **強大的社群與開發資源**：
>    - 大量現成的元件（Component）、Blueprints（自動化模板）、HACS（社群商店）可用。

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
$ . confs/imx8mm-scarthgap-rauc-home2023.12.0.conf
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

### ~~2.4.2. rauc install~~

```bash
root@imx8mm-lpddr4-evk:~# rauc install /tmp/update-bundle-imx8mm-lpddr4-evk.raucb
```

# 2. [meta-homeassistant](https://github.com/meta-homeassistant/meta-homeassistant)

> 當初一開始接觸時是使用 2023.12.0，花了很長的時間，結果發現無法支援 HACS。
>
> 現在就得考慮是不是直接升級到最新版；而要升級至 2025.7.1時，yocto 的版本又要升級至 whinlatter (5.3)；之後又遇到  i.MX Repo Manifest 只支援到 walnascar (5.2)，解決了一部分，又有另一部分顯現，而結果就是環環相扣。
>

| Check | Yocto                              | python3-homeassistant | Date                | rev                                      |
| ----- | ---------------------------------- | --------------------- | ------------------- | ---------------------------------------- |
|       | whinlatter (5.3)                   | 2025.7.1              | 2025/07/16 17:16:37 | 2376bb467084e975fd00e54834fd196ab276f76e |
|       | whinlatter (5.3)                   | 2025.7.1              | 2025/07/12 23:40:57 | 083ed472adaae7d9c01480faabd30ed284d8ce24 |
|       | whinlatter (5.3)                   | 2025.7.1              | 2025/07/12 20:51:26 | 67e0a443839df4723b740dd968f649bb7395df90 |
|       | styhead (5.1),<br/>walnascar (5.2) | 2025.6.0b5            | 2025/06/28 05:11:08 | 65755926143661407c8686ec3ffc4129d504f562 |
|       | styhead (5.1),<br>walnascar (5.2)  | 2025.5.1              | 2025/05/27 04:59:45 | 4430d53483c50f335d291fdb8790bd11203ae048 |
| v     | styhead (5.1),<br/>walnascar (5.2) | 2025.4.0              | 2025/05/27 04:42:46 | 1b37b27b8aebee02bd5da8a43129661e5f364be3 |
|       | styhead (5.1),<br/>walnascar (5.2) | 2025.4.0              | 2025/04/03 16:20:30 | 434ccbe145be248176651b3c664bf769e2b91ca8 |
|       | styhead (5.1),<br/>walnascar (5.2) | 2025.3.4              | 2025/03/30 05:32:27 | 74d21d78880832ada2c315678004af4e79d72e44 |
|       | styhead (5.1)                      | 2025.1.0              | 2025/01/06 04:52:46 | 74d90690e18de29fd7a4042752debc4a7d9cdb2c |
| v     | nanbield (4.3),<br>scarthgap (5.0) | 2023.12.0             | 2024/03/20 05:10:32 | 5ee63318c53bec1bfc2e56221783c23c61b32a1e |
|       | nanbield (4.3)                     | 2023.12.0             | 2024/02/25 05:56:17 | 863a92980349b6a80d03843ba2958b4d1deb131a |

## 2.1. Add layer

> 因為 homeassistant 相依很多套件，這邊就不列出所有的。

### 2.1.1. update $PJ_COOKER_MENU

>  IMAGE_INSTALL: python3-homeassistant
>
>  LICENSE_FLAGS_ACCEPTED: commercial

```bash
$ echo $PJ_COOKER_MENU
imx8mm-evk-scarthgap-rauc-home2023.12.0-menu.json

# 更新 $PJ_COOKER_MENU
$ vi cooker-menu/$PJ_COOKER_MENU
  ...
  "sources": [
    {
      "url": "https://github.com/meta-homeassistant/meta-homeassistant",
      "branch": "main",
      "dir": "meta-homeassistant",
      "rev": "5ee63318c53bec1bfc2e56221783c23c61b32a1e"
    },
  ],
  "layers": [
    "meta-homeassistant",
  ],
  "builds": {
    "imx8mm-evk-scarthgap-home": {
      "local.conf": [
        "IMAGE_INSTALL:append = ' python3-homeassistant'",
        "LICENSE_FLAGS_ACCEPTED += 'commercial'"
      ]
    }
  }

# 更新完記得執行
$ cooker generate

$ cat $PJ_YOCTO_BUILD_DIR/conf/local.conf
$ cat $PJ_YOCTO_BUILD_DIR/conf/bblayers.conf

$ bitbake-layers show-recipes python3-homeassistant
NOTE: Starting bitbake server...
Loading cache: 100% |############################################################| Time: 0:00:01
Loaded 5813 entries from dependency cache.
Parsing recipes: 100% |##########################################################| Time: 0:00:00
Parsing of 3734 .bb files complete (3731 cached, 3 parsed). 5811 targets, 586 skipped, 3 masked, 0 errors.
WARNING: preferred version 4.18.imx+stable of xen not available
WARNING: versions of xen available: 4.17+stable 4.18+stable 4.19+git 4.19.0+stable
WARNING: preferred version 1.24.0.imx of gst-devtools not available
WARNING: versions of gst-devtools available: 1.22.12 1.22.5.imx
=== Matching recipes: ===
python3-homeassistant:
  meta-homeassistant   2023.12.0
```

> 因為 python3-ha-av dependency ffmpeg，但是 ffmpeg LICENSE = "commercial"

```bash
$ find -name ffmpeg*.bb
./layers-scarthgap/poky/meta/recipes-multimedia/ffmpeg/ffmpeg_6.1.1.bb
./layers-scarthgap/meta-freescale/recipes-multimedia/ffmpeg/ffmpeg_4.4.1.bb

$ cat $PJ_YOCTO_LAYERS_DIR/meta-freescale/recipes-multimedia/ffmpeg/ffmpeg_4.4.1.bb | grep LICENSE_FLAGS
LICENSE_FLAGS = "commercial"
```

## 2.2. Recipes

### 2.2.1. python3-homeassistant

```bash
$ oe-pkgdata-util list-pkg-files python3-homeassistant
```

```bash
$ bb-info python3-homeassistant
$ bitbake -c build python3-homeassistant
```

#### A. homeassistant.service

> HOMEASSISTANT_CONFIG_DIR : /var/lib/homeassistant

```bash
$ vi $PJ_YOCTO_LAYERS_DIR/meta-homeassistant/recipes-homeassistant/homeassistant/python3-homeassistant/homeassistant.service
[Unit]
Description=Home Assistant
After=network.target

[Service]
Type=simple
User=@HOMEASSISTANT_USER@
ExecStart=/usr/bin/hass --skip-pip -c "@HOMEASSISTANT_CONFIG_DIR@"
Restart=on-failure

[Install]
WantedBy=multi-user.target

$ vi builds-lnk/$PJ_YOCTO_BUILD-rootfs/usr/lib/systemd/system/homeassistant.service
```

### 2.2.2. python3-ha-av

```bash
$ oe-pkgdata-util list-pkg-files python3-ha-av
```

```bash
$ bb-info python3-ha-av
$ bitbake -c build python3-ha-av
```

### 2.2.3. python3-ha-ffmpeg

```bash
$ oe-pkgdata-util list-pkg-files python3-ha-ffmpeg
```

```bash
$ bb-info python3-ha-ffmpeg
$ bitbake -c build python3-ha-ffmpeg
```

## 2.3. Check Image

> 請先編譯出 image

```bash
# 編譯
$ make
# or
$ bitbake imx-image-core
```

```bash
$ cd-rootfs
$ find123 ffmpeg pyav hass haffmpeg
```

## 2.4. Showtime

> You should now be able to access Home Assistant via web browser usually under the address: 
>
> http://<ip>:8123
>
> 預設的 Port: 8123

> 因為本篇不是研究 homeassistant，而是讓 homeassistant 在 NXP 8MMINILPD4‑EVKB 上執行。

> http://192.168.31.62:8123

<img src="./images/Yocto-NXP-8MMINILPD4-EVKB-hass.png" alt="Yocto-NXP-8MMINILPD4-EVKB-hass" style="zoom:33%;" />

### 2.4.1. Change listen port

> 這邊是 run time 就進行修改
>
> change default:8123 -> 12345

> http://192.168.31.62:12345

```bash
root@imx8mm-lpddr4-evk:~# ps -aux | grep hass
homeass+     400 50.3 13.6 2709920 262844 ?      Ssl  02:20   0:32 python3 /usr/bin/hass --skip-pip -c /var/lib/homeassistant
root         600  0.0  0.0   3508  1280 ttymxc1  S+   02:21   0:00 grep hass

root@imx8mm-lpddr4-evk:~# ls -al /var/lib/homeassistant
total 1008
drwxr-xr-x  7 homeassistant homeassistant   4096 Jul 14 02:20 .
drwxr-xr-x 15 root          root            4096 Feb 27  2024 ..
-rw-r--r--  1 homeassistant homeassistant      9 Feb 27  2024 .HA_VERSION
drwxr-xr-x  2 homeassistant homeassistant   4096 Jul 11 08:10 .cloud
drwxr-xr-x  2 homeassistant homeassistant   4096 Jul 14 02:20 .storage
-rw-r--r--  1 homeassistant homeassistant      2 Feb 27  2024 automations.yaml
drwxr-xr-x  4 homeassistant homeassistant   4096 Feb 27  2024 blueprints
-rw-r--r--  1 homeassistant homeassistant    295 Jul 14 02:20 configuration.yaml
drwxr-xr-x  2 homeassistant homeassistant   4096 Feb 27  2024 deps
-rw-r--r--  1 homeassistant homeassistant  18980 Jul 14 02:21 home-assistant.log
-rw-r--r--  1 homeassistant homeassistant  42081 Jul 14 02:19 home-assistant.log.1
-rw-r--r--  1 homeassistant homeassistant      0 Jul 14 02:20 home-assistant.log.fault
-rw-r--r--  1 homeassistant homeassistant 679936 Jul 14 02:20 home-assistant_v2.db
-rw-r--r--  1 homeassistant homeassistant  32768 Jul 14 02:21 home-assistant_v2.db-shm
-rw-r--r--  1 homeassistant homeassistant 206032 Jul 14 02:21 home-assistant_v2.db-wal
-rw-r--r--  1 homeassistant homeassistant      0 Feb 27  2024 scenes.yaml
-rw-r--r--  1 homeassistant homeassistant      0 Feb 27  2024 scripts.yaml
-rw-r--r--  1 homeassistant homeassistant    161 Feb 27  2024 secrets.yaml
drwxr-xr-x  2 homeassistant homeassistant   4096 Feb 27  2024 tts

root@imx8mm-lpddr4-evk:~# vi /var/lib/homeassistant/configuration.yaml
# 新增下面的設定
http:
  server_port: 12345

root@imx8mm-lpddr4-evk:/# systemctl restart homeassistant.service
# or
root@imx8mm-lpddr4-evk:~# reboot
```

## 2.5. Debug

> 比較有無 meta-homeassistant 後的狀況，方便評估是否

### 2.5.1. homeassistant.service

```bash
root@imx8mm-lpddr4-evk:/# ps -aux | grep home
homeass+     429  3.4 13.8 2771104 266664 ?      Ssl  07:38   0:46 python3 /usr/bin/hass --skip-pip -c /var/lib/homeassistant
root         657  0.0  0.0   3508  1280 ttymxc1  S+   08:00   0:00 grep home

root@imx8mm-lpddr4-evk:~# vi /usr/lib/systemd/system/homeassistant.service
[Unit]
Description=Home Assistant
After=network.target

[Service]
Type=simple
User=homeassistant
ExecStart=/usr/bin/hass --skip-pip -c "/var/lib/homeassistant"
Restart=on-failure

[Install]
WantedBy=multi-user.target

root@imx8mm-lpddr4-evk:~# systemctl status homeassistant.service
root@imx8mm-lpddr4-evk:~# systemctl stop homeassistant.service
root@imx8mm-lpddr4-evk:~# systemctl start homeassistant.service
```

### 2.5.2. /var/lib/homeassistant

```bash
root@imx8mm-lpddr4-evk:/var/lib/homeassistant# ls -al
total 1432
drwxr-xr-x  6 homeassistant homeassistant    4096 Jul 23 02:52 .
drwxr-xr-x 15 root          root             4096 Jul 23 02:51 ..
-rw-r--r--  1 homeassistant homeassistant       9 Feb 27  2024 .HA_VERSION
drwxr-xr-x  2 homeassistant homeassistant    4096 Jul 23 02:51 .cloud
drwxr-xr-x  2 homeassistant homeassistant    4096 Jul 23 03:22 .storage
-rw-r--r--  1 root          root                2 Mar  9  2018 automations.yaml
drwxr-xr-x  4 homeassistant homeassistant    4096 Jul 23 02:52 blueprints
-rw-r--r--  1 root          root              294 Mar  9  2018 configuration.yaml
-rw-r--r--  1 homeassistant homeassistant     796 Jul 23 03:08 home-assistant.log
-rw-r--r--  1 homeassistant homeassistant       0 Feb 27  2024 home-assistant.log.1
-rw-r--r--  1 homeassistant homeassistant       0 Feb 27  2024 home-assistant.log.fault
-rw-r--r--  1 homeassistant homeassistant    4096 Jul 23 02:51 home-assistant_v2.db
-rw-r--r--  1 homeassistant homeassistant   32768 Jul 23 03:31 home-assistant_v2.db-shm
-rw-r--r--  1 homeassistant homeassistant 1384352 Jul 23 03:31 home-assistant_v2.db-wal
-rw-r--r--  1 root          root                0 Mar  9  2018 scenes.yaml
-rw-r--r--  1 root          root                0 Mar  9  2018 scripts.yaml
-rw-r--r--  1 root          root              161 Mar  9  2018 secrets.yaml
drwxr-xr-x  2 homeassistant homeassistant    4096 Jul 23 02:52 tts
```

# 3. meta-homeassistant-plus

> 這邊要先有一個重要的認知，homeassistant 算是整合各家的 IoT 系統，當要整入 embedded 時，就有可能會有`缺失`，而這`缺失`是不是剛好是自己需要的，而之後將是個很大的考驗。
>
> 或許聰明的人就會說，「pip 安裝就好了」、「rpm 安裝也行」、「最慘的用setup 」。
>
> 問題是不是這樣，這邊不多解釋，但是 embedded engineer 必須了解。

## 3.1. create-layer

```bash
$ echo $PJ_YOCTO_LAYERS_DIR
/yocto/cookerX-scarthgap/layers-scarthgap
$ cd $PJ_YOCTO_LAYERS_DIR
$ bitbake-layers create-layer meta-homeassistant-plus
NOTE: Starting bitbake server...
Add your new layer with 'bitbake-layers add-layer meta-homeassistant-plus'

$ mv meta-homeassistant-plus/recipes-example meta-homeassistant-plus/recipes-homeassistant-plus

$ cd meta-homeassistant-plus/recipes-homeassistant-plus
$ mv example homeassistant-plus

$ cd homeassistant-plus
$ mv example_0.1.bb homeassistant-plus_0.1.bb

$ echo $PJ_COOKER_MENU
imx8mm-evk-scarthgap-home-menu.json

$ cd-root; vi ./cooker-menu/$PJ_COOKER_MENU
# add "meta-homeassistant-plus" into "layers"
$ cooker generate
$ bitbake-layers show-layers | grep meta-homeassistant-plus
$ cat $PJ_YOCTO_BUILD_DIR/conf/bblayers.conf | grep meta-homeassistant-plus

# check homeassistant-plus
$ bitbake -s | grep homeassistant-plus
homeassistant-plus                                    :0.1-r0
```

### 3.1.1. update $PJ_COOKER_MENU

> 其本上不用更動

### 3.1.2 show-recipes

```bash
$ bitbake-layers show-recipes homeassistant-plus
NOTE: Starting bitbake server...
Loading cache: 100% |############################################################| Time: 0:00:01
Loaded 5810 entries from dependency cache.
Parsing recipes: 100% |##########################################################| Time: 0:00:00
Parsing of 3735 .bb files complete (3733 cached, 2 parsed). 5812 targets, 586 skipped, 3 masked, 0 errors.
WARNING: preferred version 4.18.imx+stable of xen not available
WARNING: versions of xen available: 4.17+stable 4.18+stable 4.19+git 4.19.0+stable
WARNING: preferred version 1.24.0.imx of gst-devtools not available
WARNING: versions of gst-devtools available: 1.22.12 1.22.5.imx
=== Matching recipes: ===
homeassistant-plus:
  meta-homeassistant-plus 0.1
```

### 3.1.3. python3-homeassistant_%.bbappend

> 儘量不要去更改 python3-homeassistant.bb，而使用 *.bbappend

```bash
$ cd $PJ_YOCTO_LAYERS_DIR
$ vi $PJ_YOCTO_LAYERS_DIR/meta-homeassistant-plus/recipes-homeassistant-plus/homeassistant-plus/python3-homeassistant_%.bbappend

$ bitbake-layers show-appends | grep homeassistant

$ bitbake -c build python3-homeassistant
```

### 3.1.4. Files

```bash
$ tree -L 4 ${PJ_YOCTO_LAYERS_DIR}/meta-homeassistant-plus/recipes-homeassistant-plus/homeassistant-plus
/yocto/cookerX-scarthgap/layers-scarthgap/meta-homeassistant-plus/recipes-homeassistant-plus/homeassistant-plus
├── files
│   ├── automations.yaml
│   ├── configuration.yaml
│   ├── scenes.yaml
│   ├── scripts.yaml
│   └── secrets.yaml
├── homeassistant-plus_0.1.bb
└── python3-homeassistant_%.bbappend

1 directory, 7 files
```

#### A. configuration.yaml

> 這邊是 compile time 就進行修改
>
> change default:8123 -> 12345

> http://192.168.31.62:12345

```bash

# Loads default set of integrations. Do not remove.
default_config:

# Load frontend themes from the themes folder
frontend:
  themes: !include_dir_merge_named themes

automation: !include automations.yaml
script: !include scripts.yaml
scene: !include scenes.yaml

http:
  server_port: 12345

```

## 3.2. Add recipes - ONVIF

#### onvif-zeep

> pypi: [onvif-zeep 0.2.12](https://pypi.org/project/onvif-zeep)
>
> ONVIF Client Implementation in Python

```bash
$ bitbake -s | grep onvif-zeep
# yocto 未內建 python3-onvif-zeep
$ bb-info python3-onvif-zeep
$ bitbake -c build python3-onvif-zeep
```

#### onvif-zeep-async

> pypi: [onvif-zeep-async 4.0.1](https://pypi.org/project/onvif-zeep-async)
>
> ONVIF Client Implementation in Python 3

```bash
$ bitbake -s | onvif-zeep-async
# yocto 未內建 python3-onvif-zeep-async，這邊採用舊版 3.1.13
$ bb-info python3-onvif-zeep-async
$ bitbake -c build python3-onvif-zeep-async
```

#### requests-file

> pypi: [requests-file 2.1.0](https://pypi.org/project/requests-file)
>
> Requests-File is a transport adapter for use with the [Requests](https://github.com/kennethreitz/requests) Python library to allow local filesystem access via file:// URLs.

```bash
$ bitbake -s | grep requests-file
# yocto 已經內建 python3-requests-file
$ bb-info python3-requests-file
$ bitbake -c build python3-requests-file
```

#### wsdiscovery

> pypi: [WSDiscovery 2.1.2](https://pypi.org/project/WSDiscovery)
>
> This is WS-Discovery implementation for Python 3. It allows to both discover services and publish discoverable services. For Python 2 support, use the latest 1.x version of this package.

```bash
$ bitbake -s | grep wsdiscovery
# yocto 未內建 python3-onvif-zeep-async
$ bb-info python3-wsdiscovery
$ bitbake -c build python3-wsdiscovery
```

#### zeep

> pypi: [zeep 4.3.1](https://pypi.org/project/zeep)
>
> a modern parsing library

```bash
$ bitbake -s | grep zeep
# yocto 未內建 python3-zeep
$ bb-info python3-zeep
$ bitbake -c build python3-zeep
```

## 3.3. Add recipes - Homekit

#### aiohomekit

> pypi: [aiohomekit 3.2.15](https://pypi.org/project/aiohomekit/)
>
> This library implements the HomeKit protocol for controlling Homekit accessories using asyncio.
>
> It's primary use is for with Home Assistant. We target the same versions of python as them and try to follow their code standards.
>
> At the moment we don't offer any API guarantees. API stability and documentation will happen after we are happy with how things are working within Home Assistant.

```bash
$ bitbake -s | grep aiohomekit
# yocto 未內建 python3-aiohomekit，這邊採用舊版 3.2.7
$ bb-info python3-aiohomekit
# 清除 bitbake cache
$ bitbake -p -f
$ bitbake -c cleansstate python3-aiohomekit
$ bitbake -c cleanall python3-aiohomekit
$ bitbake -c build python3-aiohomekit
```

#### commentjson

> pypi: [commentjson 0.9.0](https://pypi.org/project/commentjson)
>
> commentjson (Comment JSON) is a Python package that helps you create JSON files with Python and JavaScript style inline comments. Its API is very similar to the Python standard library’s [json](http://docs.python.org/2/library/json.html) module.

```bash
$ bitbake -s | grep commentjson
# yocto 未內建 python3-commentjson
$ bb-info python3-commentjson
$ bitbake -c build python3-commentjson
```

#### hap-python

> pypi: [HAP-python 4.9.2](https://pypi.org/project/HAP-python)
>
> HomeKit Accessory Protocol implementation in python 3. With this project, you can integrate your own smart devices and add them to your iOS Home app. Since Siri is integrated with the Home app, you can start voice-control your accessories right away.

```bash
$ bitbake -s | grep hap
# yocto 未內建 python3-hap-python
$ bb-info python3-hap-python
# 清除 bitbake cache
$ bitbake -p -f
$ bitbake -c cleansstate python3-hap-python
$ bitbake -c cleanall python3-hap-python
$ bitbake -c build python3-hap-python
```

#### lark

> pypi: [lark 1.2.2](https://pypi.org/project/lark)
>
> Lark is a modern general-purpose parsing library for Python. With Lark, you can parse any context-free grammar, efficiently, with very little code. 

```bash
$ bitbake -s | grep lark
# yocto 未內建 python3-lark
$ bb-info python3-lark
$ bitbake -c build python3-lark
```

## 3.4. Add recipes - Apple TV

#### chacha20poly1305

> pypi: [chacha20poly1305 0.0.3](https://pypi.org/project/chacha20poly1305)
>
> Simple pure-python chacha20-poly1305 implementation based on [tlslite-ng](https://github.com/tomato42/tlslite-ng) code. Designed to be compatible with Cryptography API.

```bash
$ bitbake -s | grep chacha20poly1305
# yocto 未內建 python3-chacha20poly1305
$ bb-info python3-chacha20poly1305
$ bitbake -c build python3-chacha20poly1305
```

#### chacha20poly1305_reuseable

> pypi: [chacha20poly1305-reuseable 0.13.2](https://pypi.org/project/chacha20poly1305-reuseable/)
>
> ChaCha20Poly1305 that is reuseable for asyncio

```bash
$ bitbake -s | grep chacha20poly1305
# yocto 未內建 python3-chacha20poly1305_reuseable
$ bb-info python3-chacha20poly1305-reuseable
$ bitbake -c build python3-chacha20poly1305-reuseable
```

#### filetype

> pypi: [filetype 1.2.0](https://pypi.org/project/filetype/)
>
> Small and dependency free [Python](http://python.org/) package to infer file type and MIME type checking the [magic numbers](https://en.wikipedia.org/wiki/Magic_number_(programming)#Magic_numbers_in_files) signature of a file or buffer.
>
> This is a Python port from [filetype](https://github.com/h2non/filetype) Go package.

```bash
$ bitbake -s | grep filetype
# yocto 未內建 python3-filetype
$ bb-info python3-filetype
$ bitbake -c build python3-filetype
```

#### mediafile

> pypi: [mediafile 0.13.0](https://pypi.org/project/mediafile/)
>
> MediaFile is a simple interface to the metadata tags for many audio file formats. It wraps [Mutagen](https://github.com/quodlibet/mutagen), a high-quality library for low-level tag manipulation, with a high-level, format-independent interface for a common set of tags.

```bash
$ bitbake -s | grep mediafile
# yocto 未內建 python3-mediafile
$ bb-info python3-mediafile
$ bitbake -c build python3-mediafile
```

#### pyatv

> pypi: [pyatv 0.16.1](https://pypi.org/project/pyatv/)
>
> This is an asyncio python library for interacting with Apple TV and AirPlay devices. It mainly targets Apple TVs (all generations, **including tvOS 15 and later**), but also supports audio streaming via AirPlay to receivers like the HomePod, AirPort Express and third-party speakers. It can act as remote control to the Music app/iTunes in macOS.

```bash
$ bitbake -s | grep pyatv
# yocto 未內建 python3-pyatv，這邊採用舊版 0.14.5
$ bb-info python3-pyatv
# 清除 bitbake cache
$ bitbake -p -f
$ bitbake -c cleansstate python3-pyatv
$ bitbake -c cleanall python3-pyatv
$ bitbake -c build python3-pyatv
```

#### srptools

> pypi: [srptools 1.0.1](https://pypi.org/project/srptools/)
>
> *Tools to implement Secure Remote Password (SRP) authentication*
>
> SRP is a secure password-based authentication and key-exchange protocol - a password-authenticated key agreement protocol (PAKE).
>
> This package contains protocol implementation for Python 2 and 3.
>
> You may import it into you applications and use its API or you may use srptools command-line utility (CLI):

```bash
$ bitbake -s | grep srptools
# yocto 未內建 python3-tuya-iot-py-sdk
$ bb-info python3-srptools
$ bitbake -c build python3-srptools
```

## 3.5. Add recipes - synologydsm

#### py-synologydsm-api

> pypi: [py-synologydsm-api 2.7.3](https://pypi.org/project/py-synologydsm-api/)
>
> Python API for communication with Synology DSM

```bash
$ bitbake -s | grep py-synologydsm-api
# yocto 未內建 python3-py-synologydsm-api
$ bb-info python3-py-synologydsm-api
$ bitbake -c build python3-py-synologydsm-api
```

## 3.6. Add recipes - tuya_iot

> [tuya-smart-life](https://github.com/tuya/tuya-smart-life)
>
> This project has now officially been integrated into the Home Assistant official project core repository, corresponding to version 2024.2. This project will no longer continue to iterate. Subsequent iterations and support will be carried out under the Home Assistant official project.

> [tuya-home-assistant](https://github.com/tuya/tuya-home-assistant)
>
> Tuya has developed a new HA integration called [Smart Life](https://github.com/tuya/tuya-smart-life), available for free to developers. Currently in beta testing, it eliminates the need to register a cloud development project on Tuya IoT platform and extend the Tuya cloud development IoT Core Service resources when expired. This significantly lowers the access barrier and enhances user experience.

#### tuya-iot-py-sdk

> pypi: [tuya-iot-py-sdk 0.6.6](https://pypi.org/project/tuya-iot-py-sdk)
>
> A Python sdk for Tuya Open API, which provides IoT capabilities, maintained by Tuya officialA Python sdk for Tuya Open API, which provides IoT capabilities, maintained by Tuya official

```bash
$ bitbake -s | grep tuya
# yocto 未內建 python3-tuya-iot-py-sdk
$ bb-info python3-tuya-iot-py-sdk
# 清除 bitbake cache
$ bitbake -p -f
$ bitbake -c cleansstate python3-tuya-iot-py-sdk
$ bitbake -c cleanall python3-tuya-iot-py-sdk
$ bitbake -c build python3-tuya-iot-py-sdk
```

#### tuya-device-sharing-sdk

> pypi: [tuya-device-sharing-sdk 0.2.1](https://pypi.org/project/tuya-device-sharing-sdk/) (新版)
>
> A Python sdk for Tuya Open API, which provides basic IoT capabilities like device management capabilities, helping you create IoT solutions. With diversified devices and industries, Tuya IoT Development Platform opens basic IoT capabilities like device management, AI scenarios, and data analytics services, as well as industry capabilities, helping you create IoT solutions.

```bash
$ bitbake -s | grep tuya
# yocto 未內建 python3-tuya-iot-py-sdk
$ bb-info python3-tuya-device-sharing-sdk
# 清除 bitbake cache
$ bitbake -p -f
$ bitbake -c cleansstate python3-tuya-device-sharing-sdk
$ bitbake -c cleanall python3-tuya-device-sharing-sdk
$ bitbake -c build python3-tuya-device-sharing-sdk
```

```bash
$ devtool add python3-tuya-device-sharing-sdk \
  https://files.pythonhosted.org/packages/b6/95/21737fb84c23571694a41518eef425d7a31d7d4e179be0927597754d713f/tuya-device-sharing-sdk-0.2.1.tar.gz
$ ll ${PJ_YOCTO_BUILD_DIR}/workspace/recipes/python3-tuya-device-sharing-sdk/python3-tuya-device-sharing-sdk_0.2.1.bb
-rw-rw-r-- 1 lanka lanka 1306 Aug 11 11:52 ./builds/build-imx8mm-evk-walnascar-rauc-home/workspace/recipes/python3-tuya-device-sharing-sdk/python3-tuya-device-sharing-sdk_0.2.1.bb
.bb

$ devtool build python3-tuya-device-sharing-sdk
$ devtool reset python3-tuya-device-sharing-sdk
```

## 3.7. Add recipes - pysensibo

#### miniaudio

> pypi: [miniaudio 1.61](https://pypi.org/project/miniaudio/)
>
> Multiplatform audio playback, recording, decoding and sample format conversion for Linux (including Raspberri Pi), Windows, Mac and others.

```bash
$ bitbake -s | grep miniaudio
# yocto 未內建 python3-miniaudio
$ bb-info python3-miniaudio
# 清除 bitbake cache
$ bitbake -p -f
$ bitbake -c cleansstate python3-miniaudio
$ bitbake -c cleanall python3-miniaudio
$ bitbake -c build python3-miniaudio
```

#### pysensibo

> pypi: [pysensibo 1.2.1](https://pypi.org/project/pysensibo/)
>
> asyncio-friendly python API for Sensibo ([https://sensibo.com](https://sensibo.com/)). Supported on Python 3.11+

```bash
$ bitbake -s | grep sensibo
# yocto 未內建 python3-pysensibo
$ bb-info python3-pysensibo
# 清除 bitbake cache
$ bitbake -p -f
$ bitbake -c cleansstate python3-pysensibo
$ bitbake -c cleanall python3-pysensibo
$ bitbake -c build python3-pysensibo
```

## 3.8. Add recipes - python_otbr_api

#### bitstruct

> pypi: [bitstruct 8.21.0](https://pypi.org/project/bitstruct/)
>
> This module is intended to have a similar interface as the python struct module, but working on bits instead of primitive data types (char, int, …).

```bash
$ bitbake -s | grep bitstruct
# yocto 已經內建 python3-bitstruct
$ bb-info python3-bitstruct
$ bitbake -c build python3-bitstruct
```

#### python_otbr_api

> pypi: [python-otbr-api 2.6.0](https://pypi.org/project/python-otbr-api/)
>
> Python package to interact with an OTBR via its REST API

```bash
$ bitbake -s | grep otbr
# yocto 未內建 python3-otbr-api
# 因為採用 inherit pypi，檔案名就只能 python3-python-otbr-api
$ bb-info python3-python-otbr-api
# 清除 bitbake cache
$ bitbake -p -f
$ bitbake -c cleansstate python3-python-otbr-api
$ bitbake -c cleanall python3-python-otbr-api
$ bitbake -c build python3-python-otbr-api
```

## 3.9. Add recipes - No module named 'xxxx'

```bash
# 查看是否已經安裝至 yocto-rootfs 
$ cd-rootfs
$ pwd
/yocto/cookerX-scarthgap/builds-lnk/imx8mm-evk-scarthgap-home-rootfs

$ find123 pyasn1* pydantic* bitstruct* python_otbr_api* miniaudio* pysensibo* tuya_iot* srptools* chacha20poly1305* pyatv* mediafile* filetype* hap-python* aiohomekit* synology_dsm* commentjson* lark* zeep* onvif_zeep* onvif_zeep_async* requests_file* wsdiscovery*
```

```bash
# 可以進到 NXP 板子裏直接查看 log
root@imx8mm-lpddr4-evk:/# cat /var/lib/homeassistant/home-assistant.log
```

#### pyasn1

> pypi: [pyasn1 0.6.1](https://pypi.org/project/pyasn1/)
>
> This is a free and open source implementation of ASN.1 types and codecs as a Python package. It has been first written to support particular protocol (SNMP) but then generalized to be suitable for a wide range of protocols based on [ASN.1 specification](https://www.itu.int/rec/dologin_pub.asp?lang=e&id=T-REC-X.208-198811-W!!PDF-E&type=items).

```bash
$ bitbake -s | grep pyasn1
# yocto 已經內建 python3-pyasn1
$ bb-info python3-pyasn1
$ bitbake -c build python3-pyasn1
```

#### pydantic

> pypi: [pydantic 2.11.7](https://pypi.org/project/pydantic/)
>
> Data validation using Python type hints.
>
> Fast and extensible, Pydantic plays nicely with your linters/IDE/brain. Define how data should be in pure, canonical Python 3.9+; validate it with Pydantic.

```bash
$ bitbake -s | grep pydantic
# yocto 已經內建 python3-pydantic
$ bb-info python3-pydantic
$ bitbake -c build python3-pydantic
```

#### pytest-sugar

> pypi: [pytest-sugar 1.0.0](https://pypi.org/project/pytest-sugar)
>
> This plugin extends [pytest](http://pytest.org/) by showing failures and errors instantly, adding a progress bar, improving the test results, and making the output look better.

```bash
$ bitbake -s | grep pytest-sugar
# yocto 未內建 python3-pytest-sugar
$ bb-info python3-pytest-sugar
$ bitbake -c build python3-pytest-sugar
```

# Appendix

# I. Study

# II. Debug

## II.1. linux (with RAUC)

> 同樣使用 FSL i.MX8MM EVK board，在 RAUC 之上加入 Home Assistant，查看系統的變化 。
>
> 畢竟各家公司在開發自家產品時，參照公板後進行客製化，就可以在硬體評估時判斷是否可行。

#### A. CPU

> pass

#### B. RAM

```bash
root@imx8mm-lpddr4-evk:~# free -h
               total        used        free      shared  buff/cache   available
Mem:           1.8Gi       567Mi       1.0Gi       9.0Mi       353Mi       1.3Gi
Swap:             0B          0B          0B

root@imx8mm-lpddr4-evk:~# cat /proc/meminfo
MemTotal:        1925928 kB
MemFree:         1090408 kB
MemAvailable:    1345220 kB
Buffers:           30456 kB
Cached:           301328 kB
SwapCached:            0 kB
Active:            57492 kB
Inactive:         554620 kB
Active(anon):        672 kB
Inactive(anon):   288884 kB
Active(file):      56820 kB
Inactive(file):   265736 kB
Unevictable:           0 kB
Mlocked:               0 kB
SwapTotal:             0 kB
SwapFree:              0 kB
Dirty:                 0 kB
Writeback:             0 kB
AnonPages:        280364 kB
Mapped:            89060 kB
Shmem:              9212 kB
KReclaimable:      29768 kB
Slab:              59044 kB
SReclaimable:      29768 kB
SUnreclaim:        29276 kB
KernelStack:        2640 kB
PageTables:         2608 kB
SecPageTables:         0 kB
NFS_Unstable:          0 kB
Bounce:                0 kB
WritebackTmp:          0 kB
CommitLimit:      962964 kB
Committed_AS:     593776 kB
VmallocTotal:   133141626880 kB
VmallocUsed:        9452 kB
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

```bash
root@imx8mm-lpddr4-evk:~# df -h
Filesystem      Size  Used Avail Use% Mounted on
/dev/root       2.3G  1.8G  374M  83% /
devtmpfs        619M  4.0K  619M   1% /dev
tmpfs           941M     0  941M   0% /dev/shm
tmpfs           377M  9.0M  368M   3% /run
tmpfs           941M  8.0K  941M   1% /tmp
tmpfs           941M   12K  941M   1% /var/volatile
/dev/mmcblk2p4  3.8G  1.1M  3.6G   1% /data
/dev/mmcblk2p2  1.4G 1003M  283M  79% /run/media/mmcblk2p2
/dev/mmcblk2p1  333M   37M  297M  11% /run/media/boot-mmcblk2p1
tmpfs           189M  4.0K  189M   1% /run/user/0

root@imx8mm-lpddr4-evk:~#  mount | grep '^/dev'
/dev/mmcblk2p3 on / type ext4 (rw,relatime)
/dev/mmcblk2p4 on /data type ext4 (rw,relatime)
/dev/mmcblk2p2 on /run/media/mmcblk2p2 type ext4 (rw,relatime)
/dev/mmcblk2p1 on /run/media/boot-mmcblk2p1 type vfat (rw,relatime,gid=6,fmask=0007,dmask=0007,allow_utime=0020,codepage=437,iocharset=iso8859-1,shortname=mixed,errors=remount-ro)

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
[    0.000000] Kernel command line: console=ttymxc1,115200 root=/dev/mmcblk2p3 rootwait rw rauc.slot=B
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
[    0.000000] sched_clock: 56 bits at 8MHz, resolution 125ns, wraps every 2199023255500ns
[    0.000444] Console: colour dummy device 80x25
[    0.000510] Calibrating delay loop (skipped), value calculated using timer frequency.. 16.00 BogoMIPS (lpj=32000)
[    0.000521] pid_max: default: 32768 minimum: 301
[    0.000587] LSM: initializing lsm=capability,integrity
[    0.000683] Mount-cache hash table entries: 4096 (order: 3, 32768 bytes, linear)
[    0.000695] Mountpoint-cache hash table entries: 4096 (order: 3, 32768 bytes, linear)
[    0.002214] RCU Tasks: Setting shift to 2 and lim to 1 rcu_task_cb_adjust=1.
[    0.002280] RCU Tasks Trace: Setting shift to 2 and lim to 1 rcu_task_cb_adjust=1.
[    0.002459] rcu: Hierarchical SRCU implementation.
[    0.002463] rcu:     Max phase no-delay instances is 1000.
[    0.003650] EFI services will not be available.
[    0.003856] smp: Bringing up secondary CPUs ...
[    0.004395] Detected VIPT I-cache on CPU1
[    0.004460] GICv3: CPU1: found redistributor 1 region 0:0x00000000388a0000
[    0.004504] CPU1: Booted secondary processor 0x0000000001 [0x410fd034]
[    0.005035] Detected VIPT I-cache on CPU2
[    0.005078] GICv3: CPU2: found redistributor 2 region 0:0x00000000388c0000
[    0.005102] CPU2: Booted secondary processor 0x0000000002 [0x410fd034]
[    0.005575] Detected VIPT I-cache on CPU3
[    0.005617] GICv3: CPU3: found redistributor 3 region 0:0x00000000388e0000
[    0.005638] CPU3: Booted secondary processor 0x0000000003 [0x410fd034]
[    0.005705] smp: Brought up 1 node, 4 CPUs
[    0.005711] SMP: Total of 4 processors activated.
[    0.005716] CPU features: detected: 32-bit EL0 Support
[    0.005718] CPU features: detected: 32-bit EL1 Support
[    0.005723] CPU features: detected: CRC32 instructions
[    0.005788] CPU: All CPU(s) started at EL2
[    0.005811] alternatives: applying system-wide alternatives
[    0.007667] devtmpfs: initialized
[    0.015020] clocksource: jiffies: mask: 0xffffffff max_cycles: 0xffffffff, max_idle_ns: 7645041785100000 ns
[    0.015044] futex hash table entries: 1024 (order: 4, 65536 bytes, linear)
[    0.033671] pinctrl core: initialized pinctrl subsystem
[    0.035749] DMI not present or invalid.
[    0.036393] NET: Registered PF_NETLINK/PF_ROUTE protocol family
[    0.037337] DMA: preallocated 256 KiB GFP_KERNEL pool for atomic allocations
[    0.037437] DMA: preallocated 256 KiB GFP_KERNEL|GFP_DMA pool for atomic allocations
[    0.037554] DMA: preallocated 256 KiB GFP_KERNEL|GFP_DMA32 pool for atomic allocations
[    0.037615] audit: initializing netlink subsys (disabled)
[    0.037780] audit: type=2000 audit(0.036:1): state=initialized audit_enabled=0 res=1
[    0.038290] thermal_sys: Registered thermal governor 'step_wise'
[    0.038294] thermal_sys: Registered thermal governor 'power_allocator'
[    0.038330] cpuidle: using governor menu
[    0.038546] hw-breakpoint: found 6 breakpoint and 4 watchpoint registers.
[    0.038631] ASID allocator initialised with 32768 entries
[    0.039569] Serial: AMBA PL011 UART driver
[    0.039635] imx mu driver is registered.
[    0.039655] imx rpmsg driver is registered.
[    0.046281] platform soc@0: Fixed dependency cycle(s) with /soc@0/bus@30000000/efuse@30350000/unique-id@4
[    0.049930] imx8mm-pinctrl 30330000.pinctrl: initialized IMX pinctrl driver
[    0.050596] platform 30350000.efuse: Fixed dependency cycle(s) with /soc@0/bus@30000000/clock-controller@30380000
[    0.051781] platform 30350000.efuse: Fixed dependency cycle(s) with /soc@0/bus@30000000/clock-controller@30380000
[    0.058345] platform 32e00000.lcdif: Fixed dependency cycle(s) with /soc@0/bus@32c00000/mipi_dsi@32e10000
[    0.058568] platform 32e00000.lcdif: Fixed dependency cycle(s) with /soc@0/bus@32c00000/mipi_dsi@32e10000
[    0.058681] platform 32e10000.mipi_dsi: Fixed dependency cycle(s) with /soc@0/bus@30800000/i2c@30a30000/adv7535@3d
[    0.058710] platform 32e10000.mipi_dsi: Fixed dependency cycle(s) with /soc@0/bus@32c00000/lcdif@32e00000
[    0.058985] platform 32e20000.csi1_bridge: Fixed dependency cycle(s) with /soc@0/bus@32c00000/mipi_csi@32e30000
[    0.059216] platform 32e20000.csi1_bridge: Fixed dependency cycle(s) with /soc@0/bus@32c00000/mipi_csi@32e30000
[    0.059331] platform 32e30000.mipi_csi: Fixed dependency cycle(s) with /soc@0/bus@32c00000/csi1_bridge@32e20000
[    0.059396] platform 32e30000.mipi_csi: Fixed dependency cycle(s) with /soc@0/bus@30800000/i2c@30a40000/ov5640_mipi@3c
[    0.060044] platform 32e40000.usb: Fixed dependency cycle(s) with /soc@0/bus@30800000/i2c@30a30000/tcpc@50
[    0.065723] Modules: 2G module region forced by RANDOMIZE_MODULE_REGION_FULL
[    0.065751] Modules: 0 pages in range for non-PLT usage
[    0.065754] Modules: 515376 pages in range for PLT usage
[    0.066547] HugeTLB: registered 1.00 GiB page size, pre-allocated 0 pages
[    0.066554] HugeTLB: 0 KiB vmemmap can be freed for a 1.00 GiB page
[    0.066559] HugeTLB: registered 32.0 MiB page size, pre-allocated 0 pages
[    0.066562] HugeTLB: 0 KiB vmemmap can be freed for a 32.0 MiB page
[    0.066566] HugeTLB: registered 2.00 MiB page size, pre-allocated 0 pages
[    0.066571] HugeTLB: 0 KiB vmemmap can be freed for a 2.00 MiB page
[    0.066575] HugeTLB: registered 64.0 KiB page size, pre-allocated 0 pages
[    0.066581] HugeTLB: 0 KiB vmemmap can be freed for a 64.0 KiB page
[    0.068511] ACPI: Interpreter disabled.
[    0.069420] iommu: Default domain type: Translated
[    0.069429] iommu: DMA domain TLB invalidation policy: strict mode
[    0.069822] SCSI subsystem initialized
[    0.069938] libata version 3.00 loaded.
[    0.070128] usbcore: registered new interface driver usbfs
[    0.070157] usbcore: registered new interface driver hub
[    0.070183] usbcore: registered new device driver usb
[    0.071296] mc: Linux media interface: v0.10
[    0.071333] videodev: Linux video capture interface: v2.00
[    0.071395] pps_core: LinuxPPS API ver. 1 registered
[    0.071399] pps_core: Software ver. 5.3.6 - Copyright 2005-2007 Rodolfo Giometti <giometti@linux.it>
[    0.071416] PTP clock support registered
[    0.071713] EDAC MC: Ver: 3.0.0
[    0.072150] scmi_core: SCMI protocol bus registered
[    0.072536] FPGA manager framework
[    0.072613] Advanced Linux Sound Architecture Driver Initialized.
[    0.073326] Bluetooth: Core ver 2.22
[    0.073350] NET: Registered PF_BLUETOOTH protocol family
[    0.073353] Bluetooth: HCI device and connection manager initialized
[    0.073361] Bluetooth: HCI socket layer initialized
[    0.073366] Bluetooth: L2CAP socket layer initialized
[    0.073377] Bluetooth: SCO socket layer initialized
[    0.073746] vgaarb: loaded
[    0.074261] clocksource: Switched to clocksource arch_sys_counter
[    0.074481] VFS: Disk quotas dquot_6.6.0
[    0.074511] VFS: Dquot-cache hash table entries: 512 (order 0, 4096 bytes)
[    0.074677] pnp: PnP ACPI: disabled
[    0.081443] NET: Registered PF_INET protocol family
[    0.081595] IP idents hash table entries: 32768 (order: 6, 262144 bytes, linear)
[    0.083146] tcp_listen_portaddr_hash hash table entries: 1024 (order: 2, 16384 bytes, linear)
[    0.083174] Table-perturb hash table entries: 65536 (order: 6, 262144 bytes, linear)
[    0.083187] TCP established hash table entries: 16384 (order: 5, 131072 bytes, linear)
[    0.083335] TCP bind hash table entries: 16384 (order: 7, 524288 bytes, linear)
[    0.083792] TCP: Hash tables configured (established 16384 bind 16384)
[    0.083890] UDP hash table entries: 1024 (order: 3, 32768 bytes, linear)
[    0.083936] UDP-Lite hash table entries: 1024 (order: 3, 32768 bytes, linear)
[    0.084090] NET: Registered PF_UNIX/PF_LOCAL protocol family
[    0.084500] RPC: Registered named UNIX socket transport module.
[    0.084505] RPC: Registered udp transport module.
[    0.084508] RPC: Registered tcp transport module.
[    0.084510] RPC: Registered tcp-with-tls transport module.
[    0.084513] RPC: Registered tcp NFSv4.1 backchannel transport module.
[    0.085555] PCI: CLS 0 bytes, default 64
[    0.085915] kvm [1]: IPA Size Limit: 40 bits
[    0.087969] kvm [1]: GICv3: no GICV resource entry
[    0.087975] kvm [1]: disabling GICv2 emulation
[    0.087995] kvm [1]: GIC system register CPU interface enabled
[    0.088020] kvm [1]: vgic interrupt IRQ9
[    0.088041] kvm [1]: Hyp mode initialized successfully
[    0.089243] Initialise system trusted keyrings
[    0.089423] workingset: timestamp_bits=42 max_order=19 bucket_order=0
[    0.089697] squashfs: version 4.0 (2009/01/31) Phillip Lougher
[    0.089916] NFS: Registering the id_resolver key type
[    0.089942] Key type id_resolver registered
[    0.089946] Key type id_legacy registered
[    0.089963] nfs4filelayout_init: NFSv4 File Layout Driver Registering...
[    0.089973] nfs4flexfilelayout_init: NFSv4 Flexfile Layout Driver Registering...
[    0.089991] jffs2: version 2.2. (NAND) \xc2\xa9 2001-2006 Red Hat, Inc.
[    0.090185] 9p: Installing v9fs 9p2000 file system support
[    0.123742] NET: Registered PF_ALG protocol family
[    0.123753] Key type asymmetric registered
[    0.123757] Asymmetric key parser 'x509' registered
[    0.123799] Block layer SCSI generic (bsg) driver version 0.4 loaded (major 243)
[    0.123805] io scheduler mq-deadline registered
[    0.123809] io scheduler kyber registered
[    0.123837] io scheduler bfq registered
[    0.130993] EINJ: ACPI disabled.
[    0.141946] imx-sdma 302c0000.dma-controller: Direct firmware load for imx/sdma/sdma-imx7d.bin failed with error -2
[    0.141963] imx-sdma 302c0000.dma-controller: Falling back to sysfs fallback for: imx/sdma/sdma-imx7d.bin
[    0.147981] mxs-dma 33000000.dma-controller: initialized
[    0.149264] SoC: i.MX8MM revision 1.0
[    0.149694] Bus freq driver module loaded
[    0.162622] Serial: 8250/16550 driver, 4 ports, IRQ sharing enabled
[    0.165713] 30860000.serial: ttymxc0 at MMIO 0x30860000 (irq = 18, base_baud = 5000000) is a IMX
[    0.165842] serial serial0: tty port ttymxc0 registered
[    0.166361] 30880000.serial: ttymxc2 at MMIO 0x30880000 (irq = 19, base_baud = 5000000) is a IMX
[    0.167050] 30890000.serial: ttymxc1 at MMIO 0x30890000 (irq = 20, base_baud = 1500000) is a IMX
[    0.167089] printk: console [ttymxc1] enabled
[    1.470317] imx-drm display-subsystem: bound imx-lcdif-crtc.0 (ops lcdif_crtc_ops)
[    1.478054] imx_sec_dsim_drv 32e10000.mipi_dsi: version number is 0x1060200
[    1.485082] [drm:drm_bridge_attach] *ERROR* failed to attach bridge /soc@0/bus@32c00000/mipi_dsi@32e10000 to encoder DSI-34: -517
[    1.496780] imx_sec_dsim_drv 32e10000.mipi_dsi: Failed to attach bridge: 32e10000.mipi_dsi
[    1.505056] imx_sec_dsim_drv 32e10000.mipi_dsi: failed to bind sec dsim bridge: -517
[    1.519392] loop: module loaded
[    1.524248] megasas: 07.725.01.00-rc1
[    1.533282] spi-nor spi0.0: n25q256ax1 (32768 Kbytes)
[    1.542684] tun: Universal TUN/TAP device driver, 1.6
[    1.548678] thunder_xcv, ver 1.0
[    1.551950] thunder_bgx, ver 1.0
[    1.555215] nicpf, ver 1.0
[    1.560213] hns3: Hisilicon Ethernet Network Driver for Hip08 Family - version
[    1.567448] hns3: Copyright (c) 2017 Huawei Corporation.
[    1.572798] hclge is initializing
[    1.576153] e1000: Intel(R) PRO/1000 Network Driver
[    1.581038] e1000: Copyright (c) 1999-2006 Intel Corporation.
[    1.586816] e1000e: Intel(R) PRO/1000 Network Driver
[    1.591788] e1000e: Copyright(c) 1999 - 2015 Intel Corporation.
[    1.597736] igb: Intel(R) Gigabit Ethernet Network Driver
[    1.603183] igb: Copyright (c) 2007-2014 Intel Corporation.
[    1.608795] igbvf: Intel(R) Gigabit Virtual Function Network Driver
[    1.615073] igbvf: Copyright (c) 2009 - 2012 Intel Corporation.
[    1.621171] sky2: driver version 1.30
[    1.625442] usbcore: registered new device driver r8152-cfgselector
[    1.631747] usbcore: registered new interface driver r8152
[    1.637696] VFIO - User Level meta-driver version: 0.3
[    1.645723] usbcore: registered new interface driver uas
[    1.651084] usbcore: registered new interface driver usb-storage
[    1.657164] usbcore: registered new interface driver usbserial_generic
[    1.663723] usbserial: USB Serial support registered for generic
[    1.669760] usbcore: registered new interface driver ftdi_sio
[    1.675532] usbserial: USB Serial support registered for FTDI USB Serial Device
[    1.682873] usbcore: registered new interface driver usb_serial_simple
[    1.689428] usbserial: USB Serial support registered for carelink
[    1.695545] usbserial: USB Serial support registered for flashloader
[    1.701929] usbserial: USB Serial support registered for funsoft
[    1.707965] usbserial: USB Serial support registered for google
[    1.713917] usbserial: USB Serial support registered for hp4x
[    1.719695] usbserial: USB Serial support registered for kaufmann
[    1.725816] usbserial: USB Serial support registered for libtransistor
[    1.732369] usbserial: USB Serial support registered for moto_modem
[    1.738662] usbserial: USB Serial support registered for motorola_tetra
[    1.745303] usbserial: USB Serial support registered for nokia
[    1.751163] usbserial: USB Serial support registered for novatel_gps
[    1.757544] usbserial: USB Serial support registered for siemens_mpi
[    1.763925] usbserial: USB Serial support registered for suunto
[    1.769872] usbserial: USB Serial support registered for vivopay
[    1.775908] usbserial: USB Serial support registered for zio
[    1.781603] usbcore: registered new interface driver usb_ehset_test
[    1.791179] input: 30370000.snvs:snvs-powerkey as /devices/platform/soc@0/30000000.bus/30370000.snvs/30370000.snvs:snvs-powerkey/input/input0
[    1.805946] snvs_rtc 30370000.snvs:snvs-rtc-lp: registered as rtc0
[    1.812178] snvs_rtc 30370000.snvs:snvs-rtc-lp: setting system clock to 2025-08-04T01:59:29 UTC (1754272769)
[    1.822164] i2c_dev: i2c /dev entries driver
[    1.828390] mx6s-csi 32e20000.csi1_bridge: initialising
[    1.834624] mxc_mipi-csi 32e30000.mipi_csi: supply mipi-phy not found, using dummy regulator
[    1.843383] mxc_mipi-csi 32e30000.mipi_csi: mipi csi v4l2 device registered
[    1.850359] CSI: Registered sensor subdevice: mxc_mipi-csi.0
[    1.856046] mxc_mipi-csi 32e30000.mipi_csi: lanes: 2, hs_settle: 13, clk_settle: 2, wclk: 1, freq: 333000000
[    1.870150] device-mapper: ioctl: 4.48.0-ioctl (2023-03-01) initialised: dm-devel@redhat.com
[    1.878718] Bluetooth: HCI UART driver ver 2.3
[    1.883185] Bluetooth: HCI UART protocol H4 registered
[    1.888332] Bluetooth: HCI UART protocol BCSP registered
[    1.893679] Bluetooth: HCI UART protocol LL registered
[    1.898825] Bluetooth: HCI UART protocol ATH3K registered
[    1.904251] Bluetooth: HCI UART protocol Three-wire (H5) registered
[    1.910648] Bluetooth: HCI UART protocol Broadcom registered
[    1.916339] Bluetooth: HCI UART protocol QCA registered
[    1.923218] sdhci: Secure Digital Host Controller Interface driver
[    1.929430] sdhci: Copyright(c) Pierre Ossman
[    1.934392] Synopsys Designware Multimedia Card Interface Driver
[    1.941093] sdhci-pltfm: SDHCI platform and OF driver helper
[    1.949440] ledtrig-cpu: registered to indicate activity on CPUs
[    1.957045] SMCCC: SOC_ID: ARCH_SOC_ID not implemented, skipping ....
[    1.963976] usbcore: registered new interface driver usbhid
[    1.969564] usbhid: USB HID core driver
[    1.979435] mmc2: SDHCI controller on 30b60000.mmc [30b60000.mmc] using ADMA
[    1.979556] hw perfevents: enabled with armv8_cortex_a53 PMU driver, 7 counters available
[    1.997992]  cs_system_cfg: CoreSight Configuration manager initialised
[    2.005724] platform soc@0: Fixed dependency cycle(s) with /soc@0/bus@30000000/efuse@30350000
[    2.015400] optee: probing for conduit method.
[    2.019886] optee: revision 4.4 (60beb308810f9561)
[    2.020725] optee: dynamic shared memory is enabled
[    2.030796] optee: initialized driver
[    2.036805] hantrodec 0 : module inserted. Major = 509
[    2.042528] hantrodec 1 : module inserted. Major = 509
[    2.048631] hx280enc: module inserted. Major <508>
[    2.058351] NET: Registered PF_LLC protocol family
[    2.063253] u32 classifier
[    2.064999] mmc2: new HS400 Enhanced strobe MMC card at address 0001
[    2.065998]     input device check on
[    2.073177] mmcblk2: mmc2:0001 DG4032 29.1 GiB
[    2.075999]     Actions configured
[    2.082759]  mmcblk2: p1 p2 p3 p4
[    2.084198] NET: Registered PF_INET6 protocol family
[    2.088668] mmcblk2boot0: mmc2:0001 DG4032 4.00 MiB
[    2.094205] Segment Routing with IPv6
[    2.098856] mmcblk2boot1: mmc2:0001 DG4032 4.00 MiB
[    2.100771] In-situ OAM (IOAM) with IPv6
[    2.107511] mmcblk2rpmb: mmc2:0001 DG4032 4.00 MiB, chardev (234:0)
[    2.109609] NET: Registered PF_PACKET protocol family
[    2.120890] bridge: filtering via arp/ip/ip6tables is no longer available by default. Update your scripts to load br_netfilter if you need this.
[    2.134933] Bluetooth: RFCOMM TTY layer initialized
[    2.139834] Bluetooth: RFCOMM socket layer initialized
[    2.144992] Bluetooth: RFCOMM ver 1.11
[    2.148756] Bluetooth: BNEP (Ethernet Emulation) ver 1.3
[    2.154076] Bluetooth: BNEP filters: protocol multicast
[    2.159314] Bluetooth: BNEP socket layer initialized
[    2.164287] Bluetooth: HIDP (Human Interface Emulation) ver 1.2
[    2.170215] Bluetooth: HIDP socket layer initialized
[    2.176250] 8021q: 802.1Q VLAN Support v1.8
[    2.180469] lib80211: common routines for IEEE802.11 drivers
[    2.186140] lib80211_crypt: registered algorithm 'NULL'
[    2.186145] lib80211_crypt: registered algorithm 'WEP'
[    2.186151] lib80211_crypt: registered algorithm 'CCMP'
[    2.186156] lib80211_crypt: registered algorithm 'TKIP'
[    2.186188] 9pnet: Installing 9P2000 support
[    2.190633] Key type dns_resolver registered
[    2.195674] NET: Registered PF_VSOCK protocol family
[    2.223536] registered taskstats version 1
[    2.228043] Loading compiled-in X.509 certificates
[    2.255955] gpio gpiochip0: Static allocation of GPIO base is deprecated, use dynamic allocation.
[    2.266458] gpio gpiochip1: Static allocation of GPIO base is deprecated, use dynamic allocation.
[    2.276903] gpio gpiochip2: Static allocation of GPIO base is deprecated, use dynamic allocation.
[    2.287463] gpio gpiochip3: Static allocation of GPIO base is deprecated, use dynamic allocation.
[    2.297926] gpio gpiochip4: Static allocation of GPIO base is deprecated, use dynamic allocation.
[    2.311261] usb_phy_generic usbphynop1: dummy supplies not allowed for exclusive requests
[    2.319745] usb_phy_generic usbphynop2: dummy supplies not allowed for exclusive requests
[    2.329085] i2c i2c-0: IMX I2C adapter registered
[    2.335568] adv7511 1-003d: supply avdd not found, using dummy regulator
[    2.342440] adv7511 1-003d: supply dvdd not found, using dummy regulator
[    2.344023] nxp-pca9450 0-0025: pca9450a probed.
[    2.349201] adv7511 1-003d: supply pvdd not found, using dummy regulator
[    2.360527] adv7511 1-003d: supply a2vdd not found, using dummy regulator
[    2.367357] adv7511 1-003d: supply v3p3 not found, using dummy regulator
[    2.374102] adv7511 1-003d: supply v1p2 not found, using dummy regulator
[    2.381638] adv7511 1-003d: Probe failed. Remote port 'mipi_dsi@32e10000' disabled
[    2.389461] platform 32e40000.usb: Fixed dependency cycle(s) with /soc@0/bus@30800000/i2c@30a30000/tcpc@50
[    2.399261] i2c 1-0050: Fixed dependency cycle(s) with /soc@0/bus@32c00000/usb@32e40000
[    2.410237] i2c i2c-1: IMX I2C adapter registered
[    2.416796] pca953x 2-0020: using no AI
[    2.424914] ov5640_mipi 2-003c: No sensor reset pin available
[    2.430743] ov5640_mipi 2-003c: supply DOVDD not found, using dummy regulator
[    2.437999] ov5640_mipi 2-003c: supply DVDD not found, using dummy regulator
[    2.445113] ov5640_mipi 2-003c: supply AVDD not found, using dummy regulator
[    2.462871] ov5640_mipi 2-003c: Read reg error: reg=300a
[    2.468197] ov5640_mipi 2-003c: Camera is not found
[    2.473384] i2c i2c-2: IMX I2C adapter registered
[    2.481225] imx6q-pcie 33800000.pcie: host bridge /soc@0/pcie@33800000 ranges:
[    2.484151] imx-drm display-subsystem: bound imx-lcdif-crtc.0 (ops lcdif_crtc_ops)
[    2.488528] imx6q-pcie 33800000.pcie:       IO 0x001ff80000..0x001ff8ffff -> 0x0000000000
[    2.496230] imx_sec_dsim_drv 32e10000.mipi_dsi: version number is 0x1060200
[    2.504259] imx6q-pcie 33800000.pcie:      MEM 0x0018000000..0x001fefffff -> 0x0018000000
[    2.511253] [drm:drm_bridge_attach] *ERROR* failed to attach bridge /soc@0/bus@32c00000/mipi_dsi@32e10000 to encoder DSI-34: -19
[    2.531011] imx_sec_dsim_drv 32e10000.mipi_dsi: Failed to attach bridge: 32e10000.mipi_dsi
[    2.539289] imx_sec_dsim_drv 32e10000.mipi_dsi: failed to bind sec dsim bridge: -19
[    2.546958] imx-drm display-subsystem: bound 32e10000.mipi_dsi (ops imx_sec_dsim_ops)
[    2.555425] [drm] Initialized imx-drm 1.0.0 20120507 for display-subsystem on minor 0
[    2.563306] imx-drm display-subsystem: [drm] Cannot find any crtc or sizes
[    2.574585] pps pps0: new PPS source ptp0
[    2.690703] mdio_bus 30be0000.ethernet-1:00: Fixed dependency cycle(s) with /soc@0/bus@30800000/ethernet@30be0000/mdio/ethernet-phy@0/vddio-regulator
[    2.734759] imx6q-pcie 33800000.pcie: iATU: unroll T, 4 ob, 4 ib, align 64K, limit 4G
[    2.790693] vddio: Bringing 1500000uV into 1800000-1800000uV
[    2.798356] fec 30be0000.ethernet eth0: registered PHC device 0
[    2.811172] imx-cpufreq-dt imx-cpufreq-dt: cpu speed grade 3 mkt segment 0 supported-hw 0x8 0x1
[    2.825525] galcore: clk_get vg clock failed, disable vg!
[    2.825530] sdhci-esdhc-imx 30b50000.mmc: Got CD GPIO
[    2.836609] Galcore version 6.4.11.p2.745085
[    2.854283] mmc0: SDHCI controller on 30b40000.mmc [30b40000.mmc] using ADMA
[    2.854560] mmc1: SDHCI controller on 30b50000.mmc [30b50000.mmc] using ADMA
[    2.885600] [drm] Initialized vivante 1.0.0 20170808 for 38000000.gpu on minor 1
[    2.898207] OF: graph: no port node found in /soc@0/bus@30800000/i2c@30a30000/tcpc@50/connector
[    2.906993] OF: graph: no port node found in /soc@0/bus@30800000/i2c@30a30000/tcpc@50/connector
[    2.915722] OF: graph: no port node found in /soc@0/bus@30800000/i2c@30a30000/tcpc@50/connector
[    2.919965] mmc0: new ultra high speed SDR104 SDIO card at address 0001
[    2.950312] cfg80211: Loading compiled-in X.509 certificates for regulatory database
[    2.960078] Loaded X.509 cert 'sforshee: 00b28ddf47aef9cea7'
[    2.966364] Loaded X.509 cert 'wens: 61c038651aabdcf94bd0ac7ff06c7248db18c600'
[    2.973646] clk: Disabling unused clocks
[    2.977643] platform regulatory.0: Direct firmware load for regulatory.db failed with error -2
[    2.980559] ALSA device list:
[    2.986267] platform regulatory.0: Falling back to sysfs fallback for: regulatory.db
[    2.989250]   No soundcards found.
[    3.744596] imx6q-pcie 33800000.pcie: Phy link never came up
[    4.003650] ddrc freq set to low bus mode
[    4.756332] imx6q-pcie 33800000.pcie: Phy link never came up
[    4.765502] imx6q-pcie 33800000.pcie: PCI host bridge to bus 0000:00
[    4.772510] pci_bus 0000:00: root bus resource [bus 00-ff]
[    4.778083] pci_bus 0000:00: root bus resource [io  0x0000-0xffff]
[    4.784312] pci_bus 0000:00: root bus resource [mem 0x18000000-0x1fefffff]
[    4.791418] pci 0000:00:00.0: [16c3:abcd] type 01 class 0x060400
[    4.797559] pci 0000:00:00.0: reg 0x10: [mem 0x00000000-0x000fffff]
[    4.804145] pci 0000:00:00.0: reg 0x38: [mem 0x00000000-0x0000ffff pref]
[    4.811086] pci 0000:00:00.0: supports D1
[    4.815127] pci 0000:00:00.0: PME# supported from D0 D1 D3hot D3cold
[    4.825696] pci 0000:00:00.0: BAR 0: assigned [mem 0x18000000-0x180fffff]
[    4.832617] pci 0000:00:00.0: BAR 6: assigned [mem 0x18100000-0x1810ffff pref]
[    4.839877] pci 0000:00:00.0: PCI bridge to [bus 01-ff]
[    4.848283] pcieport 0000:00:00.0: PME: Signaling with IRQ 221
[    4.861054] ddrc freq set to high bus mode
[    4.889965] EXT4-fs (mmcblk2p3): mounted filesystem edb0b809-343a-4c76-a5ef-e5b7f5c707a0 r/w with ordered data mode. Quota mode: none.
[    4.902165] VFS: Mounted root (ext4 filesystem) on device 179:3.
[    4.908934] devtmpfs: mounted
[    4.913079] Freeing unused kernel memory: 4032K
[    4.917733] Run /sbin/init as init process
[    4.921837]   with arguments:
[    4.921839]     /sbin/init
[    4.921842]   with environment:
[    4.921844]     HOME=/
[    4.921846]     TERM=linux
[    5.052074] systemd[1]: systemd 255.4^ running in system mode (+PAM -AUDIT -SELINUX -APPARMOR +IMA -SMACK +SECCOMP -GCRYPT -GNUTLS -OPENSSL +ACL +BLKID -CURL -ELFUTILS -FIDO2 -IDN2 -IDN -IPTC +KMOD -LIBCRYPTSETUP +LIBFDISK -PCRE2 -PWQUALITY -P11KIT -QRENCODE -TPM2 -BZIP2 -LZ4 -XZ -ZLIB +ZSTD -BPF_FRAMEWORK -XKBCOMMON +UTMP +SYSVINIT default-hierarchy=unified)
[    5.083965] systemd[1]: Detected architecture arm64.
[    5.106945] systemd[1]: Hostname set to <imx8mm-lpddr4-evk>.
[    5.200449] systemd-sysv-generator[132]: SysV service '/etc/init.d/rc.local' lacks a native systemd unit file. ~ Automatically generating a unit file for compatibility. Please update package to include a native systemd unit file, in order to make it safe, robust and future-proof. ! This compatibility logic is deprecated, expect removal soon. !
[    5.525643] systemd[1]: Queued start job for default target Graphical Interface.
[    5.554524] systemd[1]: Created slice Slice /system/getty.
[    5.576702] systemd[1]: Created slice Slice /system/modprobe.
[    5.600662] systemd[1]: Created slice Slice /system/serial-getty.
[    5.624676] systemd[1]: Created slice Slice /system/systemd-fsck.
[    5.648143] systemd[1]: Created slice User and Session Slice.
[    5.670740] systemd[1]: Started Dispatch Password Requests to Console Directory Watch.
[    5.698668] systemd[1]: Started Forward Password Requests to Wall Directory Watch.
[    5.722468] systemd[1]: Expecting device /dev/mmcblk2p4...
[    5.742639] systemd[1]: Reached target Path Units.
[    5.762395] systemd[1]: Reached target Remote File Systems.
[    5.782428] systemd[1]: Reached target Slice Units.
[    5.802409] systemd[1]: Reached target Swaps.
[    5.852333] systemd[1]: Listening on RPCbind Server Activation Socket.
[    5.878532] systemd[1]: Reached target RPC Port Mapper.
[    5.899181] systemd[1]: Listening on Syslog Socket.
[    5.918959] systemd[1]: Listening on initctl Compatibility Named Pipe.
[    5.943963] systemd[1]: Listening on Journal Audit Socket.
[    5.962938] systemd[1]: Listening on Journal Socket (/dev/log).
[    5.983126] systemd[1]: Listening on Journal Socket.
[    6.003130] systemd[1]: Listening on Network Service Netlink Socket.
[    6.030129] systemd[1]: Listening on udev Control Socket.
[    6.051210] systemd[1]: Listening on udev Kernel Socket.
[    6.071076] systemd[1]: Listening on User Database Manager Socket.
[    6.127235] systemd[1]: Mounting Huge Pages File System...
[    6.155362] systemd[1]: Mounting POSIX Message Queue File System...
[    6.182304] systemd[1]: Mounting Kernel Debug File System...
[    6.202752] systemd[1]: Kernel Trace File System was skipped because of an unmet condition check (ConditionPathExists=/sys/kernel/tracing).
[    6.220724] systemd[1]: Mounting Temporary Directory /tmp...
[    6.260852] systemd[1]: Starting Create List of Static Device Nodes...
[    6.285958] systemd[1]: Starting Load Kernel Module configfs...
[    6.310500] systemd[1]: Starting Load Kernel Module drm...
[    6.333899] systemd[1]: Starting Load Kernel Module fuse...
[    6.363896] systemd[1]: Starting RPC Bind...
[    6.369635] fuse: init (API version 7.39)
[    6.390957] systemd[1]: File System Check on Root Device was skipped because of an unmet condition check (ConditionPathIsReadWrite=!/).
[    6.410310] systemd[1]: Starting Journal Service...
[    6.432320] systemd[1]: Load Kernel Modules was skipped because no trigger condition checks were met.
[    6.446139] systemd[1]: Starting Generate network units from Kernel command line...
[    6.478544] systemd[1]: Starting Remount Root and Kernel File Systems...
[    6.502354] systemd[1]: Starting Apply Kernel Variables...
[    6.502938] systemd-journald[148]: Collecting audit messages is enabled.
[    6.534142] systemd[1]: Starting Coldplug All udev Devices...
[    6.540458] EXT4-fs (mmcblk2p3): re-mounted edb0b809-343a-4c76-a5ef-e5b7f5c707a0 r/w. Quota mode: none.
[    6.554477] systemd[1]: Starting Virtual Console Setup...
[    6.581508] systemd[1]: Started RPC Bind.
[    6.599270] systemd[1]: Mounted Huge Pages File System.
[    6.619222] systemd[1]: Started Journal Service.
[    6.762332] systemd-journald[148]: Received client request to flush runtime journal.
[    6.960775] audit: type=1334 audit(1754272774.644:2): prog-id=6 op=LOAD
[    6.967512] audit: type=1334 audit(1754272774.652:3): prog-id=7 op=LOAD
[    7.063969] audit: type=1334 audit(1754272774.748:4): prog-id=8 op=LOAD
[    7.071010] audit: type=1334 audit(1754272774.756:5): prog-id=9 op=LOAD
[    7.078197] audit: type=1334 audit(1754272774.760:6): prog-id=10 op=LOAD
[    8.350558] Registered IR keymap rc-empty
[    8.355657] rc rc0: gpio_ir_recv as /devices/platform/ir-receiver/rc/rc0
[    8.365349] input: gpio_ir_recv as /devices/platform/ir-receiver/rc/rc0/input1
[    8.433629] debugfs: File 'Playback' in directory 'dapm' already present!
[    8.441742] debugfs: File 'Capture' in directory 'dapm' already present!
[    8.452343] EXT4-fs (mmcblk2p4): mounted filesystem 130d59ae-6f2f-45bf-b247-8efb2a1726a6 r/w with ordered data mode. Quota mode: none.
[    8.519559] caam-snvs 30370000.caam-snvs: ipid matched - 0x3e
[    8.530982] caam-snvs 30370000.caam-snvs: violation handlers armed - non-secure state
[    8.593501] caam 30900000.crypto: device ID = 0x0a16040100000000 (Era 9)
[    8.600453] caam 30900000.crypto: job rings = 1, qi = 0
[    8.847177] EXT4-fs (mmcblk2p2): mounted filesystem 98ed423a-dee0-4be7-b332-3da98b714ea7 r/w with ordered data mode. Quota mode: none.
[    8.950859] caam algorithms registered in /proc/crypto
[    8.956456] caam 30900000.crypto: caam pkc algorithms registered in /proc/crypto
[    8.966846] caam 30900000.crypto: rng crypto API alg registered prng-caam
[    8.973970] caam 30900000.crypto: registering rng-caam
[    8.983330] random: crng init done
[    8.989935] Device caam-keygen registered
[    9.192467] audit: type=1334 audit(1754272776.876:7): prog-id=11 op=LOAD
[    9.238960] audit: type=1334 audit(1754272776.924:8): prog-id=12 op=LOAD
[   10.024154] imx-sdma 30bd0000.dma-controller: firmware found.
[   10.024154] imx-sdma 302b0000.dma-controller: firmware found.
[   10.024331] imx-sdma 30bd0000.dma-controller: loaded firmware 4.6
[   10.042365] imx-sdma 302c0000.dma-controller: firmware found.
[   10.137697] audit: type=1334 audit(1754272777.820:9): prog-id=13 op=LOAD
[   10.144545] audit: type=1334 audit(1754272777.828:10): prog-id=14 op=LOAD
[   10.151494] audit: type=1334 audit(1754272777.836:11): prog-id=15 op=LOAD
[   10.742978] Qualcomm Atheros AR8031/AR8033 30be0000.ethernet-1:00: attached PHY driver (mii_bus:phy_addr=30be0000.ethernet-1:00, irq=POLL)
[   14.851485] fec 30be0000.ethernet eth0: Link is Up - 1Gbps/Full - flow control off
[   14.937117] kauditd_printk_skb: 19 callbacks suppressed
[   14.937128] audit: type=1334 audit(1754272782.620:19): prog-id=19 op=LOAD
[   14.949440] audit: type=1334 audit(1754272782.628:20): prog-id=20 op=LOAD
[   14.956933] audit: type=1334 audit(1754272782.632:21): prog-id=21 op=LOAD
[   19.172831] platform backlight: deferred probe pending
[   19.178035] platform sound-ak4458: deferred probe pending
[   32.616967] audit: type=1006 audit(1754272800.383:22): pid=649 uid=0 old-auid=4294967295 auid=0 tty=(none) old-ses=4294967295 ses=3 res=1
[   32.630374] audit: type=1300 audit(1754272800.383:22): arch=c00000b7 syscall=64 success=yes exit=1 a0=8 a1=ffffdaa14e20 a2=1 a3=1 items=0 ppid=1 pid=649 auid=0 uid=0 gid=0 euid=0 suid=0 fsuid=0 egid=0 sgid=0 fsgid=0 tty=(none) ses=3 comm="(systemd)" exe="/usr/lib/systemd/systemd-executor" key=(null)
[   32.657126] audit: type=1327 audit(1754272800.383:22): proctitle="(systemd)"
[   32.679922] audit: type=1334 audit(1754272800.448:23): prog-id=22 op=LOAD
[   32.686804] audit: type=1300 audit(1754272800.448:23): arch=c00000b7 syscall=280 success=yes exit=8 a0=5 a1=ffffc5406188 a2=90 a3=0 items=0 ppid=1 pid=649 auid=0 uid=0 gid=0 euid=0 suid=0 fsuid=0 egid=0 sgid=0 fsgid=0 tty=(none) ses=3 comm="systemd" exe="/usr/lib/systemd/systemd" key=(null)
[   32.713360] audit: type=1327 audit(1754272800.448:23): proctitle="(systemd)"
[   32.721128] audit: type=1334 audit(1754272800.448:24): prog-id=22 op=UNLOAD
[   32.728838] audit: type=1300 audit(1754272800.448:24): arch=c00000b7 syscall=57 success=yes exit=0 a0=8 a1=1 a2=0 a3=ffffb2867e60 items=0 ppid=1 pid=649 auid=0 uid=0 gid=0 euid=0 suid=0 fsuid=0 egid=0 sgid=0 fsgid=0 tty=(none) ses=3 comm="systemd" exe="/usr/lib/systemd/systemd" key=(null)
[   32.755089] audit: type=1327 audit(1754272800.448:24): proctitle="(systemd)"
[   32.762886] audit: type=1334 audit(1754272800.448:25): prog-id=23 op=LOAD

```

# III. Glossary

# IV. Tool Usage

# Author

> Created and designed by [Lanka Hsu](lankahsu@gmail.com).

# License

> [CrossCompilationX](https://github.com/lankahsu520/CrossCompilationX) is available under the BSD-3-Clause license. See the LICENSE file for more info.

