package matcher.handlers;

import java.io.File;

import matcher.entities.BaseInstance;
import matcher.exceptions.ApplicationException;
import matcher.patterns.ConflictPattern;

public class ChangeInstanceHandler {
		
	private ProcessBaseHandler pbh;
	
	public ChangeInstanceHandler() {
		this.pbh = new ProcessBaseHandler();
	}

	public void getChangeInstance(File base, File firstVariant, File secondVariant, ConflictPattern cp) 
			throws ApplicationException {
		//TODO incomplete
			BaseInstance baseInstance = pbh.getBaseInstance(base, cp);		
	}
}