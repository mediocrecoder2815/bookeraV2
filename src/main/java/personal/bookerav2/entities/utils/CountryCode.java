package personal.bookerav2.entities.utils;

public enum CountryCode {
    cz ("Czechia"),
    kz ("Kazachstan"),
    us("United States"),
    ru("Russia");

    private String fullName;
    private CountryCode(String fullName){
        this.fullName = fullName;
    }
    public String getGetFullName(){
        return fullName;
    }
}
