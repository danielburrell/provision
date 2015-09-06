import java.util.ArrayList;
import java.util.List;


public class Disk {
    private int size;
    private String label;
    private boolean isPrimary;
    private String type;
    private boolean isReadOnly;
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
        for (Disk disk : disks){
            if (disk.isPrimary()) {
                return disk;
            }
        }
        return null;
    }
	public static List<Disk> findSecondary(List<Disk> disks) {
		List<Disk> secondaryDisks = new ArrayList<Disk>(disks.size());
		for (Disk disk : disks){
            if (!disk.isPrimary()) {
            	secondaryDisks.add(disk);
            }
        }
		return secondaryDisks;
	}
    
}
