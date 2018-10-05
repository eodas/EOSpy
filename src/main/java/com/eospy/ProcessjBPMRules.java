package com.eospy;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.jbpm.process.instance.event.listeners.RuleAwareProcessEventLister;
import org.jbpm.process.instance.event.listeners.TriggerRulesEventListener;

import com.eospy.model.Devices;
import com.eospy.model.DevicesList;
import com.eospy.model.ServerEvent;

import com.eospy.ui.MainWindow;
import com.eospy.util.AgendaListener;
import com.eospy.util.SystemOutProcessEventListener;
import com.eospy.util.WorkingMemoryListener;

/**
 * The EOSpy AI-IoT application
 */
public class ProcessjBPMRules {

	private DevicesList devices;
	private KieSession kSession;
	private KieContainer kContainer;

	private boolean knowledgeDebug = false;
	private String kSessionName = "";
	private String processID = "";

	public ProcessjBPMRules(DevicesList devices, String kSessionName, String processID, boolean knowledgeDebug) {
		super();
		this.devices = devices;
		this.kSessionName = kSessionName;
		this.processID = processID;
		this.knowledgeDebug = knowledgeDebug;
	}

	public KieSession createSession(String kSessionName) {
		if (kContainer == null) {
			// load up the knowledge base
			KieServices ks = KieServices.Factory.get();
			kContainer = ks.getKieClasspathContainer();
		}
		if (kContainer == null) {
		}

		// kContainer.getKieBase("rules");
		kSession = kContainer.newKieSession(kSessionName);
		if (kSession == null) {
			System.err.println("ERROR: Cannot find <ksession name=" + kSessionName + "> match in kmodule.xml file.");
			return null;
		}

		if (knowledgeDebug) {
			AgendaListener agendaListener = new AgendaListener();
			WorkingMemoryListener memoryListener = new WorkingMemoryListener();
			kSession.addEventListener(agendaListener);
			kSession.addEventListener(memoryListener);
			// ksession.setGlobal("helper", helper);
			// ksession.setGlobal("logger", logger);
			// kSession.setGlobal("busCalendar", busCalendar);
		}
		return kSession;
	}

	public void receive(ServerEvent serverevent) {
		ProcessInstance instance = null;
		// load up the knowledge base
		this.kSession = createSession(this.kSessionName);

		if (knowledgeDebug) {
			// KieSession ksession = this.createDefaultSession();
			kSession.addEventListener(new SystemOutProcessEventListener());
			kSession.addEventListener(new RuleAwareProcessEventLister());
			kSession.addEventListener(new TriggerRulesEventListener(kSession));
		}

		Devices device = this.devices.getDevice(serverevent.getId());
		if (knowledgeDebug) {
			System.out.println("> " + new SimpleDateFormat("yyy-mm-dd hh:mm:ss").format(serverevent.getDeviceTime())
					+ " id " + device.getId() + "-" + serverevent.getName() + " event " + serverevent.getEvent());
		}
		for (Devices devices : this.devices.getDevices()) {
			kSession.insert(devices);
		}
		kSession.insert(serverevent);

		try {
			// go! - fire rules
			long noOfRulesFired = this.kSession.fireAllRules();
			if (knowledgeDebug) {
				System.out.println("> kSession no of Rules Fired " + noOfRulesFired);
			}
			MainWindow.getInstance().updateDevice(device.getId());
			MainWindow.getInstance().updateEvent(serverevent);

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", device.getId());
			params.put("name", serverevent.getName());
			params.put("event", serverevent.getEvent());
			params.put("adderss", serverevent.getAddress());
			params.put("textMessage", serverevent.getTextMessage());
			params.put("temp", serverevent.getTemp());
			params.put("light", serverevent.getLight());
			params.put("keyPress", serverevent.getKeypress());
			params.put("alarm", serverevent.getAlarm());

			// go! - start jBPM processID
			if (processID != null && !processID.isEmpty()) {
				// Start the process with knowledge session
				instance = kSession.startProcess(processID, params);
			}
			if (instance.getState() != 2) {
				System.out.println(">>" + instance.getState());
			}
			kSession.dispose();

		} catch (Exception e) {
			System.err.println("=============================================================");
			System.err.println("Unexpected exception caught: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void log(String message) {
		MainWindow.getInstance().log(message);
	}
}
