package LinGUIne.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class SwitchToBasicPerspectiveHandler {

	private static final String PERSPECTIVE_ID =
			"linguine.perspective.basicPerspective";
	
	private MPerspective basicPerspective;
	
	@Inject
	public SwitchToBasicPerspectiveHandler(MApplication app,
			EModelService modelService){
		
		basicPerspective = (MPerspective)modelService.find(PERSPECTIVE_ID, app);
	}
	
	@CanExecute
	public boolean canExecute(MApplication app, EModelService modelService){
		MPerspective activePerspective = modelService.getActivePerspective(
				app.getSelectedElement());
		
		return basicPerspective != activePerspective;
	}
	
	@Execute
	public void execute(EPartService partService) {
		partService.switchPerspective(basicPerspective);
	}
}
