.EXPORT_ALL_VARIABLES:

include mk/function.mk

#********************************************************************************
#** CLEANDIRS **
#********************************************************************************
LIBS_CHECK_yes = 
BINS_CHECK_yes =

# USERS_yes
include mk/user.mk

BB_TASKS=listtasks configure clean cleanall fetch compile build install package_qa#
#BB ?= avahi

.DEFAULT_GOAL = all

# .cook-build -> cook-patched -> .cook-update -> .cook-init
.PHONY: all distclean cook-clean cook-target-clean cook-lnk cook-dry-run cook-build cook-rebuild cook
all: cook-build check

distclean: cook-clean
	rm -rf builds

cook-clean:
	rm -f .cook-*

cook-target-clean:
	${PJ_ROOT}/confs/sh/cooker_123.sh clean

cook-lnk:
	${PJ_ROOT}/confs/sh/cooker_123.sh lnk

cook-dry-run:
	${PJ_ROOT}/confs/sh/cooker_123.sh dry-run

cook-build: .cook-patched
	${PJ_ROOT}/confs/sh/cooker_123.sh build

cook-rebuild: cook-clean cook-target-clean cook-build

cook:
	${PJ_ROOT}/confs/sh/cooker_123.sh cook

.cook-init:
	# please use cooker_123.sh, dont call "cooker init xxx/xxx.json"
	${PJ_ROOT}/confs/sh/cooker_123.sh init
	touch $@

.cook-update: .cook-init
	${PJ_ROOT}/confs/sh/cooker_123.sh update
	touch $@

.cook-generate: .cook-update 
	${PJ_ROOT}/confs/sh/cooker_123.sh generate
	touch $@

.cook-patched: .cook-generate
	@if [ -d patches/$(PJ_YOCTO_LAYERS_PATH) ]; then \
		cp -avr patches/$(PJ_YOCTO_LAYERS_PATH)/* $(PJ_YOCTO_LAYERS_PATH); \
	fi
	touch $@

.PHONY: builds-history bb-linker lnk-generate toolchain toolchain-pure $(BB_TASKS)
builds-history:
	@cd $(PJ_YOCTO_BUILD_DIR) \
	&& buildhistory-collect-srcrevs -a

bb-linker:
	${PJ_ROOT}/confs/sh/bb_linker.sh

lnk-generate: cook-lnk bb-linker

toolchain:
	#cooker build -s $(PJ_YOCTO_TARGET)
	bitbake core-image-base -c populate_sdk

toolchain-pure:
	bitbake meta-toolchain

$(BB_TASKS):
	@echo "BB_TASK=[$@], BB=[$(BB)]"
ifeq ("$(BB)", "")
	@echo "Example:"
	@echo "  make listtasks BB=avahi"
	@echo "  make configure BB=avahi"
	@echo "  make clean BB=avahi"
	@echo "  make cleanall BB=avahi"
	@echo "  make fetch BB=avahi"
	@echo "  make compile BB=avahi"
	@echo "  make build BB=avahi"
	@echo "  make install BB=avahi"
	@echo "  make package_qa BB=avahi"
else
	@for BBx in $(BB); do \
		echo "[bitbake -c $@ $$BBx]"; \
		bitbake -c $@ $$BBx; \
	done
endif
	@echo

%:
	@: