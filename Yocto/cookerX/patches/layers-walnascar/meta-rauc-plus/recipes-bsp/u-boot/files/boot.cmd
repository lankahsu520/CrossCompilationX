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
