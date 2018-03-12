package com.iscreate.plat.workflow.constraint;

public class FlowRelation {

	public enum Relation {

		Relation_0("r_0","FlowA 和  FlowB 没有关系"),
		Relation_1("r_1","FlowA 可以启动 FlowB 并要等待 FlowB 完成"), 
		Relation_2("r_2","FlowA 可以启动 FlowB 不需要等待 FlowB 完成"),
		Relation_3("r_3","FlowB 可以启动 FlowA 并要等待 FlowA 完成"),
		Relation_4("r_4","FlowB 可以启动 FlowA 不需要等待 FlowA 完成");
		

        String code;
        
		String type;

		Relation(String code,String type) {
			this.code=code;
			this.type = type;
		}
  
		public String toString() {
			return this.code;
		}
		
		
	}
	
	
	
	
}
