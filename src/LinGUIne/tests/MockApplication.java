package LinGUIne.tests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MAddon;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingContext;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCategory;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MHandler;
import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MExpression;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuContribution;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarContribution;
import org.eclipse.e4.ui.model.application.ui.menu.MTrimContribution;

public class MockApplication implements MApplication {
	@Override
	public List<MWindow> getChildren() { return null; }

	@Override
	public MWindow getSelectedElement() { return null; }

	@Override
	public void setSelectedElement(MWindow value) {}

	@Override
	public Object getWidget() { return null; }

	@Override
	public void setWidget(Object value) {}

	@Override
	public Object getRenderer() { return null; }

	@Override
	public void setRenderer(Object value) {}

	@Override
	public boolean isToBeRendered() { return false; }

	@Override
	public void setToBeRendered(boolean value) {}

	@Override
	public boolean isOnTop() { return false; }

	@Override
	public void setOnTop(boolean value) {}

	@Override
	public boolean isVisible() { return false; }

	@Override
	public void setVisible(boolean value) {}

	@Override
	public MElementContainer<MUIElement> getParent() { return null; }

	@Override
	public void setParent(MElementContainer<MUIElement> value) {}

	@Override
	public String getContainerData() { return null; }

	@Override
	public void setContainerData(String value) {}

	@Override
	public MPlaceholder getCurSharedRef() { return null; }

	@Override
	public void setCurSharedRef(MPlaceholder value) {}

	@Override
	public MExpression getVisibleWhen() { return null; }

	@Override
	public void setVisibleWhen(MExpression value) {}

	@Override
	public String getAccessibilityPhrase() { return null; }

	@Override
	public void setAccessibilityPhrase(String value) {}

	@Override
	public String getLocalizedAccessibilityPhrase() { return null; }

	@Override
	public String getElementId() { return null; }

	@Override
	public void setElementId(String value) {}

	@Override
	public Map<String, String> getPersistedState() {
		return new HashMap<String, String>();
	}

	@Override
	public List<String> getTags() { return null; }

	@Override
	public String getContributorURI() { return null; }

	@Override
	public void setContributorURI(String value) {}

	@Override
	public Map<String, Object> getTransientData() { return null; }

	@Override
	public IEclipseContext getContext() { return null; }

	@Override
	public void setContext(IEclipseContext value) {}

	@Override
	public List<String> getVariables() { return null; }

	@Override
	public Map<String, String> getProperties() { return null; }

	@Override
	public List<MHandler> getHandlers() { return null; }

	@Override
	public List<MBindingTable> getBindingTables() { return null; }

	@Override
	public List<MBindingContext> getRootContext() { return null; }

	@Override
	public List<MPartDescriptor> getDescriptors() { return null; }

	@Override
	public List<MBindingContext> getBindingContexts() { return null; }

	@Override
	public List<MMenuContribution> getMenuContributions() { return null; }

	@Override
	public List<MToolBarContribution> getToolBarContributions() { return null; }

	@Override
	public List<MTrimContribution> getTrimContributions() { return null; }

	@Override
	public List<MUIElement> getSnippets() { return null; }

	@Override
	public List<MCommand> getCommands() { return null; }

	@Override
	public List<MAddon> getAddons() { return null; }

	@Override
	public List<MCategory> getCategories() { return null; }
}
