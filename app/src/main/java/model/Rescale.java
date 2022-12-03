package model;

public class Rescale {
    public int horizontalOffset;
    public int cropTop;
    public int cropBottom;
    public int width;

    public Rescale(int horizontalOffset, int cropTop, int cropBottom, int width) {
        this.horizontalOffset = horizontalOffset;
        this.cropTop = cropTop;
        this.cropBottom = cropBottom;
        this.width = width;
    }
}
