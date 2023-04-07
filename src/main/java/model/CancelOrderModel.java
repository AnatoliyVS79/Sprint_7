package model;

public class CancelOrderModel extends Request {
    public final String track;

    public CancelOrderModel(String track) {
        this.track = track;
    }
}

