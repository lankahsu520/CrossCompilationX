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

# cook-build -> .cook-patched -> .cook-update -> .cook-init
.PHONY: all distclean cook-clean cook-target-clean cook-lnk cook-dry-run cook-build cook-bundle cook-rebuild cook
all: cook-build cook-bundle lnk-generate

distclean: cook-clean
	rm -rf builds

cook-clean:
	rm -f .cook-*

cook-target-clean:
	$(PJ_ROOT_DIR)/confs/sh/cooker_123.sh clean

cook-lnk:
	$(PJ_ROOT_DIR)/confs/sh/cooker_123.sh lnk

cook-dry-run:
	$(PJ_ROOT_DIR)/confs/sh/cooker_123.sh dry-run

cook-build: .cook-patched
	$(PJ_ROOT_DIR)/confs/sh/cooker_123.sh build

cook-rebuild: cook-clean cook-target-clean cook-build

cook-bundle:
ifneq ("$(PJ_YOCTO_BUNDLE)", "")
	$(PJ_ROOT_DIR)/confs/sh/cooker_123.sh bundle
endif

cook:
	$(PJ_ROOT_DIR)/confs/sh/cooker_123.sh cook

.cook-init:
	# please use cooker_123.sh, dont call "cooker init xxx/xxx.json"
	$(PJ_ROOT_DIR)/confs/sh/cooker_123.sh init
	touch $@

.cook-update: .cook-init
	$(PJ_ROOT_DIR)/confs/sh/cooker_123.sh update
	touch $@

.cook-generate: .cook-update 
	$(PJ_ROOT_DIR)/confs/sh/cooker_123.sh generate
	touch $@

.cook-patched: .cook-generate
	#@for item in $(PJ_YOCTO_LAYERS_PATCH_FOLDER); do \
	#	if [ -d "patches/$$item" ]; then \
	#		cp -avr patches/$$item/* $(PJ_YOCTO_LAYERS_FOLDER); \
	#	fi; \
	#done
	@if [ -d patches/$(PJ_YOCTO_LAYERS_FOLDER) ]; then \
		cp -avr patches/$(PJ_YOCTO_LAYERS_FOLDER)/* $(PJ_YOCTO_LAYERS_FOLDER); \
	fi
ifeq ("$(PJ_HAS_RAUC_KEY)", "yes")
	cp -av $(PJ_YOCTO_ROOT)/rauc-keys/ca.cert.pem $(PJ_YOCTO_LAYERS_DIR)/meta-rauc-plus/recipes-core/rauc/files
endif
	cd $(PJ_YOCTO_LAYERS_FOLDER) \
	&& grep -rl '\$${BSPDIR}/sources/' meta-* | xargs -r sed -i 's#\$${BSPDIR}/sources/#\$${COOKER_LAYER_DIR}/#g'
	touch $@

.PHONY: builds-history bb-linker lnk-generate toolchain toolchain-pure rootfs $(BB_TASKS)
builds-history:
	@cd $(PJ_YOCTO_BUILD_DIR) \
	&& buildhistory-collect-srcrevs -a

bb-linker:
	$(PJ_ROOT_DIR)/confs/sh/bb_linker.sh

lnk-generate: cook-lnk bb-linker

toolchain:
	#cooker build -s $(PJ_YOCTO_TARGET)
	#bitbake core-image-base -c populate_sdk
	bitbake $(PJ_YOCTO_IMAGE) -c populate_sdk

toolchain-pure:
	bitbake meta-toolchain

bundle:
	bitbake $(PJ_YOCTO_BUNDLE)

image: cook-build
	#bitbake $(PJ_YOCTO_IMAGE)

rootfs:
	bitbake -f $(PJ_YOCTO_IMAGE) -c rootfs

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