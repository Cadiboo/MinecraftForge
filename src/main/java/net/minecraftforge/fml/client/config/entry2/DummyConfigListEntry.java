package net.minecraftforge.fml.client.config.entry2;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry2.widget.ConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry2.widget.WidgetValueReference;

import javax.annotation.Nullable;

/**
 * @author Cadiboo
 */
public class DummyConfigListEntry extends ConfigListEntry<String> {

	public DummyConfigListEntry(final ConfigScreen configScreen, final String label, final String comment) {
		super(configScreen, new DummyWidget(label, comment));
	}

	private static class DummyWidget extends Widget implements ConfigListEntryWidget<String> {

		private final String label;
		private final String comment;

		public DummyWidget(final String label, final String comment) {
			super(0, 0, 0, 0, label);
			this.label = label;
			this.comment = comment;
		}

		@Override
		public WidgetValueReference<String> getWidgetValueReference() {
			return null;
		}

		@Nullable
		@Override
		public String getLabel() {
			return label;
		}

		@Nullable
		@Override
		public String getTranslationKey() {
			return label;
		}

		@Nullable
		@Override
		public String getComment() {
			return comment;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		@Override
		public void resetToDefault() {
		}

		@Override
		public boolean isChanged() {
			return false;
		}

		@Override
		public void undoChanges() {
		}

		@Override
		public boolean isValid() {
			return true;
		}

		@Override
		public boolean isWidgetValueValid() {
			return true;
		}

		@Override
		public void updateWidgetValue() {
		}

		@Override
		public void render(final int mouseX, final int mouseY, final float partialTicks) {
		}

	}

}
