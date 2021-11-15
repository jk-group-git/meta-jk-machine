#@TYPE: Machine
#@NAME: JK PL-xxx devices
#@SOC: i.MX6DL
#@DESCRIPTION: Ka-Ro electronics i.MX6DL TX6DL Computer-On-Module based machine config
#@MAINTAINER: Daniel Wagener <daniel.wagener@kernelconcepts.de>

require conf/machine/include/tx6-karo-common.inc

MACHINEOVERRIDES =. "mx6:mx6dl:tx6u-8133:"

SOC_FAMILY = "mx6:mx6qdl:mx6dl:tx6u:tx6"

KERNEL_DEVICETREE ?= "imx6dl-tx6u-8133.dtb"

UBOOT_MACHINE = "tx6u-8133_config"

TXTYPE = "imx6dl-tx6"
TXNVM = "emmc"
TXBASE = "pl-161"
TXBASE_append = ""

PREFERRED_PROVIDER_virtual/kernel = "linux-karo"
PREFERRED_VERSION_linux-karo = "4.9.11"
PREFERRED_PROVIDER_virtual/bootloader = "u-boot"
PREFERRED_PROVIDER_u-boot = "u-boot-pl-161"
PREFERRED_VERSION_imx-gpu-viv = "6%"

KERNEL_IMAGETYPE="uImage"
IMAGE_FSTYPES = "tar.gz"

MACHINE_HAS_VIVANTE_KERNEL_DRIVER_SUPPORT = "1"

UBOOT_MACHINE = "tx6u-8133-pl161_config"

MACHINE_FEATURES += "wifi bluetooth"
