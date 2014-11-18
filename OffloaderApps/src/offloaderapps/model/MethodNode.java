package offloaderapps.model;

import java.math.BigInteger;
import java.util.Map;
import java.util.Stack;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;


public class MethodNode {
	//this is used as the key to lookup methods in the graph
	MethodSignature methodSignature;
	
	private Map<MethodSignature, MethodNode> map;
	
	public MethodDeclaration md;
	
	BigInteger cost; //complete cost
	BigInteger flatCost; //"flat" cost: don't take recursion into account
	
	String expr;
	
	boolean recursive;
	
	/**
	 * @author mcrasso To account external calls per method 
	 */
	private int externalCalls;
	
	public void incExternalCalls() {
		this.externalCalls++;
	}
	
	public int getExternalCalls() {
		return externalCalls;
	}
	
	public MethodNode(MethodDeclaration md, Map<MethodSignature, MethodNode> map) {
		this.methodSignature = MethodSignature.from(md.resolveBinding());
		this.md = md;
		this.map = map;
		this.cost = null; 
		this.flatCost = null;
		this.recursive = false;
	}

	public MethodSignature getSignature() {
		return methodSignature;
	}

	
	public String getExpr() {
		return this.expr;
	}
		
	public String toString() {
		return this.methodSignature.toString();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MethodNode ) {
			MethodNode other = (MethodNode) obj;
			return this.methodSignature.equals(other.methodSignature);
		} 
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.methodSignature.hashCode();
	}
	
	public void setRecursive() {
		this.recursive = true;
	}
	
}
