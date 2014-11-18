package writer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jdt.launching.JavaRuntime;

public class Builder {

	
	private IJavaProject javaProject;
	
	private Collection<ICompilationUnit> movableClasses;
	
	public void createJavaServerProject(IJavaProject clientProject) throws CoreException{
	
		// create a project with name "TESTJDT"
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(clientProject.getElementName() + "Server");
		project.create(null);
		project.open(null);
		 
		//set the Java nature
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		 
		//create the project
		project.setDescription(description, null);
		IJavaProject javaProject = JavaCore.create(project);
		 
		//set the build path
		//create folder by using sources package
		IFolder folder = project.getFolder("src");
		folder.create(true, true, null);
		
		//create folder by using resources package
		IFolder folder2 = project.getFolder("lib");
		folder2.create(true, true, null);
		
		
		ArrayList<IClasspathEntry> buildPath = new ArrayList<IClasspathEntry>();

		buildPath.add(JavaCore.newSourceEntry(project.getFullPath().append("src")));
		
		buildPath.add(JavaRuntime.getDefaultJREContainerEntry());
		
		buildPath.addAll(loadJars(folder2));
		
		javaProject.setRawClasspath(buildPath.toArray(new IClasspathEntry[buildPath.size()]), null);	
		
	    this.javaProject = javaProject;
	    
	    movableClasses = new ArrayList<ICompilationUnit>();
	    
	    createServerMain();
	    
	    for (IPackageFragmentRoot foldersPk: clientProject.getPackageFragmentRoots()){
	    	if (foldersPk.getKind() == IPackageFragmentRoot.K_SOURCE && !foldersPk.getElementName().equals("gen")){
	    		for (IJavaElement jElem: foldersPk.getChildren()){
	    			IPackageFragment pk = (IPackageFragment)jElem;
					for (ICompilationUnit unit : pk.getCompilationUnits()) {
						if (isOfflodeable(unit)){
							copyClass(pk,unit);
							movableClasses.add(unit);
						}
					}
	    		}
	    	}
			
		}
	 	
	}
	
	public IJavaProject getJavaProject() {
		return javaProject;
	}
	
	public Collection<ICompilationUnit> getMovableClasses() {
		return movableClasses;
	}

	private ArrayList<IClasspathEntry> loadJars(IFolder folder) {
		

		 ArrayList<IClasspathEntry> array = new  ArrayList<IClasspathEntry>();
		try {
			
			InputStream is = getClass().getResourceAsStream("/resources/servlet-api-2.5.jar");
			IFile filenew = folder.getFile("servlet-api-2.5.jar");
			filenew.create(is, false, null);
			array.add(JavaCore.newLibraryEntry(filenew.getFullPath(), null, null));
		
			is = getClass().getResourceAsStream("/resources/jetty-util-7.6.0.v20120127.jar");
			filenew = folder.getFile("jetty-util-7.6.0.v20120127.jar");
			filenew.create(is, false, null);
			array.add(JavaCore.newLibraryEntry(filenew.getFullPath(), null, null));
			
			is = getClass().getResourceAsStream("/resources/jetty-server-7.6.0.v20120127.jar");
			filenew = folder.getFile("jetty-server-7.6.0.v20120127.jar");
			filenew.create(is, false, null);
			array.add(JavaCore.newLibraryEntry(filenew.getFullPath(), null, null));
			
			is = getClass().getResourceAsStream("/resources/jetty-io-7.6.0.v20120127.jar");
			filenew = folder.getFile("jetty-io-7.6.0.v20120127.jar");
			filenew.create(is, false, null);
			array.add(JavaCore.newLibraryEntry(filenew.getFullPath(), null, null));
			
			is = getClass().getResourceAsStream("/resources/jetty-http-7.6.0.v20120127.jar");
			filenew = folder.getFile("jetty-http-7.6.0.v20120127.jar");
			filenew.create(is, false, null);
			array.add(JavaCore.newLibraryEntry(filenew.getFullPath(), null, null));
			
			is = getClass().getResourceAsStream("/resources/jetty-continuation-7.6.0.v20120127.jar");
			filenew = folder.getFile("jetty-continuation-7.6.0.v20120127.jar");
			filenew.create(is, false, null);
			array.add(JavaCore.newLibraryEntry(filenew.getFullPath(), null, null));
			

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return array;
	}
	
	
	public void createServerMain(){

		//Add folder to Java element
		IFolder folder = javaProject.getProject().getFolder("src");
		IPackageFragmentRoot srcFolder = javaProject.getPackageFragmentRoot(folder);
		//create package fragment
		
		InputStream is = getClass().getResourceAsStream("/resources/ServerMain.java");
		IFile filenew = folder.getFile("ServerMain.java");
		try {

			filenew.create(is, false, null);
			ICompilationUnit javaClass = JavaCore.createCompilationUnitFrom(filenew);
					
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public boolean isOfflodeable(ICompilationUnit unit) throws JavaModelException{
		
		boolean isOfflodeable = true;
		for (IImportDeclaration imp :unit.getImports()){
			if (imp.getElementName().contains("android")){
				isOfflodeable = false;
				break;
			}
		}
		return isOfflodeable;
	}
	
	public void copyClass(IPackageFragment pk,ICompilationUnit unit) throws JavaModelException{
		//Add folder to Java element
		IFolder folder = javaProject.getProject().getFolder("src");
		IPackageFragmentRoot srcFolder = javaProject.getPackageFragmentRoot(folder);
		
		//create package fragment
		IPackageFragment fragment = srcFolder.createPackageFragment(
		pk.getElementName(),false, null);
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit); // set source
		parser.setResolveBindings(true); // we need bindings later
		CompilationUnit cu = (CompilationUnit) parser
				.createAST(null ); 
		fragment.createCompilationUnit(unit.getElementName(), cu.toString(), false, null);
	}
	
	public void generateProxies(IJavaProject project){
		
		//PASO 1 GENERO LAS INTERFACES
//		for (ICompilationUnit unit : movableClasses){
//			project
//			
//			
//		}
		
		
		
		
		
	}
	
	
}
