 
package LinGUIne.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

/**
 * Handler to switch to the advanced Perspective. Only active when in the basic
 * Perspective.
 * 
 * @author Kyle Mullins
 */
public class SwitchToAdvancedPerspectiveHandler {

	private static final String PERSPECTIVE_ID =
			"linguine.perspective.advancedPerspective";
	
	private MPerspective advancedPerspective;
	
	@Inject
	public SwitchToAdvancedPerspectiveHandler(MApplication app,
			EModelService modelService){
		
		advancedPerspective = (MPerspective)modelService.find(
				PERSPECTIVE_ID, app);
	}
	
	@CanExecute
	public boolean canExecute(MApplication app, EModelService modelService){
		MPerspective activePerspective = modelService.getActivePerspective(
				app.getSelectedElement());
		
		return advancedPerspective != activePerspective;
	}
	
	@Execute
	public void execute(EPartService partService) {
		partService.switchPerspective(advancedPerspective);
	}
}