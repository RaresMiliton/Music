package validation;

public interface IValidation<E> {
    void validate(E elem) throws ValidationException;
}
