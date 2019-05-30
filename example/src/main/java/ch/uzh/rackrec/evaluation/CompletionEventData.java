package ch.uzh.rackrec.example;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

public class CompletionEventData {
	private Set<Pair<IMemberName, Double>> eventResult;
	private Context eventContext;
	
	
	public CompletionEventData(Set<Pair<IMemberName, Double>> eventResult, Context eventContext) {
		super();
		this.eventResult = eventResult;
		this.eventContext = eventContext;
	}
	public Context getEventContext() {
		return eventContext;
	}
	public Set<Pair<IMemberName, Double>> getEventResult() {
		return eventResult;
	}
	public void setEventContext(Context eventContext) {
		this.eventContext = eventContext;
	}
	public void setEventResult(Set<Pair<IMemberName, Double>> eventResult) {
		this.eventResult = eventResult;
	}

}
