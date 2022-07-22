package com.edwkaitwra.backend.utils;

public record ErrorMessages() {
    public static final String UserIsNotActivatedException = "Ο χρήστης δεν είναι ενεργοποιημένος, παρακαλώ επισκεφθείτε το e-mail σας ώστε να το ενεργοποιήσετε";
    public static final String UsernameOrPasswordDoesNotExist = "Το email ή ο κωδικός πρόσβασης δεν υπάρχουν";
    public static final String BadCredentials = "Το e-mail ή ο κωδικός χρήστης δεν ειναι σωστός";

}
