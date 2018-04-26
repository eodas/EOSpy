package com.eospy.util;

import org.kie.api.event.rule.*;

public class WorkingMemoryListener implements RuleRuntimeEventListener {

	@Override
	public void objectDeleted(ObjectDeletedEvent arg0) {
		System.out.println(
				"--- Removed " + arg0.getFactHandle().toExternalForm() + " of class " + arg0.getOldObject().getClass()); // .info
	}

	@Override
	public void objectInserted(ObjectInsertedEvent arg0) {
		System.out.println(
				"--- Inserted " + arg0.getFactHandle().toExternalForm().toString() + arg0.getObject().toString());
	}

	@Override
	public void objectUpdated(ObjectUpdatedEvent arg0) {
		System.out.println(
				"--- Updated " + arg0.getFactHandle().toExternalForm().toString() + arg0.getObject().toString());
	}
}