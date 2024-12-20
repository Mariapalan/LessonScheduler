package offerings;

public class Location {

    private String address;
    private String city;
    private String province;

    // Constructor
    public Location(String address, String city, String province) {
        this.address = address;
        this.city = city;
        this.province = province;
    }

    // Method to get the full address as a string
    public String fullAddress() {
        return address + ", " + city + ", " + province;
    }

    // Overriding the toString method to return the full address
    @Override
    public String toString() {
        return fullAddress();
    }

    // Getter for city
    public String getCity() {
        return city;
    }

    // Setter for city
    public void setCity(String city) {
        this.city = city;
    }

    // Getter for province
    public String getProvince() {
        return province;
    }

    // Setter for province
    public void setProvince(String province) {
        this.province = province;
    }

    public static Location fromString(String addressString) throws IllegalArgumentException {
        String[] parts = addressString.split(",\s*");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid address format. Expected: 'address, city, province'");
        }

        String address = parts[0].trim();
        String city = parts[1].trim();
        String province = parts[2].trim();

        return new Location(address, city, province);
    }

    @Override
    public boolean equals(Object location) {
        if (location instanceof Location) {
            return this.fullAddress().equals(((Location) location).fullAddress());
        }
        return false;
    }
}
