package za.co.botcoin.objs;

public class Order
{
    private String id;
    private String type;
    private String state;
    private String limitPrice;
    private String limitVolume;
    private String pair;
    private String createdTime;
    private String completedTime;

    public Order()
    {
        this.id = "";
        this.type = "";
        this.state = "";
        this.limitPrice = "";
        this.limitVolume = "";
        this.pair = "";
        this.createdTime = "";
        this.completedTime = "";
    }

    public Order(String id, String type, String state, String limitPrice, String limitVolume, String pair, String createdTime, String completedTime) {

        this.id = id;
        this.type = type;
        this.state = state;
        this.limitPrice = limitPrice;
        this.limitVolume = limitVolume;
        this.pair = pair;
        this.createdTime = createdTime;
        this.completedTime = completedTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(String limitPrice) {
        this.limitPrice = limitPrice;
    }

    public String getLimitVolume() {
        return limitVolume;
    }

    public void setLimitVolume(String limitVolume) {
        this.limitVolume = limitVolume;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(String completedTime) {
        this.completedTime = completedTime;
    }
}
