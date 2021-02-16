package models;

public class IpAddress implements Comparable<IpAddress> {
    private int maskInit;
    private int maskSecond;
    private int maskMiddle;
    private int maskEnd;

    public IpAddress(int maskInit, int maskSecond, int maskMiddle, int maskEnd) {
        this.maskSecond = maskSecond;
        this.maskInit = maskInit;
        this.maskMiddle = maskMiddle;
        this.maskEnd = maskEnd;
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

    public IpAddress getNext(){
        int end = maskEnd;
        int middle = maskMiddle;
        int init = maskInit;
        if(end == 255){
            end = 0;
            if(middle == 255){
                middle = 0;
                if(maskSecond == 255){
                    maskSecond = 0;
                    if(init == 255){
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
        if(maskInit > o.maskInit){
            return 1;
        } else if(maskInit < o.maskInit){
            return -1;
        } else{
            if(maskSecond > o.maskSecond){
                return 1;
            } else if(maskSecond < o.maskSecond){
                return -1;
            } else{
                if(maskMiddle > o.maskMiddle){
                    return 1;
                } else if(maskMiddle < o.maskMiddle){
                    return -1;
                } else{
                    return Integer.compare(maskEnd, o.maskEnd);
                }
            }
        }
    }
}
