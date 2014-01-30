package LinGUIne.model.annotations;

import LinGUIne.model.IProjectDataContents;
import LinGUIne.model.TextDataContents;

public class TextSequence implements IAnnotatable {

	private int startIndex;
	private int sequenceLength;
	
	public TextSequence(int start, int length){
		startIndex = start;
		sequenceLength = length;
	}
	
	@Override
	public String getText(IProjectDataContents data) {
		if(data instanceof TextDataContents){
			return ((TextDataContents)data).getText().substring(startIndex,
					startIndex + sequenceLength);
		}
		
		return null;
	}
}
