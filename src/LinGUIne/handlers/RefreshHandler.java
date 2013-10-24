package LinGUIne.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

/**
 * Handles refreshing the Project Explorer.
 * 
 * @author Kyle Mullins
 */
public class RefreshHandler {
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		
	}
}
