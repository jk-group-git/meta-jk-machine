require recipes-bsp/u-boot/u-boot.inc

DESCRIPTION = "Das U-Boot for Ka-Ro electronics TX Computer-On-Modules based pl-161."
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=0507cd7da8e7ad6d6701926ec9b84c95"

PROVIDES = "u-boot"

export jk_version
UBOOT_LOCALVERSION = "-${jk_version}"

PV = "v2015.10-rc2+git${SRCPV}-${jk_version}"

SRCREV = "KARO-TX6-2018-01-08"
SRCBRANCH = "master"
SRC_URI = "git://github.com/karo-electronics/karo-tx-uboot.git;branch=${SRCBRANCH} \
           file://0001-tx6-allow-access-of-3rd-i2c-bus.patch \
           file://0002-add-pl-161_defconfig.patch \
           file://0003-karo-fdt-make-fdtsize-non-static-and-accessible.patch \
           file://0004-karo-fdt-functions-add-function-to-change-string-val.patch \
           file://0005-tx6-pl-161-remove-some-unneeded-setup-functions.patch \
           file://0006-tx6-add-functions-to-probe-for-presence-of-ft5x06.patch \
           file://0007-add-function-to-setup-devicetree-for-pl-161-displays.patch \
           file://0008-pl-161-revise-display-configure-code.patch \
           file://u-boot.env \
"

S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE  = "(tx6[qsu]-.*|txul-.*|imx6.*-tx.*)"

python check_sanity_everybuild_append () {
    if d.getVar('UBOOT_MACHINE') != None and d.getVar('IMAGE_BASENAME') != 'u-boot-karo':
        status.addresult("Error: cannot build %s in build dir that has been configured for 'u-boot' build only" % d.getVar('IMAGE_BASENAME'), d)

    elif d.getVar('IMAGE_BASENAME') == 'karo-image-x11' and d.getVar('DISTRO') != 'karo-x11':
        status.addresult("Error: cannot build '%s' with DISTRO '%s'" % \
           (d.getVar('IMAGE_BASENAME'), d.getVar('DISTRO')))
    else
        bb.error("Ka-Ro sanity check passed")
}

do_deploy_append(){
    install -m 0644 ${THISDIR}/${PN}/u-boot.env ${DEPLOYDIR}/
    echo "uenv_version=$jk_version" >> ${DEPLOYDIR}/u-boot.env
    echo "rootpart_uuid=0cc66cc0-02" >> ${DEPLOYDIR}/u-boot.env
    echo "sparepart_uuid=0cc66cc0-03" >> ${DEPLOYDIR}/u-boot.env
    echo -ne "\x00" >> ${DEPLOYDIR}/u-boot.env
    install -m 0644 ${THISDIR}/${PN}/u-boot.env ${DEPLOYDIR}/u-boot.penv
    echo "uenv_version=$jk_version" >> ${DEPLOYDIR}/u-boot.penv
    echo -ne "\x00" >> ${DEPLOYDIR}/u-boot.penv
}
