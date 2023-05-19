package org.kellot.resource;

import java.awt.image.BufferedImage;

public class ResponseData {
    private byte[] data;
    private boolean isValid;
    private BufferedImage bufferedImage;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
        this.isValid = data.length > 0;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        this.isValid = bufferedImage != null;
    }

    public boolean isValid() {
        return isValid;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
}
