package org.kellot.resource;

public class ResponseData {
    private byte[] data;
    private boolean isValid;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
        this.isValid = data.length > 0;
    }

    public boolean isValid() {
        return isValid;
    }
}
