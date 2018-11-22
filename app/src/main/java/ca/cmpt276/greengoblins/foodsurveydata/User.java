package ca.cmpt276.greengoblins.foodsurveydata;

import java.util.Comparator;

import static android.util.Half.EPSILON;

public class User {

    private String firstName;
    private String lastName;
    private double pledgeAmount;
    private String emailAddress;
    private String city;
    private boolean showNamePublic;
    private int avatarID;

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
        this.avatarID = 0; // USED FOR TESTING
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

    public int getAvatarID() {
        return avatarID;
    }

    public void setAvatarID(int avatarID) {
        this.avatarID = avatarID;
    }

    public static Comparator<User> COMPARE_BY_FIRST_NAME = new Comparator<User>() {
        @Override
        public int compare(User a, User b) {
            if(!a.isShowNamePublic()) return 1;
            if(!b.isShowNamePublic()) return -1;
            return a.getFirstName().compareToIgnoreCase( b.getFirstName() );
        }
    };

    public static Comparator<User> COMPARE_BY_LOCATION = new Comparator<User>() {
        @Override
        public int compare(User a, User b) {
            if(a.getCity().isEmpty()) return 1;
            if(b.getCity().isEmpty()) return -1;
            return a.getCity().compareToIgnoreCase( b.getCity() );
        }
    };

    public static Comparator<User> COMPARE_BY_PLEDGE_VALUE = new Comparator<User>() {
        @Override
        public int compare(User a, User b) {
            final double PLEDGE_VALUE_EPSILON = 0.001;
            double result = a.getPledgeAmount() - b.getPledgeAmount();
            if( Math.abs(result) < PLEDGE_VALUE_EPSILON ) return 0;
            if( result > 0.0 ) return -1;
            return 1;
        }
    };
}
