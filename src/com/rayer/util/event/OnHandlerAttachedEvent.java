package com.rayer.util.event;


/**
 * The event that notify a handler attached to EventManager and registered an event
 * @author rayer
 *
 */
public class OnHandlerAttachedEvent extends EventBase {

	@Override
	public EventParamBase createParameters() {
		return new EventParamBase(OnHandlerAttachedEvent.class){

			@Override
			public int getArg1() {
				return 0;
			}

			@Override
			public int getArg2() {
				return 0;
			}

			@Override
			public Object getObj() {
				return null;
			}};
	}

}
