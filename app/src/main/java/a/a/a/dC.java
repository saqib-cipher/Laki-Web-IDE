package a.a.a;

public class dC {
    public static final int[] a = new int[gC.a.values().length];

    static {
        try {
            a[gC.a.VIEW.ordinal()] = 1;
            a[gC.a.FAB.ordinal()] = 2;
            a[gC.a.VARIABLE.ordinal()] = 3;
            a[gC.a.LIST.ordinal()] = 4;
            a[gC.a.COMPONENT.ordinal()] = 5;
            a[gC.a.EVENT.ordinal()] = 6;
            a[gC.a.FUNC.ordinal()] = 7;
            a[gC.a.LOGIC.ordinal()] = 8;
        } catch (Exception ignored) {
        }
    }
}
