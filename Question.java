
public class Question {

	private String answer;
	private String questionText;

	public Question( String questionText, String answer ){
		this.questionText = questionText;
		this.answer = answer;
	}

	public String getQuestion() {
		return questionText;
	}

	public String getAnswer() {
		return answer;
	}

}
