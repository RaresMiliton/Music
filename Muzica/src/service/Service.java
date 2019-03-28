package service;

import domain.Muzica;
import repository.MuzicaRepository;
import ui.ChangeEventType;
import ui.MuzicaEvent;
import ui.Observable;
import ui.Observer;
import validation.IValidation;
import validation.MuzicaValidation;

import java.util.ArrayList;

public class Service implements Observable<MuzicaEvent> {
    private IValidation<Muzica> validatorMuz;
    public ArrayList<Observer<MuzicaEvent>> observers;
    private MuzicaRepository repoMuz = null;

    public Service(String fileMuz) throws Exception{
        validatorMuz = new MuzicaValidation();
        observers = new ArrayList<>();
        repoMuz = new MuzicaRepository(validatorMuz,fileMuz,"domain.Muzica");
    }
    @Override
    public void addObserver(Observer<MuzicaEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<MuzicaEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(MuzicaEvent muzicaEvent) {
        observers.forEach(obs -> obs.update(muzicaEvent));
    }

    public void addMuzica(String artist,String album, String verdict, String melodii, String recomandare) throws Exception{
        Muzica m = new Muzica(artist,album,verdict,melodii,recomandare);
        this.validatorMuz.validate(m);
        this.repoMuz.save(m);
        notifyObservers(new MuzicaEvent(null,m, ChangeEventType.ADD));
    }

    public void removeMuzica(String artist, String album) throws Exception{
        String id = artist+"&"+album;
        Muzica m = repoMuz.findOne(id);
        if(m==null) throw new ServiceException("Nu exista aceasta entitate");
        this.repoMuz.delete(m.getId());
        notifyObservers(new MuzicaEvent(null,m,ChangeEventType.DELETE));
    }

    public void updateMuzica(String artist,String album, String verdict, String melodii, String recomandare) throws Exception{
        String id = artist+"&"+album;
        Muzica m = repoMuz.findOne(id);
        if(m==null) throw new ServiceException("Nu exista aceasta entitate");
        Muzica up_m = new Muzica(artist,album,verdict,melodii,recomandare);
        this.repoMuz.update(up_m);
        notifyObservers(new MuzicaEvent(m,up_m,ChangeEventType.UPDATE));
    }

    public ArrayList<Muzica> allMusic() throws Exception {
        Iterable<Muzica> it= this.repoMuz.findAll();
        ArrayList<Muzica> arr = new ArrayList<>();
        it.forEach(arr::add);
        /*if(arr.size()==0){
            throw new ServiceException("Lista goala");
        }*/
        return arr;
    }
}
