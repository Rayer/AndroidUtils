package com.rayer.util.event;



public class OnHandlerDetachedEvent extends EventBase {

	@Override
	public EventParamBase createParameters() {
		return new EventParamBase(OnHandlerDetachedEvent.class){

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
