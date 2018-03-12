package com.iscreate.plat.workflow.processor.jbpm.extend.xml;

import org.jbpm.pvm.internal.cmd.CommandService;
import org.jbpm.pvm.internal.wire.binding.WireDescriptorBinding;
import org.jbpm.pvm.internal.wire.descriptor.ObjectDescriptor;
import org.jbpm.pvm.internal.wire.descriptor.ReferenceDescriptor;
import org.jbpm.pvm.internal.xml.Parse;
import org.jbpm.pvm.internal.xml.Parser;
import org.w3c.dom.Element;

import com.iscreate.plat.workflow.processor.jbpm.extend.cmd.IscreateRepositoryServiceImpl;

public class IscreateRepositoryServiceBinding extends WireDescriptorBinding {

	public IscreateRepositoryServiceBinding(){
		super("iscreate-repository-service");
	}
	
	public Object parse(Element element, Parse parse, Parser parser) {
	    ObjectDescriptor descriptor = new ObjectDescriptor(IscreateRepositoryServiceImpl.class);
	    descriptor.addInjection("commandService", new ReferenceDescriptor(CommandService.NAME_TX_REQUIRED_COMMAND_SERVICE));
	    return descriptor;
	  }
}
