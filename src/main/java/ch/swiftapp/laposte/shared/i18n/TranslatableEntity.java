package ch.swiftapp.laposte.shared.i18n;

import java.util.Set;

public interface TranslatableEntity<T extends BaseTranslation> {
    Set<T> getTranslations();

    default T getTranslation(String locale) {
        return TranslationUtils.resolve(getTranslations(), locale);
    }
}

