
NPROC=$(shell nproc)
PKGS=$(wildcard pkgs/*.tar*)
GLIBC_PATCH_KERNEL26=linux-2.6.24 linux-2.6.38.8

.PHONY: all clean distclean compile install romfs
all: .all

clean:
	@rm -f .0*

distclean: clean
	@rm -rf $(PJ_CROSSTOOLX_SDK)/build_xxx $(PJ_CROSSTOOLX_SDK)/libs

.all: .010-gcc-3rd


.010-gcc-3rd: .009-glibc-2nd
	@echo '=>> $@'
	cd $(PJ_CROSSTOOLX_SDK)/build_xxx/$(PJ_GCC_VERSION) \
	&& colormake -j$(NPROC) \
	&& colormake install
	touch $@

.009-glibc-2nd: .008-gcc-2nd
	@echo '=>> $@'
	cd $(PJ_CROSSTOOLX_SDK)/build_xxx/$(PJ_GLIBC_VERSION) \
	&& colormake -j$(NPROC) \
	&& colormake install
	touch $@

.008-gcc-2nd: .007-libc
	@echo '=>> $@'
	cd $(PJ_CROSSTOOLX_SDK)/build_xxx/$(PJ_GCC_VERSION) \
	&& colormake -j$(NPROC) all-target-libgcc \
	&& colormake install-target-libgcc
	touch $@

.007-libc: .006a-csu
	@echo '=>> $@'
	touch $(PJ_TOOLCHAIN_PREFIX)/$(PJ_HOST)/include/gnu/stubs.h
	$(PJ_HOST)-gcc -nostdlib -nostartfiles -shared -x c /dev/null -o $(PJ_TOOLCHAIN_PREFIX)/$(PJ_HOST)/lib/libc.so
	touch $@

.006a-csu: .006-glibc-1st
	@echo '=>> $@'
	cd $(PJ_CROSSTOOLX_SDK)/build_xxx/$(PJ_GLIBC_VERSION) \
	&& colormake -j$(NPROC) csu/subdir_lib \
	&& install csu/crt*.o $(PJ_TOOLCHAIN_PREFIX)/$(PJ_HOST)/lib
	touch $@

.006-glibc-1st: .005-linux-header
	@echo '=>> $@'
	mkdir -p $(PJ_CROSSTOOLX_SDK)/build_xxx/$(PJ_GLIBC_VERSION) \
	&& cd $(PJ_CROSSTOOLX_SDK)/build_xxx/$(PJ_GLIBC_VERSION) \
	&& $(PJ_CROSSTOOLX_SDK)/libs/$(PJ_GLIBC_VERSION)/configure \
		--prefix=$(PJ_TOOLCHAIN_PREFIX)/$(PJ_HOST) \
		--build=$(MACHTYPE) --host=$(PJ_HOST) \
		--target=$(PJ_HOST) \
		--with-headers=$(PJ_TOOLCHAIN_PREFIX)/$(PJ_HOST)/include \
	&& colormake install-headers
	touch $@

.005-linux-header: .004-gcc-1st
	@echo '=>> $@'
	cd $(PJ_CROSSTOOLX_SDK)/libs/$(PJ_LINUX_KERNEL_VERSION) \
	&& make ARCH=$(PJ_ARCH) INSTALL_HDR_PATH=$(PJ_TOOLCHAIN_PREFIX)/$(PJ_HOST) headers_install

ifeq ("$(PJ_LINUX_KERNEL_VERSION)", "linux-2.6.24")
	cp $(PJ_CROSSTOOLX_SDK)/libs/$(PJ_LINUX_KERNEL_VERSION)/include/linux/falloc.h $(PJ_TOOLCHAIN_PREFIX)/$(PJ_HOST)/include/linux
endif
	touch $@

.004-gcc-1st: .003-binutils
	@echo '=>> $@'
	cd $(PJ_CROSSTOOLX_SDK)/libs/$(PJ_GCC_VERSION); ./contrib/download_prerequisites \
	&& mkdir -p $(PJ_CROSSTOOLX_SDK)/build_xxx/$(PJ_GCC_VERSION) \
	&& cd $(PJ_CROSSTOOLX_SDK)/build_xxx/$(PJ_GCC_VERSION) \
	&& $(PJ_CROSSTOOLX_SDK)/libs/$(PJ_GCC_VERSION)/configure --prefix=$(PJ_TOOLCHAIN_PREFIX) --target=$(PJ_HOST) --enable-languages=c,c++ $(PJ_GCC_CONFIGURE_FLAGS) \
	&& colormake -j$(NPROC) all-gcc \
	&& colormake install-gcc
	touch $@

.003-binutils: .002-unpack
	@echo '=>> $@'
	mkdir -p $(PJ_CROSSTOOLX_SDK)/build_xxx/$(PJ_BINUTILS_VERSION) \
	&& cd $(PJ_CROSSTOOLX_SDK)/build_xxx/$(PJ_BINUTILS_VERSION) \
	&& $(PJ_CROSSTOOLX_SDK)/libs/$(PJ_BINUTILS_VERSION)/configure --prefix=$(PJ_TOOLCHAIN_PREFIX) --target=$(PJ_HOST) \
	&& colormake -j$(NPROC) \
	&& colormake install
	touch $@

.002-unpack: .001-folder
	@echo '=>> $@'
	if [ ! -d "$(PJ_CROSSTOOLX_SDK)/libs/$(PJ_LINUX_KERNEL_VERSION)" ]; then \
		cd $(PJ_CROSSTOOLX_SDK) \
		&& mkdir -p $(PJ_CROSSTOOLX_SDK)/libs \
		&& for f in $(PKGS); do tar xvf $$f -C $(PJ_CROSSTOOLX_SDK)/libs/; done \
	fi

	[ -d patches/$(PJ_LINUX_KERNEL_VERSION) ] && cp -avr patches/$(PJ_LINUX_KERNEL_VERSION)/* libs/$(PJ_LINUX_KERNEL_VERSION) || echo -n ""
ifneq ($(filter $(PJ_LINUX_KERNEL_VERSION),$(GLIBC_PATCH_KERNEL26)),)
	[ -d patches/$(PJ_GLIBC_VERSION) ] && cp -avr patches/$(PJ_GLIBC_VERSION)/* libs/$(PJ_GLIBC_VERSION) || echo -n ""
endif
	touch $@

.001-folder:
	@echo '=>> $@'
	mkdir -p $(PJ_CROSSTOOLX_SDK)/pkgs
	[ ! -f "$(PJ_CROSSTOOLX_SDK)/pkgs/$(PJ_BINUTILS_VERSION).tar.xz" ] && curl -C - https://ftp.gnu.org/gnu/binutils/$(PJ_BINUTILS_VERSION).tar.xz -o $(PJ_CROSSTOOLX_SDK)/pkgs/$(PJ_BINUTILS_VERSION).tar.xz || echo -n ""
	[ ! -f "$(PJ_CROSSTOOLX_SDK)/pkgs/$(PJ_GCC_VERSION).tar.xz" ] && curl -C - https://ftp.gnu.org/gnu/gcc/$(PJ_GCC_VERSION)/$(PJ_GCC_VERSION).tar.xz -o $(PJ_CROSSTOOLX_SDK)/pkgs/$(PJ_GCC_VERSION).tar.xz || echo -n ""
	[ ! -f "$(PJ_CROSSTOOLX_SDK)/pkgs/$(PJ_LINUX_KERNEL_VERSION).tar.xz" ] && curl -C - https://cdn.kernel.org/pub/linux/kernel/v$(PJ_LINUX_KERNEL_VERSION_FOLDER)/$(PJ_LINUX_KERNEL_VERSION).tar.xz -o $(PJ_CROSSTOOLX_SDK)/pkgs/$(PJ_LINUX_KERNEL_VERSION).tar.xz || echo -n ""
	[ ! -f "$(PJ_CROSSTOOLX_SDK)/pkgs/$(PJ_GLIBC_VERSION).tar.xz" ] && curl -C - https://ftp.gnu.org/gnu/glibc/$(PJ_GLIBC_VERSION).tar.xz -o $(PJ_CROSSTOOLX_SDK)/pkgs/$(PJ_GLIBC_VERSION).tar.xz || echo -n ""
	touch $@
