package emulator.ui.swt;

import org.eclipse.swt.events.*;

final class Class118 extends SelectionAdapter {
    private final M3GViewUI aClass90_1184;

    Class118(final M3GViewUI aClass90_1184) {
        super();
        this.aClass90_1184 = aClass90_1184;
    }

    public final void widgetSelected(final SelectionEvent selectionEvent) {
        M3GViewUI.method510(this.aClass90_1184, 1);
        M3GViewUI.method505(this.aClass90_1184).setEnabled(false);
        M3GViewUI.method252(this.aClass90_1184);
    }
}
