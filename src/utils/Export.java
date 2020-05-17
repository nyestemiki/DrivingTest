package utils;

import controllers.MainController;
import models.Question;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Export {
    private List<Question> questions;

    public Export(List<Question> questions) {
        this.questions = questions;
    }

    public void generate() {
        String head = getHead();
        StringBuilder body = buildBody();
        String foot = getFoot();

        exportHtml(head + body + foot);
    }

    private String getHead() {
        return "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><link rel='stylesheet' type='text/css' " +
                "href='../styles/export.css'><meta name=\"viewport\" content=\"width=device-width, " +
                "initial-scale=1.0\"><title>Export</title></head><body>";
    }

    private StringBuilder buildBody() {
        StringBuilder htmlBody = new StringBuilder();

        htmlBody.append("<div class='questions'>");

        for (Question question : questions) {
            htmlBody.append("<div class='question ").append(validate(question)).append("'>")
                    .append("<div class='title'>").append(question.getQuestion()).append("</div>")
                    .append("<div class='content'>")
                    .append("<div class='image'>").append("<img src='../resources/images/")
                    .append(question.getImageId()).append(".png'/>").append("</div>")
                    .append("<div class='answers'>").append(answersForm(question))
                    .append(getAnswers(question)).append("</div>").append("</div>").append("</div>");
        }

        htmlBody.append("</div>");

        return htmlBody;
    }

    private String getFoot() {
        return "</body></html>";
    }

    private String validate(Question question) {
        return question.getAnswer() ? "correct" : "incorrect";
    }

    private String answersForm(Question question) {
        return "<div class='answer_title'>Answer" + (question.getCorrectAnswers().size() == 1 ? "" : "s") + "</div>";
    }

    private StringBuilder getAnswers(Question question) {
        StringBuilder answers = new StringBuilder();

        List<Long> correctAnswers = question.getCorrectAnswers();

        if (correctAnswers.contains(0L)) {
            answers.append("<div class='answer'>").append(question.getAnswer1()).append("</div>");
        }
        if (correctAnswers.contains(1L)) {
            answers.append("<div class='answer'>").append(question.getAnswer2()).append("</div>");
        }
        if (correctAnswers.contains(2L)) {
            answers.append("<div class='answer'>").append(question.getAnswer3()).append("</div>");
        }
        if (correctAnswers.contains(3L)) {
            answers.append("<div class='answer'>").append(question.getAnswer4()).append("</div>");
        }

        return answers;
    }

    private void exportHtml(String content) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        String filename = "src/exports/export_"+dtf.format(now)+".html";

        try {
            File file = new File(filename);
            if (file.createNewFile()) {
                FileWriter writer = new FileWriter(file);
                writer.write(content);
                writer.close();
            } else {
                System.out.println("File could not be created");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while generating html.");
            e.printStackTrace();
        }
    }
}
