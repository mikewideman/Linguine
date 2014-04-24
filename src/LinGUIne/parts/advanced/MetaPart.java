
package LinGUIne.parts.advanced;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import LinGUIne.events.LinGUIneEvents;

/**
 * Part that shows metadata about the current selection.
 * 
 * @author Peter Dimou
 * @author Kyle Mullins
 */
public class MetaPart {

	private IPropertiesProvider currentProvider;
	
	private StackLayout mainLayout;
	private Composite mainParent;
	private Composite currentComposite;
	private Composite defaultComposite;

	@PostConstruct
	public void createComposite(Composite parent){
		mainParent = parent;
		
		mainLayout = new StackLayout();
		mainParent.setLayout(mainLayout);
		
		defaultComposite = new Composite(mainParent, SWT.NONE);
		defaultComposite.setLayout(new GridLayout(1, false));
		
		Label lblDefaultInfo = new Label(defaultComposite, SWT.NONE);
		lblDefaultInfo.setText("No properties to display at this time.");
		
		currentComposite = defaultComposite;
		mainLayout.topControl = currentComposite;
	}

	@Focus
	public void setFocus() {}
	
	/**
	 * Called when the active Part changes. If the active Part is an
	 * IPropertiesProvider, then it changes the currently displayed properties
	 * view if necessary.
	 */
	@Inject
	public void activePartChanged(@Optional @Named(IServiceConstants.
			ACTIVE_PART) MPart part){

		if(part != null && part.getObject() != null &&
				part.getObject() instanceof IPropertiesProvider){
			
			currentProvider = (IPropertiesProvider)part.getObject();
			
			changeProperties(currentProvider.getProperties(mainParent));
		}
	}
	
	/**
	 * Called when an IPropertiesProvider changes the contents of its properties
	 * view and updates the currently displayed properties view if necessary.
	 */
	@Inject
	public void propertiesViewChanged(@Optional @UIEventTopic(LinGUIneEvents.
			UILifeCycle.PROPERTIES_VIEW_CHANGED) IPropertiesProvider
			propertiesProvider){
		
		if(propertiesProvider != null && currentProvider == propertiesProvider){
			changeProperties(propertiesProvider.getProperties(mainParent));
		}
	}
	
	/**
	 * Swaps out the current properties Composite for the new one if it is not
	 * null.
	 */
	private void changeProperties(Composite newProperties){
		if(newProperties != null){
			currentComposite = newProperties;
			mainLayout.topControl = currentComposite;
			
			if(!mainLayout.topControl.isDisposed()){
				mainParent.layout();
			}
		}
	}
}

