 
package LinGUIne.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

/**
 * Handler to toggle between the advanced and basic Perspectives.
 * 
 * @author Kyle Mullins
 */
public class SwitchPerspectiveHandler {
	@Execute
	public void execute(MApplication app, EPartService partService,
			EModelService modelService) {
		MPerspective advancedPerspective = (MPerspective)modelService.find(
				"linguine.perspective.advancedPerspective", app);
		
		MPerspective basicPerspective = (MPerspective)modelService.find(
				"linguine.perspective.basicPerspective", app);
		
		MPerspective activePerspective = modelService.getActivePerspective(
				app.getSelectedElement());
		
		if(activePerspective == advancedPerspective){
			partService.switchPerspective(basicPerspective);
		}
		else{
			partService.switchPerspective(advancedPerspective);
		}
	}
}