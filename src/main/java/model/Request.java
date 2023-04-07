package model;

import client.Utils;

public class Request {
    public String toJsonString() {
        return Utils.serializeToJsonIgnoreNulls(this);
    }
}
