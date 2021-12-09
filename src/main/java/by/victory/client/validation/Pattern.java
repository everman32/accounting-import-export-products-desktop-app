package by.victory.client.validation;

public enum Pattern {
    AUTHORIZATION("[A-Za-z0-9]*",
            "[A-Za-z0-9]{5,20}",
            "Разрешено вводить буквы английского алфавита и арабские цифры. " +
                    "Длина должна быть от 5 до 20 символов"),
    PERSON_DATA("[A-Za-zА-Яа-яЁё'-]*",
            "Разрешено вводить буквы русского и английского алфавитов и символы -'"),
    DATE("[0-9.]*",
            "^(?:(?:31(\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\.)" +
                    "(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\.)0?2\\3" +
                    "(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])" +
                    "|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\.)(?:(?:0?[1-9])" +
                    "|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)\\d{2})$",
            "Дата должна быть в формате дд.мм.гггг"),
    EMAIL("[A-Za-z0-9!#$%&'*+-/=?^_`{|}~(),:;<>@\\[\\]. ]*",
            "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\" +
                    "x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9]" +
                    "(?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9]" +
                    "[0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*" +
                    "[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\" +
                    "x0c\\x0e-\\x7f])+)\\])",
                    "Разрешены буквы англисйкого алфавита, арабские цифры и символы !#$%&'*+-/=?^_`{|}~" +
                    "(),:;<>@[]. и пробел. Адрес электронной почты должен быть в формате XXXXX@XXX.XX."),
    PHONE_NUMBER("[+0-9]*",
            "^(\\+)?([0-9]){11,12}$",
            "Номер телефона должен быть в одном из форматов XXXXXXXXXXX, XXXXXXXXXXXX, +XXXXXXXXXXX " +
                    "или +XXXXXXXXXXXX"),
    SPACE_DATA("[A-Za-zА-Яа-яЁё-]*",
            "Разрешено вводить буквы русского и английского алфавитов и символ -"),
    POSTAL_CODE("[0-9-]*",
            "Разрешено вводить арабские цифры и символ -"),
    NAME("[A-Za-zА-Яа-яЁё0-9-.() ]*",
            "Разрешено вводить буквы русского и английского алфавитов, арабские цифры, " +
                    "пробел и символы -.() "),
    NUMBER("[0-9]*",
            "Разрешено вводить арабские цифры, составляющие целые числа больше нуля"),
    DOUBLE("[0-9.]*",
            "Разрешено вводить арабские цифры, составляющие вещественные числа больше нуля"),
    VIN("[A-HJ-NPR-Z0-9]*",
            "^([A-HJ-NPR-Z0-9]){11}\\d{6}",
            "Разрешено вводить прописные буквы английского алфавита кроме I, O, Q и арабские цифры. " +
                    "Номер должен состоять из 17 символов. Последние 6 знаков должны быть цифрами");

    private final String inTimeTemplate;
    private final String clarification;
    private final String finalTemplate;

    Pattern(final String inTimeTemplate, final String clarification) {
        this.inTimeTemplate = inTimeTemplate;
        this.finalTemplate="[]";
        this.clarification=clarification;
    }
    Pattern(final String inTimeTemplate, final String finalTemplate, final String clarification) {
        this.inTimeTemplate = inTimeTemplate;
        this.finalTemplate=finalTemplate;
        this.clarification=clarification;
    }

    public String inTimeTemplateToString() {
        return inTimeTemplate;
    }
    public String finalTemplateToString() {return finalTemplate;}
    public String clarificationToString() {
        return clarification;
    }
}
