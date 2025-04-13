SUMMARY = "A small image just capable of allowing a device to boot an update with swupdate."

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    swupdate \
    swupdate-www \
"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_ROOTFS_SIZE ?= "8192"
IMAGE_ROOTFS_EXTRA_SPACE:append = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " + 4096", "", d)}"

# Define the image type included in the swu file for this image
IMAGE_FSTYPE_SWU ?= "ext4.gz"

# Artifact name of this image. Also useful for sw-description templating
# Example: filename = "@@ROOTFS_IMAGE@@";
ROOTFS_IMAGE ?= "${IMAGE_LINK_NAME}.${IMAGE_FSTYPE_SWU}"

# Add the image type of this image explicitely otherwise the artifacts
# gets deleted before do_swuimage task runs
IMAGE_FSTYPES:append = " ${IMAGE_FSTYPE_SWU}"

# Artifact name of this image. Also useful for sw-description templating
# Example: filename = "@@ROOTFS_IMAGE@@";
ROOTFS_IMAGE ?= "${IMAGE_LINK_NAME}.${IMAGE_FSTYPE_SWU}"

SWUPDATE_IMAGES_FSTYPES[core-image-minimal-swupdate] = ".${IMAGE_FSTYPE_SWU}"

inherit  swupdate-image

# A bit hacky: Get the u-boot binary into the swu file
DEPENDS += " virtual/bootloader"
do_swupdate_copy_bootloader() {
    cp "${RECIPE_SYSROOT}/boot/MLO" "${S}"
    cp "${RECIPE_SYSROOT}/boot/u-boot.img" "${S}"
}
addtask do_swupdate_copy_bootloader before do_swuimage
SWUPDATE_IMAGES:append:beaglebone-yocto = " MLO u-boot.img"
