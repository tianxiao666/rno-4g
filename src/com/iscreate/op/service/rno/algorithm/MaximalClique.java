package com.iscreate.op.service.rno.algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MaximalClique {

	private HashSet<Edge> edges = null;
	private Map<String, HashSet<String>> nodeToNeis = null;

	private HashSet<String> initVertexs;

	public HashSet<String> getInitVertexs() {
		return initVertexs;
	}

	public void setInitVertexs(HashSet<String> initVertexs) {
		this.initVertexs = initVertexs;
	}

	public HashSet<Edge> getEdges() {
		return edges;
	}

	public void setEdges(HashSet<Edge> edges) {
		this.edges = edges;
	}

	public Map<String, HashSet<String>> getNodeToNeis() {
		return nodeToNeis;
	}

	public void setNodeToNeis(Map<String, HashSet<String>> nodeToNeis) {
		this.nodeToNeis = nodeToNeis;
	}
	
	
	public List<Set<String>> calculateMaximalClique(){
		List<Set<String>> results=new ArrayList<Set<String>>();
		if(edges==null || nodeToNeis==null || initVertexs==null
				|| edges.isEmpty()||nodeToNeis.isEmpty()||initVertexs.isEmpty()
				){
			return results;
		}
		
		IK(new HashSet<String>(),initVertexs,new HashSet<String>(),results);
		
		return results;
		
	}

	/**
	 * The Bron-Kerbosch algorithm
	 * 
	 * @param R
	 * @param P
	 * @param X
	 * @author brightming 2014-9-12 上午10:45:38
	 */
	private void BronKerbosch2(HashSet<String> R, HashSet<String> P,
			HashSet<String> X, List<Set<String>> results) {
		if (P.isEmpty() && X.isEmpty()) {
			results.add(new HashSet(R));
			return;
		}
		if (P.isEmpty()) {
			return;
		}

		HashSet<String> p2 = new HashSet<String>(P);

		HashSet<String> nvs = null;
		for (String v : p2) {
			nvs = nodeToNeis.get(v);
			HashSet<String> r1 = new HashSet<String>(R);
			r1.add(v);
			HashSet<String> p1 = new HashSet<String>(P);
			p1.retainAll(nvs);

			HashSet<String> x1 = new HashSet<String>(X);
			x1.retainAll(nvs);

			BronKerbosch2(r1, p1, x1, results);

			P.remove(v);
			X.add(v);
		}

	}

	private void IK(HashSet<String> R, HashSet<String> P, HashSet<String> X,
			List<Set<String>> results) {
		// System.out.println(">>BronKerbosch2.R=" + R + ",P=" + P + ",X=" + X);
		if (P.isEmpty() && X.isEmpty()) {
			results.add(new HashSet(R));
			return;
		}
		if (P.isEmpty()) {
			return;
		}

		HashSet<String> p2 = new HashSet<String>(P);
		String[] parr = new String[p2.size()];
		parr = p2.toArray(parr);
		String pivot = parr[0];// random selection

		HashSet<String> pivotNeis = nodeToNeis.get(pivot);// neighbours of pivot
		if(pivotNeis==null){
			pivotNeis=new HashSet<String>();
		}

		for (String v : P) {
			if (pivotNeis.contains(v)) {
				continue;
			}
			HashSet<String> vNeis = nodeToNeis.get(v);

			p2.remove(v);
			HashSet<String> Rnew = new HashSet<String>(R);
			Rnew.add(v);

			HashSet<String> Pnew = new HashSet<String>(p2);
			Pnew.retainAll(vNeis);

			HashSet<String> Xnew = new HashSet<String>(X);
			Xnew.retainAll(vNeis);

			IK(Rnew, Pnew, Xnew, results);

			X.add(v);
		}

	}

}
