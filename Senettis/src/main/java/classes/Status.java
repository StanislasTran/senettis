package classes;

/**
 * This enumeration describe the status of an object into the system.
 * PUBLISHED means that the system consider the object and the users can interact with it
 * ARCHIVED means that the system don't consider the object and the user cans interact with it
 * DRAFT means that there is some restriction on the object usage
 * 
 *
 */
public enum Status {
	PUBLISHED("Publié"), ARCHIVED("Archivé"), DRAFT("Brouillon");

	final private String value;

	Status(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	public static Status getStatus(String value) {
		Status status=null;
		switch(value){
			case "Publié" :
				status=PUBLISHED;
				break;
			case "Archivé" :
				status=ARCHIVED;
				break;
			case "Brouillon" :
				status=DRAFT;
				break;
			default:
				status=null;
				break;
		}
		return status;
	}

	
	public boolean equals(String value) {
		return this.value.equals(value);
	}
	
}
