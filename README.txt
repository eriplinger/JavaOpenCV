
# install v4l drivers
wget http://www.linux-projects.org/listing/uv4l_repo/lrkey.asc && sudo apt-key add ./lrkey.asc
sudo apt-get update
sudo apt-get install uv4l uv4l-raspicam
sudo apt-get install uv4l-raspicam-extras uv4l-utils

sudo modprobe bcm2835-v4l2

uv4l --driver raspicam --auto-video_nr

#test
dd if=/dev/video0 of=snapshot.jpeg bs=11M count=1

# edit /boot/config.txt
#added 2016.07.13 for v4l
start_file=start_x.elf
fixup_file=fixup_x.dat
disable_camera_led=0
cma_lwm=
cma_hwm=
cma_offline_start=

#create /etc/modprobe.d/blacklist.conf
blacklist i2c_bcm2708

#create /etc/modules-load.d/rpi-camera.conf
bcm2835-v4l2

#reboot