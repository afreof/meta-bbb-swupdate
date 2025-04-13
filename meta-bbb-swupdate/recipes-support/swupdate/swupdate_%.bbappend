FILESEXTRAPATHS:append := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://09-swupdate-args \
    "

# additional dependencies required to run swupdate on the target
RDEPENDS:${PN} += "u-boot-fw-utils u-boot-env"

# Should be fixed upstream
DEPENDS += "udev"

do_install:append() {
    install -m 0644 ${UNPACKDIR}/09-swupdate-args ${D}${libdir}/swupdate/conf.d/
    sed -i "s#@MACHINE@#${MACHINE}#g" ${D}${libdir}/swupdate/conf.d/09-swupdate-args

#    install -d ${D}${sysconfdir}
#    install -m 644 ${UNPACKDIR}/swupdate.cfg ${D}${sysconfdir}
}
