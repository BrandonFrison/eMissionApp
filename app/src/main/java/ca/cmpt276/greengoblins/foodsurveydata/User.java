package ca.cmpt276.greengoblins.foodsurveydata;

public class User {

    private String firstName;
    private String lastName;
    private double pledgeAmount;
    private String emailAddress;
    private String city;
    private boolean showNamePublic;

    // Need this default constructor in order for datasnapshot to work
    public User() {
    }

    public User(String emailAddress) {
        this( emailAddress, "", "", 0);
    }

    public User(String emailAddress, String firstName, String lastName) {
        this( emailAddress, firstName, lastName, 0);
    }

    public User(String emailAddress, String firstName, String lastName, double pledgeAmount) {
        this( emailAddress, firstName, lastName, "", pledgeAmount, false );
    }

    public User(String emailAddress, String firstName, String lastName, String city, double pledgeAmount, boolean showName) {
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.pledgeAmount = pledgeAmount;
        this.showNamePublic = showName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getPledgeAmount() {
        return pledgeAmount;
    }

    public void setPledgeAmount(double pledgeAmount) {
        this.pledgeAmount = pledgeAmount;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isShowNamePublic() {
        return showNamePublic;
    }

    public void setShowNamePublic(boolean showNamePublic) {
        this.showNamePublic = showNamePublic;
    }
}
