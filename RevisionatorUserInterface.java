
import java.io.PrintStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RevisionatorUserInterface {
    private Scanner input;
    private PrintStream output;
    private Revisionator revisionator;

    public RevisionatorUserInterface() {
        input = new Scanner(System.in);
        output = System.out;
        revisionator = new Revisionator();
    }

    public void run() {
        while (true) {
            showMenu();
            askUserForMenuChoice();
            int choice = getNumberFromUser();
            executeChoice(choice);
        }
    }

    private void showMenu() {
        output.println("Choose one of the following options:");
        output.println("1. Take a quiz");
        output.println("2. Quit");
    }

    private void askUserForMenuChoice() {
        output.print("Enter your choice: ");
    }

    private int getNumberFromUser() {
    
			int numberFromUser = 0;
			boolean validNumber = false;
			while (!validNumber) {
				try {
						String text = getTextFromUser();
						numberFromUser = Integer.parseInt(text);
						validNumber = true;
				} catch (Exception e) {
						System.out.print("Please enter a valid number: ");
				}
			}

		return numberFromUser;

		}

    private void executeChoice(int choice) {
        if (choice == 1) {
            quiz();
        }  else if (choice == 2) {
            output.println("Bye!");
            System.exit(0);
        } else {
            output.println("I don't know how to do that.");
        }
    }

    private void quiz() {
			
		List<String> myCategoryList = getValidQuestionCategoriesFromUser();
		List<Question> myQuestionsList = revisionator.getQuizFromCategories( myCategoryList , getValidNumberOfQuestions( myCategoryList ));
		openingMessage( myCategoryList , myQuestionsList );
		runQuiz( myQuestionsList );
// the instruction comments in quiz() and runQuiz() say both methods should give feedback at the end of the quiz, with runQuiz()'s
// feedback being more detailed. i diddn't output any more feedback from the quiz() method because i felt all of the feedback 
// is output in runQuiz(). the runQuiz() has a void return type too. so i felt it must be self contained in a sense, and that
// the instuctions in the below comment were a bit misleading ?


			//this method gets the question categories and the number of questions wanted in the quiz from the user. After that it makes a list of questions from the chosen category or categories, with the right number of questions. It prints a message to the user confirming their selection, and then runs the quiz and gives feedback to the user at the end.
    	//body of method missing
    }

    private List<String> getValidQuestionCategoriesFromUser() {
        List<String> categories = getQuestionCategories();
        List<String> userSelectedCategories;
        output.println("There are questions in these categories: " + prettyPrint(categories));
        do {
            output.print("Enter the categories you would like the questions to be selected from. Please put a space between each category: ");
            String userInput = getTextFromUser();
            userSelectedCategories = getUserSelectedCategories(userInput);
            if (userSelectedCategories.size() == 0) {
                output.println("No categories recognised. Please try again");
            }
        } while (userSelectedCategories.size() == 0);

        return userSelectedCategories;
    }




    private int getValidNumberOfQuestions(List<String> categories) {
			
			int  validNumberOfQuestions = getNumberOfQuestionsInCategories(categories);
			int  usersRequest = 0;
			boolean validInput = false;
			
			while (!validInput) {
					output.println("There are " + validNumberOfQuestions + " possible questions. How many questions would you like to have in your quiz ?");
							usersRequest = getNumberFromUser();
							if (usersRequest <= 0 || usersRequest > validNumberOfQuestions) {
									output.println("invalid number ! please enter between 1 and " + validNumberOfQuestions);
							} else {
									validInput = true;
							}
			}

		return usersRequest;

			//tell the user how many questions are available. Ask them how many questions they want their quiz to have. Read the user input, and reject if number is too low or too high. Ask again for the number of questions, read the answer and keep doing this until the answer is acceptable.
		//some statements missing
		 //placeholding statement to keep the compiler happy
    }



    private int getNumberOfQuestionsInCategories(List<String> categories) {
        int total = 0;
        for (int i = 0; i < categories.size(); i++) {
            total += revisionator.getNumberOfQuestionsInCategory(categories.get(i));
        }
        return total;
    }

    private String prettyPrint(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (i < list.size() - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    private void openingMessage(List<String> categories, List<Question> questions) {
        output.println();
        String categoryOrCategories = "category";
        if (categories.size() > 1) categoryOrCategories = "categories";

        String questionOrQuestions = "question";
        if (questions.size() > 1) questionOrQuestions = "questions";

        output.println("You have selected a quiz with " + questions.size() + " " + questionOrQuestions + " from the " + categoryOrCategories + " " + prettyPrint(categories) + ". Good luck!");
        output.println();
    }



    private void runQuiz(List<Question> questions) {

      List<Boolean> answers = new ArrayList<>();
			TimeKeeper stopwatch = new TimeKeeper();

			stopwatch.start();
			for (int i=0;i<questions.size();i++) {
					answers.add( isCorrectAnswer( questions.get(i) , askQuestionAndGetAnswer( questions.get(i) , i+1 )));
			}
			stopwatch.stop();

			showQuizResults(answers,stopwatch);			
		

			//Runs the quiz and keeps a record of right answers. Times the quiz using the TimeKeeper class. Displays to the user at the end of the quiz the total number of correct answers, the time taken and the percentage correct answers with an encouraging message.
    	//body of method missing

    }



    private String askQuestionAndGetAnswer(Question question, int number) {
        output.println("Question " + number + ":");
        output.println(question.getQuestion());
        output.print("Your answer: ");
        String answer = getTextFromUser();
        output.println();

        return answer;
    }

    private boolean isCorrectAnswer(Question question, String answer) {
        return question.getAnswer().equalsIgnoreCase(answer.trim());
    }

    private void showQuizResults(List<Boolean> answers, TimeKeeper stopwatch) {
        String timeTaken = TimeFormatter.millisecondsToMinutesAndSeconds(stopwatch.getElapsedMilliseconds());
		int correct = howManyCorrect(answers);
		int total = answers.size();
		double percentage = correct*(100f/total);
		String encouragement = encouragingMessage(percentage);

        //show the percentage to 2 decimal places using the DecimalFormat class
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP); //round 0.5 to 1, round 0.4 to 0, ie normal rounding
        String roundedPercentage = df.format(percentage);

		System.out.println("You scored " + correct + "/" + total + " and took " + timeTaken + " to complete the quiz. That's " + roundedPercentage + "%. " + encouragement);
	}

	protected String encouragingMessage(double percentage) {
		if (percentage < 0) return "How have you done that?!";

		if (percentage == 0) return "You can only get better!";

		if (percentage > 0 && percentage <= 20) return "You got some right!";

		if (percentage > 20 && percentage <= 40) return "You did OK!";

		if (percentage > 40 && percentage <= 60) return "Nice job!";

		if (percentage > 60 && percentage <= 80) return "Very good!";

		return "Excellent!";
	}

	private int howManyCorrect(List<Boolean> answers) {
		int correctAnswers = 0;
        for (int i = 0; i < answers.size(); i ++) {
			if (answers.get(i)) correctAnswers++;
		}
		return correctAnswers;
	}

    private String getTextFromUser() {
        return input.nextLine();
    }

    private List<String> getQuestionCategories() {
        return revisionator.getCategories();
    }

    public List<String> getUserSelectedCategories(String input) {
        List<String> validCategories = new ArrayList<>();
        String[] chosenCategories = input.split(" ");
        List<String> existingCategories = getQuestionCategories();

        for (int i = 0; i < chosenCategories.length; i++) {
            for (int j = 0; j < existingCategories.size(); j++) {
                String category = existingCategories.get(j);
                if (category.equalsIgnoreCase(chosenCategories[i])) {
                    validCategories.add(category);
                }
            }
        }
        return validCategories;
    }

    public static void main(String[] args) {
        RevisionatorUserInterface ui = new RevisionatorUserInterface();
        ui.run();
    }
}
