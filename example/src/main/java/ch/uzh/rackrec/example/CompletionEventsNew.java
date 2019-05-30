package ch.uzh.rackrec.example;
/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import cc.kave.commons.model.events.CommandEvent;
import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.events.completionevents.IProposal;
import cc.kave.commons.model.events.IIDEEvent;
import cc.kave.commons.model.events.completionevents.TerminationState;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import cc.kave.commons.model.naming.impl.v0.types.TypeName;
import cc.kave.commons.utils.io.ReadingArchive;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;


/**
 * This class contains several code examples that explain how to read enriched
 * event streams with the CARET platform. It cannot be run, the code snippets
 * serve as documentation.
 */
public class CompletionEventsNew {

	/**
	 * this variable should point to a folder that contains a bunch of .zip
	 * files that may be nested in subfolders. If you have downloaded the event
	 * dataset from our website, please unzip the archive and point to the
	 * containing folder here.
	 */
	private static final String DIR_USERDATA = "/home/luc/Documents/RACK/Events-170301-2";
	private static String databaseType = "";

	/**
	 * 1: Find all users in the dataset.
	 */
	public static List<String> findAllUsers(String eventDataLocation) {
		// This step is straight forward, as events are grouped by user. Each
		// .zip file in the dataset corresponds to one user.

		List<String> zips = Lists.newLinkedList();
		int i = 0;
		for (File f : FileUtils.listFiles(new File(eventDataLocation), new String[] { "zip" }, true)) {
			zips.add(f.getAbsolutePath());
			i++;
		}
		return zips;
	}

	/**
	 * 2: Reading events
	 */
	public static List<CompletionEventData> readAllEvents(String eventDataLocation, String newDatabaseType) {
		databaseType = newDatabaseType;
				
		// each .zip file corresponds to a user
		List<String> userZips = findAllUsers(eventDataLocation);
		ArrayList<CompletionEventData> eventsData = new ArrayList<>();
		for (String user : userZips) {
			// you can use our helper to open a file...
			ReadingArchive ra = new ReadingArchive(new File(user));
			// ...iterate over it...
			while (ra.hasNext()) {
				// ... and desrialize the IDE event.
				IIDEEvent e = ra.getNext(IIDEEvent.class);
				// afterwards, you can process it as a Java object
				
				CompletionEventData completionEventData = process(e);
				if (!completionEventData.getEventResult().isEmpty()) {
					eventsData.add(completionEventData);
				}
			}
			ra.close();
		}
		return eventsData;
	}

	/**
	 * 4: Processing events
	 */
	private static CompletionEventData process(IIDEEvent event) {
		// once you have access to the instantiated event you can dispatch the
		// type. As the events are not nested, we did not implement the visitor
		// pattern, but resorted to instanceof checks.
        Set<Pair<IMemberName, Double>> completionEvents = new LinkedHashSet<Pair<IMemberName,Double>>(10);
        Context eventContext = null;
		if (event instanceof CompletionEvent) {
			// if the correct type is identified, you can cast it...
			CompletionEvent ce = (CompletionEvent) event;
			eventContext = ce.getContext();
			if (ce.terminatedState == TerminationState.Applied) {
				if (ce.getLastSelectedProposal().getName() instanceof MethodName){
				
					Double rank = Double.MAX_VALUE;
					for (IProposal p : ce.getProposalCollection()) {
						if (p.getName() instanceof MethodName) {
							MethodName name = (MethodName) p.getName();
							String assemblyName = name.getDeclaringType().getAssembly().getName();
							//check which database type is used as the names and contents are different
							if (databaseType.equals("extended")) {
								if (assemblyName.equals("mscorlib")) {
									//System.out.print("MethodName name: " + name.getDeclaringType().getName());
									//System.out.println(" ");
							        Pair<IMemberName, Double> currentPair = Pair.of(name, rank);
									completionEvents.add(currentPair);
									rank--;
								}
							}
							else if (databaseType.equals("basic")) {
								if (assemblyName.equals("mscorlib") && !name.getFullName().toString().equals(".ctor")) {
									//System.out.print("MethodName name: " + name.getDeclaringType().getName());
									//System.out.println(" ");
							        Pair<IMemberName, Double> currentPair = Pair.of(name, rank);
									completionEvents.add(currentPair);
									rank--;
								}
							}							
						}
					}
				}
			}
		}
		CompletionEventData eventData = new CompletionEventData(completionEvents, eventContext);
		return eventData;
	}
}