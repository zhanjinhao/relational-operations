package cn.addenda.ro.common.util;

public class Flag {

    private Flag flag;
    protected int slotNum;

    private static class NumericFlag extends Flag {

        private long flag;

        private NumericFlag(int slotNum) {
            this.flag = 1L << slotNum;
            super.slotNum = slotNum;
        }

        public boolean isEmpty(int index) {
            return (flag & 1L << index) == 0;
        }

        public void setFlag(int index) {
            flag = flag | 1L << index;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = slotNum - 1; i >= 0; i--) {
                sb.append((flag >>> i) & 1);
            }
            return sb.toString();
        }
    }

    private static class StrFlag extends Flag {

        private String flag;

        public StrFlag(int slotNum) {
            super.slotNum = slotNum;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < slotNum; i++) {
                sb = sb.append(0);
            }
            this.flag = sb.toString();
        }

        public boolean isEmpty(int index) {
            return flag.charAt(index) == '0';
        }

        public void setFlag(int index) {

            if (index == 0) {
                flag = flag.substring(0, slotNum - index - 1) + "1";
            } else {
                flag = flag.substring(0, slotNum - index - 1) + "1" + flag.substring(slotNum - index);
            }
            System.out.println(flag.length());
        }

        @Override
        public String toString() {
            return flag;
        }

    }

    private Flag() {
    }

    private Flag(int slotNum) {
        this.slotNum = slotNum;
        if (slotNum < 0) {
            throw new IllegalArgumentException("flag的位数不能小于0！");
        }
        if (slotNum < 64) {
            flag = new NumericFlag(slotNum);
        } else {
            flag = new StrFlag(slotNum);
        }
    }

    public static Flag initFlag(int slotNum) {
        return new Flag(slotNum);
    }

    public boolean isEmpty(int index) {
        if (index >= slotNum) {
            throw new IllegalArgumentException("索引值超出上界！");
        }
        return flag.isEmpty(index);
    }

    public void setFlag(int index) {
        if (index >= slotNum) {
            throw new IllegalArgumentException("索引值超出上界！");
        }
        flag.setFlag(index);
    }

    @Override
    public String toString() {
        return flag.toString();
    }

}