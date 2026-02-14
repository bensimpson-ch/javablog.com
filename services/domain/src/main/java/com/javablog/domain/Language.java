package com.javablog.domain;

/**
 * ISO 639-1 two-letter language codes for supported languages.
 */
public enum Language {

    BG("bg"),
    HR("hr"),
    CS("cs"),
    DA("da"),
    NL("nl"),
    EN("en"),
    ET("et"),
    FI("fi"),
    FR("fr"),
    DE("de"),
    EL("el"),
    HU("hu"),
    GA("ga"),
    IT("it"),
    LV("lv"),
    LT("lt"),
    MT("mt"),
    PL("pl"),
    PT("pt"),
    RO("ro"),
    SK("sk"),
    SL("sl"),
    ES("es"),
    SV("sv"),
    RU("ru"),
    UK("uk"),
    BE("be"),
    SQ("sq"),
    NO("no"),
    IS("is"),
    CA("ca"),
    EU("eu"),
    GD("gd"),
    CY("cy"),
    ZH("zh"),
    TW("tw"),
    HI("hi"),
    AR("ar"),
    ID("id"),
    BN("bn"),
    JA("ja"),
    KO("ko"),
    TR("tr"),
    HE("he");

    private final String code;

    Language(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    public static Language fromCode(String code) {
        for (Language language : values()) {
            if (language.code.equals(code)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Unknown language code: " + code);
    }
}
