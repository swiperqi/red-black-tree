package main.java;

public class CompareValue implements Comparable<CompareValue> {

    private Integer val;

    public CompareValue(Integer val) {
        this.val = val;
    }

    @Override
    public int compareTo(CompareValue o) {
        if (this.val > o.getVal()) {
            return 1;
        }else if (this.val.equals(o.getVal())) {
            return 0;
        } else {
            return -1;
        }
    }

    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val.toString();
    }
}
