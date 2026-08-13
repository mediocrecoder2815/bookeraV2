package personal.bookerav2.entities.enums;

public enum CountryCode {
    CZ ("Czechia"),
    KZ ("Kazachstan"),
    US("United States"),
    RU("Russia"),
    IT("Italy"),
    GB("Great Britannia"),
    CN("China");

    private final String fullName;
    CountryCode(String fullName){
        this.fullName = fullName;
    }
    public String getCountyName(){
        return fullName;
    }
    public static CountryCode convert(String code){
        code = code.toUpperCase();
        return switch (code){
            case "CZ" -> CountryCode.CZ;
            case "KZ" -> CountryCode.KZ;
            case "US" -> CountryCode.US;
            case "RU" -> CountryCode.RU;
            case "IT" -> CountryCode.IT;
            case "GB" -> CountryCode.GB;
            case "CN" -> CountryCode.CN;
            default -> throw new IllegalArgumentException();
            };
        }
}
