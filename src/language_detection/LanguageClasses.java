package language_detection;

public enum LanguageClasses {
    PL(0, "pl"),
    ENG(1, "eng"),
    ESP(2, "esp");

    private final int id;
    private final String code;

    LanguageClasses(int id, String code) {
        this.id = id;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public static LanguageClasses fromId(int id) {
        for (LanguageClasses lang : LanguageClasses.values()) {
            if (lang.getId() == id) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Unknown language ID: " + id);
    }

    public static LanguageClasses fromCode(String code) {
        for (LanguageClasses lang : LanguageClasses.values()) {
            if (lang.getCode().equals(code)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Unknown language code: " + code);
    }
}