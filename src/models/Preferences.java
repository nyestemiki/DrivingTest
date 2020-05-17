package models;

public class Preferences {
    private int maxTimeAvailable; // The maximum time the test has to be completed in
    private int nrTotalQuestions; // Total questions a test contains
    private int nrIncorrectQuestionsAllowed;

    public Preferences(int maxTimeAvailable, int nrTotalQuestions, int nrIncorrectQuestionsAllowed) {
        this.maxTimeAvailable = maxTimeAvailable;
        this.nrTotalQuestions = nrTotalQuestions;
        this.nrIncorrectQuestionsAllowed = nrIncorrectQuestionsAllowed;
    }

    public int getNrTotalQuestions() {
        return nrTotalQuestions;
    }

    public int getNrIncorrectQuestionsAllowed() {
        return nrIncorrectQuestionsAllowed;
    }

    public int getMaxTimeAvailable() {
        return maxTimeAvailable;
    }

    public String toString() {
        return maxTimeAvailable + " " + nrTotalQuestions + " " + nrIncorrectQuestionsAllowed;
    }
}
