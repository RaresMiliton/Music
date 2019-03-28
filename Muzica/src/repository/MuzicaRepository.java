package repository;

import domain.Muzica;
import validation.IValidation;

public class MuzicaRepository extends AbstractCrudRepository<String, Muzica> {
    public MuzicaRepository(IValidation<Muzica> validator,String fileName, String ob) throws Exception{
        super(validator,fileName,ob);
    }
}
