
package gamingcafe;

import java.awt.Image;

public class User {
    
    private String id;
    private String empNo;
    private String fName;
    private String lName;
    private String nic;
    private String email;
    private String phone;
    private String dob;
    private String userName;
    private String password;
    private String role;
    private String status;
    private Image image;

    public User(String id, String empNo, String fName, String lName, String nic, String email, String phone, String userName, String password, String role, String status, Image image) {
        this.id = id;
        this.empNo = empNo;
        this.fName = fName;
        this.lName = lName;
        this.nic = nic;
        this.email = email;
        this.phone = phone;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.status = status;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
    
    
    
}
