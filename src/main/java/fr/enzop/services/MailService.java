package fr.enzop.services;

public class MailService {

    public void sendEmail(String to, String subject, String... body) {
        // Cette méthode serait en réalité une implémentation qui envoie un mail
        System.out.println("Envoi d'un email à : " + to);
        System.out.println("Sujet : " + subject);
        for (String bodyPart : body) {
            System.out.println(bodyPart);
        }
    }
}