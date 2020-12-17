package classes;

public enum Status {
	PUBLISHED("Publi�"), ARCHIVED("Archiv�"), DRAFT("Brouillon");

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
			case "Publi�" :
				status=PUBLISHED;
				break;
			case "Archiv�" :
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
