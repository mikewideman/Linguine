package LinGUIne.utilities;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
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
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;;

public class InstallUtils {

	public static ArrayList<IInstallableUnit> generateIUList(IMetadataRepository repo){
		ArrayList<IInstallableUnit> iuList = new ArrayList<IInstallableUnit>();
		IQueryResult<IInstallableUnit> results = repo.query(QueryUtil.createIUAnyQuery(), new NullProgressMonitor());
		for (IInstallableUnit iu : results.toUnmodifiableSet()) {
			iuList.add(iu);
		}
		return iuList;
	}
	
	public static IMetadataRepository loadRepository(IProvisioningAgent agent, URI repositoryLocation) throws ProvisionException{
		IMetadataRepositoryManager manager = (IMetadataRepositoryManager) agent.getService(IMetadataRepositoryManager.SERVICE_NAME);
		IArtifactRepositoryManager artifactManager = (IArtifactRepositoryManager) agent.getService("org.eclipse.equinox.internal.p2.metadata.repository.ArtifactRepositoryManager");
		
		System.out.println(agent.getService(IMetadataRepositoryManager.SERVICE_NAME));
		if(manager == null){
			System.out.println("Metadata Manager is null.");
		}
		if(artifactManager == null){
			System.out.println("Artifact Manager is null");
		}
		
		artifactManager.addRepository(repositoryLocation);
		IMetadataRepository repository = manager.loadRepository(repositoryLocation, new NullProgressMonitor());
		return repository;
	}
	
	public static IQueryResult<IInstallableUnit> getInstallable(IMetadataRepository repository){
		return repository.query(QueryUtil.createIUAnyQuery(), new NullProgressMonitor());
	}
	
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
