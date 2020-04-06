package me.Sa1ZeR_.DiverseCart;

public enum CartType {
    ITEM("item");

    private String name;

    CartType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CartType getType(String type) {
        for(CartType t : values()) {
            if(t.getName().equalsIgnoreCase(type)) {
                return t;
            }
        }
        return null;
    }
}
