package personal.bookerav2.entities.enums;


public enum BookStatus {
    IN_PLANS("IN PLANS"),
    READING("READING"),
    DONE ("DONE");
    private final String status;

    BookStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
}
