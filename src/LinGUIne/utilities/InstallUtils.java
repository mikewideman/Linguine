package LinGUIne.utilities;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.internal.p2.ui.ProvUI;
import org.eclipse.equinox.internal.p2.ui.ProvUIActivator;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.InstallOperation;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.equinox.p2.ui.ProvisioningUI;

/**
 * Provides all the back end functionality of the p2 install process
 * 
 * @author Matthew Talbot
 *
 */
public class InstallUtils {

	/**
	 * Generates a list of all the Installable Units from a repository
	 * @param repo the metadata repository that is the source of the installable units
	 * @return array list of all installable units in a repository
	 */
	public static ArrayList<IInstallableUnit> generateIUList(IMetadataRepository repo){
		ArrayList<IInstallableUnit> iuList = new ArrayList<IInstallableUnit>();
		IQueryResult<IInstallableUnit> results = repo.query(QueryUtil.createIUAnyQuery(), new NullProgressMonitor());
		for (IInstallableUnit iu : results.toUnmodifiableSet()) {
			iuList.add(iu);
		}
		return iuList;
	}
	
	/**
	 * Establishes all the repositories and repository managers needed for the install job
	 * 
	 * @param agent the current ProvisioningAgent
	 * @param repositoryLocation the valid p2 repository location 
	 * @return metadata repository object from a valid p2 repository
	 * @throws ProvisionException
	 */
	@SuppressWarnings("restriction")
	public static IMetadataRepository loadRepository(IProvisioningAgent agent, URI repositoryLocation) throws ProvisionException{
		final ProvisioningUI ui = ProvUIActivator.getDefault().getProvisioningUI();
		IArtifactRepositoryManager artifactManager = ProvUI.getArtifactRepositoryManager(ui.getSession());
		IMetadataRepositoryManager manager = (IMetadataRepositoryManager) agent.getService(IMetadataRepositoryManager.SERVICE_NAME);
		artifactManager.addRepository(repositoryLocation);
		IMetadataRepository repository = manager.loadRepository(repositoryLocation, new NullProgressMonitor());
		return repository;
	}
	
	/**
	 * Queries a repository to get a collection of results
	 * 
	 * @param repository
	 * @return
	 */
	public static IQueryResult<IInstallableUnit> getInstallable(IMetadataRepository repository){
		return repository.query(QueryUtil.createIUAnyQuery(), new NullProgressMonitor());
	}
	
	/**
	 * Starts the install job
	 * 
	 * @param agent current provisioning agent
	 * @param toInstall list of the IUs to be installed
	 * @param uri the repository URI
	 * @return true if install was completed, false if not
	 */
	public static boolean installIUs(final IProvisioningAgent agent, Collection<IInstallableUnit> toInstall, URI uri){
		InstallOperation installOperation = new InstallOperation(new ProvisioningSession(agent), toInstall);
		installOperation.getProvisioningContext().setArtifactRepositories(new URI[]{uri});
		installOperation.getProvisioningContext().setMetadataRepositories(new URI[]{uri});
		IStatus result = installOperation.resolveModal(new NullProgressMonitor());
		System.out.println(result.toString());
		if (installOperation.resolveModal(new NullProgressMonitor()).isOK()) {
			Job job = installOperation.getProvisioningJob(new NullProgressMonitor());
			job.schedule();
			return true;
		}
		return false;
	}
	
}
