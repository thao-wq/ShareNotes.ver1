package server;

import java.io.Serializable;

public class DocumentMemento implements Serializable{
	private final String content;
	
	public DocumentMemento(String content){
		this.content = content;
	}
	public String getContent(){
		return content;
	}
}

