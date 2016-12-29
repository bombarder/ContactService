package parserService;


public enum  ParserState {
    NOT_IN_OBJECT,
    KEY_WAITING,
    IN_KEY,
    COLON_WAITING,
    VALUE_WAITING,
    IN_VALUE,
    COMMA_WAITING
}
