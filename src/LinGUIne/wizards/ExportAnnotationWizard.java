package LinGUIne.wizards;

import javax.inject.Inject;

import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.wizard.Wizard;

import LinGUIne.model.AnnotationSet;
import LinGUIne.model.ProjectManager;
import LinGUIne.utilities.SafeAnnotationExporter;

public class ExportAnnotationWizard extends Wizard {
	
	@Inject
	private ProjectManager projectMan;
	
	private ExportAnnotationData wizardData;
	private ExportAnnotationWizardExporterPage exporterPage;
	private ExportAnnotationWizardChooseAnnotationPage annotationPage;
	private ExportAnnotationWizardChooseFilePage filePage;
	
	public ExportAnnotationWizard(){
		super();
		
		wizardData = new ExportAnnotationData();
	}
	
	@Override
	public void addPages(){
		exporterPage = new ExportAnnotationWizardExporterPage(wizardData);
		annotationPage = new ExportAnnotationWizardChooseAnnotationPage(
				wizardData, projectMan);
		filePage = new ExportAnnotationWizardChooseFilePage(wizardData);
		
		addPage(exporterPage);
		addPage(annotationPage);
		addPage(filePage);
	}
	
	@Override
	public boolean performFinish() {
		AnnotationSet annotations = wizardData.getChosenProject().getAnnotation(
				wizardData.getChosenAnnotatedData());
		
		SafeAnnotationExporter annotationExporter = new SafeAnnotationExporter(
				getShell(), wizardData.getChosenExporter(), annotations,
				wizardData.getChosenAnnotatedData(), wizardData.getDestFile());
		
		SafeRunnable.run(annotationExporter);
		
		return true;
	}
}
