package personal.bookerav2.entities.enums;

public enum CountryCode {
    cz ("Czechia"),
    kz ("Kazachstan"),
    us("United States"),
    ru("Russia");

    private final String fullName;
    CountryCode(String fullName){
        this.fullName = fullName;
    }
    public String getGetFullName(){
        return fullName;
    }
    public static CountryCode convert(String code){
        return switch (code){
            case "cz" -> CountryCode.cz;
            case "kz" -> CountryCode.kz;
            case "us" -> CountryCode.us;
            case "ru" -> CountryCode.ru;
            default -> {throw new IllegalArgumentException();}
            };
        }
}
