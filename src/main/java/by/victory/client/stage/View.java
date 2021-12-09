package by.victory.client.stage;

public enum View {
    PACKAGE_TO_VIEW("/view/"),

    AUTHORIZATION("authorization-view"),
    REGISTRATION("registration-view"),

    ADMIN("admin-view"),
    STATISTIC("statistic-view"),

    USER("user-view"),

    EXPORT("export-view"),
    IMPORT("import-view"),

    TRIP("trip-view"),
    ADDRESS("address-view"),
    TRANSPORT("transport-view"),
    DRIVER("driver-view"),
    PRODUCT("product-view");

    private final String name;

    View(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
