public class User {
    private String userNumber;
    private String userName;
    private String userSex;
    private String userNIC;
    private String userAddress;
    private String userType; //records "member" or "visitor"
    private String bookNumber;
    private int numBooks = 0;

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setUserSex(String sex) {
        this.userSex = sex;
    }
    public void setUserNIC(String nic) {
        this.userNIC = nic;
    }
    public void setUserType(String type) {
        this.userType = type;
    }
    public void setUserAddress(String address) {
        this.userAddress = address;
    }
    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    public void setNumBooks(int numBooks) {
        this.numBooks = numBooks;
    }

    public String getUserName() {
        return userName;
    }
    public String getUserNumber() {
        return userNumber;
    }
    public String getUserSex() {
        return userSex;
    }
    public String getUserNIC() {
        return userNIC;
    }
    public String getUserAddress() {
        return userAddress;
    }
    public String getUserType() {
        return userType;
    }
    public String getBookNumber() {
        return bookNumber;
    }

    public int getNumBooks() {
        return numBooks;
    }
}
