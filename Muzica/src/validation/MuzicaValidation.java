package validation;

import domain.Muzica;

public class MuzicaValidation implements IValidation<Muzica> {
    @Override
    public void validate(Muzica elem) throws ValidationException{
        if(elem.getArtist()==null) throw new ValidationException("Artist is null");

        if(elem.getArtist().equals("")) throw new ValidationException("Artist is missing");

        if(elem.getAlbum()==null) throw new ValidationException("Album is null");

        if(elem.getAlbum().equals("")) throw new ValidationException("Album is missing");
    }
}
