
IMAGE_BOOT_FILES += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'rauc', 'boot.scr', '', d)} \
"
