package com.iscreate.plat.networkresource.structure.instance.restrain;
public abstract class StructureRestraint {
	StructureRestraint structureRestraint;

	public StructureRestraint(StructureRestraint structureRestraint) {
		this.structureRestraint = structureRestraint;
	}

	public StructureConsequence restrain() {
		if (structureRestraint != null) {
			StructureConsequence consequence = structureRestraint.restrain();
			if (consequence.getCode().indexOf("0000") < 0) {
				return consequence;
			}
		}
		return _restrain();
	}

	protected abstract StructureConsequence _restrain();
}
