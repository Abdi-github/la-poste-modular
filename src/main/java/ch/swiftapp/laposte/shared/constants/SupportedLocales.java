package ch.swiftapp.laposte.shared.constants;

import java.util.List;

public final class SupportedLocales {
    private SupportedLocales() {}

    public static final String DE = "de";
    public static final String FR = "fr";
    public static final String IT = "it";
    public static final String EN = "en";
    public static final String DEFAULT_LOCALE = DE;
    public static final List<String> ALL = List.of(DE, FR, IT, EN);

    public static boolean isSupported(String locale) {
        return locale != null && ALL.contains(locale.toLowerCase());
    }

    public static String normalize(String locale) {
        if (locale == null) return DEFAULT_LOCALE;
        String lower = locale.toLowerCase();
        if (lower.contains("-")) {
            lower = lower.split("-")[0];
        }
        return isSupported(lower) ? lower : DEFAULT_LOCALE;
    }
}

