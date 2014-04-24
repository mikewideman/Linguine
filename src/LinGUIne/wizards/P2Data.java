package LinGUIne.wizards;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.InstallOperation;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.utilities.InstallUtils;

/**
 * Data object used to carry data for the InstallWizard.
 * 
 * @author Matthew Talbot
 */
public class P2Data {
	
	private IProvisioningAgent agent;
	private IMetadataRepository metaRepo;
	private URI repositoryURI;
	private Shell parent;
	private UISynchronize sync;
	private IWorkbench workbench;
	
	private ArrayList<IInstallableUnit> selectedIUs;
	private ArrayList<IInstallableUnit> repositoryIUs;
	
	/**
	 * Constructor for the P2Data object
	 * 
	 * @param a The current runtime ProvisioningAgent
	 * @param p The current runtime Shell
	 * @param s The current runtime UISynchronize
	 * @param w the current runtime Workbench
	 */
	public P2Data(final IProvisioningAgent a, final Shell p, final UISynchronize s, final IWorkbench w){
		agent = a;
		parent = p;
		sync = s;
		workbench = w;
		selectedIUs = new ArrayList<IInstallableUnit>();
	}

	/**
	 * Sets up the p2 repository
	 * 
	 * @param repoLocation the string representation of a valid p2 repository
	 */
	public boolean initializeRepositoryData(String repoLocation){
		File f = new File(repoLocation);
		repositoryURI = f.toURI();
		repositoryIUs = new ArrayList<IInstallableUnit>();
		try{
			metaRepo = InstallUtils.loadRepository(agent, repositoryURI);
			repositoryIUs = InstallUtils.generateIUList(metaRepo);
		}
		catch(ProvisionException pe){
			return false;
			//pe.printStackTrace();
		}
		return true;
	}
	
	//Getters
	public IProvisioningAgent getAgent(){
		return agent;
	}
	public IMetadataRepository getMetaRepo(){
		return metaRepo;
	}
	public URI getRepoLocation(){
		return repositoryURI;
	}
	public ArrayList<IInstallableUnit> getSelectedIUs(){
		return selectedIUs;
	}
	public ArrayList<IInstallableUnit> getRepositoryIUs(){
		return repositoryIUs;
	}
	public ArrayList<String> getIUIDs(){
		ArrayList<String> ids = new ArrayList<String>();
		for(int i = 0; i < repositoryIUs.size(); i++){
			ids.add(repositoryIUs.get(i).getId());
		}
		return ids;
	}
	public ArrayList<String> getVersions(){
		ArrayList<String> versions = new ArrayList<String>();
		for(int i = 0; i < repositoryIUs.size(); i++){
			versions.add(repositoryIUs.get(i).getVersion().toString());
		}
		return versions;
	}
	
	public IWorkbench getWorkbench(){
		return workbench;
	}
	public UISynchronize getSync(){
		return sync;
	}
	public Shell getParent(){
		return parent;
	}
	
	/**
	 * Adds an installable unit to the list of IUs that will be installed
	 * @param index - index of the selected IU from the repository's IU list
	 */
	public void addToSelected(int index){
		selectedIUs.add(repositoryIUs.get(index));
	}
	
	/**
	 * Removes an installable unit from the list of IUs that will be installed
	 * @param index - index of the installable unit to be removed
	 */
	public void removeFromSelected(int index){
		IInstallableUnit targetIU = repositoryIUs.get(index);
		for(int i = 0; i < selectedIUs.size(); i++){
			if(targetIU.compareTo(selectedIUs.get(i)) == 0){
				selectedIUs.remove(i);
			}
		}
	}
}
