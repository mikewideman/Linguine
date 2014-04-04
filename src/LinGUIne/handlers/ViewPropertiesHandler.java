package LinGUIne.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class ViewPropertiesHandler {

	private static final String PART_ID =
			"linguine.part.advanced.propertiesPart";
	private static final String PERSPECTIVE_ID =
			"linguine.perspective.advancedPerspective";
	
	@CanExecute
	public boolean canExecute(MApplication application, EModelService modelService){
		MPerspective activePerspective = modelService.getActivePerspective(
				application.getSelectedElement());
		
		return activePerspective.getElementId().equals(PERSPECTIVE_ID);
	}
	
	@Execute
	public void execute(MApplication application, EPartService partService,
			EModelService modelService){
		
		MPart properties = (MPart)modelService.find(PART_ID, application);
		
		boolean isVisible = partService.isPartVisible(properties);
		
		if(isVisible){
			partService.hidePart(properties);
		}
		else{
			partService.activate(properties);
		}
	}
}
