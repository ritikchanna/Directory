package ritik.project.dummy;

/**
 * Created by SuperUser on 09-11-2016.
 */

public class student {
    public String name;
    public String roll;
    public String phone;
    public String email;

    public String getVerified_by() {
        return verified_by;
    }

    public void setVerified_by(String verified_by) {
        this.verified_by = verified_by;
    }

    public String verified_by;

    public String getName() {
        return name;
    }

    public String getRoll() {
        return roll;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }
}
