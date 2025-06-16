# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2017 NXP
# Copyright 2018 kernel concepts GmbH
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Linux Kernel provided and supported by NXP"
DESCRIPTION = "Linux Kernel provided and supported by NXP with focus on \
i.MX Family Reference Boards. It includes support for many IPs such as GPU, VPU and IPU."

export jk_version
LOCALVERSION = "-${jk_version}"

require recipes-kernel/linux/linux-imx.inc

SRC_URI = " \
git://github.com/nxp-imx/linux-imx.git;protocol=https;branch=${SRCBRANCH} \
file://defconfig \
file://set-enet_ref_clk-to-50-mhz.patch \
file://ethernet-update-driver.patch \
file://0001-usb-misc-add-USB251xB-xBi-Hi-Speed-Hub-Controller-Dr.patch \
file://0002-usb-usb251xb-remove-max_-power-current-_-sp-bp-prope.patch \
file://0003-usb-usb251xb-dt-add-unit-suffix-to-oc-delay-and-powe.patch \
file://0004-doc-dt-bindings-usb251xb-mark-reg-as-required.patch \
file://0005-add-pl-161_defconfig.patch \
file://0006-ARM-defconfig-pl161-add-usb-mass-storage.patch \
file://0007-ARM-config-pl-161-add-gpio-buttons.patch \
file://0008-ARM-defconfig-pl-161-add-gpio-fan.patch \
file://0009-ARM-defconfig-pl-161-add-various-thermal-monitoring.patch \
file://0011-imx-thermal-add-support-for-thermal-zone-in-devicetr.patch \
file://0012-Input-edt-ft5x06-make-distinction-between-m06-m09-ge.patch \
file://0013-edt-ft5x06-fixes.patch \
file://0014-add-support-for-edt-m12-touch.patch \
file://0015-input-touch-edt-add-special-version-for-pl161.patch \
file://0016-set-uart-fifo-limit-to-1-char.patch \
file://0017-imx-ldb-driver-make-the-crtc-statement-in-devictree-.patch \
file://0018-imx-ldb-use-native-videomode-instead-of-first.patch \
file://0019-ARM-defconfig-add-new-defconfigs-for-pl-900-and-pl-t.patch \
file://0020-video-mxc-ldb-remove-need-for-primary-attribute.patch \
file://0021-video-mxc-allow-to-set-an-fb-name-in-devicetree.patch \
"

DEPENDS += "lzop-native bc-native"

SRCBRANCH = "imx_4.9.11_1.0.0_ga"
SRCREV = "c27010d99a3d91703ea2d1a3f9630a9dedc3f86f"

KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"

COMPATIBLE_MACHINE  = "(tx6[qus]-.*)"

FILESEXTRAPATHS_prepend = "${THISDIR}/${P}/txbase/${TXBASE}:"

KERNEL_IMAGETYPE = "uImage"

# Add NXP binary blob driver for the Vivante GPU to the kernel image.
# Otherwise settings in the Kernel defconfig are actively delete or ignored and
# the required external LKM ([RFS]/lib/modules/<kernel-version>/extra) is not
# available at all ( exception being if specific images are being "bitbaked"
# like: fsl-image-multimedia-full )

# Add Vivante GPU driver support
# Handle Vivante kernel driver setting:
#   0 - machine does not have Vivante GPU driver support
#   1 - machine has Vivante GPU driver support
MACHINE_HAS_VIVANTE_KERNEL_DRIVER_SUPPORT ?= "1"

# Use Vivante kernel driver module:
#   0 - enable the builtin kernel driver module
#   1 - enable the external kernel module
MACHINE_USES_VIVANTE_KERNEL_DRIVER_MODULE ?= "0"

# Add TX6 module specific DT file(s)
SRC_URI += "file://imx6qdl-tx6.dtsi;subdir=git/arch/arm/boot/dts \
	    file://imx6qdl-tx6-gpio.h;subdir=git/include/dt-bindings/gpio \
	   "

# Add baseboard dtsi file(s)
SRC_URI += "file://txbase-${TXBASE}.dtsi;subdir=git/arch/arm/boot/dts"

# Add TX6 (machine) specific DTS file(s)
SRC_URI += "file://${TXTYPE}-${TXNVM}-${TXBASE}.dts;subdir=git/arch/arm/boot/dts\
           "

KERNEL_DEVICETREE = "${TXTYPE}-${TXNVM}-${TXBASE}.dtb"

do_add_jk_version () {
    echo "/ { version = \"${jk_version}\"; };" > ${S}/arch/arm/boot/dts/${TXTYPE}-${TXNVM}-${TXBASE}-version.dtsi
}

addtask add_jk_version after do_unpack_and_patch
addtask add_jk_version after do_unpack
addtask add_jk_version after do_patch
addtask add_jk_version before do_deploy_archives
addtask add_jk_version before do_compile

# for some reason, task order gets messed up (do_compile while do_unpack_and_patch is running)
# lets try to do what we can to save it
addtask unpack_and_patch before do_configure
