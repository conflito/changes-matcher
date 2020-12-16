package matcher.entities;

import matcher.entities.deltas.DeltaInstance;

public class ChangeInstance {

	private BaseInstance baseInstance;
	private DeltaInstance firstDelta;
	private DeltaInstance secondDelta;
	
	public ChangeInstance(BaseInstance baseInstance, DeltaInstance firstDelta, DeltaInstance secondDelta) {
		super();
		this.baseInstance = baseInstance;
		this.firstDelta = firstDelta;
		this.secondDelta = secondDelta;
	}

	public BaseInstance getBaseInstance() {
		return baseInstance;
	}

	public DeltaInstance getFirstDelta() {
		return firstDelta;
	}

	public DeltaInstance getSecondDelta() {
		return secondDelta;
	}
}
