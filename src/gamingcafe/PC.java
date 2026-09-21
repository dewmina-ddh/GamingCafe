
package gamingcafe;

public class PC {
    
    private String id;
    private String name;
    private String category;
    private String cpu;
    private String mBoard;
    private int ram;
    private String vga;
    private String ip;
    private int hRate;
    private String status;

    public PC(String id, String name, String category, String cpu, String mBoard, int ram, String vga, String ip, int hRate, String status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.cpu = cpu;
        this.mBoard = mBoard;
        this.ram = ram;
        this.vga = vga;
        this.ip = ip;
        this.hRate = hRate;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getmBoard() {
        return mBoard;
    }

    public void setmBoard(String mBoard) {
        this.mBoard = mBoard;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public String getVga() {
        return vga;
    }

    public void setVga(String vga) {
        this.vga = vga;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int gethRate() {
        return hRate;
    }

    public void sethRate(int hRate) {
        this.hRate = hRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    
    
}
