package com.iscreate.op.service.rno.algorithm;


public  class Edge {

	String start;
	String end;

	public Edge(String s, String e) {
		this.start = s;
		this.end = e;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (start.equals(other.start) && end.equals(other.end)
				|| start.equals(other.end) && end.equals(other.start)) {
			return true;
		} else {
			return false;
		}
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	
}