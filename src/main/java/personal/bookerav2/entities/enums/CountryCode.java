package personal.bookerav2.entities.enums;

public enum CountryCode {
    cz ("Czechia"),
    kz ("Kazachstan"),
    us("United States"),
    ru("Russia");

    private final String fullName;
    private CountryCode(String fullName){
        this.fullName = fullName;
    }
    public String getGetFullName(){
        return fullName;
    }
}
