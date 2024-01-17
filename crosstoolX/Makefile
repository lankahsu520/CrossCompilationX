
NPROC=$(shell nproc)
PKGS=$(wildcard pkgs/*.tar*)

.PHONY: all clean distclean compile install romfs
all: .all

clean:
	@rm -f .0*

distclean: clean
	@rm -rf $(PJ_CROSSTOOLX_SDK)/build_xxx

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

.007-libc: .006-glibc-1st
	@echo '=>> $@'
	touch $(PJ_TOOLCHAIN_PREFIX)/$(PJ_HOST)/include/gnu/stubs.h
	$(PJ_HOST)-gcc -nostdlib -nostartfiles -shared -x c /dev/null -o $(PJ_TOOLCHAIN_PREFIX)/$(PJ_HOST)/lib/libc.so
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
	&& colormake install-headers \
	&& colormake -j$(NPROC) csu/subdir_lib \
	&& install csu/crt*.o $(PJ_TOOLCHAIN_PREFIX)/$(PJ_HOST)/lib
	touch $@

.005-linux-header: .004-gcc-1st
	@echo '=>> $@'
	cd $(PJ_CROSSTOOLX_SDK)/libs/$(PJ_LINUX_KERNEL_VERSION) \
	&& make ARCH=$(PJ_ARCH) INSTALL_HDR_PATH=$(PJ_TOOLCHAIN_PREFIX)/$(PJ_HOST) headers_install
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
	touch $@

.001-folder:
	@echo '=>> $@'
	touch $@
