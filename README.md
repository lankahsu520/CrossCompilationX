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

# Native-Compilation vs Cross-Compilation

# 1. Host (Ubuntu 20.04.4 LTS) and Target (Raspberry Pi 3 B+)

## 1.1. Native compiler

```mermaid
flowchart LR
	subgraph Host[Host - Ubuntu 20.04.4 LTS]
		subgraph Native[Native compiler]
			gcc[gcc helloworld.c]
		end
	end

	subgraph Target[Target - Raspberry Pi]
		helloworld_run_pi[-bash: ./helloworld: cannot execute binary file: Exec format error]
		subgraph DockerPIU[Docker - Ubuntu 20.04]
			helloworld_run_pi_u_docker[bash: ./helloworld: cannot execute binary file: Exec format error]
		end
	end


	subgraph HostA[Host - Ubuntu 20.04.4 LTS]
		helloworld_run_host[Hello world !!!]
		subgraph DockerAU2004[Docker - Ubuntu 20.04]
			helloworld_run_AU2004_docker[Hello world !!!]
		end
 		subgraph DockerAU2204[Docker - Ubuntu 22.04]
			helloworld_run_AU2204_docker[Hello world !!!]
		end
  end
	
	gcc --> |run|helloworld_run_host
	gcc --> |run|helloworld_run_AU2004_docker
	gcc --> |run|helloworld_run_AU2204_docker

	gcc --> |run|helloworld_run_pi
	gcc --> |run|helloworld_run_pi_u_docker
	
```
```bash
$ file helloworld
helloworld: ELF 64-bit LSB shared object, x86-64, version 1 (SYSV), dynamically linked, interpreter /lib64/ld-linux-x86-64.so.2, BuildID[sha1]=18d2f341bfac8c548cedce30a01e9a865ba383f8, for GNU/Linux 3.2.0, not stripped

```

## 1.2. Cross compiler

```mermaid
flowchart LR
	subgraph Host[Host - Ubuntu 20.04.4 LTS]
		subgraph Native[Cross compiler]
			gcc[arm-linux-gnueabihf-gcc helloworld.c]
		end
	end

	subgraph Target[Target - Raspberry Pi]
		helloworld_run_pi[Hello world !!!]
		subgraph DockerPIU[Docker - Ubuntu 20.04]
			helloworld_run_pi_u_docker[Hello world !!!]
		end
	end


	subgraph HostA[Host - Ubuntu 20.04.4 LTS]
		helloworld_run_host[-bash: ./helloworld: cannot execute binary file: Exec format error]
		subgraph DockerAU2004[Docker - Ubuntu 20.04]
			helloworld_run_AU2004_docker[bash: ./helloworld: cannot execute binary file: Exec format error]
		end
 		subgraph DockerAU2204[Docker - Ubuntu 22.04]
			helloworld_run_AU2204_docker[bash: ./helloworld: cannot execute binary file: Exec format error]
		end
  end
	
	gcc --> |run|helloworld_run_host
	gcc --> |run|helloworld_run_AU2004_docker
	gcc --> |run|helloworld_run_AU2204_docker

	gcc --> |run|helloworld_run_pi
	gcc --> |run|helloworld_run_pi_u_docker
	
```
```bash
$ sudo apt install crossbuild-essential-armhf
$ arm-linux-gnueabihf-gcc --version
arm-linux-gnueabihf-gcc (Ubuntu 9.4.0-1ubuntu1~20.04.1) 9.4.0
Copyright (C) 2019 Free Software Foundation, Inc.
This is free software; see the source for copying conditions.  There is NO
warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

or

$ mkdir /opt/pi; cd /opt/pi; 
$ git clone https://github.com/raspberrypi/tools
$ cd /opt/pi/tools; mv arm-bcm2708 /opt; cd opt
$ ll arm-bcm2708/
total 28
drwxrwxr-x  7 lanka lanka 4096  ???  20 21:45 ./
drwxrwxrwx 16 root  root  4096  ???  20 21:50 ../
drwxrwxr-x  7 lanka lanka 4096  ???  20 21:45 arm-bcm2708hardfp-linux-gnueabi/
drwxrwxr-x  7 lanka lanka 4096  ???  20 21:44 arm-bcm2708-linux-gnueabi/
lrwxrwxrwx  1 lanka lanka   29  ???  20 21:45 arm-linux-gnueabihf -> arm-rpi-4.9.3-linux-gnueabihf/
drwxrwxr-x  8 lanka lanka 4096  ???  20 21:45 arm-rpi-4.9.3-linux-gnueabihf/
drwxrwxr-x  7 lanka lanka 4096  ???  20 21:45 gcc-linaro-arm-linux-gnueabihf-raspbian/
drwxrwxr-x  7 lanka lanka 4096  ???  20 21:45 gcc-linaro-arm-linux-gnue
$ ./gcc-linaro-arm-linux-gnueabihf-raspbian/bin/arm-linux-gnueabihf-gcc --version
arm-linux-gnueabihf-gcc (crosstool-NG linaro-1.13.1-4.8-2014.01 - Linaro GCC 2013.11) 4.8.3 20140106 (prerelease)
Copyright (C) 2013 Free Software Foundation, Inc.
This is free software; see the source for copying conditions.  There is NO
warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

```

