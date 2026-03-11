package personal.bookerav2.entities.enums;


public enum BookStatus {
    READ ("Read"),
    READING("Reading"),
    IN_PLANS("In plans");
    private final String status;

    private BookStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
}
