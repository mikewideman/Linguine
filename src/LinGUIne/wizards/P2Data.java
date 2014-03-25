package LinGUIne.wizards;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.InstallOperation;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;

import LinGUIne.utilities.InstallUtils;

public class P2Data {
	
	private IProvisioningAgent agent;
	private IMetadataRepository metaRepo;
	private IArtifactRepository artRepo;
	private URI repositoryURI;
	private InstallOperation installOperation;
	
	private ArrayList<IInstallableUnit> selectedIUs;
	private ArrayList<IInstallableUnit> repositoryIUs;
	
	public P2Data(IProvisioningAgent a){
		agent = a;
		selectedIUs = new ArrayList<IInstallableUnit>();
	}

	public void initializeRepositoryData(String repoLocation){
		File f = new File(repoLocation);
		repositoryURI = f.toURI();
		repositoryIUs = new ArrayList<IInstallableUnit>();
		try{
			metaRepo = InstallUtils.loadRepository(agent, repositoryURI);
			repositoryIUs = InstallUtils.generateIUList(metaRepo);
		}
		catch(ProvisionException pe){
			pe.printStackTrace();
		}
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
	
	public void addToSelected(int index){
		selectedIUs.add(repositoryIUs.get(index));
	}
	
	public void removeFromSelected(int index){
		IInstallableUnit targetIU = repositoryIUs.get(index);
		for(int i = 0; i < selectedIUs.size(); i++){
			if(targetIU.compareTo(selectedIUs.get(i)) == 0){
				selectedIUs.remove(i);
			}
		}
	}
}
