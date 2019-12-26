package net.minecraftforge.fml.client.config.entry2.widget;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Cadiboo
 */
public class WidgetValueReference<T> {

	private final Supplier<T> getter;
	private final Consumer<T> setter;
	private final Supplier<T> defaultValueGetter;
	private final BooleanSupplier isDefault;
	private final Runnable resetToDefault;
	private final BooleanSupplier isChanged;
	private final Runnable undoChanges;
	private final Predicate<T> isValid;
	@Nullable
	private final Runnable save;

	public WidgetValueReference(final Supplier<T> getter, final Consumer<T> setter, final Supplier<T> defaultValueGetter, final BooleanSupplier isDefault, final Runnable resetToDefault, final BooleanSupplier isChanged, final Runnable undoChanges, final Predicate<T> isValid) {
		this(getter, setter, defaultValueGetter, isDefault, resetToDefault, isChanged, undoChanges, isValid, null);
	}

	public WidgetValueReference(final Supplier<T> getter, final Consumer<T> setter, final Supplier<T> defaultValueGetter, final BooleanSupplier isDefault, final Runnable resetToDefault, final BooleanSupplier isChanged, final Runnable undoChanges, final Predicate<T> isValid, @Nullable final Runnable save) {
		this.getter = getter;
		this.setter = setter;
		this.defaultValueGetter = defaultValueGetter;
		this.isDefault = isDefault;
		this.resetToDefault = resetToDefault;
		this.isChanged = isChanged;
		this.undoChanges = undoChanges;
		this.isValid = isValid;
		this.save = save;
	}

	public T get() {
		return getter.get();
	}

	public void set(T newValue) {
		setter.accept(newValue);
	}

	public T getDefault() {
		return defaultValueGetter.get();
	}

	public boolean isDefault() {
		return isDefault.getAsBoolean();
	}

	public void resetToDefault() {
		resetToDefault.run();
	}

	public void save() {
		if (save != null)
			save.run();
	}

	public boolean isChanged() {
		return isChanged.getAsBoolean();
	}

	public void undoChanges() {
		undoChanges.run();
	}

	public boolean isValid() {
		return isValid.test(get());
	}

}
