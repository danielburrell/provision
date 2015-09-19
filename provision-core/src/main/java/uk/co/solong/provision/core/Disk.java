package uk.co.solong.provision.core;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Disk {
    private int size;
    private String label;
    private boolean isPrimary;
    private String type;
    private boolean isReadOnly;
    private boolean isSwap;
    private int diskId;
    private String blockDeviceAssignment;

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static Disk findPrimary(List<Disk> disks) {
        for (Disk disk : disks) {
            if (disk.isPrimary()) {
                return disk;
            }
        }
        return null;
    }

    public static Disk findSwap(List<Disk> disks) {
        for (Disk disk : disks) {
            if (disk.isSwap()) {
                return disk;
            }
        }
        return null;
    }

    public static List<Disk> findSecondary(List<Disk> disks) {
        List<Disk> secondaryDisks = new ArrayList<Disk>(disks.size());
        for (Disk disk : disks) {
            if (!disk.isPrimary() && !disk.isSwap()) {
                secondaryDisks.add(disk);
            }
        }
        return secondaryDisks;
    }

    public static String generateDiskString(List<Disk> disks) {
        Map<String, String> diskMap = new TreeMap<String, String>();
        diskMap.put("/dev/xvda", "");
        diskMap.put("/dev/xvdb", "");
        diskMap.put("/dev/xvdc", "");
        diskMap.put("/dev/xvdd", "");
        diskMap.put("/dev/xvde", "");
        diskMap.put("/dev/xvdf", "");
        diskMap.put("/dev/xvdg", "");
        diskMap.put("/dev/xvdh", "");
        diskMap.put("initrd", "");

        for (Disk disk : disks) {
            diskMap.put(disk.getBlockDeviceAssignment(), String.valueOf(disk.getDiskId()));
        }
        StringBuilder b = new StringBuilder();

        for (String key : diskMap.keySet()) {
            b.append(diskMap.get(key)).append(",");
        }
        return b.toString();

    }

    public int getDiskId() {
        return diskId;
    }

    public void setDiskId(int diskId) {
        this.diskId = diskId;
    }

    public boolean isSwap() {
        return isSwap;
    }

    public void setSwap(boolean isSwap) {
        this.isSwap = isSwap;
    }

    public String getBlockDeviceAssignment() {
        return blockDeviceAssignment;
    }

    public void setBlockDeviceAssignment(String blockDeviceAssignment) {
        this.blockDeviceAssignment = blockDeviceAssignment;
    }

}
