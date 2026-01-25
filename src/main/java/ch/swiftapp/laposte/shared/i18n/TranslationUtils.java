package ch.swiftapp.laposte.shared.i18n;

import ch.swiftapp.laposte.shared.constants.SupportedLocales;
import java.util.Set;

public final class TranslationUtils {
    private TranslationUtils() {}

    public static <T extends BaseTranslation> T resolve(Set<T> translations, String locale) {
        if (translations == null || translations.isEmpty()) return null;
        String normalized = SupportedLocales.normalize(locale);
        for (T t : translations) {
            if (normalized.equalsIgnoreCase(t.getLocale())) return t;
        }
        for (T t : translations) {
            if (SupportedLocales.DEFAULT_LOCALE.equalsIgnoreCase(t.getLocale())) return t;
        }
        return translations.iterator().next();
    }
}