```bash
$ file helloworld
helloworld: ELF 32-bit LSB shared object, ARM, EABI5 version 1 (SYSV), dynamically linked, interpreter /lib/ld-linux-armhf.so.3, BuildID[sha1]=6fb61694cee3b1f7fc227de834d182e4388a9b09, for GNU/Linux 3.2.0, with debug_info, not stripped

```

# 2. Host (Raspberry Pi 3 B+) and Target (Ubuntu 20.04.4 LTS)

## 2.1. Native compiler

```mermaid
flowchart LR
	subgraph Host[Host - Raspberry Pi]
		subgraph Native[Native compiler]
			gcc[gcc helloworld.c]
		end
	end

	subgraph Target[Target - Ubuntu 20.04.4 LTS]
		helloworld_run_ubuntu[-bash: ./helloworld: cannot execute binary file: Exec format error]
		subgraph DockerUbuntuPI[Docker - Raspberry Pi]
			helloworld_run_ubuntu_pi_docker[4-???]
		end
	end


	subgraph HostA[Host - Raspberry Pi]
		helloworld_run_host[Hello world !!!]
		subgraph DockerAU2004[Docker - Ubuntu 20.04]
			helloworld_run_AU2004_docker[Hello world !!!]
		end
  end
	
	gcc --> |run|helloworld_run_host
	gcc --> |run|helloworld_run_AU2004_docker

	gcc --> |run|helloworld_run_ubuntu
	gcc --> |run|helloworld_run_ubuntu_pi_docker
	
```
## 2.2. Cross compiler

```mermaid
flowchart LR
	subgraph Host[Host - Raspberry Pi]
		subgraph Native[Cross compiler]
			gcc[x86_64-unknown-linux-gnu-gcc helloworld.c]
		end
	end

	subgraph Target[Target - Ubuntu 20.04.4 LTS]
		helloworld_run_ubuntu[Hello world !!!]
		subgraph DockerUbuntuPI[Docker - Raspberry Pi]
			helloworld_run_ubuntu_pi_docker[Hello world !!!]
		end
	end


	subgraph HostA[Host - Raspberry Pi]
		helloworld_run_host[-bash: ./helloworld: cannot execute binary file: Exec format error]
		subgraph DockerAU2004[Docker - Ubuntu 20.04]
			helloworld_run_AU2004_docker[bash: ./helloworld: cannot execute binary file: Exec format error]
		end
  end
	
	gcc --> |run|helloworld_run_host
	gcc --> |run|helloworld_run_AU2004_docker

	gcc --> |run|helloworld_run_ubuntu
	gcc --> |run|helloworld_run_ubuntu_pi_docker
	
```

# Appendix

# I. Study

