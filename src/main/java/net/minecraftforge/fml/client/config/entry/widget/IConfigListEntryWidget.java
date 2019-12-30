package net.minecraftforge.fml.client.config.entry.widget;

import net.minecraft.client.gui.widget.TextFieldWidget;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @param <T> The type param. E.g. Boolean or Float.
 * @author Cadiboo
 */
public interface IConfigListEntryWidget<T> {

	Callback<T> getCallback();

	/**
	 * Call {@link TextFieldWidget#tick()} for any TextFieldWidget objects in this entry.
	 */
	default void tick() {
	}

	/**
	 * Handles drawing any tooltips that apply to this entry.
	 */
	default void renderToolTip(int mouseX, int mouseY, float partialTicks) {
	}

	/**
	 * Gets the default value
	 */
	default T getDefault() {
		return getCallback().getDefault();
	}

	/**
	 * Is this value equal to the default value?
	 */
	default boolean isDefault() {
		return getCallback().isDefault();
	}

	/**
	 * Sets this value to the default value.
	 */
	default void resetToDefault() {
		getCallback().resetToDefault();
		updateWidgetValue();
	}

	/**
	 * Is this value different from the initial value?
	 */
	default boolean isChanged() {
		return getCallback().isChanged();
	}

	/**
	 * Sets this value to the initial value.
	 */
	default void undoChanges() {
		getCallback().undoChanges();
		updateWidgetValue();
	}

	/**
	 * Handles saving any changes that have been made to this entry back to the underlying object.
	 * It is a good practice to check isChanged() before performing the save action.
	 */
	default void save() {
		getCallback().save();
	}

	/**
	 * Is this value is a valid value?
	 */
	default boolean isValid() {
		return getCallback().isValid() && isWidgetValueValid();
	}

	/**
	 * Is the value of the widget a valid value?
	 */
	boolean isWidgetValueValid();

	/**
	 * Updates the widgets value to reflect the stored value.
	 */
	void updateWidgetValue();

	/**
	 * @author Cadiboo
	 */
	class Callback<T> {

		private final Supplier<T> getter;
		private final Consumer<T> setter;
		private final Supplier<T> defaultValueGetter;
		private final BooleanSupplier isDefault;
		private final Runnable resetToDefault;
		private final BooleanSupplier isChanged;
		private final Runnable undoChanges;
		private final Predicate<Object> isValid;
		@Nullable
		private final Runnable save;

		public Callback(final Supplier<T> getter, final Consumer<T> setter, final Supplier<T> defaultValueGetter, final BooleanSupplier isDefault, final Runnable resetToDefault, final BooleanSupplier isChanged, final Runnable undoChanges, final Predicate<Object> isValid) {
			this(getter, setter, defaultValueGetter, isDefault, resetToDefault, isChanged, undoChanges, isValid, null);
		}

		public Callback(final Supplier<T> getter, final Consumer<T> setter, final Supplier<T> defaultValueGetter, final BooleanSupplier isDefault, final Runnable resetToDefault, final BooleanSupplier isChanged, final Runnable undoChanges, final Predicate<Object> isValid, @Nullable final Runnable save) {
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
			final T o = get();
			return isValid(o);
		}

		public boolean isValid(final Object o) {
			return isValid.test(o);
		}

	}

}
