package offloaderapps.handler;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Vector;

import offloaderapps.model.DependencyModel;
import offloaderapps.model.adapter.TypeAdapter;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import writer.Builder;

/**
 * This is the handler invoked when the user activate the
 * menu on the package explorer.  
 * @author juanx.ane
 *
 */
public class ProjectHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveMenuSelection(event);
		IJavaProject project = (IJavaProject)selection.getFirstElement();
		try {
			analyzeJavaProject(project);
		} catch (Exception e) {
			throw new ExecutionException("Error calculating metric", e);
		}

		return null;
	}

	private void analyzeJavaProject(final IJavaProject project) throws Exception {
		Job job = new Job("Offloading Android app") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					//TODO: most of the time is spent on parsing all the java source files
					//      rather than on analyzing it.  So we I need a better way to show the progress
					//      maybe by first counting the files and report progression based on that.
					long startTime = System.currentTimeMillis();
					DependencyModel javaModel = new DependencyModel(project);
					
					Builder bu = new Builder();
					bu.createJavaServerProject(project);
					
					//bu.generateProxies(javaModel.getTypes());
					
					long endTime = System.currentTimeMillis();
					System.out.println("Runtime: " + (endTime - startTime) / 1000.0);

				} catch (Exception e ) {
					e.printStackTrace();
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}

		};
		job.setUser(true);
		job.schedule();
		
	}



}