-  [Raspberry PI + cross compile & build kernel](https://hackmd.io/@0p3Xnj8xQ66lEl0EHA_2RQ/HJRXge9FO)
- [Crosstool-NG ??????](https://www.eebreakdown.com/2015/05/crosstool-ng.html)
- [crosstool-ng?????????????????????](https://www.crifan.com/files/doc/docbook/crosstool_ng/release/htmls/crosstool_ng_config_para.html)
- [Using the toolchain](https://crosstool-ng.github.io/docs/toolchain-usage/)


# II. Debug

## II.1. C++: fatal error: Killed signal terminated program cc1plus

```bash
mkdir /work/codebase/swap
dd if=/dev/zero of=/work/codebase/swap/swap0 bs=64M count=64
chmod 0600 /work/codebase/swap/swap0
mkswap /work/codebase/swap/swap0
sudo swapon /work/codebase/swap/swap0
swapon -s


sudo swapoff /work/codebase/swap/swap0
sudo rm /work/codebase/swap/swap0
sudo swapoff -a

```

## II.2. fatal error: cannot execute 'cc1': execvp: No such file or directory

```bash
If you got the error at compile time, please make sure your gcc is in $PATH.

```

## II.3. ./helloworld: /lib/x86_64-linux-gnu/libc.so.6: version `GLIBC_2.34' not found (required by ./helloworld)

```bash
Please check libc version on your target machine !!!

Host: Raspberry Pi 3

Target: Ubuntu 20.04 
$ ll /lib/x86_64-linux-gnu/libc.so.6
lrwxrwxrwx 1 root root 12  ???   7 09:24 /lib/x86_64-linux-gnu/libc.so.6 -> libc-2.31.so*
$ ldd --version
ldd (Ubuntu GLIBC 2.31-0ubuntu9.9) 2.31
Copyright (C) 2020 Free Software Foundation, Inc.
This is free software; see the source for copying conditions.  There is NO
warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
Written by Roland McGrath and Ulrich Drepper.

```

## II.4. mp-divrem_1.s:130: Error: selected processor does not support `mls r1,r4,r8,r11' in ARM mode

```bash
$ vi .config
#https://github.com/crosstool-ng/crosstool-ng/issues/1248
CT_EXTRA_CFLAGS_FOR_HOST="-march=armv7-a -mfloat-abi=hard -mtune=cortex-a5"
$ ct-ng oldconfig

```

## II.5. [ERROR]      ../sysdeps/nptl/pthread.h:719:47: error: argument 1 of type 'struct __jmp_buf_tag *' declared as a pointer [-Werror=array-parameter=]

```bash
$ vi .config
#https://github.com/crosstool-ng/crosstool-ng/issues/1712
# CT_GLIBC_ENABLE_WERROR is not set


```

# III. [crosstool-ng](https://github.com/crosstool-ng/crosstool-ng)

## III.1. Install and Build

```bash
sudo apt-get install -y libtool-bin 
sudo apt-get install -y help2man
sudo apt-get install -y flex texinfo
sudo apt-get install -y gawk
sudo apt-get install -y libncurses5-dev

sudo apt-get install -y bison
sudo apt-get install -y byacc
```

```bash
$ git clone https://github.com/crosstool-ng/crosstool-ng.git
or
$ wget https://github.com/crosstool-ng/crosstool-ng/archive/refs/tags/crosstool-ng-1.25.0.tar.gz
$ tar -zxvf crosstool-ng-1.25.0.tar.gz

$ cd crosstool-ng-crosstool-ng-1.25.0
$ ./bootstrap
$ ./configure --prefix=/work/codebase/crosstool-ng
$ make; make install
$ export PATH=/work/codebase/crosstool-ng/bin:$PATH

$ ct-ng list-samples
$ ct-ng show-x86_64-unknown-linux-gnu
$ mkdir -p /work/codebase/x86_64; cd /work/codebase/x86_64
$ ct-ng x86_64-unknown-linux-gnu
$ ct-ng menuconfig
Paths and misc options
	Number of parallel jobs
Paths and misc options
	Debug crosstool-NG
		Save intermediate steps
C-library
	Version of glibc (2.31)  --->

# if change .config
$ vi .config
$ ct-ng oldconfig
```

## III.2. Build

```bash
$ ct-ng build

```

##### - PI3

```bash
[INFO ]  Performing some trivial sanity checks
[WARN ]  Number of open files 1024 may not be sufficient to build the toolchain; increasing to 2048
[INFO ]  Build started 20220826.204623
[INFO ]  Building environment variables
[WARN ]  Directory '/home/pi/src' does not exist.
[WARN ]  Will not save downloaded tarballs to local storage.
[EXTRA]  Preparing working directories
[EXTRA]  Installing user-supplied crosstool-NG configuration
[EXTRA]  =================================================================
[EXTRA]  Dumping internal crosstool-NG configuration
[EXTRA]    Building a toolchain for:
[EXTRA]      build  = armv7l-unknown-linux-gnueabihf
[EXTRA]      host   = armv7l-unknown-linux-gnueabihf
[EXTRA]      target = x86_64-unknown-linux-gnu
[EXTRA]  Dumping internal crosstool-NG configuration: done in 8.79s (at 11:40)
[INFO ]  =================================================================
[INFO ]  Retrieving needed toolchain components' tarballs
[INFO ]  Retrieving needed toolchain components' tarballs: done in 7.51s (at 11:48)
[INFO ]  =================================================================
[INFO ]  Extracting and patching toolchain components
[INFO ]  Extracting and patching toolchain components: done in 8.43s (at 11:56)
[INFO ]  Saving state to restart at step 'companion_tools_for_build'...
[INFO ]  Saving state to restart at step 'companion_libs_for_build'...
[INFO ]  =================================================================
[INFO ]  Installing ncurses for build
[EXTRA]    Configuring ncurses
[EXTRA]    Building ncurses
[EXTRA]    Installing ncurses
[INFO ]  Installing ncurses for build: done in 225.90s (at 15:45)
[INFO ]  Saving state to restart at step 'binutils_for_build'...
[INFO ]  Saving state to restart at step 'companion_tools_for_host'...
[INFO ]  Saving state to restart at step 'companion_libs_for_host'...
[INFO ]  =================================================================
[INFO ]  Installing zlib for host
[EXTRA]    Configuring zlib
[EXTRA]    Building zlib
[EXTRA]    Installing zlib
[INFO ]  Installing zlib for host: done in 21.87s (at 16:12)
[INFO ]  =================================================================
[INFO ]  Installing GMP for host
[EXTRA]    Configuring GMP
[EXTRA]    Building GMP
[EXTRA]    Installing GMP
[INFO ]  Installing GMP for host: done in 659.19s (at 27:12)
[INFO ]  =================================================================
[INFO ]  Installing MPFR for host
[EXTRA]    Configuring MPFR
[EXTRA]    Building MPFR
[EXTRA]    Installing MPFR
[INFO ]  Installing MPFR for host: done in 349.29s (at 33:01)
[INFO ]  =================================================================
[INFO ]  Installing ISL for host
[EXTRA]    Configuring ISL
[EXTRA]    Building ISL
[EXTRA]    Installing ISL
[INFO ]  Installing ISL for host: done in 348.67s (at 38:50)
[INFO ]  =================================================================
[INFO ]  Installing MPC for host
[EXTRA]    Configuring MPC
[EXTRA]    Building MPC
[EXTRA]    Installing MPC
[INFO ]  Installing MPC for host: done in 137.69s (at 41:08)
[INFO ]  =================================================================
[INFO ]  Installing expat for host
[EXTRA]    Configuring expat
[EXTRA]    Building expat
[EXTRA]    Installing expat
[INFO ]  Installing expat for host: done in 221.18s (at 44:49)
[INFO ]  =================================================================
[INFO ]  Installing ncurses for host
[EXTRA]    Configuring ncurses
[EXTRA]    Building ncurses
[EXTRA]    Installing ncurses
[INFO ]  Installing ncurses for host: done in 304.56s (at 49:53)
[INFO ]  =================================================================
[INFO ]  Installing libiconv for host
[EXTRA]    Skipping (included in GNU C library)
[INFO ]  Installing libiconv for host: done in 0.21s (at 49:54)
[INFO ]  =================================================================
[INFO ]  Installing gettext for host
[EXTRA]    Skipping (included in GNU C library)
[INFO ]  Installing gettext for host: done in 0.19s (at 49:54)
[INFO ]  Saving state to restart at step 'binutils_for_host'...
[INFO ]  =================================================================
[INFO ]  Installing binutils for host
[EXTRA]    Configuring binutils
[EXTRA]    Building binutils
[EXTRA]    Installing binutils
[EXTRA]    Installing ld wrapper
[INFO ]  Installing binutils for host: done in 4913.90s (at 132:04)
[INFO ]  Saving state to restart at step 'libc_headers'...
[INFO ]  Saving state to restart at step 'kernel_headers'...
[INFO ]  =================================================================
[INFO ]  Installing kernel headers
[EXTRA]    Installing kernel headers
[EXTRA]    Checking installed headers
[INFO ]  Installing kernel headers: done in 369.97s (at 140:22)
[INFO ]  Saving state to restart at step 'cc_core'...
[INFO ]  =================================================================
[INFO ]  Installing core C gcc compiler
[EXTRA]    Configuring core C gcc compiler
[EXTRA]    Building gcc
[EXTRA]    Installing gcc
[EXTRA]    Housekeeping for core gcc compiler
[EXTRA]       '' --> lib (gcc)   lib64 (os)
[INFO ]  Installing core C gcc compiler: done in 16565.30s (at 417:46)
[INFO ]  Saving state to restart at step 'libc_main'...
[INFO ]  =================================================================
[INFO ]  Installing C library
[INFO ]    =================================================================
[INFO ]    Building for multilib 1/1: ''
[EXTRA]      Configuring C library
[EXTRA]      Building C library
[EXTRA]      Installing C library
[INFO ]    Building for multilib 1/1: '': done in 6735.80s (at 535:00)
[INFO ]  Installing C library: done in 6737.20s (at 535:00)
[INFO ]  Saving state to restart at step 'cc_for_build'...
[INFO ]  Saving state to restart at step 'cc_for_host'...
[INFO ]  =================================================================
[INFO ]  Installing final gcc compiler
[EXTRA]    Configuring final gcc compiler
[EXTRA]    Building final gcc compiler
[EXTRA]    Installing final gcc compiler
[EXTRA]    Housekeeping for final gcc compiler
[EXTRA]       '' --> lib (gcc)   lib64 (os)
[INFO ]  Installing final gcc compiler: done in 20695.56s (at 362:01)
[INFO ]  Saving state to restart at step 'libc_post_cc'...
[INFO ]  Saving state to restart at step 'companion_libs_for_target'...
[INFO ]  Saving state to restart at step 'binutils_for_target'...
[INFO ]  Saving state to restart at step 'debug'...
[INFO ]  =================================================================
[INFO ]  Installing cross-gdb
[EXTRA]    Configuring cross gdb
[EXTRA]    Building cross gdb
[EXTRA]    Installing cross gdb
[EXTRA]    Installing '.gdbinit' template
[INFO ]  Installing cross-gdb: done in 3311.65s (at 449:59)
[INFO ]  =================================================================
[INFO ]  Installing gdb server
[EXTRA]    Configuring native gdb
[EXTRA]    Building native gdb
[EXTRA]    Installing native gdb
[INFO ]  Installing gdb server: done in 839.45s (at 463:58)
[INFO ]  Saving state to restart at step 'test_suite'...
[INFO ]  Saving state to restart at step 'finish'...
[INFO ]  =================================================================
[INFO ]  Finalizing the toolchain's directory
[INFO ]    Stripping all toolchain executables
[EXTRA]    Installing the populate helper
[EXTRA]    Installing a cross-ldd helper
[EXTRA]    Creating toolchain aliases
[EXTRA]    Removing installed documentation
[EXTRA]    Collect license information from: /tmp/sdk/x86_64/.build/x86_64-unknown-linux-gnu/src
[EXTRA]    Put the license information to: /home/pi/x-tools/x86_64-unknown-linux-gnu/share/licenses
[INFO ]  Finalizing the toolchain's directory: done in 573.96s (at 491:11)
[INFO ]  Build completed at 20220827.161713
[INFO ]  (elapsed: 1170:50.55)
[INFO ]  Finishing installation (may take a few seconds)...

```

##### - PI4

```bash
[INFO ]  Performing some trivial sanity checks
[WARN ]  Number of open files 1024 may not be sufficient to build the toolchain; increasing to 2048
[INFO ]  Build started 20220823.144114
[INFO ]  Building environment variables
[WARN ]  Directory '/home/pi/src' does not exist.
[WARN ]  Will not save downloaded tarballs to local storage.
[EXTRA]  Preparing working directories
[EXTRA]  Installing user-supplied crosstool-NG configuration
[EXTRA]  =================================================================
[EXTRA]  Dumping internal crosstool-NG configuration
[EXTRA]    Building a toolchain for:
[EXTRA]      build  = aarch64-unknown-linux-gnu
[EXTRA]      host   = aarch64-unknown-linux-gnu
[EXTRA]      target = x86_64-unknown-linux-gnu
[EXTRA]  Dumping internal crosstool-NG configuration: done in 0.24s (at 00:05)
[INFO ]  =================================================================
[INFO ]  Retrieving needed toolchain components' tarballs
[INFO ]  Retrieving needed toolchain components' tarballs: done in 1.61s (at 00:07)
[INFO ]  =================================================================
[INFO ]  Extracting and patching toolchain components
[INFO ]  Extracting and patching toolchain components: done in 1.77s (at 00:09)
[INFO ]  =================================================================
[INFO ]  Installing ncurses for build
[EXTRA]    Configuring ncurses
[EXTRA]    Building ncurses
[EXTRA]    Installing ncurses
[INFO ]  Installing ncurses for build: done in 61.80s (at 01:11)
[INFO ]  =================================================================
[INFO ]  Installing zlib for host
[EXTRA]    Configuring zlib
[EXTRA]    Building zlib
[EXTRA]    Installing zlib
[INFO ]  Installing zlib for host: done in 6.06s (at 01:17)
[INFO ]  =================================================================
[INFO ]  Installing GMP for host
[EXTRA]    Configuring GMP
[EXTRA]    Building GMP
[EXTRA]    Installing GMP
[INFO ]  Installing GMP for host: done in 105.77s (at 03:02)
[INFO ]  =================================================================
[INFO ]  Installing MPFR for host
[EXTRA]    Configuring MPFR
[EXTRA]    Building MPFR
[EXTRA]    Installing MPFR
[INFO ]  Installing MPFR for host: done in 63.25s (at 04:06)
[INFO ]  =================================================================
[INFO ]  Installing ISL for host
[EXTRA]    Configuring ISL
[EXTRA]    Building ISL
[EXTRA]    Installing ISL
[INFO ]  Installing ISL for host: done in 103.09s (at 05:49)
[INFO ]  =================================================================
[INFO ]  Installing MPC for host
[EXTRA]    Configuring MPC
[EXTRA]    Building MPC
[EXTRA]    Installing MPC
[INFO ]  Installing MPC for host: done in 21.25s (at 06:10)
[INFO ]  =================================================================
[INFO ]  Installing expat for host
[EXTRA]    Configuring expat
[EXTRA]    Building expat
[EXTRA]    Installing expat
[INFO ]  Installing expat for host: done in 26.54s (at 06:37)
[INFO ]  =================================================================
[INFO ]  Installing ncurses for host
[EXTRA]    Configuring ncurses
[EXTRA]    Building ncurses
[EXTRA]    Installing ncurses
[INFO ]  Installing ncurses for host: done in 58.99s (at 07:36)
[INFO ]  =================================================================
[INFO ]  Installing libiconv for host
[EXTRA]    Skipping (included in GNU C library)
[INFO ]  Installing libiconv for host: done in 0.03s (at 07:36)
[INFO ]  =================================================================
[INFO ]  Installing gettext for host
[EXTRA]    Skipping (included in GNU C library)
[INFO ]  Installing gettext for host: done in 0.03s (at 07:36)
[INFO ]  =================================================================
[INFO ]  Installing binutils for host
[EXTRA]    Configuring binutils
[EXTRA]    Building binutils
[EXTRA]    Installing binutils
[EXTRA]    Installing ld wrapper
[INFO ]  Installing binutils for host: done in 1636.02s (at 34:52)
[INFO ]  =================================================================
[INFO ]  Installing kernel headers
[EXTRA]    Installing kernel headers
[EXTRA]    Checking installed headers
[INFO ]  Installing kernel headers: done in 36.70s (at 35:29)
[INFO ]  =================================================================
[INFO ]  Installing core C gcc compiler
[EXTRA]    Configuring core C gcc compiler
[EXTRA]    Building gcc
[EXTRA]    Installing gcc
[EXTRA]    Housekeeping for core gcc compiler
[EXTRA]       '' --> lib (gcc)   lib64 (os)
[INFO ]  Installing core C gcc compiler: done in 2842.99s (at 82:52)
[INFO ]  =================================================================
[INFO ]  Installing C library
[INFO ]    =================================================================
[INFO ]    Building for multilib 1/1: ''
[EXTRA]      Configuring C library
[EXTRA]      Building C library
[EXTRA]      Installing C library
[INFO ]    Building for multilib 1/1: '': done in 963.85s (at 98:56)
[INFO ]  Installing C library: done in 964.07s (at 98:56)
[INFO ]  =================================================================
[INFO ]  Installing final gcc compiler
[EXTRA]    Configuring final gcc compiler
[EXTRA]    Building final gcc compiler
[EXTRA]    Installing final gcc compiler
[EXTRA]    Housekeeping for final gcc compiler
[EXTRA]       '' --> lib (gcc)   lib64 (os)
[INFO ]  Installing final gcc compiler: done in 3781.51s (at 161:58)
[INFO ]  =================================================================
[INFO ]  Installing cross-gdb
[EXTRA]    Configuring cross gdb
[EXTRA]    Building cross gdb
[EXTRA]    Installing cross gdb
[EXTRA]    Installing '.gdbinit' template
[INFO ]  Installing cross-gdb: done in 1127.05s (at 180:45)
[INFO ]  =================================================================
[INFO ]  Installing gdb server
[EXTRA]    Configuring native gdb
[EXTRA]    Building native gdb
[EXTRA]    Installing native gdb
[INFO ]  Installing gdb server: done in 177.82s (at 183:43)
[INFO ]  =================================================================
[INFO ]  Finalizing the toolchain's directory
[INFO ]    Stripping all toolchain executables
[EXTRA]    Installing the populate helper
[EXTRA]    Installing a cross-ldd helper
[EXTRA]    Creating toolchain aliases
[EXTRA]    Removing installed documentation
[EXTRA]    Collect license information from: /work/codebase/x86_64-unknown-linux-gnu/.build/x86_64-unknown-linux-gnu/src
[EXTRA]    Put the license information to: /home/pi/x-tools/x86_64-unknown-linux-gnu/share/licenses
[INFO ]  Finalizing the toolchain's directory: done in 27.44s (at 184:10)
[INFO ]  Build completed at 20220823.174522
[INFO ]  (elapsed: 184:08.63)
[INFO ]  Finishing installation (may take a few seconds)...
[184:10] / 

```

## III.3. Continue

```bash
$ ct-ng list-steps
Available build steps, in order:
  - companion_tools_for_build
  - companion_libs_for_build
  - binutils_for_build
  - companion_tools_for_host
  - companion_libs_for_host
  - binutils_for_host
  - libc_headers
  - kernel_headers
  - cc_core
  - libc_main
  - cc_for_build
  - cc_for_host
  - libc_post_cc
  - companion_libs_for_target
  - binutils_for_target
  - debug
  - test_suite
  - finish
Use "<step>" as action to execute only that step.
Use "+<step>" as action to execute up to that step.
Use "<step>+" as action to execute from that step onward.
$ ct-ng libc_main+

$ ct-ng build RESTART=cc_core
```

## III.4. Set Environment

```bash
$ export PATH=~/x-tools/x86_64-unknown-linux-gnu/bin:$PATH

```


# Author

Created and designed by [Lanka Hsu](lankahsu@gmail.com).

# License

[CrossCompilationX](https://github.com/lankahsu520/CrossCompilationX) is available under the BSD-3-Clause license. See the LICENSE file for more info.

