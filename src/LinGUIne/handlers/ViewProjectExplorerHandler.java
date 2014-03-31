package LinGUIne.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class ViewProjectExplorerHandler {

	@CanExecute
	public boolean canExecute(MApplication application, EPartService partService,
			EModelService modelService){
		
//		MPart projectExplorer = (MPart)modelService.find("linguine.part.advanced"
//				+ ".projectExplorer", application);
		
		return true;
	}
	
	@Execute
	public void execute(MApplication application, EPartService partService,
			EModelService modelService){
		
		MPart projectExplorer = (MPart)modelService.find("linguine.part.advanced"
				+ ".projectExplorer", application);
		
		partService.activate(projectExplorer);
	}
}
