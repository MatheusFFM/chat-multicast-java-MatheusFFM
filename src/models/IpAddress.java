package models;

public class IpAddress implements Comparable<IpAddress> {
    private int maskInit;
    private int maskSecond;
    private int maskMiddle;
    private int maskEnd;
    private static final int MASK_LIMIT = 255;

    public IpAddress(int maskInit, int maskSecond, int maskMiddle, int maskEnd) {
        maskInit = Math.min(maskInit, MASK_LIMIT);
        maskSecond = Math.min(maskSecond, MASK_LIMIT);
        maskMiddle = Math.min(maskMiddle, MASK_LIMIT);
        maskEnd = Math.min(maskEnd, MASK_LIMIT);

        this.maskSecond = maskSecond;
        this.maskInit = maskInit;
        this.maskMiddle = maskMiddle;
        this.maskEnd = maskEnd;
    }

    public static IpAddress ipFromString(String address) {
        String[] ipSplit = address.split("[.]");
        int maskInit = Math.min(Integer.parseInt(ipSplit[0]), MASK_LIMIT);
        int maskSecond = Math.min(Integer.parseInt(ipSplit[1]), MASK_LIMIT);
        int maskMiddle = Math.min(Integer.parseInt(ipSplit[2]), MASK_LIMIT);
        int maskEnd = Math.min(Integer.parseInt(ipSplit[3]), MASK_LIMIT);

        return new IpAddress(maskInit, maskSecond, maskMiddle, maskEnd);
    }

    public int getMaskInit() {
        return maskInit;
    }

    public void setMaskInit(int maskInit) {
        this.maskInit = maskInit;
    }

    public int getMaskSecond() {
        return maskSecond;
    }

    public void setMaskSecond(int maskSecond) {
        this.maskSecond = maskSecond;
    }

    public int getMaskMiddle() {
        return maskMiddle;
    }

    public void setMaskMiddle(int maskMiddle) {
        this.maskMiddle = maskMiddle;
    }

    public int getMaskEnd() {
        return maskEnd;
    }

    public void setMaskEnd(int maskEnd) {
        this.maskEnd = maskEnd;
    }

    public IpAddress getNext() {
        int end = maskEnd;
        int middle = maskMiddle;
        int init = maskInit;
        if (end == 255) {
            end = 0;
            if (middle == 255) {
                middle = 0;
                if (maskSecond == 255) {
                    maskSecond = 0;
                    if (init == 255) {
                        return null;
                    } else {
                        init++;
                    }
                } else {
                    maskSecond++;
                }
            } else {
                middle++;
            }
        } else {
            end++;
        }
        return new IpAddress(init, maskSecond, middle, end);
    }

    @Override
    public String toString() {
        return maskInit + "." + maskSecond + "." + maskMiddle + "." + maskEnd;
    }

    @Override
    public int compareTo(IpAddress o) {
        if (maskInit > o.maskInit) {
            return 1;
        } else if (maskInit < o.maskInit) {
            return -1;
        } else {
            if (maskSecond > o.maskSecond) {
                return 1;
            } else if (maskSecond < o.maskSecond) {
                return -1;
            } else {
                if (maskMiddle > o.maskMiddle) {
                    return 1;
                } else if (maskMiddle < o.maskMiddle) {
                    return -1;
                } else {
                    return Integer.compare(maskEnd, o.maskEnd);
                }
            }
        }
    }
}
