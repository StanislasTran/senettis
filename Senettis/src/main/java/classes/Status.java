package classes;

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
